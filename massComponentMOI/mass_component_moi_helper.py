import massComponentMOI.solution


def calculateMOI(param):
    moi1 = massComponentMOI.solution.calculate_unit_rot_moi(param['radius'])
    moi2 = massComponentMOI.solution.calculate_Long_moi(param['radius'], param['length'])
    return [moi1, moi2]
