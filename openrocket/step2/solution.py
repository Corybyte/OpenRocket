import math
from common_helper import average

def calculateCP(foreRadius: float, aftRadius: float, length: float, fullVolume: float,
                mach: float, AOA: float, planformCenter: float, planformArea: float, 
                refArea: float, sinAOA: float, sincAOA: float) -> tuple[float, float, float, float]:		
    A0 = math.pi * math.pow(foreRadius, 2)
    A1 = math.pi * math.pow(aftRadius, 2)
			
    cnaCache = 2 * (A1 - A0)
    cpCache = (length * A1 - fullVolume) / (A1 - A0)
		
    a = (cpCache, 0, 0, cnaCache * sincAOA / refArea)
    mul = 1
    if mach < 0.05 and AOA > math.pi / 4:
        mul = math.pow(mach / 0.05, 2)
    b = (planformCenter, 0, 0, mul * 1.1 * planformArea / refArea * sinAOA * sincAOA)

    cp = average(a, b)
    return cp