import step27.solution


def calculateMOI(param):
    moi1 = step27.solution.calculate_unit_rot_moi(param['innerRadius'], param['outerRadius'])
    moi2 = step27.solution.calculate_Long_moi(param['innerRadius'], param['outerRadius'], param['length'])
    return [moi1, moi2]
