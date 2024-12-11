import parachuteMOI.solution


def calculateMOI(param):
    moi1 = parachuteMOI.solution.calculate_unit_rot_moi(param['radius'])
    moi2 = parachuteMOI.solution.calculate_Long_moi(param['radius'], param['length'])
    return [moi1, moi2]
