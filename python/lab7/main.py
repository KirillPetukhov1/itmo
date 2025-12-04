import logging
import sys
import functools


def logger(func=None, *, handle=sys.stdout):
    if func is None:
        return lambda func: logger(func, handle=handle)

    @functools.wraps(func)
    def inner(*args, **kwargs):
        if type(handle) is logging.Logger:
            handle.info(f'Вызов функции с аргументами')
        else:
            print(args)
        return func(*args, **kwargs)

    return inner
    
    
@logger
def f(b):
    print(b)
    
f(1123)