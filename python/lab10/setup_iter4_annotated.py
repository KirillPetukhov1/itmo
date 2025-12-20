from setuptools import setup
from Cython.Build import cythonize

setup(
    ext_modules=cythonize(r"C:\Users\k4484\itmo\itmo-1\python\lab10\cython_iter4.pyx", annotate=True),
)

# python .\python\lab10\setup_iter4_annotated.py build_ext --inplace
# python .\setup_iter4_annotated.py build_ext --inplace