from collections.abc import Callable

13

def gen_bin_tree(
        height: int, 
        root: int, 
        l_b: Callable[[int], int] = lambda x: x+1,
        r_b: Callable[[int], int] = lambda y: y-1
    ) -> dict[str, list]:
    """
    """
    roots = []
    if height > 0:
        roots.append(gen_bin_tree(height-1, l_b(root)))
        roots.append(gen_bin_tree(height-1, r_b(root)))
    return {str(root): roots}
    
# print(gen_bin_tree(3, 13, lambda x: x**2, lambda x: x + 4))
print(gen_bin_tree(1, 10, lambda x: x**2, lambda x: 2 * (x + 4)))