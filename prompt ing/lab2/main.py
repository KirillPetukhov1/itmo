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
Твоя задача - написать абзац (4–6 предложений) для комментария по теме: «Навыки, которые становятся важнее в эпоху ИИ (и почему это не кодинг)». Основное требование: используй один конкретный пример из школьной жизни (Пример: 
1)	Разрешение спора в команде: разрешение спора в команде часто требует эмпатии и тонкого понимания эмоций людей, чего ИИ пока не может полностью учитывать. Поэтому он может предложить варианты, но не всегда способен выбрать тот, который сохранит доверие и гармонию в группе.
2)	Оценка достоверности информации из интернета: Проверка информации в интернете требует умения отличать надёжные источники от манипулятивных и учитывать контекст, который ИИ может интерпретировать неверно. Поэтому он иногда принимает фейковые или искажённые данные за достоверные, тогда как человеку проще распознать сомнительный материал.
). После примера прямо напиши, какие человеческие качества он показывает и почему они ценны. Аргументация должна быть логичной и реалистичной.

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