def calculateCP(outerRadius, chord, ar,
                poly, mach, beta):
    if outerRadius < 0.001:
        return 0
    cp = calculateCPPos(mach, beta, ar, poly) * chord
    return cp


def calculateCPPos(mach, beta, ar, poly):
    ############### Begin ###############
    pass

    ############### End ###############
