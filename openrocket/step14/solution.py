
def calculateCP(outerRadius: float, chord: float, ar: float,
                poly: tuple, mach: float, beta: float) -> float:
    if outerRadius < 0.001:
        return 0
    cp = calculateCPPos(mach, beta, ar, poly) * chord
    return cp

def calculateCPPos(mach: float, beta: float, ar: float, poly: tuple) -> float:
    if mach <= 0.5:
        return 0.25
    if mach >= 2:
        return (ar * beta - 0.67) / (2 * ar * beta - 1)

    x = 1.0
    val = 0

    for i in range(len(poly)):
        val += poly[i] * x
        x *= mach
    return val
