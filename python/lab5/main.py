from collections.abc import Callable


def main():
    bin_tree = gen_bin_tree(3, 5, lambda x: x + 1, lambda x: x ** 2)
    for i in bin_tree:
        print(i)
        



def gen_bin_tree(
    height: int = 3,
    root: int = 13,
    l_b: Callable[[int], int] = lambda x: x+1,
    r_b: Callable[[int], int] = lambda y: y-1
) -> dict[str, list]:
    """Построить бинарное дерево нерекурсивным способом.
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
    bin_tree = [[root]]
    
    for i in range(height):
        new_level = []
        for node in bin_tree[i]:
            new_level.append(l_b(node))
            new_level.append(r_b(node))
        bin_tree.append(new_level)
        
    return bin_tree


if __name__ == "__main__":
    main()