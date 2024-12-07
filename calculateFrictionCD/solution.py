import math
import sys


def calculateFrictionCD(mach, velocity, lengthAerodynamic, kinematicViscosity, isPerfectFinish, aeroDynamic, refArea,
                        component, finish_ordinal, roughnessSize, wetArea, thickness, macLength, finArea,
                        otherComponentFrictionCD, instanceCount, axialOffset, length, foreRadius, aftRadius):
    Re = calculateReynoldsNumber(velocity, lengthAerodynamic, kinematicViscosity)
    Cf = calculateFrictionCoefficient(isPerfectFinish, mach, Re)
    roughnessCorrection = calculateRoughnessCorrection(mach)
    # Calculate the friction drag coefficient.
    otherFrictionCD = 0
    bodyFrictionCD = 0
    maxR = 0
    minX = float('inf')
    maxX = 0
    roughnessLimited = [None] * 9
    # component=componentName  去除cal helper
    for item in range(len(component)):
        if roughnessLimited[finish_ordinal[item]] is None:
            roughnessLimited[finish_ordinal[item]] = 0.032 * pow(roughnessSize[item] / aeroDynamic,
                                                                0.2) * roughnessCorrection

        componentCf = 0.0
        if isPerfectFinish:
            if Re > 1.0e6 and roughnessLimited[finish_ordinal[item]] > Cf:
                componentCf = roughnessLimited[finish_ordinal[item]]
            else:
                componentCf = Cf
        else:
            componentCf = max(Cf, roughnessLimited[finish_ordinal[item]])
        # 判断是否是对称
        componentFrictionCD = 0.0
        if component[item] == 'Symmetriccomponent':
            componentFrictionCD = calculateSymmetricComponentFrictionCD(componentCf, wetArea[item], refArea)
        elif component[item] == 'Finset':
            componentFrictionCD = calculateFinsetFrictionCD(componentCf, thickness[item], macLength[item],
                                                            finArea[item],
                                                            refArea)
        else:
            componentFrictionCD = otherComponentFrictionCD[item]
        if component[item] == "Symmetriccomponent":
            bodyFrictionCD += instanceCount[item] * componentFrictionCD
            componentMinX = axialOffset[item]
            minX = min(minX, componentMinX)
            componentMaxX = componentMinX + length[item]
            maxX = max(maxX, componentMaxX)
            componentMaxR = max(foreRadius[item], aftRadius[item])
            maxR = max(maxR, componentMaxR)
        else:
            otherFrictionCD += instanceCount[item] * componentFrictionCD
    fB = (maxX - minX + 0.0001) / maxR
    correction = (1 + 1.0 / (2 * fB))

    return otherFrictionCD + correction * bodyFrictionCD


def calculateReynoldsNumber(velocity, lengthAerodynamic, kinematicViscosity):
    return velocity * lengthAerodynamic / kinematicViscosity


def calculateFrictionCoefficient(isPerfectFinish, mach, Re):
    Cf = 0
    c1 = 1.0
    c2 = 1.0
    if isPerfectFinish:
        # Assume partial laminar layer.  Roughness-limitation is checked later.
        if Re < 1e4:
            Cf = 1.33e-2
        elif Re < 5.39e5:
            Cf = 1.328 / math.sqrt(Re)

        # Compressibility correction
        if mach < 1.1:
            if Re > 1e6:
                if Re < 3e6:
                    c1 = 1 - 0.1 * pow(mach, 2) * (Re - 1e6) / 2e6
                else:
                    c1 = 1 - 0.1 * pow(mach, 2)

        if mach > 0.9:
            if Re > 1e6:
                if Re < 3e6:
                    c2 = 1 + (1.0 / pow(1 + 0.045 * pow(mach, 2), 0.21) - 1) * (Re - 1e6) / 2e6
                else:
                    c2 = 1.0 / pow(1 + 0.045 * pow(mach, 2), 0.25)

        if mach < 0.9:
            Cf *= c1
        elif mach < 1.1:
            Cf *= (c2 * (mach - 0.9) / 0.2 + c1 * (1.1 - mach) / 0.2)
        else:
            Cf *= c2
    else:
        if Re < 1e4:
            Cf = 1.48e-2
        else:
            Cf = 1.0 / pow(1.50 * math.log(Re) - 5.6, 2)

        if mach < 1.1:
            c1 = 1 - 0.1 * pow(mach, 2)
        if mach > 0.9:
            c2 = 1 / pow(1 + 0.15 * pow(mach, 2), 0.58)
        if mach < 0.9:
            Cf *= c1
        elif mach < 1.1:
            Cf *= c2 * (mach - 0.9) / 0.2 + c1 * (1.1 - mach) / 0.2
        else:
            Cf *= 0.2
    return Cf


def calculateRoughnessCorrection(mach):
    c1 = 0
    c2 = 0
    roughnessCorrection = 0
    if mach < 0.9:
        roughnessCorrection = 1 - 0.1 * pow(mach, 2)
    elif mach > 1.1:
        roughnessCorrection = 1 / (1 + 0.18 * pow(mach, 2))
    else:
        c1 = 1 - 0.1 * pow(0.9, 2)
        c2 = 1.0 / (1 + 0.18 * pow(1.1, 2))
        roughnessCorrection = c1 * (mach - 0.9) / 0.2 + c1 * (1.1 - mach) / 0.2
    return roughnessCorrection


def calculateSymmetricComponentFrictionCD(componentCf, wetArea, refArea):
    return componentCf * wetArea / refArea


def calculateFinsetFrictionCD(componentCf, thickness, macLength, finArea, refArea):
    if finArea < 0.00000001:
        return 0.0
    cd = componentCf * (1 + 2 * thickness / macLength) * 2 * finArea / refArea
    return cd
