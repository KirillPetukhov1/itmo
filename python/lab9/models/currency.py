

class Currency:
    """
    Модель, представляющая валюту с её характеристиками.
    """

    def __init__(self, id: int, num_code: str, char_code: str,
                 name: str, value: float, nominal: int) -> None:
        """
        Инициализирует объект Currency.

        Args:
            id (int): Уникальный идентификатор валюты
            num_code (str): Цифровой код валюты
            char_code (str): Символьный код валюты (ISO 4217)
            name (str): Название валюты
            value (float): Курс валюты
            nominal (int): Номинал (за сколько единиц валюты указан курс)

        Raises:
            ValueError: Если id пустая строка
            ValueError: Если num_code не в диапазоне 001-999
            ValueError: Если char_code не соответствует формату ISO 4217
            ValueError: Если name пустая строка
            ValueError: Если value <= 0
            ValueError: Если nominal <= 0
        """
        self.id = id
        self.num_code = num_code
        self.char_code = char_code
        self.name = name
        self.value = value
        self.nominal = nominal

    @property
    def id(self) -> str:
        """
        Returns:
            str: Уникальный идентификатор валюты
        """
        return self.__id

    @property
    def num_code(self) -> str:
        """
        Returns:
            int: Цифровой код валюты
        """
        return self.__num_code

    @property
    def char_code(self) -> str:
        """
        Returns:
            str: Символьный код валюты (ISO 4217)
        """
        return self.__char_code

    @property
    def name(self) -> str:
        """
        Returns:
            str: Название валюты
        """
        return self.__name

    @property
    def value(self) -> float:
        """
        Returns:
            float: Курс валюты
        """
        return self.__value

    @property
    def nominal(self) -> int:
        """
        Returns:
            int: Номинал валюты
        """
        return self.__nominal

    @property
    def unit_value(self) -> float:
        """
        Returns:
            float: Курс за 1 единицу валюты
        """
        return self.__value / self.__nominal

    @id.setter
    def id(self, value: str) -> None:
        """
        Args:
            value (int): Уникальный идентификатор валюты

        Raises:
            TypeError: Если value не является целым числом
            ValueError: Если value меньше нуля
        """
        if not isinstance(value, int):
            raise TypeError("Идентификатор должен быть целым числом")
        if value < 1:
            raise ValueError("Идентификатор не может быть меньше 1")
        self.__id = value

    @num_code.setter
    def num_code(self, value: str) -> None:
        """
        Args:
            value (str): Цифровой код валюты

        Raises:
            TypeError: Если value не является целым числом
            ValueError: Если value не в диапазоне 1-999
        """
        if not isinstance(value, str):
            raise TypeError("Цифровой код должен быть строкой")
        if not len(value) == 3:
            raise ValueError("Цифровой код должен быть в диапазоне 001-999")
        self.__num_code = value

    @char_code.setter
    def char_code(self, value: str) -> None:
        """
        Args:
            value (str): Символьный код валюты (ISO 4217)

        Raises:
            TypeError: Если value не является строкой
            ValueError: Если value не состоит из 3 латинских букв
        """
        if not isinstance(value, str):
            raise TypeError("Символьный код должен быть строкой")
        value = value.strip().upper()
        if len(value) != 3 or not value.isalpha():
            raise ValueError(
                "Символьный код должен состоять из 3 латинских букв")
        self.__char_code = value

    @name.setter
    def name(self, value: str) -> None:
        """
        Args:
            value (str): Название валюты

        Raises:
            TypeError: Если value не является строкой
            ValueError: Если value пустая строка
        """
        if not isinstance(value, str):
            raise TypeError("Название валюты должно быть строкой")
        if not value.strip():
            raise ValueError("Название валюты не может быть пустым")
        self.__name = value.strip()

    @value.setter
    def value(self, value: float) -> None:
        """
        Args:
            value (float): Курс валюты

        Raises:
            TypeError: Если value не является числом
            ValueError: Если value <= 0
        """
        if not isinstance(value, (int, float)):
            raise TypeError("Курс должен быть числом")
        if value <= 0:
            raise ValueError("Курс должен быть положительным числом")
        self.__value = float(value)

    @nominal.setter
    def nominal(self, value: int) -> None:
        """
        Args:
            value (int): Номинал валюты

        Raises:
            TypeError: Если value не является целым числом
            ValueError: Если value <= 0
        """
        if not isinstance(value, int):
            raise TypeError("Номинал должен быть целым числом")
        if value <= 0:
            raise ValueError("Номинал должен быть положительным числом")
        self.__nominal = value

    def __str__(self) -> str:
        """
        Возвращает строковое представление объекта Currency.

        Returns:
            str: Строковое представление валюты
        """
        return (f"{self.id}: {self.char_code} "
                f"({self.num_code}) - {self.name}, "
                f"курс: {self.value:.4f} за {self.nominal} ")
