import math

import MyComponent
from common_helper import average
import sys

rocket = None


def calculateCP(componentList: list) -> float:
    max_float = sys.float_info.max
    worst = [max_float, 0, 0, 0]
    for i in range(360):
        assemblyCp = [0, 0, 0, 0]
        for component in componentList:
            # 存在cp
            if component.rocket_component_calc:
                componentCp = [component.cp[0], component.cp[1], component.cp[2],
                               component.cp[3]]
                assemblyCp = merge(assemblyCp, componentCp)
        if (assemblyCp[3] > 0.00000001) and (assemblyCp[0] < worst[0]):
            worst = assemblyCp

    return worst


def merge(a, b):
    if (a[3] < 0.00000001):
        a = b
    else:
        a = average(a, b)
    return a
