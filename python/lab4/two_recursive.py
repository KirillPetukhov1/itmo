import timeit
import matplotlib.pyplot as plt
from functools import lru_cache


def fact_recursive(n: int) -> int:
    """Рекурсивный факториал"""
    if n == 0:
        return 1
    return n * fact_recursive(n - 1)


@lru_cache(1024)
def fact_recursive_cached(n: int) -> int:
    """Рекурсивный закэшированный факториал"""
    if n == 0:
        return 1
    return n * fact_recursive_cached(n - 1)


def benchmark(func, n, number=1, repeat=5):
    """Возвращает среднее время выполнения func(n)"""
    if hasattr(func, 'cache_clear'):
        # т.к. проводим чистый бенчмарк, сбрасываем кеш, иначе имеем
        # константное время, за счет использования уже
        # посчитанных значений
        times = timeit.repeat(
            lambda: func(n),
            lambda: func.cache_clear(),
            number=number,
            repeat=repeat)
    else:
        times = timeit.repeat(lambda: func(n), number=number, repeat=repeat)
    return min(times)


def main():
    # фиксированный набор данных
    test_data = list(range(10, 900, 10))

    res_recursive = []
    res_recursive_cached = []

    for n in test_data:
        res_recursive.append(benchmark(fact_recursive, n))
        res_recursive_cached.append(benchmark(fact_recursive_cached, n))

    # Визуализация
    plt.plot(test_data, res_recursive, label="Рекурсивный")
    plt.plot(test_data, res_recursive_cached, label="Рекурсивный кэшированный")
    plt.xlabel("n")
    plt.ylabel("Время (сек)")
    plt.title("Сравнение рекурсивного кешированного и не кешированного факториала")
    plt.legend()
    plt.show()


if __name__ == "__main__":
    main()
