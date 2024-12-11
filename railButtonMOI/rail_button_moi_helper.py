import railButtonMOI.solution


def calculateMOI(param):
    moi1 = railButtonMOI.solution.calculate_unit_rot_moi()
    moi2 = railButtonMOI.solution.calculate_Long_moi()
    return [moi1, moi2]
