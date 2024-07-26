import math


def calculateMOI(innerRadius: float, outerRadius: float, bodyRadius: float,
                 fins: int) -> float:
    icentermass = (math.pow(innerRadius, 2) + math.pow(outerRadius, 2)) / 2
    # 5.76999
    print(icentermass)
    if fins == 1:
        return icentermass
    else:
        return fins * (icentermass + math.pow(outerRadius, 2) + bodyRadius)
