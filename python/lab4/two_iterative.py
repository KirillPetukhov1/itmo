import timeit
import matplotlib.pyplot as plt
from functools import lru_cache


def fact_iterative(n: int) -> int:
    """Нерекурсивный факториал"""
    res = 1
    for i in range(1, n + 1):
        res *= i
    return res


@lru_cache(1024)
def fact_iterative_cached(n: int) -> int:
    """Нерекурсивный закэшированный факториал"""
    res = 1
    for i in range(1, n + 1):
        res *= i
    return res


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
    return min(times)


def main():
    # фиксированный набор данных
    test_data = list(range(10, 900, 10))

    res_iterative = []
    res_iterative_cached = []

    for n in test_data:
        res_iterative.append(benchmark(fact_iterative, n))
        res_iterative_cached.append(benchmark(fact_iterative_cached, n))

    # Визуализация
    plt.plot(test_data, res_iterative, label="Итеративный")
    plt.plot(test_data, res_iterative_cached, label="Итеративный кэшированный")
    plt.xlabel("n")
    plt.ylabel("Время (сек)")
    plt.title("Сравнение итеративного кешированного и не кешированного факториала")
    plt.legend()
    plt.show()


if __name__ == "__main__":
    main()
