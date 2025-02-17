import math


def calculateAxialCD(aoa, cd, axialDragPoly1, axialDragPoly2):
    ############### Begin #############
    # 将攻角限制在0到π之间
    aoa = clamp(aoa, 0, math.pi)

    # 初始化乘数mul为0
    mul = 0

    # 如果攻角大于π/2，取其对称值
    if aoa > math.pi / 2:
        aoa = math.pi - aoa

    # 如果攻角小于17度，使用axialDragPoly1多项式进行计算
    if aoa < 17 * math.pi / 180:
        mul = polyEval(aoa, axialDragPoly1)
    else:
        # 如果攻角大于或等于17度，使用axialDragPoly2多项式进行计算
        mul = polyEval(aoa, axialDragPoly2)

    # 如果攻角小于π/2，返回mul与cd的乘积
    if aoa < math.pi / 2:
        return mul * cd
    else:
        # 如果攻角大于或等于π/2，返回mul与cd的负乘积
        return -mul * cd
    ############### End #############



def clamp(value, min_value, max_value):
    """
    限制值在指定范围内
    :param value: 要限制的值
    :param min_value: 最小值
    :param max_value: 最大值
    :return: 限制后的值
    """
    return max(min_value, min(value, max_value))
def polyEval(x,coefficients):
    """
    计算多项式的值。
    :param x: 自变量值
    :param coefficients: 多项式的系数列表，从高次项到低次项
    :return: 多项式计算结果
    """
    result = 0
    v = 1
    for coefficient in reversed(coefficients):
        result += coefficient * v
        v *= x
    return result