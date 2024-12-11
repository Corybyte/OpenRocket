import shockCordMOI.solution


def calculateMOI(param):

    moi1 = shockCordMOI.solution.calculate_unit_rot_moi(param['radius'])
    moi2 = shockCordMOI.solution.calculate_Long_moi(param['radius'], param['length'])
    return [moi1, moi2]
