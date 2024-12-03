import math

import common_helper
import step15.solution


def calculateMOI(param):
    print(param)
    moi1 = step15.solution.calculate_unit_rot_moi(param['innerRadius'], param['outerRadius'],
                                                  param['bodyRadius'], param['fins'])
    moi2 = step15.solution.calculate_Long_moi(param['innerRadius'], param['outerRadius'],
                                              param['fins'], param['length'], param['axialOffset'])

    return [moi1, moi2]
