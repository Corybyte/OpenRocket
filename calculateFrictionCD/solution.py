import math
import sys


def calculateFrictionCD(mach, velocity, lengthAerodynamic, kinematicViscosity, isPerfectFinish, aeroDynamic, refArea,
                        component, finish_ordinal, roughnessSize, wetArea, thickness, macLength, finArea,
                        otherComponentFrictionCD, instanceCount, axialOffset, length, foreRadius, aftRadius):
    ############### Begin #############
    # 计算雷诺数
    Re = calculateReynoldsNumber(velocity, lengthAerodynamic, kinematicViscosity)
    # 计算摩擦系数
    Cf = calculateFrictionCoefficient(isPerfectFinish, mach, Re)
    # 计算粗糙度修正系数
    roughnessCorrection = calculateRoughnessCorrection(mach)

    # 初始化摩擦阻力系数变量
    otherFrictionCD = 0
    bodyFrictionCD = 0
    maxR = 0
    minX = float('inf')
    maxX = 0
    roughnessLimited = [None] * 9

    # 处理每个部件的摩擦系数
    for item in range(len(component)):
        # 如果该部件的粗糙度修正系数尚未计算，则计算
        if roughnessLimited[finish_ordinal[item]] is None:
            roughnessLimited[finish_ordinal[item]] = 0.032 * pow(roughnessSize[item] / aeroDynamic,
                                                                 0.2) * roughnessCorrection

        # 计算该部件的摩擦系数
        componentCf = 0.0
        if isPerfectFinish:
            # 完美表面处理的情况
            if Re > 1.0e6 and roughnessLimited[finish_ordinal[item]] > Cf:
                componentCf = roughnessLimited[finish_ordinal[item]]
            else:
                componentCf = Cf
        else:
            # 非完美表面处理的情况
            componentCf = max(Cf, roughnessLimited[finish_ordinal[item]])

        # 根据部件类型计算摩擦阻力系数
        componentFrictionCD = 0.0
        if component[item] == 'Symmetriccomponent':
            componentFrictionCD = calculateSymmetricComponentFrictionCD(componentCf, wetArea[item], refArea)
        elif component[item] == 'Finset':
            componentFrictionCD = calculateFinsetFrictionCD(componentCf, thickness[item], macLength[item],
                                                            finArea[item], refArea)
        else:
            componentFrictionCD = otherComponentFrictionCD[item]

        # 处理对称部件的摩擦阻力系数
        if component[item] == "Symmetriccomponent":
            bodyFrictionCD += instanceCount[item] * componentFrictionCD
            componentMinX = axialOffset[item]
            minX = min(minX, componentMinX)
            componentMaxX = componentMinX + length[item]
            maxX = max(maxX, componentMaxX)
            componentMaxR = max(foreRadius[item], aftRadius[item])
            maxR = max(maxR, componentMaxR)
        else:
            otherFrictionCD += instanceCount[item] * componentFrictionCD

    # 计算摩擦阻力的修正系数
    fB = (maxX - minX + 0.0001) / maxR
    correction = (1 + 1.0 / (2 * fB))

    # 返回最终的摩擦阻力系数
    return otherFrictionCD + correction * bodyFrictionCD
    ############### End #############


def calculateReynoldsNumber(velocity, lengthAerodynamic, kinematicViscosity):
    # 计算雷诺数
    return velocity * lengthAerodynamic / kinematicViscosity


def calculateFrictionCoefficient(isPerfectFinish, mach, Re):
    Cf = 0
    c1 = 1.0
    c2 = 1.0
    if isPerfectFinish:
        # 假设部分层流，粗糙度限制稍后检查
        if Re < 1e4:
            Cf = 1.33e-2
        elif Re < 5.39e5:
            Cf = 1.328 / math.sqrt(Re)
        # 压缩性修正
        if mach < 1.1:
            if Re > 1e6:
                if Re < 3e6:
                    c1 = 1 - 0.1 * pow(mach, 2) * (Re - 1e6) / 2e6
                else:
                    c1 = 1 - 0.1 * pow(mach, 2)
        if mach > 0.9:
            if Re > 1e6:
                if Re < 3e6:
                    c2 = 1 + (1.0 / pow(1 + 0.045 * pow(mach, 2), 0.21) - 1) * (Re - 1e6) / 2e6
                else:
                    c2 = 1.0 / pow(1 + 0.045 * pow(mach, 2), 0.25)
        if mach < 0.9:
            Cf *= c1
        elif mach < 1.1:
            Cf *= (c2 * (mach - 0.9) / 0.2 + c1 * (1.1 - mach) / 0.2)
        else:
            Cf *= c2
    else:
        # 非完美表面处理的情况
        if Re < 1e4:
            Cf = 1.48e-2
        else:
            Cf = 1.0 / pow(1.50 * math.log(Re) - 5.6, 2)
        if mach < 1.1:
            c1 = 1 - 0.1 * pow(mach, 2)
        if mach > 0.9:
            c2 = 1 / pow(1 + 0.15 * pow(mach, 2), 0.58)
        if mach < 0.9:
            Cf *= c1
        elif mach < 1.1:
            Cf *= c2 * (mach - 0.9) / 0.2 + c1 * (1.1 - mach) / 0.2
        else:
            Cf *= 0.2
    return Cf


def calculateRoughnessCorrection(mach):
    c1 = 0
    c2 = 0
    roughnessCorrection = 0
    if mach < 0.9:
        roughnessCorrection = 1 - 0.1 * pow(mach, 2)
    elif mach > 1.1:
        roughnessCorrection = 1 / (1 + 0.18 * pow(mach, 2))
    else:
        c1 = 1 - 0.1 * pow(0.9, 2)
        c2 = 1.0 / (1 + 0.18 * pow(1.1, 2))
        roughnessCorrection = c1 * (mach - 0.9) / 0.2 + c1 * (1.1 - mach) / 0.2
    return roughnessCorrection


def calculateSymmetricComponentFrictionCD(componentCf, wetArea, refArea):
    # 计算对称部件的摩擦阻力系数
    return componentCf * wetArea / refArea


def calculateFinsetFrictionCD(componentCf, thickness, macLength, finArea, refArea):
    # 计算鳍片集的摩擦阻力系数
    if finArea < 0.00000001:
        return 0.0
    cd = componentCf * (1 + 2 * thickness / macLength) * 2 * finArea / refArea
    return cd
