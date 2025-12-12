import io
import logging
import unittest
from unittest.mock import patch, Mock
import requests
from requests.exceptions import ConnectionError, Timeout
from main import get_currencies, logger


class TestGetCurrencies(unittest.TestCase):
    """Тестирование функции get_currencies"""
    
    @patch('main.requests.get')
    def test_get_currencies_success(self, mock_get):
        """Тест корректного возврата реальных курсов"""
        mock_response = Mock()
        mock_response.json.return_value = {
            "Valute": {
                "USD": {"Value": 75.5},
                "EUR": {"Value": 85.3},
                "GBP": {"Value": 95.7}
            }
        }
        mock_get.return_value = mock_response
        
        result = get_currencies(['USD', 'EUR', 'GBP'])
        
        expected = {
            'USD': 75.5,
            'EUR': 85.3,
            'GBP': 95.7
        }
        self.assertEqual(result, expected)
    
    @patch('main.requests.get')
    def test_get_currencies_nonexistent_currency(self, mock_get):
        """Тест поведения при несуществующей валюте"""
        mock_response = Mock()
        mock_response.json.return_value = {
            "Valute": {
                "USD": {"Value": 75.5},
                "EUR": {"Value": 85.3}
            }
        }
        mock_get.return_value = mock_response
        
        result = get_currencies(['USD', 'XYZ', 'EUR'])
        
        self.assertEqual(result['USD'], 75.5)
        self.assertEqual(result['EUR'], 85.3)
        self.assertIn('не найден', result['XYZ'])
    
    @patch('main.requests.get')
    def test_connection_error(self, mock_get):
        """Тест выброса ConnectionError"""
        mock_get.side_effect = ConnectionError("Connection failed")
        
        with self.assertRaises(ConnectionError):
            get_currencies(['USD'])
    
    def test_invalid_url_value_error(self):
        """Тест выброса ValueError при неверном URL"""
        with self.assertRaises(ValueError):
            get_currencies(['USD'], url='invalid_url')
    
    @patch('main.requests.get')
    def test_key_error_in_response(self, mock_get):
        """Тест KeyError при отсутствии ключа Valute в ответе"""
        mock_response = Mock()
        mock_response.json.return_value = {}
        mock_response.raise_for_status = Mock()
        mock_get.return_value = mock_response
        
        result = get_currencies(['USD'])
        self.assertEqual(result, {})


class TestLoggerDecorator(unittest.TestCase):
    """Тестирование поведения декоратора-логгера"""
    
    def setUp(self):
        self.stream = io.StringIO()
    
    def test_logger_successful_execution(self):
        """Тест логов при успешном выполнении"""
        @logger(handle=self.stream)
        def test_func(x, y=10):
            return x * y
        
        result = test_func(5, y=2)
        
        log_output = self.stream.getvalue()
        
        # Проверяем результат функции
        self.assertEqual(result, 10)
        
        # Проверяем наличие сообщений о старте и окончании
        self.assertIn('Calling a function with arguments:', log_output)
        self.assertIn('The function is executed with the result:', log_output)
        
        # Проверяем запись аргументов
        self.assertIn('5', log_output)
        self.assertIn('y=2', log_output)
        
        # Проверяем запись результата
        self.assertIn('10', log_output)
    
    def test_logger_error_handling(self):
        """Тест логов при ошибках"""
        @logger(handle=self.stream)
        def test_func():
            raise ValueError("Test error")
        
        # Проверяем, что исключение пробрасывается
        with self.assertRaises(ValueError):
            test_func()
        
        log_output = self.stream.getvalue()
        
        # Проверяем наличие ERROR сообщения (для StringIO это просто текст)
        self.assertRegex(log_output, r"Unexpected error")
        self.assertIn("Test error", log_output)
    
    def test_logger_with_logging_module_success(self):
        """Тест с использованием logging.Logger"""
        # Создаем отдельный логгер для теста
        test_logger = logging.getLogger("test_logger")
        test_logger.setLevel(logging.INFO)
        
        # Используем StringIO для захвата логов
        log_stream = io.StringIO()
        handler = logging.StreamHandler(log_stream)
        handler.setFormatter(logging.Formatter('%(levelname)s: %(message)s'))
        test_logger.addHandler(handler)
        
        @logger(handle=test_logger)
        def test_func(x):
            return x * 3
        
        result = test_func(4)
        
        self.assertEqual(result, 12)
        
        log_output = log_stream.getvalue()
        self.assertIn("INFO: Calling a function with arguments:", log_output)
        self.assertIn("INFO: The function is executed with the result:", log_output)
    
    def test_logger_with_logging_module_error(self):
        """Тест ошибок с использованием logging.Logger"""
        test_logger = logging.getLogger("test_logger2")
        test_logger.setLevel(logging.INFO)
        
        log_stream = io.StringIO()
        handler = logging.StreamHandler(log_stream)
        handler.setFormatter(logging.Formatter('%(levelname)s: %(message)s'))
        test_logger.addHandler(handler)
        
        @logger(handle=test_logger)
        def test_func():
            raise ConnectionError("API unavailable")
        
        with self.assertRaises(ConnectionError):
            test_func()
        
        log_output = log_stream.getvalue()
        self.assertRegex(log_output, r"ERROR: The API is unavailable")
    
    @patch('main.requests.get')
    def test_logger_integration_with_get_currencies(self, mock_get):
        """Интеграционный тест декоратора с функцией get_currencies"""
        mock_response = Mock()
        mock_response.json.return_value = {
            "Valute": {"USD": {"Value": 75.5}}
        }
        mock_get.return_value = mock_response
        
        # Тестируем с StringIO
        stream = io.StringIO()
        
        # Создаем декорированную версию функции
        decorated_func = logger(handle=stream)(get_currencies)
        
        result = decorated_func(['USD'])
        
        # Проверяем результат
        self.assertEqual(result, {'USD': 75.5})
        
        # Проверяем логи
        log_output = stream.getvalue()
        self.assertIn('Calling a function with arguments:', log_output)
        self.assertIn('The function is executed with the result:', log_output)
        self.assertIn('USD=75.5', log_output)


if __name__ == '__main__':
    unittest.main()