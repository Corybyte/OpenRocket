import math

def calculate_unit_rot_moi(r1: float, r2: float) -> float:
    """
    Calculates the unit rotational moment of inertia for a frustum or a cylinder.

    Args:
        r1 (float): Radius of one end of the frustum.
        r2 (float): Radius of the other end of the frustum.

    Returns:
        float: The unit rotational moment of inertia.
    """
    # Define an epsilon value for floating point comparison
    EPSILON = 0.00000001

    # Check for cylinder special case
    if abs(r1 - r2) < EPSILON:
        return 10.0 * pow2(r1) / 6.0

    return (math.pow(r2, 5) - math.pow(r1, 5)) / (math.pow(r2, 3) - math.pow(r1, 3))


def pow2(x: float) -> float:
    """
    Computes the square of a number.

    Args:
        x (float): The number to be squared.

    Returns:
        float: The square of the input number.
    """
    return x ** 2

