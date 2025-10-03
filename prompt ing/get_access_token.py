import requests
import uuid
from pathlib import Path
import logging

logging.basicConfig(level=logging.INFO, 
                    filename="py_log.log", 
                    format="%(asctime)s %(levelname)s %(message)s")


ACCESS_TOKEN_FILE = Path(__file__).parent / 'resources' / 'access_token.txt'

def get_access_token() -> str:
    return ACCESS_TOKEN_FILE.read_text()


def get_new_access_token() -> str:
    url = "https://ngw.devices.sberbank.ru:9443/api/v2/oauth"

    headers = {
        "Content-Type": "application/x-www-form-urlencoded",
        "Accept": "application/json",
        "RqUID": str(uuid.uuid4()),
        "Authorization": "Basic OThhZGViNTgtN2E0Mi00YmExLTgzMTctM2YwNjFmNGI0NzNkOmM2YzYzMGJlLTczMGQtNDk3MC04MjRlLWQwZjBkZWRkM2U5Mg=="
    }

    data = {"scope": "GIGACHAT_API_B2B"}

    response = requests.post(url, headers=headers, data=data, verify=False).json()

    try:
        access_token = response['access_token']
    except (KeyError) as e:
        e.add_note(response)
        logging.exception(f'{type(e).__name__}: {e}')
        raise

    try:
        ACCESS_TOKEN_FILE.write_text(access_token)
    except (Exception) as e:
        e.add_note(access_token)
        e.add_note(ACCESS_TOKEN_FILE)
        logging.exception(f'{type(e).__name__}: {e}')
        raise

    return access_token

get_new_access_token()