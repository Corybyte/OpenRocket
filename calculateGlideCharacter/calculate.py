import math


def calculateCharacter(cl_max, n_max, refArea, gravity):
    # 标准空气密度
    p = 1.225
    V = math.sqrt((2 * n_max * gravity) / p * cl_max * refArea)
    r = V * V / (9.8 * math.sqrt(n_max * n_max - 1))
    return r
