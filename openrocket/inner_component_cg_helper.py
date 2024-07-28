import math

import common_helper
import step25.solution


def calculateCG(param):
    cg = step25.solution.calculateCG(param['instanceCount'], param['innerRadius'],
                                     param['outerRadius'], param['length'], param['density'], param['instanceOffsets'])
    return cg[0]
