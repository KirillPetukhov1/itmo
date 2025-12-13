

class Author:
    """
    Модель, представляющая автора проекта.
    """

    def __init__(self, name: str, group: str, github_url: str) -> None:
        """
        Инициализирует объект Author.

        Args:
            name (str): Имя автора (не может быть пустым, минимум 2 символа)
            group (str): Учебная группа (не может быть пустым, минимум 5 символов)
            github_url (str): Ссылка на GitHub профиль (не может быть пустым, минимум 10 символов)

        Raises:
            ValueError: Если имя пустое или слишком короткое
            ValueError: Если группа не соответствует формату
            ValueError: Если github_url не является валидным URL GitHub
        """
        self.name = name
        self.group = group
        self.github_url = github_url

    @property
    def name(self) -> str:
        """
        Returns:
            str: Имя автора
        """
        return self.__name

    @property
    def group(self) -> str:
        """
        Returns:
            str: Учебная группа
        """
        return self.__group

    @property
    def github_url(self) -> str:
        """
        Returns:
            str: Ссылка на GitHub
        """
        return self.__github_url

    @name.setter
    def name(self, value: str) -> None:
        """
        Args:
            value (str): Новое имя автора

        Raises:
            ValueError: Если имя пустое или слишком короткое
            TypeError: Если имя не является строкой
        """
        if not isinstance(value, str):
            raise TypeError("Имя должно быть строкой")

        value = value.strip()
        if not value:
            raise ValueError("Имя не может быть пустым")
        if len(value) < 2:
            raise ValueError("Имя должно содержать минимум 2 символа")

        self.__name = value

    @group.setter
    def group(self, value: str) -> None:
        """
        Args:
            value (str): Новая учебная группа

        Raises:
            ValueError: Если группа не соответствует формату
            TypeError: Если группа не является строкой
        """
        if not isinstance(value, str):
            raise TypeError("Группа должна быть строкой")

        value = value.strip()

        if not value:
            raise ValueError("Группа не может быть пустой")
        if len(value) < 5:
            raise ValueError("Группа должна содержать минимум 5 символов")

        self.__group = value

    @github_url.setter
    def github_url(self, value: str) -> None:
        """
        Args:
            value (str): Новая ссылка на GitHub

        Raises:
            ValueError: Если ссылка не является валидным URL GitHub
            TypeError: Если ссылка не является строкой
        """
        if not isinstance(value, str):
            raise TypeError("Ссылка на GitHub должна быть строкой")

        value = value.strip()

        if not value:
            raise ValueError("Ссылка на GitHub не может быть пустой")
        if len(value) < 10:
            raise ValueError(
                "Ссылка на GitHub должна содержать минимум 10 символов")

        self.__github_url = value
