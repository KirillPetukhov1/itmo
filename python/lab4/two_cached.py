import timeit
import matplotlib.pyplot as plt
from functools import lru_cache


@lru_cache(1024)
def fact_recursive_cached(n: int) -> int:
    """Рекурсивный закэшированный факториал"""
    if n == 0:
        return 1
    return n * fact_recursive_cached(n - 1)


@lru_cache(1024)
def fact_iterative_cached(n: int) -> int:
    """Нерекурсивный закэшированный факториал"""
    res = 1
    for i in range(1, n + 1):
        res *= i
    return res


def benchmark(func, n, number=1, repeat=5):
    """Возвращает среднее время выполнения func(n)"""
    # т.к. проводим чистый бенчмарк, сбрасываем кеш, иначе имеем
    # константное время, за счет использования уже
    # посчитанных значений
    times = timeit.repeat(
        lambda: func(n),
        lambda: func.cache_clear(),
        number=number,
        repeat=repeat)
    return min(times)


def main():
    # фиксированный набор данных
    test_data = list(range(10, 900, 10))

    res_recursive_cached = []
    res_iterative_cached = []

    for n in test_data:
        res_recursive_cached.append(benchmark(fact_recursive_cached, n))
        res_iterative_cached.append(benchmark(fact_iterative_cached, n))

    # Визуализация
    plt.plot(test_data, res_recursive_cached, label="Рекурсивный кэшированный")
    plt.plot(test_data, res_iterative_cached, label="Итеративный кэшированный")
    plt.xlabel("n")
    plt.ylabel("Время (сек)")
    plt.title("Сравнение кэшированного рекурсивного и итеративного факториала")
    plt.legend()
    plt.show()


if __name__ == "__main__":
    main()
