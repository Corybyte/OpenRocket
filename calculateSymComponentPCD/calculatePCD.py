from openrocket.calculateSymComponentPCD.solution import calculatePressureCD


def calculate(param):
    foreRadius = param['forceRadius']
    aftRadius = param['aftRadius']
    length = param['length']
    mach = param['mach']
    frontalArea = param['frontalArea']
    refArea = param['refArea']
    interpolatorValue = param['interpolatorValue']
    fineness = param['fineness']
    return calculatePressureCD(foreRadius, aftRadius, length,
                               mach, frontalArea, refArea, interpolatorValue, fineness)
