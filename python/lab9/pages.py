from models import Author, App, User, Currency, UserCurrency
from jinja2 import Template


APP_NAME = 'CurrenciesListApp'


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
    UserCurrency(15, 9, 7, 1735678800, 1738357200),
    UserCurrency(16, 1, 8, 1735765200, 1738443600),
    UserCurrency(17, 4, 8, 1735851600, 1738530000),
    UserCurrency(18, 5, 9, 1735678800, 1738357200),
    UserCurrency(19, 6, 9, 1735765200, 1738443600),
    UserCurrency(20, 7, 10, 1735851600, 1738530000)
]


def get_main_page(tm: Template) -> str:
    return tm.render(
        myapp=app.name,
        author=app.author,
    )


def get_author_page(tm: Template) -> str:
    return tm.render(
        myapp=app.name,
        author=app.author,
    )


def get_users_page(tm: Template) -> str:
    return tm.render(
        myapp=app.name,
        author=app.author,
        users=users,
    )


def get_user_page(tm: Template, user_id: int, currencies: list[Currency]) -> str:
    return tm.render(
        myapp=app.name,
        author=app.author,
        **get_user_page_data(
            user_id,
            users,
            currencies,
            user_currencies
        ),
    )


def get_currencies_page(tm: Template, currencies: list[Currency]) -> str:
    return tm.render(
        myapp=app.name,
        author=app.author,
        currencies=currencies,
    )
