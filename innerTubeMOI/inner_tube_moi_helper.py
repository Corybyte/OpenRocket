import innerTubeMOI.solution


def calculateMOI(param):
    moi1 = innerTubeMOI.solution.calculate_unit_rot_moi(param['innerRadius'], param['outerRadius'])
    moi2 = innerTubeMOI.solution.calculate_Long_moi(param['innerRadius'], param['outerRadius'], param['length'])
    return [moi1, moi2]
