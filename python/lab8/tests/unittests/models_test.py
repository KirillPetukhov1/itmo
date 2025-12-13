import sys
import os
sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..', '..'))

from models import Author, App, User, Currency, UserCurrency
import time
import pytest


class TestApp:
    """Тесты для модели App"""
    
    def test_app_creation_success(self):
        """Тест успешного создания объекта App"""
        author = Author("Иван Иванов", "ИТ-201", "https://github.com/ivanov")
        app = App("MyApp", "1.0.0", author)
        
        assert app.name == "MyApp"
        assert app.version == "1.0.0"
        assert app.author == author
    
    def test_app_validation_failures(self):
        """Тест валидации при создании App"""
        author = Author("Иван Иванов", "ИТ-201", "https://github.com/ivanov")
        
        with pytest.raises(ValueError, match="Название приложения не может быть пустым"):
            App("", "1.0.0", author)
        
        with pytest.raises(ValueError, match="Версия приложения не может быть пустой"):
            App("MyApp", "", author)
        
        with pytest.raises(TypeError, match="Автор должен быть объектом класса Author"):
            App("MyApp", "1.0.0", "not an author")


class TestAuthor:
    """Тесты для модели Author"""
    
    def test_author_creation_success(self):
        """Тест успешного создания объекта Author"""
        author = Author("Иван Иванов", "ИТ-201", "https://github.com/ivanov")
        
        assert author.name == "Иван Иванов"
        assert author.group == "ИТ-201"
        assert author.github_url == "https://github.com/ivanov"
    
    def test_author_validation_failures(self):
        """Тест валидации при создании Author"""
        with pytest.raises(ValueError, match="Имя должно содержать минимум 2 символа"):
            Author("И", "ИТ-201", "https://github.com/ivanov")
        
        with pytest.raises(ValueError, match="Группа должна содержать минимум 5 символов"):
            Author("Иван", "ИТ", "https://github.com/ivanov")
        
        with pytest.raises(ValueError, match="Ссылка на GitHub должна содержать минимум 10 символов"):
            Author("Иван Иванов", "ИТ-201", "github")


class TestCurrency:
    """Тесты для модели Currency"""
    
    def test_currency_creation_success(self):
        """Тест успешного создания объекта Currency"""
        currency = Currency(1, 840, "USD", "Доллар США", 91.45, 1)
        
        assert currency.id == 1
        assert currency.num_code == 840
        assert currency.char_code == "USD"
        assert currency.name == "Доллар США"
        assert currency.value == 91.45
        assert currency.nominal == 1
        assert currency.unit_value == 91.45
    
    def test_currency_validation_failures(self):
        """Тест валидации при создании Currency"""
        with pytest.raises(ValueError, match="Цифровой код должен быть в диапазоне 1-999"):
            Currency(1, 1000, "USD", "Доллар США", 91.45, 1)
        
        with pytest.raises(ValueError, match="Символьный код должен состоять из 3 латинских букв"):
            Currency(1, 840, "US", "Доллар США", 91.45, 1)
        
        with pytest.raises(ValueError, match="Курс должен быть положительным числом"):
            Currency(1, 840, "USD", "Доллар США", -10, 1)


class TestUser:
    """Тесты для модели User"""
    
    def test_user_creation_success(self):
        """Тест успешного создания объекта User"""
        user = User(1, "Иван Иванов", "ivan@example.com")
        
        assert user.id == 1
        assert user.name == "Иван Иванов"
        assert user.email == "ivan@example.com"
    
    def test_user_validation_failures(self):
        """Тест валидации при создании User"""
        with pytest.raises(ValueError, match="Имя должно содержать минимум 2 символа"):
            User(1, "И", "ivan@example.com")
        
        long_email = "a" * 256 + "@example.com"
        with pytest.raises(ValueError, match="Email не может превышать 255 символов"):
            User(1, "Иван", long_email)
        
        with pytest.raises(ValueError, match="Идентификатор должен быть положительным числом"):
            User(0, "Иван Иванов", "ivan@example.com")


class TestUserCurrency:
    """Тесты для модели UserCurrency"""
    
    def test_user_currency_creation_success(self):
        """Тест успешного создания объекта UserCurrency"""
        current_time = int(time.time())
        future_time = current_time + 86400  # +1 день
        
        user_currency = UserCurrency(1, 1, 1, current_time, future_time)
        
        assert user_currency.id == 1
        assert user_currency.currency_id == 1
        assert user_currency.user_id == 1
        assert user_currency.start_timestamp == current_time
        assert user_currency.end_timestamp == future_time
        assert user_currency.duration == 86400
    
    def test_user_currency_validation_failures(self):
        """Тест валидации при создании UserCurrency"""
        current_time = int(time.time())
        
        with pytest.raises(ValueError, match="Временная метка окончания должна быть больше метки начала"):
            UserCurrency(1, 1, 1, current_time, current_time - 100)
        
        with pytest.raises(ValueError, match="Временная метка начала должна быть положительной"):
            UserCurrency(1, 1, 1, -1, current_time + 100)
        
        with pytest.raises(ValueError, match="Временная метка окончания должна быть неотрицательной"):
            UserCurrency(1, 1, 1, current_time, -1)
            
            
if __name__ == "__main__":
    pytest.main([__file__, "-v"])