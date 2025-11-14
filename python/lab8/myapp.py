from jinja2 import Environment, PackageLoader, select_autoescape
from models import Author

from http.server import HTTPServer, BaseHTTPRequestHandler
from urllib.parse import parse_qs

APP_NAME = 'CurrenciesListApp'

env = Environment(
    loader=PackageLoader("myapp"),
    autoescape=select_autoescape()
)

home_template = env.get_template("index.html")
author_template = env.get_template('author.html')

main_author = Author('Kirill Petukhov', 'P3124', 'https://github.com/KirillPetukhov1')

home_page = home_template.render(myapp=APP_NAME,
                            navigation=[{'caption': 'Основная страница',
                                         'href': "https://nickzhukov.ru"},
                                        {'caption': 'Об авторе',
                                         'href': "/author"}],
                            author_name=main_author.name,
                            group=main_author.group
                            )
author_page = author_template.render(myapp=APP_NAME,
                              author_name=main_author.name,
                              group=main_author.group,
                              github_url=main_author.github_url
                              )


class SimpleHTTPRequestHandler(BaseHTTPRequestHandler):

    def do_GET(self):
        global home_page, author_page
        path = self.path.split('/')[1:]
        if path[0] == '':
            self.__send_ok_html_response(home_page)
        elif path[0] == 'author':
            self.__send_ok_html_response(author_page)
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
