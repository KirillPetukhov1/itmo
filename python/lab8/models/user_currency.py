import time


class UserCurrency:
    """
    Модель, представляющая подписку пользователя на валюту.
    """
    
    __start_timestamp = 1
    __end_timestamp = 253402203600
    
    def __init__(self, id: int, currency_id: int, user_id: int, 
                 start_timestamp: int, end_timestamp: int) -> None:
        """
        Инициализирует объект UserCurrency.
        
        Args:
            id (int): Уникальный идентификатор подписки
            currency_id (int): Идентификатор валюты (внешний ключ к Currency)
            user_id (int): Идентификатор пользователя (внешний ключ к User)
            start_timestamp (int): Временная метка начала подписки
            end_timestamp (int): Временная метка окончания подписки (0 - бессрочная)
            
        Raises:
            ValueError: Если id <= 0
            ValueError: Если currency_id <= 0
            ValueError: Если user_id <= 0
            ValueError: Если start_timestamp <= 0
            ValueError: Если end_timestamp < 0
            ValueError: Если end_timestamp <= start_timestamp
        """
        self.id = id
        self.currency_id = currency_id
        self.user_id = user_id
        self.start_timestamp = start_timestamp
        self.end_timestamp = end_timestamp
    
    @property
    def id(self) -> int:
        """
        Returns:
            int: Уникальный идентификатор подписки
        """
        return self.__id
    
    @property
    def currency_id(self) -> int:
        """
        Returns:
            int: Идентификатор валюты
        """
        return self.__currency_id
    
    @property
    def user_id(self) -> int:
        """
        Returns:
            int: Идентификатор пользователя
        """
        return self.__user_id
    
    @property
    def start_timestamp(self) -> int:
        """
        Returns:
            int: Временная метка начала подписки
        """
        return self.__start_timestamp
    
    @property
    def end_timestamp(self) -> int:
        """
        Returns:
            int: Временная метка окончания подписки
        """
        return self.__end_timestamp
    
    @property
    def is_active(self) -> bool:
        """
        Returns:
            bool: Активна ли подписка в текущий момент
        """
        current_time = int(time.time())
        return self.__start_timestamp <= current_time <= self.__end_timestamp
    
    @property
    def duration(self) -> int:
        """
        Returns:
            int: Длительность подписки в секундах
        """
        return self.__end_timestamp - self.__start_timestamp
    
    @id.setter
    def id(self, value: int) -> None:
        """
        Args:
            value (int): Уникальный идентификатор подписки
            
        Raises:
            TypeError: Если value не является целым числом
            ValueError: Если value <= 0
        """
        if not isinstance(value, int):
            raise TypeError("Идентификатор должен быть целым числом")
        if value <= 0:
            raise ValueError("Идентификатор должен быть положительным числом")
        self.__id = value
    
    @currency_id.setter
    def currency_id(self, value: int) -> None:
        """
        Args:
            value (int): Идентификатор валюты
            
        Raises:
            TypeError: Если value не является целым числом
            ValueError: Если value <= 0
        """
        if not isinstance(value, int):
            raise TypeError("Идентификатор валюты должен быть целым числом")
        if value <= 0:
            raise ValueError("Идентификатор валюты должен быть положительным числом")
        self.__currency_id = value
    
    @user_id.setter
    def user_id(self, value: int) -> None:
        """
        Args:
            value (int): Идентификатор пользователя
            
        Raises:
            TypeError: Если value не является целым числом
            ValueError: Если value <= 0
        """
        if not isinstance(value, int):
            raise TypeError("Идентификатор пользователя должен быть целым числом")
        if value <= 0:
            raise ValueError("Идентификатор пользователя должен быть положительным числом")
        self.__user_id = value
    
    @start_timestamp.setter
    def start_timestamp(self, value: int) -> None:
        """
        Args:
            value (int): Временная метка начала подписки
            
        Raises:
            TypeError: Если value не является целым числом
            ValueError: Если value <= 0
            ValueError: Если value > текущего времени (предупреждение)
        """
        if not isinstance(value, int):
            raise TypeError("Временная метка начала должна быть целым числом")
        if value <= 0:
            raise ValueError("Временная метка начала должна быть положительной")
        
        if value > self.__end_timestamp:
            raise ValueError("Временная метка начала не может быть больше метки окончания")
        
        self.__start_timestamp = value
    
    @end_timestamp.setter
    def end_timestamp(self, value: int) -> None:
        """
        Args:
            value (int): Временная метка окончания подписки (0 - бессрочная)
            
        Raises:
            TypeError: Если value не является целым числом
            ValueError: Если value < 0
            ValueError: Если value <= start_timestamp
        """
        if not isinstance(value, int):
            raise TypeError("Временная метка окончания должна быть целым числом")
        if value < 0:
            raise ValueError("Временная метка окончания должна быть неотрицательной")
        
        if value <= self.__start_timestamp:
            raise ValueError("Временная метка окончания должна быть больше метки начала")
        
        self.__end_timestamp = value