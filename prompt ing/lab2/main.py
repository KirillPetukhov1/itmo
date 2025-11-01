from decouple import config
from pathlib import Path
from gigachat import GigaChat
from gigachat.models import Chat, Messages, MessagesRole

# путь до сертификата
ca = Path(__file__).parent.parent / 'resources' / 'russian_trusted_root_ca.cer'


payload = Chat(
    messages=[
        # загрузка запроса
        Messages(
            role=MessagesRole.USER,
            content='''
                Твоя задача - написать абзац (4–6 предложений) для комментария по теме: «Навыки, которые становятся важнее в эпоху ИИ (и почему это не кодинг)». Основное требование: используй один конкретный пример из области научных исследований (например, открытие пенициллина Александром Флемингом в 1928 году). Текст должен содержать конкретные реальные факты, но не содержать ссылок на них. Любые конкретные факты (годы, фамилии, названия) помечай в квадратных скобках: [1956], [Тьюринг]. Если не уверен — ставь [проверить]. После примера прямо напиши, какие человеческие качества он показывает и почему они ценны. Аргументация должна быть логичной и реалистичной.
            '''
        )
    ]
)


with GigaChat(
    credentials=config('AUTH_KEY_2-PRO'),
    ca_bundle_file=str(ca),
    model='Gigachat-2-Pro',
    scope='GIGACHAT_API_B2B'
) as giga:
    response = giga.chat(payload)
    print('Gigachat-2-Pro: ')
    print(response.choices[0].message.content)


with GigaChat(
    credentials=config('AUTH_KEY_2-MAX'),
    ca_bundle_file=str(ca),
    model='Gigachat-2-Max',
    scope='GIGACHAT_API_B2B'
) as giga:
    response = giga.chat(payload)
    print('Gigachat-2-Max: ')
    print(response.choices[0].message.content)