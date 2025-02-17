def calculatePressureCD(finArea, mach,
                        crossSection, cosGammaLead, span, thickness, refArea):
    # 如果鳍片面积小于一个极小值，返回0
    if finArea < 0.00000001:
        return 0.0

    # 计算停滞压强系数和基面压强系数
    stagnationCD = calculateStagnationCD(mach)
    baseCD = calculateBaseCD(mach)
    cd = 0

    # 根据不同的横截面形状计算不同的压强系数
    if crossSection == '翼型' or crossSection == '圆形':
        ############### Begin #############
        # 对于亚音速（Mach < 0.9），计算压强系数
        if mach < 0.9:
            cd = pow(1 - pow(mach, 2), -0.417) - 1
        # 对于0.9 <= Mach < 1，按公式计算压强系数
        elif mach < 1:
            cd = 1 - 1.785 * (mach - 0.9)
        else:
            # 对于超音速（Mach >= 1），使用给定公式计算
            cd = 1.214 - 0.502 / pow(mach, 2) + 0.1095 / pow(pow(mach, 2), 2)
        ############### End #############
    elif crossSection == '矩形':
        # 如果是矩形横截面，使用停滞压强系数
        cd = stagnationCD

    # 根据引导角（cosGammaLead）调整压强系数
    cd *= pow(cosGammaLead, 2)

    # 如果是矩形横截面，加入基面压强系数
    if crossSection == '矩形':
        cd += baseCD
    # 如果是圆形横截面，加入基面压强系数的一半
    elif crossSection == '圆形':
        cd += baseCD / 2

    ############### Begin #############
    # 根据展长（span）和厚度（thickness）对压强系数进行进一步调整
    cd *= span * thickness / refArea
    ############### End #############
    return cd


def calculateStagnationCD(mach):
    # 对于Mach <= 1，计算停滞压强系数
    if mach <= 1:
        pressure = 1 + pow(mach, 2) / 4 + pow(pow(mach, 2), 2) / 40
    else:
        # 对于Mach > 1，使用给定公式计算
        pressure = 1.84 - 0.76 / pow(mach, 2) + 0.166 / pow(pow(mach, 2), 2) + 0.035 / pow(mach * mach * mach, 2)

    # 返回计算后的停滞压强系数
    return 0.85 * pressure


def calculateBaseCD(mach):
    # 对于Mach <= 1，返回压强系数
    if mach <= 1:
        return 0.12 + 0.13 * mach * mach
    else:
        # 对于Mach > 1，按给定公式返回压强系数
        return 0.25 / mach
