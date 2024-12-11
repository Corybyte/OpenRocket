import noseConeCP
from utils.common_helper import save_params


def calculateCP(param):
    save_params(param, "/data/workspace/myshixun/noseConeCP/params.json")
    ans = noseConeCP.solution.calculateCP(param['foreRadius'], param['aftRadius'], param['length'], param['fullVolume'],
                                          param['mach'], param['AOA'], param['planformCenter'],
                                          param['planformArea'], param['refArea'], param['sinAOA'], param['sincAOA'])
    return ans[0]
