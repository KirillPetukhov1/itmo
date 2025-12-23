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
        
    Examples:
        >>> import math
        
        # Пример 1: Тригонометрическая функция sin(x) от 0 до pi
        >>> result1 = integrate(math.sin, 0, math.pi/2, n_iter=10000)
        >>> abs(result1 - 2.0) < 0.001
        True
        
        # Пример 2: Полиномиальная функция x² от 0 до 3
        >>> def poly_func(x: float) -> float:
        ...     return x**2
        >>> result2 = integrate(poly_func, 0, 3, n_iter=10000)
        >>> abs(result2 - 9.0) < 0.001
        True
    """
    acc = 0
    step = (b - a) / n_iter
    for i in range(n_iter):
        acc += f(a + i*step) * step
    return acc


if __name__ == "__main__":
    for i in range(5):
        print(f"===={10**i*100} итераций====")
        print(sum(timeit.repeat(lambda: integrate(math.sin, 0, math.pi / 2, n_iter=10**i*100), number=100//(10**(i==4)), repeat=5))/5)
    
