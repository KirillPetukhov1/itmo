import pandas as pd
import numpy as np
import torch
import torch.nn as nn
from torch.utils.data import Dataset, DataLoader
from sklearn.model_selection import train_test_split
from sklearn.metrics import f1_score, classification_report
from sklearn.preprocessing import LabelEncoder
from transformers import AutoTokenizer, AutoModel
from transformers import get_linear_schedule_with_warmup
from tqdm.auto import tqdm
import warnings
from torch.optim import AdamW


warnings.filterwarnings('ignore')

class FocalLoss(nn.Module):
    """Focal Loss для решения проблемы дисбаланса классов"""
    def __init__(self, alpha=1, gamma=2, reduction='mean'):
        super(FocalLoss, self).__init__()
        self.alpha = alpha
        self.gamma = gamma
        self.reduction = reduction

    def forward(self, inputs, targets):
        ce_loss = nn.CrossEntropyLoss(reduction='none')(inputs, targets)
        pt = torch.exp(-ce_loss)
        focal_loss = self.alpha * (1-pt)**self.gamma * ce_loss
        
        if self.reduction == 'mean':
            return focal_loss.mean()
        elif self.reduction == 'sum':
            return focal_loss.sum()
        else:
            return focal_loss

class TextClassificationDataset(Dataset):
    """Датасет для текстовой классификации"""
    def __init__(self, texts, labels, tokenizer, max_length=512):
        self.texts = texts
        self.labels = labels
        self.tokenizer = tokenizer
        self.max_length = max_length

    def __len__(self):
        return len(self.texts)

    def __getitem__(self, idx):
        text = str(self.texts[idx])
        label = self.labels[idx]

        encoding = self.tokenizer(
            text,
            truncation=True,
            padding='max_length',
            max_length=self.max_length,
            return_tensors='pt'
        )

        return {
            'input_ids': encoding['input_ids'].flatten(),
            'attention_mask': encoding['attention_mask'].flatten(),
            'label': torch.tensor(label, dtype=torch.long)
        }

class RuBERTClassifier(nn.Module):
    """Классификатор на основе ruBERT-large"""
    def __init__(self, n_classes, model_name='ai-forever/ruBert-large'):
        super(RuBERTClassifier, self).__init__()
        self.bert = AutoModel.from_pretrained(model_name)
        self.drop = nn.Dropout(p=0.3)
        self.classifier = nn.Linear(self.bert.config.hidden_size, n_classes)

    def forward(self, input_ids, attention_mask):
        outputs = self.bert(
            input_ids=input_ids,
            attention_mask=attention_mask
        )
        pooled_output = outputs.pooler_output
        output = self.drop(pooled_output)
        return self.classifier(output)

class BERTTrainer:
    """Класс для обучения модели"""
    def __init__(self, model, tokenizer, device, n_classes, label_encoder):
        self.model = model
        self.tokenizer = tokenizer
        self.device = device
        self.n_classes = n_classes
        self.label_encoder = label_encoder

    def train_epoch(self, dataloader, optimizer, scheduler, criterion):
        self.model.train()
        total_loss = 0
        progress_bar = tqdm(dataloader, desc='Training')

        for batch in progress_bar:
            input_ids = batch['input_ids'].to(self.device)
            attention_mask = batch['attention_mask'].to(self.device)
            labels = batch['label'].to(self.device)

            optimizer.zero_grad()
            outputs = self.model(input_ids=input_ids, attention_mask=attention_mask)
            loss = criterion(outputs, labels)
            loss.backward()
            
            torch.nn.utils.clip_grad_norm_(self.model.parameters(), max_norm=1.0)
            optimizer.step()
            scheduler.step()

            total_loss += loss.item()
            progress_bar.set_postfix({'loss': loss.item()})

        return total_loss / len(dataloader)

    def evaluate(self, dataloader, criterion):
        self.model.eval()
        total_loss = 0
        all_predictions = []
        all_labels = []

        with torch.no_grad():
            for batch in tqdm(dataloader, desc='Evaluation'):
                input_ids = batch['input_ids'].to(self.device)
                attention_mask = batch['attention_mask'].to(self.device)
                labels = batch['label'].to(self.device)

                outputs = self.model(input_ids=input_ids, attention_mask=attention_mask)
                loss = criterion(outputs, labels)
                total_loss += loss.item()

                _, preds = torch.max(outputs, dim=1)
                all_predictions.extend(preds.cpu().numpy())
                all_labels.extend(labels.cpu().numpy())

        return all_predictions, all_labels, total_loss / len(dataloader)

def calculate_f1_metrics(y_true, y_pred, n_classes, label_encoder):
    """Вычисление macro-f1 и дисперсии f1 по классам"""
    f1_scores = f1_score(y_true, y_pred, average=None, labels=range(n_classes))
    macro_f1 = f1_score(y_true, y_pred, average='macro')
    f1_variance = np.var(f1_scores)
    
    # Вывод F1-score для каждого класса с оригинальными названиями
    print("\nF1-scores per class:")
    for i, class_name in enumerate(label_encoder.classes_):
        print(f"  {class_name}: {f1_scores[i]:.4f}")
    
    return macro_f1, f1_variance, f1_scores

