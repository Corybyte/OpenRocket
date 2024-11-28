import math

import common_helper
import step22.solution


def calculateCG(param):
    cg = step22.solution.calculateCG(param['instanceCount'], param['innerRadius'],
                                     param['outerRadius'], param['length'], param['density'], param['instanceOffsets'])
    return cg[0]
