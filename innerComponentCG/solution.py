import math
from utils.common_helper import average


def calculateCG(instanceCount, innerRadius, outerRadius, length, density,
                instanceOffsets):
    ############### Begin #################
    # 初始化质心坐标cg为[0, 0, 0, 0]
    cg = [0, 0, 0, 0]

    # 计算每个实例的质量，公式为圆柱体体积 * 密度
    instanceMass = math.pi * max(outerRadius * outerRadius - innerRadius * instanceCount, 0) * length * density

    # 如果只有一个实例，质心在长度的中点
    if instanceCount == 1:
        cg = [length / 2, 0, 0, instanceMass]
    else:
        # 如果有多个实例，遍历每个实例的偏移量
        for i in range(instanceOffsets):
            # 更新每个实例的质量值
            instanceOffsets[i][3] = instanceMass
            # 计算所有实例的平均质心
            cg = average(cg, instanceMass[i])
        # 更新质心位置，使其偏移至整个结构的中心
        cg = [cg[0] + length / 2, 0, 0]

    return cg  # 返回计算得到的质心坐标
    ############### End #################
