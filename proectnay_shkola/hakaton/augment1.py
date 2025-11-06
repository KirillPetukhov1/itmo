import torch
from transformers import BertTokenizer, BertForMaskedLM
import random
import re
from typing import List, Tuple
import numpy as np

class RussianTextAugmenter:
    def __init__(self, model_name='cointegrated/rubert-tiny'):
        """
        Инициализация аугментатора
        
        Args:
            model_name: название предобученной модели для русского языка
        """
        self.device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
        self.tokenizer = BertTokenizer.from_pretrained(model_name)
        self.model = BertForMaskedLM.from_pretrained(model_name)
        self.model.to(self.device)
        self.model.eval()
        
        # Слова, которые не следует заменять (служебные части речи)
        self.stop_words = {
            'и', 'в', 'во', 'не', 'что', 'он', 'на', 'я', 'с', 'со', 'как', 'а', 
            'то', 'все', 'она', 'так', 'его', 'но', 'да', 'ты', 'к', 'у', 'же',
            'вы', 'за', 'бы', 'по', 'только', 'ее', 'мне', 'было', 'вот', 'от',
            'меня', 'еще', 'нет', 'о', 'из', 'ему', 'теперь', 'когда', 'даже',
            'ну', 'вдруг', 'ли', 'если', 'уже', 'или', 'ни', 'быть', 'был',
            'него', 'до', 'вас', 'нибудь', 'опять', 'уж', 'вам', 'ведь', 'там',
            'потом', 'себя', 'ничего', 'ей', 'может', 'они', 'тут', 'где', 'есть',
            'надо', 'ней', 'для', 'мы', 'тебя', 'их', 'чем', 'была', 'сам', 'чтоб',
            'без', 'будто', 'чего', 'раз', 'тоже', 'себе', 'под', 'будет', 'ж',
            'тогда', 'кто', 'этот', 'того', 'потому', 'этого', 'какой', 'совсем',
            'ним', 'здесь', 'этом', 'один', 'почти', 'мой', 'тем', 'чтобы', 'нее',
            'сейчас', 'были', 'куда', 'зачем', 'всех', 'никогда', 'можно', 'при',
            'наконец', 'два', 'об', 'другой', 'хоть', 'после', 'над', 'больше',
            'тот', 'через', 'эти', 'нас', 'про', 'всего', 'них', 'какая', 'много',
            'разве', 'три', 'эту', 'моя', 'впрочем', 'хорошо', 'свою', 'этой',
            'перед', 'иногда', 'лучше', 'чуть', 'том', 'нельзя', 'такой', 'им',
            'более', 'всегда', 'конечно', 'всю', 'между'
        }
    
    def _split_into_sentences(self, text: str) -> List[str]:
        """Разделение текста на предложения"""
        sentences = re.split(r'(?<=[.!?])\s+', text)
        return [s.strip() for s in sentences if s.strip()]
    
    def _mask_tokens(self, tokens: List[str], mask_ratio: float = 0.3) -> Tuple[List[str], List[int]]:
        """
        Маскирование токенов для замены
        
        Args:
            tokens: список токенов
            mask_ratio: доля токенов для маскирования
            
        Returns:
            masked_tokens: токены с масками
            masked_positions: позиции замаскированных токенов
        """
        masked_tokens = tokens.copy()
        masked_positions = []
        
        # Выбираем токены для маскирования (исключая служебные слова и знаки препинания)
        candidate_positions = [
            i for i, token in enumerate(tokens) 
            if (token.lower() not in self.stop_words and 
                re.match(r'^[а-яё]+$', token.lower()) and
                len(token) > 2)
        ]
        
        num_to_mask = max(1, int(len(candidate_positions) * mask_ratio))
        positions_to_mask = random.sample(candidate_positions, min(num_to_mask, len(candidate_positions)))
        
        for pos in positions_to_mask:
            masked_tokens[pos] = '[MASK]'
            masked_positions.append(pos)
            
        return masked_tokens, masked_positions
    
    def _predict_masked_tokens(self, masked_tokens: List[str], masked_positions: List[int]) -> List[str]:
        """
        Предсказание замен для замаскированных токенов
        
        Args:
            masked_tokens: токены с масками
            masked_positions: позиции замаскированных токенов
            
        Returns:
            predicted_tokens: предсказанные токены
        """
        if not masked_positions:
            return masked_tokens
            
        # Токенизация
        text = ' '.join(masked_tokens)
        inputs = self.tokenizer(text, return_tensors='pt', padding=True, truncation=True)
        inputs = {k: v.to(self.device) for k, v in inputs.items()}
        
        # Предсказание
        with torch.no_grad():
            outputs = self.model(**inputs)
            predictions = outputs.logits
        
        predicted_tokens = masked_tokens.copy()
        
        for pos in masked_positions:
            # Получаем индекс маски в токенизированном представлении
            tokenized = self.tokenizer.convert_ids_to_tokens(inputs['input_ids'][0])
            mask_indices = [i for i, token in enumerate(tokenized) if token == '[MASK]']
            
            if mask_indices:
                mask_index = mask_indices[0]
                # Получаем топ-5 предсказаний для маски
                probs = torch.softmax(predictions[0, mask_index], dim=-1)
                top_k = torch.topk(probs, 5)
                
                # Выбираем случайное предсказание (не исходное слово)
                original_token = masked_tokens[pos].lower()
                for i in range(len(top_k.indices)):
                    predicted_token_id = top_k.indices[i].item()
                    predicted_token = self.tokenizer.convert_ids_to_tokens([predicted_token_id])[0]
                    
                    # Пропускаем служебные токены и исходное слово
                    if (predicted_token not in ['[UNK]', '[CLS]', '[SEP]', '[PAD]'] and
                        not predicted_token.startswith('##') and
                        predicted_token.lower() != original_token and
                        re.match(r'^[а-яё]+$', predicted_token.lower())):
                        
                        predicted_tokens[pos] = predicted_token
                        break
                
                # Если не нашли подходящую замену, оставляем исходное слово
                if predicted_tokens[pos] == '[MASK]':
                    predicted_tokens[pos] = masked_tokens[pos]
        
        return predicted_tokens
    
    def _augment_sentence(self, sentence: str, mask_ratio: float = 0.3) -> str:
        """
        Аугментация одного предложения
        
        Args:
            sentence: исходное предложение
            mask_ratio: доля слов для замены
            
        Returns:
            augmented_sentence: аугментированное предложение
        """
        # Разбиваем на слова с сохранением пунктуации
        words = re.findall(r'\w+|[^\w\s]', sentence)
        
        if len(words) <= 3:  # Слишком короткие предложения не аугментируем
            return sentence
        
        # Маскируем и предсказываем
        masked_words, masked_positions = self._mask_tokens(words, mask_ratio)
        augmented_words = self._predict_masked_tokens(masked_words, masked_positions)
        
        # Собираем предложение обратно
        augmented_sentence = ''
        for word in augmented_words:
            if re.match(r'[^\w\s]', word):
                augmented_sentence += word
            else:
                augmented_sentence += ' ' + word
        
        return augmented_sentence.strip()
    
    def augment_text(self, text: str, mask_ratio: float = 0.3, num_iterations: int = 2) -> str:
        """
        Аугментация всего текста
        
        Args:
            text: исходный текст
            mask_ratio: доля слов для замены в каждом предложении
            num_iterations: количество итераций аугментации
            
        Returns:
            augmented_text: аугментированный текст
        """
        # Разделяем на предложения
        sentences = self._split_into_sentences(text)
        augmented_sentences = sentences.copy()
        
        # Применяем аугментацию в несколько итераций
        for iteration in range(num_iterations):
            current_mask_ratio = mask_ratio * (iteration + 1) / num_iterations
            
            for i in range(len(augmented_sentences)):
                if random.random() < 0.8:  # Аугментируем 80% предложений
                    augmented_sentences[i] = self._augment_sentence(
                        augmented_sentences[i], 
                        current_mask_ratio
                    )
        
        # Объединяем предложения обратно в текст
        augmented_text = '. '.join(augmented_sentences) + '.'
        
        # Постобработка: исправление двойных точек и пробелов
        augmented_text = re.sub(r'\.\s*\.', '.', augmented_text)
        augmented_text = re.sub(r'\s+', ' ', augmented_text)
        
        return augmented_text.strip()

def main():
    """Пример использования аугментатора"""
    
    # Инициализация аугментатора
    augmenter = RussianTextAugmenter('sberbank-ai/ruBert-large')
    
    # Пример официально-делового текста
    sample_text = """
    Уважаемые коллеги, в соответствии с решением совета директоров от 15 ноября 2023 года 
    настоящим уведомляем вас о проведении планового технического обслуживания 
    информационных систем компании. Работы будут проводиться в период с 20 по 22 ноября 
    текущего года. В указанный период возможны временные перерывы в работе 
    корпоративного портала и систем электронного документооборота. 
    Просим принять необходимые меры для минимизации возможных неудобств, 
    связанных с проведением указанных работ. По всем вопросам обращаться 
    в службу технической поддержки по телефону 8-800-123-45-67.
    """
    
    print("Исходный текст:")
    print(sample_text)
    print("\n" + "="*80 + "\n")
    
    # Аугментация текста
    augmented_text = augmenter.augment_text(
        sample_text, 
        mask_ratio=0.4,  # Более высокая доля замен
        num_iterations=3  # Больше итераций для максимального изменения
    )
    
    print("Аугментированный текст:")
    print(augmented_text)

if __name__ == "__main__":
    main()