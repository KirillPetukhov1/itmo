import sqlite3


class CurrencyRatesCRUD():
    def __init__(self, currency_rates_obj):
        self.__con = sqlite3.connect(':memory:')
        self.__createtable()
        self.__cursor = self.__con.cursor()
        self.__currency_rates_obj = currency_rates_obj

    def __createtable(self):
        self.__con.execute(
            """CREATE TABLE currency (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    num_code TEXT NOT NULL,
                    char_code TEXT NOT NULL,
                    name TEXT NOT NULL,
                    value FLOAT,
                    nominal INTEGER
                );""")
        self.__con.commit()

    def _create(self):
        __params = self.__currency_rates_obj.values
        __sqlquery = "INSERT INTO currency(num_code, char_code, name, value, nominal) VALUES(:num_code, :char_code, :name, :value, :nominal)"
        self.__cursor.executemany(__sqlquery, [{
            "num_code": i[0],
            "char_code": i[1],
            "name": i[2],
            "value": i[3],
            "nominal": i[4]
        } for i in __params])
        self.__con.commit()

    def _read(self):
        cur = self.__con.execute("SELECT id, num_code, char_code, name, value, nominal FROM currency")
        result_data = []
        for _row in cur:
            _d = {'id': int(_row[0]), 'num_code': _row[1], 'char_code': _row[2], 'name': _row[3], 'value': float(_row[4]), 'nominal': int(_row[5])}
            result_data.append(_d)

        return result_data

    def _delete(self, currency_id):
        sql = "DELETE FROM currency WHERE id = ?"
        self.__cursor.execute(sql, (currency_id))
        self.__con.commit()

        pass

    def _update(self, currency: dict['str': float]):
        # ...._update({'USD': 101.1})
        currency_code = tuple(currency.keys())[0]
        currency_value = tuple(currency.values())[0]
        sql = "UPDATE currency SET value = ? WHERE char_code = ?"
        self.__cursor.execute(sql, (currency_value, currency_code))
        self.__con.commit()

    def __del__(self):
        self.__cursor = None
        self.__con.close()
