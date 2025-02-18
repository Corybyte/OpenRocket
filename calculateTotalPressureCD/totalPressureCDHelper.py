from calculateFrictionCD.solution import calculateFrictionCD
import re

from calculateTotalPressureCD.solution import calculatetoalCD


def calculatePressureCDHelper(param):
    mach = param['mach']
    length = param['length']
    foreRadiuss = param['foreRadiuss']
    aftRadiuss = param['aftRadiuss']

    componentInstanceCount = param['componentInstanceCount']
    refArea = param['refArea']
    isSymmetricComponent = param['isSymmetricComponent']
    #  prevAftRadius = param['prevAftRadius']
    #   hasPreviousSymmetricComponent = param['hasPreviousSymmetricComponent']
    #   isComponentActives = param['isComponentActives']
    componentCD = param['componentCD']

    return calculatetoalCD(mach, componentInstanceCount, componentCD, isSymmetricComponent,
                           foreRadiuss, aftRadiuss, length, refArea)
