import sys
import os
sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..', '..'))

from controllers.currency_controller import CurrencyController
import unittest
from unittest.mock import MagicMock

class TestCurrencyController(unittest.TestCase):

    def test_list_currencies(self):
        mock_db = MagicMock()
        mock_db._read.return_value = [
            {"id": 1, "num_code": "840", "char_code": "USD", "name": "Доллар США", "value": 80.722, "nominal": 1}
        ]

        controller = CurrencyController(mock_db)
        result = controller.list_currencies()

        self.assertEqual(result[0].char_code, "USD")
        mock_db._read.assert_called_once()

    def test_update_currency(self):
        mock_db = MagicMock()
        controller = CurrencyController(mock_db)

        controller.update_currency("USD", 90)
        mock_db._update.assert_called_once_with({"USD": 90.0})


if __name__ == "__main__":
    unittest.main()