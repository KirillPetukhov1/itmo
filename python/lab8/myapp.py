from jinja2 import Environment, PackageLoader, select_autoescape
from models import Author, App, User, Currency, UserCurrency
import time
from datetime import datetime
from http.server import HTTPServer, BaseHTTPRequestHandler
from urllib.parse import parse_qs
from utils.currencies_api import get_currencies


APP_NAME = 'CurrenciesListApp'
CURRENCY_LIST = ['USD', 'EUR', 'RUB', 'GBP', 'JPY', 'CHF', 'CNY', 'AUD', 'CAD', 'INR']
CURRENCY_PARAMS = {
    'USD': [840, "Доллар США"],
    'EUR': [978, "Евро"],
    'RUB': [643, "Российский рубль"],
    'GBP': [826, "Фунт стерлингов"],
    'JPY': [392, "Японская иена"],
    'CHF': [756, "Швейцарский франк"],
    'CNY': [156, "Китайский юань"],
    'AUD': [36, "Австралийский доллар"],
    'CAD': [124, "Канадский доллар"],
    'INR': [356, "Индийская рупия"]
}


def get_user_page_data(user_id: int, users: list[User], currencies: list[Currency], user_currencies: list[UserCurrency]):
    """
    Подготавливает данные для рендеринга страницы пользователя

    Args:
        user_id (int): ID пользователя
        users (list[User]): список объектов User
        currencies (list[Currency]): список объектов Currency
        user_currencies (list[UserCurrency]): список объектов UserCurrency
    """

    user = [u for u in users if u.id == user_id]
    print(user, user_id, *[u.id for u in users])
    user_subs = []

    if user:
        user = user[0]
        for sub in user_currencies:
            if sub.user_id == user_id:
                for cur in currencies:
                    if cur.id == sub.currency_id:
                        user_subs.append([sub, cur])

    return {
        'user_id': user_id,
        'user': user,
        'user_subs': user_subs
    }


def datetimeformat(value):
    """Форматирует timestamp в читаемую дату"""
    if isinstance(value, (int, float)):
        return datetime.fromtimestamp(value).strftime('%d.%m.%Y %H:%M:%S')
    return value


def durationformat(value):
    """Форматирует длительность в секундах в читаемый формат"""
    if isinstance(value, (int, float)):
        days = value // (24 * 3600)
        hours = (value % (24 * 3600)) // 3600
        minutes = (value % 3600) // 60
        seconds = value % 60

        parts = []
        if days > 0:
            parts.append(f"{int(days)} д.")
        if hours > 0:
            parts.append(f"{int(hours)} ч.")
        if minutes > 0:
            parts.append(f"{int(minutes)} мин.")
        if seconds > 0 or not parts:
            parts.append(f"{int(seconds)} сек.")

        return ' '.join(parts)
    return value


def get_new_currencies_list() -> list[Currency]:
    result = []
    i = 1
    for code, value in get_currencies(CURRENCY_LIST).items():
        if not isinstance(value, float):
            value = 1.0
        result.append(Currency(
            id=i,
            num_code=CURRENCY_PARAMS[code][0],
            char_code=code,
            name=CURRENCY_PARAMS[code][1],
            value=value,
            nominal=1))
        i += 1
    return result


env = Environment(
    loader=PackageLoader("myapp"),
    autoescape=select_autoescape()
)
env.filters['datetimeformat'] = datetimeformat
env.filters['durationformat'] = durationformat


main_tm = env.get_template("pages/main_page.html")
author_tm = env.get_template('pages/author_page.html')
users_tm = env.get_template('pages/users_page.html')
user_tm = env.get_template('pages/user_page.html')
currencies_tm = env.get_template('pages/currencies_page.html')

main_author = Author('Kirill Petukhov', 'P3124',
                     'https://github.com/KirillPetukhov1')
app = App('CurrenciesListApp', '1.0', main_author)
users = [
    User(1, "Иван Иванов", "ivan@example.com"),
    User(2, "Петр Петров", "petr@example.com"),
    User(3, "Мария Сидорова", "maria@example.com"),
    User(4, "Анна Козлова", "anna@example.com"),
    User(5, "Сергей Смирнов", "sergey@example.com"),
    User(6, "Ольга Новикова", "olga@example.com"),
    User(7, "Дмитрий Морозов", "dmitry@example.com"),
    User(8, "Екатерина Волкова", "ekaterina@example.com"),
    User(9, "Алексей Лебедев", "alexey@example.com"),
    User(10, "Наталья Павлова", "natalya@example.com")
]
user_currencies = [
    UserCurrency(1, 1, 1, 1735678800, 1738357200),
    UserCurrency(2, 2, 1, 1735765200, 1738443600),
    UserCurrency(3, 4, 1, 1735851600, 1738530000),
    UserCurrency(4, 1, 2, 1735678800, 1738357200),
    UserCurrency(5, 5, 2, 1735765200, 1738443600),
    UserCurrency(6, 2, 3, 1735678800, 1738357200),
    UserCurrency(7, 6, 3, 1735765200, 1738443600),
    UserCurrency(8, 7, 3, 1735851600, 1738530000),
    UserCurrency(9, 1, 4, 1735678800, 1738357200),
    UserCurrency(10, 2, 5, 1735765200, 1738443600),
    UserCurrency(11, 8, 5, 1735851600, 1738530000),
    UserCurrency(12, 9, 5, 1735938000, 1738616400),
    UserCurrency(13, 1, 6, 1735678800, 1738357200),
    UserCurrency(14, 2, 6, 1735765200, 1738443600),
    UserCurrency(15, 10, 7, 1735678800, 1738357200),
    UserCurrency(16, 1, 8, 1735765200, 1738443600),
    UserCurrency(17, 4, 8, 1735851600, 1738530000),
    UserCurrency(18, 5, 9, 1735678800, 1738357200),
    UserCurrency(19, 6, 9, 1735765200, 1738443600),
    UserCurrency(20, 7, 10, 1735851600, 1738530000)
]


class SimpleHTTPRequestHandler(BaseHTTPRequestHandler):

    def do_GET(self):
        path = self.path.split('/')[1:]
        if path[0] == '':
            page = main_tm.render(myapp=app.name,
                                  author=app.author,
                                  )
            self.__send_ok_html_response(page)
        elif path[0] == 'users':
            page = users_tm.render(myapp=app.name,
                                   author=app.author,
                                   users=users
                                   )
            self.__send_ok_html_response(page)
        elif path[0].split('?')[0] == 'user':
            user_id = int(path[0].split('?')[1].split('&')[0].split('=')[1])
            page = user_tm.render(myapp=app.name,
                                  author=app.author,
                                  **get_user_page_data(
                                      user_id,
                                      users,
                                      get_new_currencies_list(),
                                      user_currencies
                                  )
                                  )
            self.__send_ok_html_response(page)
        elif path[0] == 'currencies':
            page = currencies_tm.render(myapp=app.name,
                                        author=app.author,
                                        currencies=get_new_currencies_list()
                                        )
            self.__send_ok_html_response(page)
        elif path[0] == 'author':
            page = author_tm.render(myapp=app.name,
                                    author=app.author,
                                    )
            self.__send_ok_html_response(page)
        else:
            self.send_error(404)

    def __send_ok_html_response(self, html_template: str):
        self.send_response(200)
        self.send_header('Content-Type', 'text/html; charset=utf-8')
        self.end_headers()
        self.wfile.write(bytes(html_template, "utf-8"))


if __name__ == '__main__':
    httpd = HTTPServer(('localhost', 8080), SimpleHTTPRequestHandler)
    print('server is running')
    httpd.serve_forever()
