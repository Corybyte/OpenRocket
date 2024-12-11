import launchLugMOI.solution


def calculateMOI(param):
    moi1 = launchLugMOI.solution.calculate_unit_rot_moi(param['outerRadius'], param['innerRadius'])
    moi2 = launchLugMOI.solution.calculate_Long_moi(param['outerRadius'], param['innerRadius'], param['length'])
    return [moi1, moi2]
