def main():
    """
    Ввод значений с клавиатуры для формирования
    списка, по которому мы ищем искомое число и
    искомого числа
    (опционально) предложить пользователю сформировать
    список вручную с клавиатуры

    __вызов функции guess-number с параметрами: __
      - искомое число (target)
      - список, по-которому идем
      - тип поиска (последовательный, бинарный)

    __вывод результатов на экран__
    :return:
    """
    
    target = int(input('Введите target: '))
    start_range = int(input('Введите начало диапазона: '))
    end_range = int(input('Введите конец диапазона: '))
    d = list(range(start_range, end_range + 1))

    if len(d) == 0:
        print('В диапазоне нет чисел')
    if target > d[-1] or target < d[0]:
        print('target находится вне диапазона')
    # res = guess_number(target, d, type='bin')
    # print(f'{res}')


def guess_number(target: int, lst: list[int], type='seq') -> tuple[int, int | None]:
    """Найти количество итераций, необходимое для поиска числа в 
    диапазоне с использованием выбранного алгоритма.
    
    Args:
        target (int): Искомое число.
        lst (list[int]): Массив (можно любой итерируемый объект) 
            не уменьшающихся целых чисел.
        type (string): Тип алгоритма поиска.
            ``bin`` - бинарный поиск, ``seq`` - последовательный.
    
    Returns:
        (int, int | None): Пара целых чисел: искомое число и 
            количество итераций, потраченных на поиск. В случае, 
            если искомое число не было найдено, второе число в паре 
            является None
            
    Raises:
    """
    
    if not hasattr(lst, '__iter__'):
        return

    iteration_count = None

    if type == 'seq':
        # ищем число последовательно
        ...
    elif type == 'bin':
        # ищем число с помощью алгоритма бинарного поиска
        ...

    return (target, iteration_count)
    # TypeError


if __name__ == '__main__':
    main()
