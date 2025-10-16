from collections.abc import Callable
import timeit
import matplotlib.pyplot as plt


def main():
    # фиксированный набор данных
    test_data = list(range(1, 15))

    recursive = []
    iterative = []

    for heigh in test_data:
        recursive.append(benchmark(gen_recursive_bin_tree, 1, 5, heigh))
        iterative.append(benchmark(gen_iterative_bin_tree, 1, 5, heigh))

    # Визуализация
    plt.plot(test_data, recursive, label="Рекурсивный способ")
    plt.plot(test_data, iterative, label="Итеративный способ")
    plt.xlabel("Высота")
    plt.ylabel("Время (сек)")
    plt.title("Сравнение рекурсивного и итеративного способа генерации бин дерева")
    plt.legend()
    plt.show()


def benchmark(func, number=1, repeat=5, *n):
    """Возвращает среднее время выполнения func(n)"""
    times = timeit.repeat(lambda: func(*n), number=number, repeat=repeat)
    return min(times)


def gen_iterative_bin_tree(
    height: int = 3,
    root: int = 13,
    l_b: Callable[[int], int] = lambda x: x+1,
    r_b: Callable[[int], int] = lambda y: y-1
) -> list[list[int]]:
    """Построить бинарное дерево нерекурсивным способом.
    Бинарное дерево формируется в виде списка со списками, 
    где n'ый список содержит 2**(n-1) элементов, 
    соответствующих значениям узлов.

    Args:
        height (int): Высота (глубина) дерева или поддерева.
        root (int): Значение корня дерева или поддерева.
        l_b (Callable[[int], int]): Функция для расчета значения 
            левого дочернего узла.
        r_b (Callable[[int], int]): Функция для расчета значения 
            правого дочернего узла.

    Returns:
        (list[list[int]]): Бинарное дерево в виде списка списков.
    """
    bin_tree = [[root]]

    for i in range(height):
        new_level = []
        for node in bin_tree[i]:
            new_level.append(l_b(node))
            new_level.append(r_b(node))
        bin_tree.append(new_level)

    return bin_tree


def gen_recursive_bin_tree(
    height: int = 3,
    root: int = 13,
    l_b: Callable[[int], int] = lambda x: x+1,
    r_b: Callable[[int], int] = lambda y: y-1
) -> dict[str, list]:
    """Построить бинарное дерево рекурсивным способом.
    Бинарное дерево формируется в виде словаря, где ключ - 
    значение узла, значение - пустой массив, если вершина 
    конечная, или массив с двумя поддеревьями.

    Args:
        height (int): Высота (глубина) дерева или поддерева.
        root (int): Значение корня дерева или поддерева.
        l_b (Callable[[int], int]): Функция для расчета значения 
            левого дочернего узла.
        r_b (Callable[[int], int]): Функция для расчета значения 
            правого дочернего узла.

    Returns:
        (dict[str, list]): Бинарное дерево в виде словаря.
    """
    roots = []
    if height > 0:
        roots.append(gen_recursive_bin_tree(height-1, l_b(root), l_b, r_b))
        roots.append(gen_recursive_bin_tree(height-1, r_b(root), l_b, r_b))
    return {str(root): roots}


if __name__ == "__main__":
    main()
