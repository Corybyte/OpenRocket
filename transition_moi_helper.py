import math
import step12.solution
from common_helper import get_radius, ringCG, ringVolume, ringLongitudinalUnitInertia, ringRotationalUnitInertia
from common_helper import safe_sqrt, equals


def calculateMOI(param):
    return calculate_moi(param)


def calculate_moi(param):
    length = param['length']
    divisions = param['divisions']
    foreRadius = param['transitionForeRadius']
    type = param['transitionType']
    aftRadius = param['transitionAftRadius']
    shapeParameter = param['transitionShapeParameter']
    thickness = param['thickness']
    filled = param['filled']
    wetArea = 0
    fullVolume = 0
    volume = 0
    longitudinalUnitInertia = 0
    rotationalUnitInertia = 0
    cgx = 0

    if (length <= 0):
        return
    for n in range(divisions):
        # 分割区间的左边界
        x1 = n * length / divisions
        # 分割区间的右边界
        x2 = (n + 1) * length / divisions
        # 计算每个分割区间的长度
        l = x2 - x1
        # 分割区间的外部边界半径
        r1o = get_radius(x1, type, length, foreRadius, aftRadius, shapeParameter)
        # 分割区间的内部边界半径
        r2o = get_radius(x2, type, length, foreRadius, aftRadius, shapeParameter)
        # 环的高度
        height = thickness * math.hypot(r2o - r1o, l) / l
        # 判断当前分割区间是否被填充
        if filled:
            r1i = 0
            r2i = 0
        else:
            # 计算每个分割区间中环的内部半径
            r1i = max(r1o - height, 0)
            r2i = max(r2o - height, 0)

        # 计算外部半径的梯形台体的重心
        fullCG = calculateCG(l, r1o, r2o)
        # 计算内部半径的梯形台体的重心
        innerCG = calculateCG(l, r1i, r2i)
        # 存储梯形台体内部的体积，它的值等于外部半径梯形台体的体积减去内部半径梯形台体的体积，
        dV = fullCG[1] - innerCG[1]
        # 存储了梯形台体的重心在x轴上的位置，它的值通过计算外部半径梯形台体重心在x轴上的位置与内部半径梯形台体重心在x轴上的位置的加权平均值得到。
        dCG = (fullCG[0] * fullCG[1] - innerCG[0] * innerCG[1]) / dV
        # 存储了第一矩的值。
        # 它的计算方式是将体积差值dV乘以外部半径梯形台体的重心在x轴上的位置与内部半径梯形台体的重心在x轴上的位置的加权平均值，即(x1 + dCG)。
        dCGx = dV * (x1 + dCG)
        ############### Begin ###############
        # 表示外部半径梯形台体相对于x轴的单位旋转惯性矩。

        # 表示内部半径梯形台体相对于x轴的单位旋转惯性矩。

        # 整个梯形台体相对于x轴的总旋转惯性矩


        ############### End ###############
        # 将惯性矩移动到通过组件前端的轴上的操作
        Iyy += dV * pow((x1 + dCG), 2)
        volume += dV
        cgx += dCGx
        rotationalUnitInertia += Ixx
        longitudinalUnitInertia += Iyy
    rotationalUnitInertia /= volume
    longitudinalUnitInertia /= volume
    volume *= math.pi / 3.0
    fullVolume *= math.pi / 3.0
    cgx *= math.pi / 3.0
    wetArea *= math.pi

    if volume < 0.0000000001:
        volume = 0
        cg = (param['length'] / 2, 0, 0, 0)
    else:
        cg = (cgx / volume, 0, 0, volume)

    if equals(volume, 0):
        rotationalUnitInertia = 0
        longitudinalUnitInertia = 0

    longitudinalUnitInertia = longitudinalUnitInertia - pow(cg[0], 2)

    return [rotationalUnitInertia, longitudinalUnitInertia]


def calculateCG(l, r1, r2):
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
    if volume < 0.00000001:
        cg = l / 2.0
    else:
        cg = l * (math.pow(r1, 2) + 2.0 * r1 * r2 + 3 * math.pow(r2, 2)) / (
                4.0 * (math.pow(r1, 2) + r1 * r2 + math.pow(r2, 2)))
    return (cg, volume)
