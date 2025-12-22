# Лабораторная работа 7. Логирование и обработка ошибок в Python

## Цели работы
* Освоить принципы разработки декораторов с параметрами;
* Научиться разделять ответственность функций (бизнес-логика) и декораторов (сквозная логика);
* Научиться обрабатывать исключения, возникающие при работе с внешними API;
* Освоить логирование в разные типы потоков (sys.stdout, io.StringIO, logging);
* Научиться тестировать функцию и поведение логирования.

## Исходный код декоратора с параметрами

```python
def logger(func=None, *, handle=sys.stdout):
    if func is None:
        return lambda func: logger(func, handle=handle)

    @functools.wraps(func)
    def inner(*args, **kwargs):
        if type(handle) is logging.Logger:
            handle.info(
                'Calling a function with arguments: '
                + serialize_list(args)
                + ' '
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
                + ' '
                + serialize_dict(kwargs)
                + '\n'
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
                + '\n'
            )

        return result

    return inner
```

## Исходный код `get_currencies`

```python
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
```

## Демонстрационный пример (квадратное уравнение)

```python
@logger(handle=logging.getLogger("demo"))
def solve_quadratic(a, b, c):
    # Ошибка типов
    for name, value in zip(("a", "b", "c"), (a, b, c)):
        if not isinstance(value, (int, float)):
            raise TypeError(f"Coefficient '{name}' must be numeric")

    # Ошибка: a == 0
    if a == 0 and b == 0:
        raise NonExistentEquationError("a and b cannot be zero")

    d = b*b - 4*a*c

    if d < 0:
        raise NegativeDiscriminantError("the equation does not exist")

    if d == 0:
        x = -b / (2*a)
        return (x,)

    root1 = (-b + math.sqrt(d)) / (2*a)
    root2 = (-b - math.sqrt(d)) / (2*a)
    return root1, root2
```

## Фрагменты логов

### Демо
```
INFO: Calling a function with arguments: 1 4 3 
INFO: The function is executed with the result: (-1.0, -3.0)
INFO: Calling a function with arguments: 1 2 5 
WARNING: Warning discriminant is negative: the equation does not exist
INFO: Calling a function with arguments: gs 2 5 
ERROR: Unexpected error: Coefficient 'a' must be numeric
INFO: Calling a function with arguments: 0 0 0 
CRITICAL: Error the equation does not exist: a and b cannot be zero
```

### `get_currencies`
```
INFO: Calling a function with arguments: ['USD'] 
ERROR: The API is unavailable: Connection failed
INFO: Calling a function with arguments: ['USD', 'XYZ', 'EUR'] url=https://www.cbr-xml-daily.ru/daily_json.js
INFO: The function is executed with the result: USD=79.3146 XYZ=Код валюты 'XYZ' не найден. EUR=92.8621
```

## Тесты

### Тесты функции

```python
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
```

### Тесты декоратора с логгером
```python
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
```

### Тесты декоратора с `StringIO`

```python
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
```

![alt text](image.png)