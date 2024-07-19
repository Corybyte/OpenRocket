import math
import step7.solution
from nose_cone_cg_helper import get_radius
from common_helper import safe_sqrt, equals


def calculateMOI(param):
    return calculate_moi(param['length'], param['divisions'], param['filled'], param['thickness'],
                         param['transitionType'], param['transitionForeRadius'], param['transitionAftRadius'],
                         param['transitionShapeParameter'])


def calculate_moi(length: float, divisions: int, filled: bool, thickness: float, type, foreRadius, aftRadius,
                  shapeParameter):
    rotationalUnitInertia = 0

    volume = 0
    if (length <= 0):
        return
    for n in range(divisions):
        x1 = n * length / divisions

        x2 = (n + 1) * length / divisions

        l = x2 - x1
        r1o = get_radius(x1, type, length, foreRadius, aftRadius, shapeParameter)
        r2o = get_radius(x2, type, length, foreRadius, aftRadius, shapeParameter)

        height = thickness * math.hypot(r2o - r1o, l) / l
        if filled:
            r1i = 0
            r2i = 0
        else:
            r1i = max(r1o - height, 0)
            r2i = max(r2o - height, 0)
        Ixxo = step7.solution.calculate_unit_rot_moi(r1o, r2o)
        Ixxi = step7.solution.calculate_unit_rot_moi(r1i, r2i)
        fullCG = calculateCG(l, r1o, r2o)
        innerCG = calculateCG(l, r1i, r2i)
        dV = fullCG[1] - innerCG[1]
        volume += dV

        Ixx = Ixxo * fullCG[1] - Ixxi * innerCG[1]
        rotationalUnitInertia += Ixx
    rotationalUnitInertia /= volume

    rotationalUnitInertia *= 3.0 / 10.0
    return rotationalUnitInertia


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
        cg = l * (math.pow(r1, 2) + 2.0 * r1 * r2 + 3 * math.pow(r2, 2)) / (
                4.0 * (math.pow(r1, 2) + r1 * r2 + math.pow(r2, 2)))
    return (cg, volume)

# test
if __name__ == '__main__':
    print(calculate_moi(0.15000000000000002, 128, False, 0.002, "OGIVE", 0.0, 0.025, 1.0))
