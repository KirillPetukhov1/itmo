class Author():
    """author"""

    def __init__(self, name: str, group: str, github_url: str):
        self.__name: str = name
        self.__group: str = group
        self.__github_url: str = github_url

    @property
    def name(self):
        return self.__name

    @name.setter
    def name(self, name: str):
        if type(name) is str and len(name) >= 2:
            self.__name = name
        else:
            raise ValueError('Ошибка при задании имени автора')

    @property
    def group(self):
        return self.__group

    @group.setter
    def group(self, group: str):
        if type(group) is str and len(group) >= 5:
            self.__group = group
        else:
            raise ValueError('Ошибка при задании группы автора')
        
    @property
    def github_url(self):
        return self.__github_url

    @github_url.setter
    def github_url(self, github_url: str):
        if type(github_url) is str and len(github_url) >= 5:
            self.__github_url = github_url
        else:
            raise ValueError('Ошибка при задании ссылки на github')
