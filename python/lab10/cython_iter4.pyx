
cpdef float integrate(object f, float a, float b, int n_iter=1000):
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
    cdef float acc, step
    cdef int i
    acc = 0
    step = (b - a) / n_iter
    for i in range(n_iter):
        acc += f(a + i*step) * step
    return acc