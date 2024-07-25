import math

def calculateCG(l: float, r1: float, r2: float) -> tuple[float, float]:
    """
    Calculates and returns a tuple of the CG (relative to fore end of frustum) and volume of a filled conical frustum.
    The result is also correct for cases of r1=r2, r1=0, and r2=0.
    Note: This function actually returns 3/PI times the correct value to avoid extra operations in the loop.
    This is corrected at the end of the numerical integration loop.

    Args:
        l (float): Length (height) of the frustum.
        r1 (float): Radius of the fore end of the frustum.
        r2 (float): Radius of the aft end of the frustum.

    Returns:
        tuple[float, float]: A tuple containing the volume (as mass) and the CG of the frustum. (cg, volume)
    """
    volume = l * (math.pow(r1, 2) + r1 * r2 + math.pow(r2, 2))
    cg = 0
    if volume < 0.00000001:
        cg = l / 2.0
    else:
        cg = l * (math.pow(r1, 2) + 2.0 * r1 * r2 + 3 * math.pow(r2, 2)) / (4.0 * (math.pow(r1, 2) + r1 * r2 + math.pow(r2, 2))) 
    return (cg, volume)