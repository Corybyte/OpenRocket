import step36.solution


def calculateMOI(param):
    moi1 = step36.solution.calculate_unit_rot_moi(param['radius'])
    moi2 = step36.solution.calculate_Long_moi(param['radius'], param['length'])
    return [moi1, moi2]
