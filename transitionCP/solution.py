import math
from utils.common_helper import average


def calculateCP(foreRadius, aftRadius, length, fullVolume,
                mach, AOA, planformCenter, planformArea,
                refArea, sinAOA, sincAOA):
    """
    计算空气动力中心（Center of Pressure, CP）。

    Args:
        foreRadius (float): 物体前端半径。
        aftRadius (float): 物体后端半径。
        length (float): 物体长度。
        fullVolume (float): 物体的整体体积。
        mach (float): 马赫数。
        AOA (float): 迎角（Angle of Attack）。
        planformCenter (float): 平面形状的中心位置。
        planformArea (float): 平面面积。
        refArea (float): 参考面积（通常为基准面积）。
        sinAOA (float): 迎角的正弦值。
        sincAOA (float): 迎角的正弦 Cardinal 函数（sinc(x) = sin(x)/x）。

    Returns:
        float: 计算得到的空气动力中心（CP）位置。
    """

    ############### Begin ###############
    r0 = foreRadius  # 前端半径
    r1 = aftRadius  # 后端半径

    # 判断是否为圆柱体
    if r0 == r1:
        isTube = True
        cnaCache = 0  # 对于圆柱体，cnaCache 为 0，因为无前后收缩
    else:
        isTube = False
        AO = math.pi * pow(r0, 2)  # 前端截面积
        A1 = math.pi * pow(r1, 2)  # 后端截面积
        cnaCache = 2 * (A1 - AO)  # 计算法向力导数（Normal Force Coefficient）
        cpCache = (length * A1 - fullVolume) / (A1 - AO)  # 计算静态 CP 位置

    # 低速且迎角较大时，修正升力 CP
    mul = 1
    if mach < 0.05 and AOA > math.pi / 4:
        mul = math.pow(mach / 0.05, 2)

    # 计算升力 CP（主要影响因子）
    LiftCP = (planformCenter, 0, 0, mul * 1.1 * planformArea / refArea * sinAOA * sincAOA)

    # 根据是否为圆柱体决定 CP 计算方式
    if isTube:
        cp = LiftCP  # 直接使用升力 CP
    else:
        cp = (cpCache, 0, 0, cnaCache * sincAOA / refArea)  # 计算压力中心
        cp = average(cp, LiftCP)  # 结合升力和压力计算最终 CP

    return cp[0]  # 仅返回 CP 的 x 轴位置
    ############### End ###############
