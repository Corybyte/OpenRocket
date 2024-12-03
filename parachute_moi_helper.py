import step30.solution


def calculateMOI(param):
    print(param)
    moi1 = step30.solution.calculate_unit_rot_moi(param['radius'])
    print(moi1)
    moi2 = step30.solution.calculate_Long_moi(param['radius'], param['length'])
    return [moi1, moi2]
