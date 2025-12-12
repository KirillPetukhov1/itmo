import pytest
import math
from iter1 import integrate


def test_integrate_sin_function():
    """Интеграл sin(x) на [0, pi] должен быть ~2"""
    
    result = integrate(math.sin, 0, math.pi, n_iter=10000)
    assert abs(result - 2.0) < 0.001, f"Ожидалось ~2.0, получено {result}"


def test_integrate_complex_sin_function():
    """Интеграл 2sin(2x)+sin(x) на [0, pi/2] должен быть ~3"""
    
    result = integrate(lambda x: 2*math.sin(2*x)+math.sin(x), 0, math.pi / 2, n_iter=10000)
    assert abs(result - 3.0) < 0.001, f"Ожидалось ~3.0, получено {result}"
    
    
if __name__ == "__main__":
    pytest.main([__file__, "-v"])