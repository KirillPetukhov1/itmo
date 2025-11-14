class UserCurrencies:
    def __init__(self, id: int, currencies_id: int, user_id: int, start_timestamp: int, end_timestamp: int):
        self.__id: int = id
        self.__currencies_id: int = currencies_id
        self.__user_id: int = user_id
        self.__start_timestamp: int = start_timestamp
        self.__end_timestamp: int = end_timestamp

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
    def currencies_id(self):
        return self.__currencies_id

    @currencies_id.setter
    def currencies_id(self, currencies_id: int):
        if type(currencies_id) is int and currencies_id >= 0:
            self.__currencies_id = currencies_id
        else:
            raise ValueError('Ошибка при задании id валюты')
        
    @property
    def user_id(self):
        return self.__user_id

    @user_id.setter
    def user_id(self, user_id: int):
        if type(user_id) is int and user_id >= 0:
            self.__user_id = user_id
        else:
            raise ValueError('Ошибка при задании id юзера')
        
    @property
    def start_timestamp(self):
        return self.__start_timestamp

    @start_timestamp.setter
    def start_timestamp(self, start_timestamp: int):
        if type(start_timestamp) is int and start_timestamp > 0:
            self.__start_timestamp = start_timestamp
        else:
            raise ValueError('Ошибка при задании временной метки начала подписки')
        
    @property
    def end_timestamp(self):
        return self.__end_timestamp

    @end_timestamp.setter
    def end_timestamp(self, end_timestamp: int):
        if type(end_timestamp) is int and end_timestamp >= 0:
            self.__end_timestamp = end_timestamp
        else:
            raise ValueError('Ошибка при задании временной метки окончания подписки')

