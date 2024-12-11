import math
import bodyTubeMOI.solution


def calculate_moi(param):
    if (param['filled']):
        innerRadius = 0
    else:
        innerRadius = max(param['outerRadius'] - param['thickness'], 0)
    moi = bodyTubeMOI.solution.calculate_unit_rot_moi(innerRadius, param['outerRadius'])
    moi2 = bodyTubeMOI.solution.calculate_Long_moi(param['outerRadius'], param['innerRadius'], param['length'])
    return [moi, moi2]
