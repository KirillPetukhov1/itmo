import concurrent.futures as ftres
from collections.abc import Callable
from functools import partial
from iter1 import integrate
import math
import timeit


def integrate_async(f: Callable[[float], float], a: float, b: float, *, n_jobs=2, n_iter=1000):
    """
    Интегрирует методом прямоугольников в многопоточном режиме.
    
    Args:
        f (Callable[[float], float]): Математическая функция для интегрирования.
        a (float): Нижний предел.
        b (float): Верхний предел.
        n_jobs (int): Количество потоков.
        n_iter (int): Количество итераций.
    
    Returns:
        float: значение вычисленного интеграла.
    """
    """
      - аннотировать аргументы
      - реализовать аналогичную функцию для вычисления с процессами (ProcessPoolExecutor)
      - оценить время работы программ с потоками и процессами и зафиксировать значения (2, 4, 6, 8(?))
    """

    executor = ftres.ThreadPoolExecutor(max_workers=n_jobs) # создаваемый пул тредов будет размера n_jobs

    spawn = partial(executor.submit, integrate, f, n_iter = n_iter // n_jobs)   # partial позволяет "закрепить"
                                                                                # несколько аргументов
                                                                                # для удобства вызова функции ,
                                                                                # см. пример ниже
    step = (b - a) / n_jobs
    # for i in range(n_jobs):
    #   print(f"Работник {i}, границы: {a + i * step}, {a + (i + 1) * step}")


    fs = [spawn(a + i * step, a + (i + 1) * step) for i in range(n_jobs)]    # создаем потоки с помощью генератора
                                                                             # списков; partial позволил нам

    return sum(list(f.result() for f in ftres.as_completed(fs)))                     # as.completed() берет на вход список
                                                                                # фьючерсов и как только какой-то
                                                                                # завершился, возвращает результат
                                                                                # f.result(), далее, мы эти результаты
                                                                                # складываем

# print(integrate_async(math.sin, 0, math.pi))

if __name__ == "__main__":
    # print(integrate_async(math.sin, 0, math.pi / 2, n_jobs=8))
    for n_jobs in (2, 4, 6, 8):
        times = timeit.repeat(lambda: integrate_async(math.sin, 0, math.pi / 2, n_jobs=n_jobs), number=1000, repeat=5)
        print(f'{n_jobs} потока: {sum(times)/5}')
    