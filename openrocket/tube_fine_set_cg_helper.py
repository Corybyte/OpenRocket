import math

import common_helper
import step13.solution


def calculateCG(param):
    cg = step13.solution.calculateCG(param['outerRadius'], param['innerRadius'],
                                       param['length'], param['fins'], param['density'], param['bodyRadius'])
    return cg[0]
