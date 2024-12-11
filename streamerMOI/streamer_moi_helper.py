import streamerMOI.solution


def calculateMOI(param):
    moi1 = streamerMOI.solution.calculate_unit_rot_moi(param['radius'])
    moi2 = streamerMOI.solution.calculate_Long_moi(param['radius'], param['length'])
    return [moi1, moi2]
