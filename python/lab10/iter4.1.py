from cython_iter4 import integrate
import math
import timeit


if __name__ == "__main__":
    for i in range(5):
        print(f"===={10**i*100} итераций====")
        print(sum(timeit.repeat(lambda: integrate(math.sin, 0, math.pi / 2, n_iter=10**i*100), number=100//(10**(i==4)), repeat=5))/5)