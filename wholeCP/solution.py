import math

from wholeCG import MyComponent
from utils.common_helper import average
import sys

rocket = None

#componentList为包含所有组件的列表
def calculateCP(componentList: list) -> float:
    # 定义max_float为浮点数的最大值
    max_float = sys.float_info.max
    # 定义整体压心worst
    worst = [max_float, 0, 0, 0]
    for i in range(360):
        # 定义组件集合的压心
        assemblyCp = [0, 0, 0, 0]
        ############### Begin ###############
        # 遍历当前所有组件的列表

        # 判断当前组件存在压心

        # 如果存在压心则获取当前组件的压心并与组件集合的压心合并

        ############### Begin ###############
        if (assemblyCp[3] > 0.00000001) and (assemblyCp[0] < worst[0]):
            worst = assemblyCp

    return worst


def merge(a, b):
    if (a[3] < 0.00000001):
        a = b
    else:
        a = average(a, b)
    return a
