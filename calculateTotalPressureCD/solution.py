import math
import sys

# 计算总的阻力系数（CD），考虑了多个组件
def calculatetoalCD(mach, instanceCount, componentCD, isSymmetricComponent,
                        foreRadius, aftRadius, length, prevAftRadius, hasPreviousSymmetricComponent, refArea,isComponentActive):
    ############### Begin #############
    stagnation = calculateStagnationCD(mach)  # 计算静止流阻力系数
    total = 0  # 用于累积总阻力系数
    for i in range(len(instanceCount)):
        total += componentCD[i] * instanceCount[i]  # 累加每个组件的阻力系数
        if isSymmetricComponent[i]:  # 如果该组件是对称的
            if length[i] == 0:  # 如果长度为0，则使用最大半径
                foreRadius[i] = max(foreRadius[i], aftRadius[i])  # 更新前半径为前后半径中的最大值
        radius = 0  # 初始化半径
        if hasPreviousSymmetricComponent:  # 如果有前一个对称组件
            if isComponentActive[i]:  # 如果组件激活
                radius = prevAftRadius[i]  # 使用上一个组件的后半径
            if radius < foreRadius[i]:  # 如果前半径大于上一个组件的后半径
                area = math.pi * (pow(foreRadius[i], 2) - pow(radius, 2))  # 计算截面积
                cd = stagnation * area / refArea  # 计算阻力系数
                total = total + instanceCount[i] * cd  # 累加组件的阻力系数
    return total  # 返回总阻力系数
    ############### End #############

# 根据马赫数计算静止流的阻力系数（CD）
def calculateStagnationCD(mach):
    ############### Begin #############
    if mach <= 1:  # 如果马赫数小于或等于1
        pressure = 1 + pow(mach, 2)/4 + pow(pow(mach, 2), 2) / 40  # 使用公式计算静止流压力
    else:  # 如果马赫数大于1
        pressure = 1.84 - 0.76 / pow(mach, 2) + 0.166 / pow(pow(mach, 2), 2) + 0.035 / pow(mach * mach * mach, 2)  # 使用不同的公式计算
    return 0.85 * pressure  # 返回计算得到的静止流阻力系数
    ############### End #############
