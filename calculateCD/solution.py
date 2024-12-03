import math


def calculateBaseCD(instancesList, mach, refArea, ForeRadius, AftRadius, length, nextComonents, nextRadiuss,
                    isComponentActive):
    total = 0
    base = calculateCD_mach(mach)
    for i in range(len(instancesList)):
        foreRadius = ForeRadius[i]
        aftRadius = AftRadius[i]
        if length[i] == 0:
            componentMaxR = max(foreRadius, aftRadius)
            foreRadius = aftRadius = componentMaxR
        instanceCount = instancesList[i]
        if nextComonents[i] and isComponentActive[i]:
            nextRadius = nextRadiuss[i]
        else:
            nextRadius = 0
        if nextRadius < aftRadius:
            area = math.pi * (pow(aftRadius, 2) - pow(nextRadius, 2))
            cd = base * area / refArea
            total += instanceCount * cd
    return total


def calculateCD_mach(mach):
    if mach <= 1:
        return 0.12 + 0.13 * mach * mach
    else:
        return 0.25 / mach
