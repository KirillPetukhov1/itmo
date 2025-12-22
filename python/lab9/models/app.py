from .author import Author


class App:
    """
    Модель, представляющая приложение.
    """

    def __init__(self, name: str, version: str, author: Author) -> None:
        """
        Инициализирует объект App.

        Args:
            name (str): Название приложения (не может быть пустой строкой)
            version (str): Версия приложения (не может быть пустой строкой)
            author (Author): Объект Author, представляющий автора приложения

        Raises:
            ValueError: Если name или version - пустые строки
            TypeError: Если author не является объектом Author
        """
        if not name or not isinstance(name, str):
            raise ValueError("Название приложения не может быть пустым")
        if not version or not isinstance(version, str):
            raise ValueError("Версия приложения не может быть пустой")
        if not isinstance(author, Author):
            raise TypeError("Автор должен быть объектом класса Author")

        self.__name = name
        self.__version = version
        self.__author = author

    @property
    def name(self) -> str:
        """
        Returns:
            str: Название приложения
        """
        return self.__name

    @property
    def version(self) -> str:
        """
        Returns:
            str: Версия приложения
        """
        return self.__version

    @property
    def author(self) -> Author:
        """
        Returns:
            Author: Объект автора приложения
        """
        return self.__author

    @name.setter
    def name(self, value: str) -> None:
        """ 
        Args:
            value (str): Новое название приложения

        Raises:
            ValueError: Если новое название - пустая строка
        """
        if not value or not isinstance(value, str):
            raise ValueError("Название приложения не может быть пустым")
        self.__name = value

    @version.setter
    def version(self, value: str) -> None:
        """
        Args:
            value (str): Новая версия приложения

        Raises:
            ValueError: Если новая версия - пустая строка
        """
        if not value or not isinstance(value, str):
            raise ValueError("Версия приложения не может быть пустой")
        self.__version = value

    @author.setter
    def author(self, value: Author) -> None:
        """
        Args:
            value (Author): Новый объект Author

        Raises:
            TypeError: Если value не является объектом Author
        """
        if not isinstance(value, Author):
            raise TypeError("Автор должен быть объектом класса Author")
        self.__author = value
