import math
import sys


def calculatetoalCD(mach, instanceCount, componentCD, isSymmetricComponent,
                        foreRadius, aftRadius, length, prevAftRadius, hasPreviousSymmetricComponent, refArea,isComponentActive):
    stagnation = calculateStagnationCD(mach)
    total = 0
    for i in range(len(instanceCount)):
        total += componentCD[i] * instanceCount[i]
        if isSymmetricComponent[i]:
            if length[i] == 0:
                foreRadius[i] = max(foreRadius[i], aftRadius[i])
        radius = 0
        if hasPreviousSymmetricComponent:
            if isComponentActive[i]:
                radius = prevAftRadius[i]
            if radius < foreRadius[i]:
                area = math.pi * (pow(foreRadius, 2) - pow(radius, 2))
                cd = stagnation * area / refArea
                total = total + instanceCount[i] * cd
    return total


def calculateStagnationCD(mach):
    if mach <= 1:
        pressure = 1 + pow(mach, 2) + pow(pow(mach, 2), 2) / 40
    else:
        pressure = 1.84 - 0.76 / pow(mach, 2) + 0.166 / pow(pow(mach, 2), 2) + 0.035 / pow(mach * mach * mach, 2)
    return 0.85 * pressure
