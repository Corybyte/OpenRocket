import math

from utils import common_helper
import tubeFinsetCG.solution


def calculateCG(param):
    cg = tubeFinsetCG.solution.calculateCG(param['outerRadius'], param['innerRadius'],
                                           param['length'], param['fins'], param['density'], param['bodyRadius'])
    return cg[0]
