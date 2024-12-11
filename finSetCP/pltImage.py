import json
import numpy as np
import matplotlib.pyplot as plt
from solution import calculateCP
import math


def load_params():
    # JSON 
    with open("/data/workspace/myshixun/finSetCP/params.json", "r") as f:
        param = json.load(f)
    return param


if __name__ == "__main__":
    param = load_params()
    ar = param['ar']
    denom = math.pow(1 - 3.4641 * ar, 2)  # common denominator
    poly = [0] * 6
    poly[5] = (-1.58025 * (-0.728769 + ar) * (-0.192105 + ar)) / denom
    poly[4] = (12.8395 * (-0.725688 + ar) * (-0.19292 + ar)) / denom
    poly[3] = (-39.5062 * (-0.72074 + ar) * (-0.194245 + ar)) / denom
    poly[2] = (55.3086 * (-0.711482 + ar) * (-0.196772 + ar)) / denom
    poly[1] = (-31.6049 * (-0.705375 + ar) * (-0.198476 + ar)) / denom
    poly[0] = (9.16049 * (-0.588838 + ar) * (-0.20624 + ar)) / denom
    #
    # # mach as cp
    mach = np.arange(0, 1, 0.01)
    cpx_list = np.zeros(len(mach))
    for i in range(len(mach)):
        cpx_list[i] = calculateCP(param['macLead'], param['macLength'], mach[i],
                                  ar, poly)
    plt.plot(mach, cpx_list)
    plt.title('Mach vs CPX')  # 设置标题
    plt.xlabel('Mach')  # 设置 x 轴标签
    plt.ylabel('CPX')  # 设置 y 轴标签
    plt.savefig('/data/workspace/myshixun/finSetCP/image/mach.png', dpi=300, bbox_inches='tight')