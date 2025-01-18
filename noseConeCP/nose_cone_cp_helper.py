from utils.common_helper import save_params
from noseConeCP.solution import calculateCP

def calculateNoseConeCP(param):
    save_params(param, "/data/workspace/myshixun/noseConeCP/params.json")
    ans = calculateCP(param['foreRadius'], param['aftRadius'], param['length'], param['fullVolume'],
                                          param['mach'], param['AOA'], param['planformCenter'],
                                          param['planformArea'], param['refArea'], param['sinAOA'], param['sincAOA'])
    return ans[0]
