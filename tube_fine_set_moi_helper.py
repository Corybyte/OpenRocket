import math

import common_helper
import step15.solution


def calculateMOI(param):
    moi = step15.solution.calculateMOI(param['innerRadius'], param['outerRadius'],
                                       param['bodyRadius'], param['fins'])
    return moi
