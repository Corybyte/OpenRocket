import math
import step6.solution


def calculate_moi(param):
    print(param)
    if (param['filled']):
        innerRadius = 0
    else:
        innerRadius = max(param['outerRadius'] - param['thickness'], 0)
    moi = step6.solution.calculate_unit_rot_moi(innerRadius, param['outerRadius'])
    moi2 = step6.solution.calculate_Long_moi(param['outerRadius'], param['innerRadius'], param['length'])
    return [moi, moi2]
