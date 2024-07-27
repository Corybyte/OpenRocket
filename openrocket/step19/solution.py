import math


def calculateCG(instanceSeparation: float, instanceCount: int) -> float:
    return (instanceSeparation * (instanceCount - 1)) / 2
