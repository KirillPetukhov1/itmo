def main():
    """Считать искомое число (target), границы диапазона или 
        сформированный вручную список, 
        вывести результат guess_number на экран.
    """
    
    target = int(input('Введите target: '))
    
    list_input_type = int(input('Выберите способ формирования диапазона: 1 - границы, 2 - ручной ввод. Введите только цифру 1 или 2: '))
    while list_input_type != 1 and list_input_type != 2:
        list_input_type = int(input('Выберите способ формирования диапазона: 1 - границы, 2 - ручной ввод. Введите только цифру 1 или 2: '))
    
    lst = []
    if list_input_type == 1:
        start_range = int(input('Введите начало диапазона: '))
        end_range = int(input('Введите конец диапазона: '))
        lst = range(start_range, end_range + 1)
        if target > lst[-1] or target < lst[0]:
            print('target находится вне диапазона')
    else:
        lst_len = int(input('Введите длину диапазона: '))
        lst = [0 for _ in range(lst_len)]
        for i in range(lst_len):
            lst[i] = int(input(f'Введите элемент номер {i + 1}: '))
            
    if len(lst) == 0:
        print('В диапазоне нет чисел')
        
    alg_type = input('Выберите алгоритм поиска: bin - бинарный поиск, seq - последовательный. Введите только слово bin или seq: ')
    while alg_type != 'bin' and alg_type != 'seq':
        alg_type = input('Выберите алгоритм поиска: bin - бинарный поиск, seq - последовательный. Введите только слово bin или seq: ')
    
        
    result = guess_number(target, lst, alg_type)
    
    print(f'Число {result[0]} найдено за {result[1]} попыток')    
    

def guess_number(target: int, lst: list[int], alg_type='seq') -> tuple[int, int | None]:
    """Найти количество итераций, необходимое для поиска числа в 
    диапазоне с использованием выбранного алгоритма.
    
    Args:
        target (int): Искомое число.
        lst (list[int]): Массив (можно любой итерируемый объект) 
            не уменьшающихся целых чисел.
        alg_type (string): Тип алгоритма поиска.
            ``bin`` - бинарный поиск, ``seq`` - последовательный.
    
    Returns:
        (int, int | None): Пара целых чисел: искомое число и 
            количество итераций, потраченных на поиск. В случае, 
            если искомое число не было найдено, второе число в паре 
            является None
            
    Raises:
        TypeError: lst не является итерируемым или target нельзя 
            сравнивать с элементом lst.  
    """
    
    if not hasattr(lst, '__iter__'):
        raise TypeError(f'Arg lst is \'{type(lst)}\', \'{type(lst)}\' object is non iterable.')
    
    lst = sorted(lst)

    iteration_count = 0

    if alg_type == 'seq':
        for lst_element in lst:
            iteration_count += 1
            if lst_element == target:
                return (target, iteration_count)
                
    elif alg_type == 'bin':
        left_index = 0
        right_index = len(lst) - 1
        mid_index = 0
        
        while left_index <= right_index:
            iteration_count += 1
            mid_index = (left_index + right_index) // 2
            
            if lst[mid_index] == target:
                return (target, iteration_count)
            elif lst[mid_index] < target:
                left_index = mid_index + 1
            else:
                right_index = mid_index - 1
        

    return (target, None)


if __name__ == '__main__':
    main()