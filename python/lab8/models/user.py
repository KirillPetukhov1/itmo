class User():
    def __init__(self, id: int, name: str, email: str):
        self.__id: int = id
        self.__name: str = name
        self.__email: str = email

    @property
    def id(self):
        return self.__id
    
    @id.setter
    def id(self, id: int):
        if type(id) is int and id >= 0:
            self.__id = id
        else:
            raise ValueError('Ошибка при задании id')

    @property
    def name(self):
        return self.__name

    @name.setter
    def name(self, name: str):
        if type(name) is str and len(name) >= 2:
            self.__name = name
        else:
            raise ValueError('Ошибка при задании имени юзера')

    @property
    def email(self):
        return self.__email

    @email.setter
    def email(self, email: str):
        if type(email) is str and len(email) >= 3 and email.count('@') == 1:
            self.__email = email
        else:
            raise ValueError('Ошибка при задании почты')
