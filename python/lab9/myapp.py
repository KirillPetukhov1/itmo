from jinja2 import Environment, PackageLoader, select_autoescape
import pages
from utils.jinja_formats import datetimeformat, durationformat
from controllers.database_controller import CurrencyRatesCRUD
from controllers.currency_controller import CurrencyController
from http.server import HTTPServer, BaseHTTPRequestHandler
from urllib.parse import parse_qs


class CurrencyRatesMock():
    def __init__(self):
        self.__values = [
            ('840', 'USD', 'Доллар США', 80.722, 1),
            ('978', 'EUR', 'Евро', 94.512, 1),
            ('826', 'GBP', 'Фунт стерлингов', 108.1271, 1),
            ('392', 'JPY', 'Японская иена', 51.8346, 1),
            ('756', 'CHF', 'Швейцарский франк', 101.5371, 1),
            ('156', 'CNY', 'Китайский юань', 11.4463, 1),
            ('036', 'AUD', 'Австралийский доллар', 53.3492, 1),
            ('124', 'CAD', 'Канадский доллар', 58.6046, 1),
            ('356', 'INR', 'Индийская рупия', 89.4947, 1)
        ]

    @property
    def values(self):
        return self.__values


c_r_controller = CurrencyRatesCRUD(CurrencyRatesMock())
c_r_controller._create()

c_controller = CurrencyController(c_r_controller)


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


class SimpleHTTPRequestHandler(BaseHTTPRequestHandler):

    def do_GET(self):
        url_query_dict = parse_qs(self.path.rpartition('?')[-1])
        if self.path == '/':
            page = pages.get_main_page(main_tm)
            self.__send_ok_html_response(page)

        elif 'author' in self.path:
            page = pages.get_author_page(author_tm)
            self.__send_ok_html_response(page)

        elif 'users' in self.path:
            page = pages.get_users_page(users_tm)
            self.__send_ok_html_response(page)

        elif 'user' in self.path:
            page = pages.get_user_page(user_tm,
                                       int(url_query_dict['id'][0][0]),
                                       c_controller.list_currencies())
            self.__send_ok_html_response(page)

        elif 'currencies' in self.path:
            page = pages.get_currencies_page(
                currencies_tm, c_controller.list_currencies())
            self.__send_ok_html_response(page)

        elif 'currency/delete' in self.path:
            c_controller.delete_currency(url_query_dict['id'][0][0])
            self.__send_redirect_response('/currencies')

        elif 'currency/update' in self.path:
            c_controller.update_currency(
                str(list(url_query_dict.keys())[0]).upper(),
                list(url_query_dict.values())[0][0]
            )
            self.__send_redirect_response('/currencies')

        elif 'currency/show' in self.path:
            for cur in c_controller.list_currencies():
                print(cur)
            self.__send_redirect_response('/')

        else:
            self.send_error(404)

    def __send_ok_html_response(self, html_template: str):
        self.send_response(200)
        self.send_header('Content-Type', 'text/html; charset=utf-8')
        self.end_headers()
        self.wfile.write(bytes(html_template, "utf-8"))

    def __send_redirect_response(self, url: str):
        self.send_response(302)
        self.send_header("Location", url)
        self.end_headers()


if __name__ == '__main__':
    httpd = HTTPServer(('localhost', 8080), SimpleHTTPRequestHandler)
    print('server is running')
    httpd.serve_forever()
