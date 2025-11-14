class Currencies:
    def __init__(self, id: int, code: str):
        self.__id: int = id
        self.__code: str = code

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
    def code(self):
        return self.__code

    @code.setter
    def code(self, code: str):
        if type(code) is str and len(code) >= 2:
            self.__code = code
        else:
            raise ValueError('Ошибка при задании кода валюты')
