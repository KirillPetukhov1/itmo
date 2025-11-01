import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn.preprocessing import LabelEncoder
from sklearn.model_selection import train_test_split
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.linear_model import LogisticRegression
from sklearn.naive_bayes import MultinomialNB
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import f1_score, classification_report
import warnings

warnings.filterwarnings('ignore')


IDXES = ['6029', '6526', '6644', '5931', '5980']


def remove_duplicates_instr(text: str, spaces: int = 3) -> str:
    # spaces - через сколько слов происходит разрыв    
    splitted_text = text.split()
    index = 0
    while index < len(splitted_text):
        other_substr = list(filter(lambda x: x[1] == splitted_text[index], [(k, splitted_text[k]) for k in range(index+1, len(splitted_text))]))
        if len(other_substr) == 0:
            index+=1
            continue
        new_other_substr = [other_substr[0]]
        for j in range(1, len(other_substr)):
            if new_other_substr[-1][0] + spaces < other_substr[j][0]:
                break
            new_other_substr.append(other_substr[j])
        for j in new_other_substr:
            splitted_text[j[0]] = ''
        splitted_text = [j for j in splitted_text if j != '']
        index+=1
    return ' '.join(splitted_text)


def run_model(df) -> float:
    le = LabelEncoder()

    df['label_encoded'] = le.fit_transform(df['label'])

    # Сохраняем mapping для обратного преобразования
    label_mapping = dict(zip(le.classes_, le.transform(le.classes_)))

    # Векторизация текста
    vectorizer = TfidfVectorizer(max_features=5000)
    X = vectorizer.fit_transform(df['text'])
    Y = df['label']

    # Разделение на train/test (80%/20%)
    X_train, X_test, Y_train, Y_test = train_test_split(
        X, Y,
        test_size=0.2,
        random_state=56,
        stratify=Y  # Стратификация для сохранения распределения классов
    )

    model_lg = LogisticRegression(random_state=56, max_iter=1000)
    model_lg.fit(X_train, Y_train)

    model = model_lg

    # Предсказания
    Y_pred = model.predict(X_test)

    f1_macro = f1_score(Y_test, Y_pred, average='macro')
    f1_per_class = f1_score(Y_test, Y_pred, average=None)
    f1_variance = np.var(f1_per_class)
    
    return f1_macro - 0.1*(f1_variance**0.5)


def pre_run(df, *args) -> float:
    idx_del = [IDXES[i] for i in range(5) if args[i] == 1]
    idx_change = [IDXES[i] for i in range(5) if args[i] == 2]
    
    idx_del_arr = np.array(idx_del)
    idx_change_arr = np.array(idx_change)
    
    mask1 = ~df['idx'].isin(idx_del_arr)
    mask2 = df['idx'].isin(idx_change_arr)
    
    df = df[mask1]
    df.loc[mask2, 'text'] = df.loc[mask2, 'text'].apply(lambda x: remove_duplicates_instr(x, 15))
    
    return run_model(df)

df = pd.read_csv(r'C:\Users\k4484\itmo\itmo\proectnay_shkola\hakaton del_or_change\clean_data1.csv')

best_res = [-1, -1, -1, -1, -1, 0]

# print(df['label'])

print(run_model(df))

for i1 in range(3):
    for i2 in range(3):
        for i3 in range(3):
            for i4 in range(3):
                for i5 in range(3):
                    res = pre_run(df, i1, i2, i3, i4, i5)
                    if res > best_res[-1]:
                        best_res = [i1, i2, i3, i4, i5, res]
                    print(f'{res:.4f} {best_res[-1]:.4f}')
                            
print(best_res)