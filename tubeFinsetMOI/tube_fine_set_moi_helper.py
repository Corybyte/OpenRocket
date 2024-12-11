import math

from utils import common_helper
import tubeFinsetMOI.solution


def calculateMOI(param):
    moi1 = tubeFinsetMOI.solution.calculate_unit_rot_moi(param['innerRadius'], param['outerRadius'],
                                                         param['bodyRadius'], param['fins'])
    moi2 = tubeFinsetMOI.solution.calculate_Long_moi(param['innerRadius'], param['outerRadius'],
                                                     param['fins'], param['length'], param['axialOffset'])

    return [moi1, moi2]
