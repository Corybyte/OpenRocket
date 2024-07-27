import math


def calculateMOI(outerRadius: float, innerRadius: float) -> float:
    return (math.pow(outerRadius, 2) + math.pow(innerRadius, 2)) / 2
