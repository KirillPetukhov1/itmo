from collections.abc import Callable


def main():
    """Вывести на экран бинарное дерево в виде словаря."""

    bin_tree = gen_bin_tree()
    print(form_readable_bin_tree(transform_to_dict_form(bin_tree)))


def gen_bin_tree(
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


def transform_to_dict_form(bin_tree: list[list[int]]) -> dict[str, list]:
    """Преобразовать бинарное дерево из формы списка списков
    в форму вложенных словарей

    Args:
        bin_tree (list[list[int]]): Бинарное дерево в виде списка списков.

    Returns:
        (dict[str, list]): Бинарное дерево в виде словаря.
    """
    bin_tree_last_levels = []

    for i in range(len(bin_tree)-1, -1, -1):
        bin_tree_new_last_levels = []
        for j in range(len(bin_tree[i])):
            if i == len(bin_tree) - 1:
                bin_tree_new_last_levels.append(
                    {str(bin_tree[i][j]): []}
                )
            else:
                bin_tree_new_last_levels.append(
                    {str(bin_tree[i][j]): [
                        bin_tree_last_levels[j * 2],
                        bin_tree_last_levels[j * 2 + 1]
                    ]}
                )
        bin_tree_last_levels = bin_tree_new_last_levels
        
    return bin_tree_last_levels[0]


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
    answer_str = '{' + list(bin_tree.keys())[0] + ': ['

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


if __name__ == "__main__":
    main()
