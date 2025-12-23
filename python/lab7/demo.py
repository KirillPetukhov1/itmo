from main import logger
from custom_errors import NegativeDiscriminantError, NonExistentEquationError
import logging
import math


logging.basicConfig(
    filename="python\\lab7\\demo_lab7.log",
    level=logging.INFO,
    format="%(levelname)s: %(message)s"
)


@logger(handle=logging.getLogger("demo"))
def solve_quadratic(a, b, c):
    # Ошибка типов
    for name, value in zip(("a", "b", "c"), (a, b, c)):
        if not isinstance(value, (int, float)):
            raise TypeError(f"Coefficient '{name}' must be numeric")

    # Ошибка: a == 0
    if a == 0 and b == 0:
        raise NonExistentEquationError("a and b cannot be zero")

    d = b*b - 4*a*c

    if d < 0:
        raise NegativeDiscriminantError("the equation does not exist")

    if d == 0:
        x = -b / (2*a)
        return (x,)

    root1 = (-b + math.sqrt(d)) / (2*a)
    root2 = (-b - math.sqrt(d)) / (2*a)
    return root1, root2

if __name__ == '__main__':
    solve_quadratic(1, 4, 3)
    try:
        solve_quadratic(1, 2, 5)
    except:
        pass
    try:
        solve_quadratic('gs', 2, 5)
    except:
        pass
    try:
        solve_quadratic(0, 0, 0)
    except:
        pass