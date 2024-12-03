import math

import common_helper
import step24.solution


def calculateMOI(param):
    moi1 = step24.solution.calculate_unit_rot_moi(param['innerRadius'], param['outerRadius'])
    moi2 = step24.solution.calculate_Long_moi(param['innerRadius'], param['outerRadius'], param['length'])
    return [moi1, moi2]
