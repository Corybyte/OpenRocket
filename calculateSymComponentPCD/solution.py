def calculatePressureCD(foreRadius, aftRadius, length,
                        mach, frontalArea, refArea, interpolatorValue, fineness):
    ############### Begin #############
    stagnationCD = calculateStagnationCD(mach)
    baseCD = calculateBaseCD(mach)
    if foreRadius == aftRadius:
        return 0
    if length < 0.001:
        if foreRadius < aftRadius:
            return stagnationCD * frontalArea / refArea
        else:
            return baseCD * frontalArea / refArea
    if aftRadius < foreRadius:
        if fineness >= 3:
            return 0
        cd = baseCD * frontalArea / refArea
        if fineness <= 1:
            return cd * (3 - fineness) / 2
    return interpolatorValue * frontalArea / refArea
    ############### End #############
def calculateStagnationCD(mach):
    ############### Begin #############
    if mach <= 1:
        pressure = 1 + pow(mach, 2)/4 + pow(pow(mach, 2), 2) / 40
    else:
        pressure = 1.84 - 0.76 / pow(mach, 2) + 0.166 / pow(pow(mach, 2), 2) + 0.035 / pow(mach * mach * mach, 2)
    return 0.85*pressure
    ############### End #############
def calculateBaseCD(mach):
    ############### Begin #############
    if mach <= 1:
        return 0.12 + 0.13 * mach * mach
    else:
        return 0.25 / mach
    ############### End #############