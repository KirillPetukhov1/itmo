import logging
import sys
import functools
import requests


def serialize_list(lst: list) -> str:
    return ' '.join([str(i) for i in lst])


def serialize_dict(dct: dict) -> str:
    return ' '.join([str(i) + ': ' + str(dct[i]) for i in dct])


def logger(func=None, *, handle=sys.stdout):
    if func is None:
        return lambda func: logger(func, handle=handle)

    @functools.wraps(func)
    def inner(*args, **kwargs):
        if type(handle) is logging.Logger:
            handle.info(
                'Calling a function with arguments: '
                + serialize_list(args)
                + serialize_dict(kwargs)
            )
            try:
                result = func(*args, **kwargs)
            except requests.exceptions.ConnectionError as e:
                handle.error(f"The API is unavailable: {e}")
                raise
            except requests.exceptions.Timeout as e:
                handle.error(f"The request timed out: {e}")
                raise
            except requests.exceptions.InvalidURL as e:
                handle.error(f"The URL provided was somehow invalid: {e}")
                raise
            except requests.exceptions.RequestException as e:
                handle.error(f"Error when requesting the API: {e}")
                raise
            except Exception as e:
                handle.error(f"Unexpected error: {e}")
                raise
            handle.info(
                'The function is executed with the result: '
                + serialize_dict(result)
            )
        else:
            handle.write(
                'Calling a function with arguments: '
                + serialize_list(args)
                + serialize_dict(kwargs)
            )
            try:
                result = func(*args, **kwargs)
            except requests.exceptions.ConnectionError as e:
                handle.write(f"The API is unavailable: {e}")
                raise
            except requests.exceptions.Timeout as e:
                handle.write(f"The request timed out: {e}")
                raise
            except requests.exceptions.InvalidURL as e:
                handle.write(f"The URL provided was somehow invalid: {e}")
                raise
            except requests.exceptions.RequestException as e:
                handle.write(f"Error when requesting the API: {e}")
                raise
            except Exception as e:
                handle.write(f"Unexpected error: {e}")
                raise
            handle.write(
                'The function is executed with the result: '
                + serialize_dict(result)
            )

        return result

    return inner


logging.basicConfig(
    filename="python\\lab7\\lab7.log",
    level=logging.INFO,
    format="%(levelname)s: %(message)s"
)


@logger(handle=logging.getLogger("currency"))
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


# Пример использования функции:
currency_list = ['USD', 'EUR', 'GBP', 'NNZ']

currency_data = get_currencies(
    currency_list, url='https://www.cbr-xml-daily.ru/daily_json.js')
if currency_data:
    print(currency_data)
