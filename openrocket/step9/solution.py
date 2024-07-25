import math


def calculateMOI(w: float, h: float, finArea: float, bodyRadius: float,finCount: int) -> float:
    if (w * h == 0):
        h2 = finArea
    else:
        h2 = h * finArea / w
    inertia = h2 / 12
    if (finCount == 1):
        return inertia
    return inertia + math.pow(safeSqrt(h2) / 2 + bodyRadius, 2)

def safeSqrt(d):
    if d < 0:
        if d < 0.01:
            return 0

    return math.sqrt(d)

