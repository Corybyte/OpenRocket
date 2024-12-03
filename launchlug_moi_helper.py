import step18.solution


def calculateMOI(param):
    moi1 = step18.solution.calculate_unit_rot_moi(param['outerRadius'], param['innerRadius'])
    moi2 = step18.solution.calculate_Long_moi(param['outerRadius'], param['innerRadius'], param['length'])
    return [moi1, moi2]
