import math
from collections.abc import Callable
import timeit

# итерация 1


def integrate(f: Callable[[float], float], a: float, b: float, *, n_iter=1000) -> float:
    """
    Интегрирует методом прямоугольников.
    
    Args:
        f (Callable[[float], float]): Математическая функция для интегрирования.
        a (float): Нижний предел.
        b (float): Верхний предел.
        n_iter (int): Количество итераций.
    
    Returns:
        float: значение вычисленного интеграла.
    """
    acc = 0
    step = (b - a) / n_iter
    for i in range(n_iter):
        acc += f(a + i*step) * step
    return acc


# integrate(math.cos, 0, math.pi / 2, n_iter=100)
if __name__ == "__main__":
    # print(integrate(math.sin, 0, math.pi / 2))
    times = timeit.repeat(lambda: integrate(math.sin, 0, math.pi / 2), number=1000, repeat=5)
    print(sum(times)/5)
    
