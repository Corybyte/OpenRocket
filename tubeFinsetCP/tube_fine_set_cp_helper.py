import math

from utils import common_helper
import tubeFinsetCG.solution
import tubeFinsetCP.solution


def calculateCP(param):
    cp = tubeFinsetCP.solution.calculateCP(param['outerRadius'], param['chord'],
                                           param['ar'], param['poly'], param['mach'], param['beta'])
    return cp
