import math
import finSetCP.solution
from utils.common_helper import save_params


def calculateCP(param):
    save_params(param, "/data/workspace/myshixun/finSetCP/params.json")
    ar = param['ar']
    denom = math.pow(1 - 3.4641 * ar, 2)  # common denominator
    poly = [0] * 6
    poly[5] = (-1.58025 * (-0.728769 + ar) * (-0.192105 + ar)) / denom
    poly[4] = (12.8395 * (-0.725688 + ar) * (-0.19292 + ar)) / denom
    poly[3] = (-39.5062 * (-0.72074 + ar) * (-0.194245 + ar)) / denom
    poly[2] = (55.3086 * (-0.711482 + ar) * (-0.196772 + ar)) / denom
    poly[1] = (-31.6049 * (-0.705375 + ar) * (-0.198476 + ar)) / denom
    poly[0] = (9.16049 * (-0.588838 + ar) * (-0.20624 + ar)) / denom
    return finSetCP.solution.calculateCP(param['macLead'], param['macLength'], param['mach'],
                                         ar, poly)
