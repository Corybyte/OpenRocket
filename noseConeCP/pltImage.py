import json
import math

import numpy as np
import matplotlib.pyplot as plt
from solution import calculateCP


def load_params():
    # JSON 
    with open("/data/workspace/myshixun/noseConeCP/params.json", "r") as f:
        param = json.load(f)
    return param

if __name__ == "__main__":
    param = load_params()
    # mach as cp
    mach = np.arange(0, 1, 0.01)
    cpx_list = np.zeros(len(mach))
    for i in range(len(mach)):
        cpx_list[i] = calculateCP(param['foreRadius'], param['aftRadius'], param['length'], param['fullVolume'],
                                  mach[i], param['AOA'], param['planformCenter'],
                                  param['planformArea'], param['refArea'], param['sinAOA'], param['sincAOA'])[0]
    plt.plot(mach, cpx_list)
    plt.title('Mach vs CPX')  # 设置标题
    plt.xlabel('Mach')  # 设置 x 轴标签
    plt.ylabel('CPX')  # 设置 y 轴标签
    plt.savefig('/data/workspace/myshixun/noseConeCP/image/mach.png', dpi=300, bbox_inches='tight')

    # aoa as cp
    AOAList = np.arange(-90, 90, 1)
    cpx_list2 = np.zeros(len(AOAList))
    for i in range(len(AOAList)):
        AOA = AOAList[i]
        AOA = AOA * (math.pi / 180)
        if AOA < 0:
            AOA = 0
        if AOA > math.pi:
            AOA = math.pi
        if AOA < 0.01:
            sinAOA = AOA
            sincAOA = 1.0
        else:
            sinAOA = np.sin(AOA)
            sincAOA = sinAOA / AOA
        cpx_list2[i] = calculateCP(param['foreRadius'], param['aftRadius'], param['length'], param['fullVolume'],
                                   param['mach'], AOA, param['planformCenter'],
                                   param['planformArea'], param['refArea'], sinAOA, sincAOA)[0]

    plt.plot(AOAList, cpx_list2)
    plt.title('AOA vs CPX')  # 设置标题
    plt.xlabel('AOA')  # 设置 x 轴标签
    plt.ylabel('CPX')  # 设置 y 轴标签
    plt.savefig('/data/workspace/myshixun/noseConeCP/image/aoa.png', dpi=300, bbox_inches='tight')
