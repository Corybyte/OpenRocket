import math
from common_helper import average

def calculateCP(foreRadius: float, aftRadius: float, length: float, fullVolume: float,
                mach: float, AOA: float, planformCenter: float, planformArea: float,
                refArea: float, sinAOA: float, sincAOA: float) -> float:
    r0 = foreRadius
    r1 = aftRadius
    if r0 == r1:
        isTube = True
        cnaCache = 0
    else:
        isTube = False
        AO = math.pi * pow(r0, 2)
        A1 = math.pi * pow(r1, 2)
        cnaCache = 2 * (A1 - AO)
        cpCache = (length * A1 - fullVolume) / (A1 - AO)
    mul = 1
    if mach < 0.05 and AOA > math.pi / 4:
        mul = math.pow(mach / 0.05, 2)
    LiftCP = (planformCenter, 0, 0, mul * 1.1 * planformArea / refArea * sinAOA * sincAOA)

    if isTube:
        cp = LiftCP
    else:
        cp = (cpCache, 0, 0, cnaCache * sincAOA / refArea)
        cp = average(cp,LiftCP)
    print(cp)
    return cp[0]
