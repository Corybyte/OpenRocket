import math

from utils import common_helper
import innerTubeCG.solution


def calculateCG(param):
    cg = innerTubeCG.solution.calculateCG(param['instanceCount'], param['innerRadius'],
                                          param['outerRadius'], param['length'], param['density'], param['instanceOffsets'])
    return cg[0]