def prepare_labels(df, label_col='label'):
    """Преобразование строковых меток в числовые"""
    label_encoder = LabelEncoder()
    df['label_encoded'] = label_encoder.fit_transform(df[label_col])
    print(f"Classes mapping: {dict(zip(label_encoder.classes_, range(len(label_encoder.classes_))))}")
    return df, label_encoder

def train_bert_classifier(df, text_col='text', label_col='label', 
                         test_size=0.2, batch_size=16, max_length=512,
                         n_epochs=3, learning_rate=2e-5):
    """Основная функция обучения классификатора"""
    
    # Проверка наличия GPU
    device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
    print(f"Using device: {device}")
    
    # Преобразование строковых меток в числовые
    df, label_encoder = prepare_labels(df, label_col)

    
    # Разделение на train/test
    train_df, test_df = train_test_split(
        df, test_size=test_size, random_state=42, stratify=df['label_encoded']
    )
    
    print(f"Train size: {len(train_df)}, Test size: {len(test_df)}")
    
    # Подготовка данных
    n_classes = len(df[label_col].unique())
    print(f"Number of classes: {n_classes}")
    
    # Инициализация токенизатора и модели
    model_name = 'ai-forever/ruBert-large'
    tokenizer = AutoTokenizer.from_pretrained(model_name)
    
    model = RuBERTClassifier(n_classes=n_classes, model_name=model_name)
    model = model.to(device)
    
    # Создание датасетов и даталоадеров
    train_dataset = TextClassificationDataset(
        train_df[text_col].values, 
        train_df['label_encoded'].values, 
        tokenizer, 
        max_length
    )
    test_dataset = TextClassificationDataset(
        test_df[text_col].values, 
        test_df['label_encoded'].values, 
        tokenizer, 
        max_length
    )
    
    train_loader = DataLoader(train_dataset, batch_size=batch_size, shuffle=True)
    test_loader = DataLoader(test_dataset, batch_size=batch_size, shuffle=False)
    
    # Оптимизатор и планировщик
    optimizer = AdamW(model.parameters(), lr=learning_rate, weight_decay=0.01)
    total_steps = len(train_loader) * n_epochs
    scheduler = get_linear_schedule_with_warmup(
        optimizer,
        num_warmup_steps=0,
        num_training_steps=total_steps
    )
    
    # Focal Loss
    criterion = FocalLoss(alpha=1, gamma=2)
    
    # Обучение
    trainer = BERTTrainer(model, tokenizer, device, n_classes, label_encoder)
    
    best_f1 = 0
    for epoch in range(n_epochs):
        print(f'\nEpoch {epoch + 1}/{n_epochs}')
        print('-' * 50)
        
        # Обучение
        train_loss = trainer.train_epoch(train_loader, optimizer, scheduler, criterion)
        print(f'Train loss: {train_loss:.4f}')
        
        # Валидация
        predictions, true_labels, test_loss = trainer.evaluate(test_loader, criterion)
        
        # Метрики
        macro_f1, f1_variance, class_f1_scores = calculate_f1_metrics(
            true_labels, predictions, n_classes, label_encoder
        )
        
        print(f'Test loss: {test_loss:.4f}')
        print(f'Macro F1-score: {macro_f1:.4f}')
        print(f'F1-score variance: {f1_variance:.4f}')
        # print(f'F1-scores per class: {class_f1_scores}')
        
        # Сохранение лучшей модели
        if macro_f1 > best_f1:
            best_f1 = macro_f1
            torch.save(model.state_dict(), 'best_bert_model.pth')
    
    # Финальная оценка на лучшей модели
    model.load_state_dict(torch.load('best_bert_model.pth'))
    predictions, true_labels, _ = trainer.evaluate(test_loader, criterion)
    
    macro_f1, f1_variance, class_f1_scores = calculate_f1_metrics(
        true_labels, predictions, n_classes, label_encoder
    )
    
    print('\n' + '=' * 50)
    print('FINAL RESULTS:')
    print(f'Macro F1-score: {macro_f1:.4f}')
    print(f'F1-score variance: {f1_variance:.4f}')
    print(f'Score: {(macro_f1 - 0.1 * (f1_variance**0.5)):.4f}')
    print('Classification Report:')
    print(classification_report(true_labels, predictions))
    
    return model, tokenizer, label_encoder, macro_f1, f1_variance

# Пример использования
if __name__ == "__main__":
    # Создание примера датафрейма для демонстрации
    # sample_data = {
    #     'idx': range(100),
    #     'text': ['Пример текста для классификации'] * 100,
    #     'label': [0, 1, 2] * 33 + [0]  # 3 класса
    # }
    # df = pd.DataFrame(sample_data)
    
    # Запуск обучения
    df = pd.read_csv(r'C:\Users\k4484\itmo\itmo-1\proectnay_shkola\hakaton\raw_del2.csv')
    model, tokenizer, label_encoder, macro_f1, f1_variance = train_bert_classifier(df)