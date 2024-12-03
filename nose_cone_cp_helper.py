import step2.solution
from common_helper import save_params


def calculateCP(param):
    save_params(param, "/data/workspace/myshixun/step2/params.json")
    ans = step2.solution.calculateCP(param['foreRadius'], param['aftRadius'], param['length'], param['fullVolume'],
                                     param['mach'], param['AOA'], param['planformCenter'],
                                     param['planformArea'], param['refArea'], param['sinAOA'], param['sincAOA'])
    return ans[0]
