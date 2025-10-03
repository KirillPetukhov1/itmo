from gigachat import GigaChat
from pathlib import Path
from get_access_token import get_access_token

ca = Path(__file__).parent / 'resources' / 'russian_trusted_root_ca.cer'

# print(type(ca))
# print(type(str(ca)), str(ca))

with GigaChat(
    credentials='OThhZGViNTgtN2E0Mi00YmExLTgzMTctM2YwNjFmNGI0NzNkOmM2YzYzMGJlLTczMGQtNDk3MC04MjRlLWQwZjBkZWRkM2U5Mg==',
    ca_bundle_file=str(ca),
    model='Gigachat-2-Pro',
    scope='GIGACHAT_API_B2B'
) as giga:
    response = giga.chat("hello")
    print(response.choices[0].message.content)
print(response)