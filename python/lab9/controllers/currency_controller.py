from controllers.database_controller import CurrencyRatesCRUD
from models.currency import Currency

class CurrencyController:
    def __init__(self, db_controller: CurrencyRatesCRUD):
        self.db = db_controller

    def list_currencies(self):
        return [Currency(**kwargs) for kwargs in self.db._read()]

    def update_currency(self, char_code: str, value: float):
        try:
            self.db._update({char_code: float(value)})
        except ValueError:
            pass # залогировать

    def delete_currency(self, currency_id: str):
        self.db._delete(currency_id)