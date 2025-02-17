import math


def average(a, b):
    x1 = 0
    y1 = 0
    z1 = 0
    w1 = 0
    if b == None:
        return a
    w1 = a[3] + b[3]
    if abs(w1) < math.pow(0.00000001, 2):
        x1 = (a[0] + b[0]) / 2
        y1 = (a[1] + b[1]) / 2
        z1 = (a[2] + b[2]) / 2
        w1 = 0
    else:
        x1 = (a[0] * a[3] + b[0] * b[3]) / w1
        y1 = (a[1] * a[3] + b[1] * b[3]) / w1
        z1 = (a[2] * a[3] + b[2] * b[3]) / w1
    return (x1, y1, z1, w1)


def calculateCP(foreRadius, aftRadius, length, fullVolume,
                mach, AOA, planformCenter, planformArea,
                refArea, sinAOA, sincAOA):
    ############### Begin ###############
    # 计算前端和后端的截面积
    A0 = math.pi * math.pow(foreRadius, 2)
    A1 = math.pi * math.pow(aftRadius, 2)
    # 计算法向力系数增量（cnaCache）和压力中心位置（cpCache）
    cnaCache = 2 * (A1 - A0)
    cpCache = (length * A1 - fullVolume) / (A1 - A0)
    # 创建包含压力中心和力矩的四元组 a
    a = (cpCache, 0, 0, cnaCache * sincAOA / refArea)
    # 根据速度和攻角调整影响系数 mul
    mul = 1
    if mach < 0.05 and AOA > math.pi / 4:
        mul = math.pow(mach / 0.05, 2)
    # 创建包含平面贡献的四元组 b
    b = (planformCenter, 0, 0, mul * 1.1 * planformArea / refArea * sinAOA * sincAOA)
    cp = average(a, b)
    return cp
    ############### End ###############