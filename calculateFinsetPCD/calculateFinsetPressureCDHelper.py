from openrocket.calculateFinsetPCD.solution import calculatePressureCD


def calculateFSPCD(param):
    finArea = param['finArea']
    mach = param['mach']
    crossSection = param['crossSection']
    cosGammaLead = param['cosGammaLead']
    span = param['span']
    thickness = param['thickness']
    refArea = param['refArea']
    return calculatePressureCD(finArea, mach, crossSection, cosGammaLead,
                               span, thickness, refArea)
