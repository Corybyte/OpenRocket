import math


def calculateCP(macLead, macLength, mach, ar, poly):
    """
    计算空气动力中心（Center of Pressure, CP）。

    Args:
        macLead (float): 主翼平均弦长（Mean Aerodynamic Chord, MAC）的前缘位置。
        macLength (float): 主翼平均弦长的长度。
        mach (float): 物体的马赫数（Mach number）。
        ar (float): 机翼的展弦比（Aspect Ratio）。
        poly (list[float]): 用于亚音速到超音速区间的插值多项式系数。

    Returns:
        float: 计算得到的空气动力中心（CP）位置。
    """

    ############### Begin ###############
    m = mach  # 赋值马赫数

    if m <= 0.5:
        # 当速度为亚音速（Mach ≤ 0.5）时，CP 处于平均弦长的 1/4 处
        return macLead + 0.25 * macLength

    if m >= 2:
        # 当速度为超音速（Mach ≥ 2）时，使用经验公式计算 CP 位置
        beta = safe_sqrt(1 - mach * mach)  # 计算斜率修正项 β
        return macLead + ((ar * beta - 0.67) / (2 * ar * beta - 1)) * macLength

    # 介于亚音速（0.5 < Mach < 2）之间时，使用插值多项式计算 CP 位置
    x = 1.0  # 多项式的 x 值初始化
    val = 0  # 多项式计算结果初始化
    for i in range(len(poly)):
        val += poly[i] * x  # 计算多项式的值
        x *= m  # 更新 x，使其等于 Mach 的幂次

    return macLead + val * macLength
    ############### End ###############


def safe_sqrt(x):
    """
    计算安全平方根，确保输入非负数。

    Args:
        x (float): 输入值。

    Returns:
        float: 若 x 为非负数，则返回其平方根；否则返回 0。
    """
    if x < 0:
        return 0
    else:
        return math.sqrt(x)
