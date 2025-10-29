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
                Твоя задача - создать короткий текст (3–4 предложения) с чёткой авторской позицией на тему «Навыки, которые становятся важнее в эпоху ИИ (и почему это не кодинг)». Обязательное условие - начать абзац с вводной конструкции «Я считаю, что...».
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