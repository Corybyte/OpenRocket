def calculatePressureCD(finArea, mach,
                        crossSection, cosGammaLead, span, thickness, refArea):
    if finArea < 0.00000001:
        return 0.0
    stagnationCD = calculateStagnationCD(mach)
    baseCD = calculateBaseCD(mach)
    cd = 0
    if crossSection == '翼型' or crossSection == '圆形':
        if mach < 0.9:
            cd = pow(1 - pow(mach, 2), -0.417) - 1
        elif mach < 1:
            cd = 1 - 1.785 * (mach - 0.9)
        else:
            cd = 1.214 - 0.502 / pow(mach, 2) + 0.1095 / pow(pow(mach, 2), 2)
    elif crossSection == '矩形':
        cd = stagnationCD

    cd *= pow(cosGammaLead, 2)
    if crossSection == '矩形':
        cd += baseCD
    elif crossSection == '圆形':
        cd += baseCD / 2
    cd *= span * thickness / refArea
    return cd


def calculateStagnationCD(mach):
    if mach <= 1:
        pressure = 1 + pow(mach, 2) + pow(pow(mach, 2), 2) / 40
    else:
        pressure = 1.84 - 0.76 / pow(mach, 2) + 0.166 / pow(pow(mach, 2), 2) + 0.035 / pow(mach * mach * mach, 2)
    return 0.85 * pressure


def calculateBaseCD(mach):
    if mach <= 1:
        return 0.12 + 0.13 * mach * mach
    else:
        return 0.25 / mach
