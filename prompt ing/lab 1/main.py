from decouple import config
from pathlib import Path
from gigachat import GigaChat
from gigachat.models import Chat, Messages, MessagesRole

# путь до сертификата
ca = Path(__file__).parent.parent / 'resources' / 'russian_trusted_root_ca.cer'

with GigaChat(
    credentials=config('AUTH_KEY'),
    ca_bundle_file=str(ca),
    model='Gigachat-2-Pro',
    scope='GIGACHAT_API_B2B'
) as giga:
    payload = Chat(
        messages=[
            # загрузка запроса
            Messages(
                role=MessagesRole.USER,
                content='''
                    В каком году у университета ИТМО была пиар-компания со «Вкусно и точка» и в чем она заключалась?

"
                '''
            )
        ]
    )
    response = giga.chat(payload)
    print(response.choices[0].message.content)
