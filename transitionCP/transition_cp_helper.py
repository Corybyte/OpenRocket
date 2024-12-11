import math

import transitionCP.solution

def calculateCP(param):
    ans = transitionCP.solution.calculateCP(param['foreRadius'], param['aftRadius'], param['length'], param['fullVolume'],
                                            param['mach'], param['AOA'], param['planformCenter'],
                                            param['planformArea'], param['refArea'], param['sinAOA'], param['sincAOA'])
    return ans