import math
from utils.common_helper import transform

def calculateCG(outerRadius, innerRadius, length,
                fins, density, bodyRadius):
    """
    计算部件的质心 (CG)。

    Args:
        outerRadius (float): 外部半径（外壳半径）。
        innerRadius (float): 内部半径（空心部分的半径）。
        length (float): 组件的长度。
        fins (int): 该组件上附加的翼片数量。
        density (float): 组件的材料密度。
        bodyRadius (float): 火箭主体的半径。

    Returns:
        list[float]: 变换后的质心坐标和质量值 [CG_x, CG_y, CG_z, mass]。
    """

    ############### Begin ###############

    # 计算组件的体积（假设为圆柱或圆环结构）
    volume = (outerRadius ** 2 - innerRadius ** 2) * math.pi * length

    # 计算质量
    mass = density * volume * fins  # 乘以翼片数量

    # 质心沿长度方向位于中点
    halflength = length / 2

    # 计算并返回变换后的质心
    if fins == 1:
        return transform([halflength, outerRadius + bodyRadius, 0, mass])
    else:
        return transform([halflength, 0, 0, mass])

    ############### End ###############
