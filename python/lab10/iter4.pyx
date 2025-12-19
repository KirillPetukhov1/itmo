
cpdef float integrate(f, float a, float b, int n_iter=1000):
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
    cdef float acc = 0
    step = (b - a) / n_iter
    for i in range(n_iter):
        acc += f(a + i*step) * step
    return acc


if __name__ == "__main__":
    for i in range(5):
        print(f"===={10**i*100} итераций====")
        print(sum(timeit.repeat(lambda: integrate(math.sin, 0, math.pi / 2, n_iter=10**i*100), number=100//(10**(i==4)), repeat=5))/5)
    
