import math

import common_helper
import step13.solution
import step14.solution


def calculateCP(param):
    cp = step14.solution.calculateCP(param['outerRadius'], param['chord'],
                                       param['ar'], param['poly'], param['mach'], param['beta'])
    return cp
