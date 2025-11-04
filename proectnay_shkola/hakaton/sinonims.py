import random
from razdel import tokenize
from sentence_transformers import SentenceTransformer, util
from wiki_ru_wordnet import WikiWordnet

model = SentenceTransformer('sentence-transformers/paraphrase-multilingual-MiniLM-L12-v2')
wikiwordnet = WikiWordnet()

# ✅ 1. Получаем синонимы по слову
def get_synonyms(word):
    syns = []
    for synset in wikiwordnet.get_synsets(word):
        for new_word in synset.get_words():
            if new_word.lemma() != word:
                syns.append(new_word.lemma().replace("_", " "))
    return list(set(syns))

# ✅ 2. Заменяем часть слов на синонимы
def synonym_replace_high_quality(text, replace_prob=0.5, similarity_threshold=0.5):
    tokens = [t.text for t in tokenize(text)]
    new_tokens = []
    
    for token in tokens:
        if random.random() < replace_prob:
            syns = get_synonyms(token)
            if syns:
                # Оцениваем семантическую близость через BERT
                similarities = []
                for s in syns:
                    sim = util.cos_sim(model.encode(token), model.encode(s)).item()
                    similarities.append((s, sim))
                best = max(similarities, key=lambda x: x[1])
                if best[1] >= similarity_threshold:
                    new_tokens.append(best[0])
                    continue
        new_tokens.append(token)
    
    return " ".join(new_tokens)

# print(get_synonyms('медведей'))
# print(synonym_replace_high_quality('МИНИСТЕРСТВО ПРИРОДНЫХ РЕСУРСОВ И ЭКОЛОГИИ РОССИЙСКОЙ ФЕДЕРАЦИИ ФЕДЕРАЛЬНОЕ АГЕНТСТВО ПО НЕДРОПОЛЬЗОВАНИЮ ФЕДЕРАЛЬНОЕ БЮДЖЕТНОЕ УЧРЕЖДЕНИЕ ГОСУДАРСТВЕННАЯ КОМИССИЯ ПО ЗАПАСАМ ПОЛЕЗНЫХ ИСКОПАЕМЫХ (ФБУ ГКЗ) [LOCATION] тел./факс [CONTACT]/[CONTACT]. E-mail: [CONTACT] ОКПО [ID], ОГРН [ID] ИНН/КПП [ID]/[ID] [DATE_TIME] No [DOCUMENT_NUMBER] По списку на No от Уважаемые коллеги! В связи с необходимостью внесения изменений в организационный план, информируем Вас о том, что проведение форума Технологический день [ORGANIZATION], ранее запланированного на [DATE_TIME], переносится на I квартал [DATE_TIME]. Мы выражаем признательность за Ваш интерес к участию в форуме и уверены, что предстоящее мероприятие будет способствовать укреплению сотрудничества в сфере недропользования и геологоразведки между Российской Федерацией и Республикой [COUNTRY]. Новую дату и место проведения форума Технологический день [ORGANIZATION] мы сообщим Вам дополнительным письмом. Генеральный директор [PERSON] Исп.: [PERSON] [CONTACT]","label": "Блок директора по проектированию'))