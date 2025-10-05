# a = [1, 3, 5]
# b = (3, 6, 8)
# c = {3, 6, 5}
# d = {'a': 4, 'b': 6, 'c': 8}
# e = 'ggtdt'
# f = 23

# print(hasattr(a, '__iter__'))
# print(hasattr(b, '__iter__'))
# print(hasattr(c, '__iter__'))
# print(hasattr(d, '__iter__'))
# print(hasattr(e, '__iter__'))
# print(hasattr(f, '__iter__'))

# for i in d:
#     print(i)


a = 'hg'
b = 'n'
c = 10
d = [5, 7]

print(a == d)

def add(a, b):
    """
    Add two numbers.

    Parameters:
        a (int): First number.
        b (int): Second number.

    Returns:
        int: Sum of a and b.
    """
    return a + b
print(add(3, 7))

def multiply(a, b):
    """
    Multiply two numbers.

    Args:
        a (int): First number.
        b (int): Second number.

    Returns:
        int: Product of a and b.
    """
    return a * b
print(multiply(3, 5))

def divide(a, b):
    """
    Divide two numbers.

    Parameters
    ----------
    a : float
        Dividend.
    b : float
        Divisor.

    Returns
    -------
    float
        Quotient of division.
    """
    if b == 0:
        raise ValueError("Division by zero not allowed.")
    return a / b
print(divide(6, 2))


def complex(real=0.0, imag=0.0):
    """Form a complex number.

    Keyword arguments:
    :real -- the real part (default 0.0)
    :imag -- the imaginary part (default 0.0)
    """
    if imag == 0.0 and real == 0.0:
        return complex_zero

