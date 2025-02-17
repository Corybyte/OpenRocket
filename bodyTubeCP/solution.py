import math


def calculateCP(mach, AOA, length, divisions, outerRadius,
                refArea, sinAOA, sincAOA):
    """
    计算火箭的压力中心（CP）。

    Args:
        mach (float): 马赫数（飞行速度与音速的比值）。
        AOA (float): 迎角（Angle of Attack）。
        length (float): 火箭的总长度。
        divisions (int): 计算CP时的离散分割数。
        outerRadius (float): 火箭的外半径（假设保持不变）。
        refArea (float): 参考面积。
        sinAOA (float): sin(AOA) 值，预计算以优化计算效率。
        sincAOA (float): sin²(AOA) 值，预计算以优化计算效率。

    Returns:
        tuple: (planCenter, 0, 0, 计算得到的气动力参数)
    """

    # 额外的乘数用于防止火箭在顶点（apogee）转向时变得不稳定并开始水平振荡。
    # 这种影响仅在 AOA > 45° 且速度低于 15 m/s 时出现，在正常飞行中无影响。

    ############### Begin ###############
    # 初始化平面中心和总平面面积
    planCenter = 0
    planArea = 0

    # 计算火箭的平面中心和总平面面积
    for n in range(divisions):
        x1 = n * length / divisions  # 当前分区的起始位置
        x2 = (n + 1) * length / divisions  # 当前分区的结束位置
        l = x2 - x1  # 当前分区的长度

        # 分割区间的外部边界半径（假设火箭是圆柱体，半径不变）
        r1o = outerRadius
        r2o = outerRadius

        # 计算该分区的增量面积 dA
        dA = l * (r1o + r2o)
        planArea += dA  # 累加总平面面积

        # 计算该分区的平面矩
        planMoment = dA * x1 + 2.0 * math.pow(l, 2) * (r1o / 6.0 + r2o / 3.0)
        planCenter += planMoment  # 累加平面中心贡献值

    # 归一化平面中心，确保其相对位置合理
    if planArea > 0:
        planCenter /= planArea

    # 根据飞行条件调整影响系数 mul
    mul = 1
    if mach < 0.05 and AOA > math.pi / 4:
        mul = math.pow(mach / 0.05, 2)  # 低速高攻角时降低影响

    # 返回压力中心和气动力参数
    return (planCenter, 0, 0, mul * 1.1 * planArea / refArea * sinAOA * sincAOA)
    ############### End ###############
