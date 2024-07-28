import math

from common_helper import average


def calculateCG(instanceCount: int, innerRadius: float, outerRadius: float, length: float, density: float,
                instanceOffsets: tuple[tuple]) -> float:
    cg = [0, 0, 0, 0]
    instanceMass = math.pi * max(outerRadius * outerRadius - innerRadius * instanceCount, 0) * length * density
    if instanceCount == 1:
        cg = [length / 2, 0, 0, instanceMass]
    else:
        for i in range(instanceOffsets):
            instanceOffsets[i][3] = instanceMass
            cg = average(cg, instanceMass[i])
        cg = [cg[0] + length / 2, 0, 0]
    return cg
