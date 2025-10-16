from collections.abc import Callable


def main():
    """Вывести на экран бинарное дерево в виде словаря."""

    bin_tree = gen_bin_tree(3)
    print(form_readable_bin_tree(bin_tree))


def gen_bin_tree(
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
        roots.append(gen_bin_tree(height-1, l_b(root), l_b, r_b))
        roots.append(gen_bin_tree(height-1, r_b(root), l_b, r_b))
    return {str(root): roots}


def form_readable_bin_tree(
    bin_tree: dict[str, list],
    indention: int = 0
) -> str:
    """Сформировать в виде читаемого словаря бинарное дерево 
    или его поддерево.

    Args:
        bin_tree (dict[str, list]): Бинарное дерево в виде словаря.
        indention (int): отступ от края в предыдущем узле.
            Для формирования без отступа от края не устанавливать.

    Returns:
        str: Словарь в читаемом виде.
    """
    answer_str = '{\'' + list(bin_tree.keys())[0] + '\': ['

    if bin_tree[list(bin_tree.keys())[0]] != []:
        indention += len(answer_str)
        answer_str += form_readable_bin_tree(
            bin_tree[list(bin_tree.keys())[0]][0],
            indention
        )
        answer_str += ',\n' + ' ' * indention
        answer_str += form_readable_bin_tree(
            bin_tree[list(bin_tree.keys())[0]][1],
            indention
        )

    answer_str += ']}'
    return answer_str


if __name__ == '__main__':
    main()
