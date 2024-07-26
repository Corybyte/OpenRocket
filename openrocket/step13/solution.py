import math

import common_helper


def calculateCG(outerRadius: float, innerRadius: float, length: float,
                fins: int, density: float, bodyRadius: float) -> tuple[float, float, float]:
    volume = outerRadius * outerRadius - innerRadius * innerRadius
    volume *= math.pi
    volume *= length
    volume *= fins

    mass = density * volume

    halflength = length / 2
    if (fins == 1):
        return common_helper.transform([halflength, outerRadius + bodyRadius, 0, mass])
    else:
        return common_helper.transform([halflength, 0, 0, mass])
