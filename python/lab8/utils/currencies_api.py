import requests


def get_currencies(currency_codes: list, url="https://www.cbr-xml-daily.ru/daily_json.js") -> dict:
    """
    Получает курсы валют с API Центробанка России.

    Args:
        currency_codes (list): Список символьных кодов валют (например, ['USD', 'EUR']).

    Returns:
        dict: Словарь, где ключи - символьные коды валют, а значения - их курсы.
              Возвращает None в случае ошибки запроса.
    """
    response = requests.get(url)

    response.raise_for_status()
    data = response.json()
    currencies = {}

    if "Valute" in data:
        for code in currency_codes:
            if code in data["Valute"]:
                currencies[code] = data["Valute"][code]["Value"]
            else:
                currencies[code] = f"Код валюты '{code}' не найден."
    return currencies