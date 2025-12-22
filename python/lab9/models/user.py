

class User:
    """
    Модель, представляющая пользователя системы.
    """
    
    def __init__(self, id: int, name: str, email: str) -> None:
        """
        Инициализирует объект User.
        
        Args:
            id (int): Уникальный идентификатор пользователя
            name (str): Имя пользователя
            email (str): Электронная почта пользователя
            
        Raises:
            ValueError: Если id <= 0
            ValueError: Если name пустая строка или слишком короткое
            ValueError: Если email не соответствует формату email адреса
        """
        self.id = id
        self.name = name
        self.email = email
    
    @property
    def id(self) -> int:
        """
        Returns:
            int: Уникальный идентификатор пользователя
        """
        return self.__id
    
    @property
    def name(self) -> str:
        """
        Returns:
            str: Имя пользователя
        """
        return self.__name
    
    @property
    def email(self) -> str:
        """
        Returns:
            str: Электронная почта пользователя
        """
        return self.__email
    
    @id.setter
    def id(self, value: int) -> None:
        """
        Args:
            value (int): Уникальный идентификатор пользователя
            
        Raises:
            TypeError: Если value не является целым числом
            ValueError: Если value <= 0
        """
        if not isinstance(value, int):
            raise TypeError("Идентификатор должен быть целым числом")
        if value <= 0:
            raise ValueError("Идентификатор должен быть положительным числом")
        self.__id = value
    
    @name.setter
    def name(self, value: str) -> None:
        """
        Args:
            value (str): Имя пользователя
            
        Raises:
            TypeError: Если value не является строкой
            ValueError: Если value пустая строка
            ValueError: Если value короче 2 символов
            ValueError: Если value длиннее 100 символов
        """
        if not isinstance(value, str):
            raise TypeError("Имя должно быть строкой")
        
        value = value.strip()
        if not value:
            raise ValueError("Имя не может быть пустым")
        if len(value) < 2:
            raise ValueError("Имя должно содержать минимум 2 символа")
        if len(value) > 255:
            raise ValueError("Имя не может превышать 255 символов")
        
        self.__name = value
    
    @email.setter
    def email(self, value: str) -> None:
        """
        Args:
            value (str): Электронная почта пользователя
            
        Raises:
            TypeError: Если value не является строкой
            ValueError: Если value длиннее 255 символов
        """
        if not isinstance(value, str):
            raise TypeError("Email должен быть строкой")
        
        value = value.strip().lower()
        if len(value) > 255:
            raise ValueError("Email не может превышать 255 символов")
        
        self.__email = value