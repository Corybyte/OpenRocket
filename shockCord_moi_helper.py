import step33.solution


def calculateMOI(param):

    moi1 = step33.solution.calculate_unit_rot_moi(param['radius'])
    moi2 = step33.solution.calculate_Long_moi(param['radius'], param['length'])
    return [moi1, moi2]
