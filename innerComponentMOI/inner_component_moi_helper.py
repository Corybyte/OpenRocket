import innerComponentMOI.solution


def calculateMOI(param):
    moi1 = innerComponentMOI.solution.calculate_unit_rot_moi(param['innerRadius'], param['outerRadius'])
    moi2 = innerComponentMOI.solution.calculate_Long_moi(param['innerRadius'], param['outerRadius'], param['length'])
    return [moi1, moi2]
