import math


# 计算基准的阻力系数（CD），考虑多个组件
def calculateBaseCD(instancesList, mach, refArea, ForeRadius, AftRadius, length, nextComonents, nextRadiuss,
                    isComponentActive):
    ############### Begin #############
    total = 0  # 用于累积总的CD
    base = calculateCD_mach(mach)  # 计算根据马赫数得到的基准阻力系数
    for i in range(len(instancesList)):
        foreRadius = ForeRadius[i]  # 当前组件的前半径
        aftRadius = AftRadius[i]  # 当前组件的后半径

        # 如果组件的长度为0，使用最大半径作为前后半径
        if length[i] == 0:
            componentMaxR = max(foreRadius, aftRadius)
            foreRadius = aftRadius = componentMaxR

        instanceCount = instancesList[i]  # 当前组件的数量

        # 如果该组件有后继组件且激活，获取后继组件的半径
        if nextComonents[i] and isComponentActive[i]:
            nextRadius = nextRadiuss[i]
        else:
            nextRadius = 0  # 否则没有后继组件，半径为0

        # 如果后继组件的半径小于当前组件的后半径，计算阻力系数
        if nextRadius < aftRadius:
            area = math.pi * (pow(aftRadius, 2) - pow(nextRadius, 2))  # 计算组件的截面积
            cd = base * area / refArea  # 计算阻力系数
            total += instanceCount * cd  # 累加当前组件的总阻力系数

    return total  # 返回计算得到的总阻力系数
    ############### End ###############


# 根据马赫数计算基础阻力系数（CD）
def calculateCD_mach(mach):
    ############### Begin #############
    if mach <= 1:
        return 0.12 + 0.13 * mach * mach  # 对于马赫数小于或等于1，使用公式计算
    else:
        return 0.25 / mach  # 对于马赫数大于1，使用公式计算
    ############### End ###############
