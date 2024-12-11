import json
import math
import os


def safe_sqrt(x):
    if x < 0:
        return 0
    else:
        return math.sqrt(x)


def equals(a, b):
    epsilon = 0.00000001
    absb = abs(b)
    if absb < epsilon / 2:
        return abs(a) < epsilon / 2
    return abs(a - b) < epsilon * absb


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


def transform(orig):
    matrix = [[0, 0, 0], [0, 0, 0], [0, 0, 0]]
    matrix[0][0] = 1
    matrix[1][2] = 1
    matrix[1][2] = 1

    x = matrix[0][0] * orig[0] + matrix[0][1] * orig[0] + matrix[2][2] * orig[0]
    y = matrix[1][0] * orig[0] + matrix[1][1] * orig[0] + matrix[2][2] * orig[0]
    z = matrix[2][0] * orig[0] + matrix[2][1] * orig[0] + matrix[2][2] * orig[0]
    return [x, y, z]


def CONICAL_radius(x, radius, length, param):
    return radius * x / length


def ELLIPSOID_radius(x, radius, length, param):
    x = x * radius / length
    return safe_sqrt(2 * radius * x - x * x)


def HAACK_radius(x, radius, length, param):
    theta = math.acos(1 - 2 * x / length)
    if equals(param, 0):
        return radius * safe_sqrt((theta - math.sin(2 * theta) / 2) / math.pi)
    return radius * safe_sqrt((theta - math.sin(2 * theta) / 2 + param * math.pow(math.sin(theta), 3)) / math.pi)


def OGIVE_radius(x, radius, length, param):
    if length < radius:
        x = x * radius / length
        length = radius
    if param < 0.001:
        return CONICAL_radius(x, radius, length, param)
    R = safe_sqrt((math.pow(length, 2) + math.pow(radius, 2)) * (
            math.pow((2 - param) * length, 2) + math.pow(param * radius, 2)) / (4 * math.pow(param * radius, 2)))
    L = length / param
    y0 = safe_sqrt(R * R - L * L)
    return safe_sqrt(R * R - (L - x) * (L - x)) - y0


def PARABOLIC_radius(x, radius, length, param):
    return radius * ((2 * x / length - param * math.pow(x / length, 2)) / (2 - param))


def POWER_radius(x, radius, length, param):
    if param <= 0.00001:
        if x <= 0.00001:
            return 0
        else:
            return radius
    return radius * math.pow(x / length, param)


def get_radius(x, type, length, foreRadius, aftRadius, shapeParameter):
    if x < 0:
        return foreRadius
    if x >= length:
        return aftRadius

    r1 = foreRadius
    r2 = aftRadius

    if r1 == r2:
        return r1

    if r1 > r2:
        x = length - x
        tmp = r1
        r1 = r2
        r2 = tmp

    if type == "CONICAL":
        return r1 + CONICAL_radius(x, r2 - r1, length, shapeParameter)
    elif type == "ELLIPSOID":
        return r1 + ELLIPSOID_radius(x, r2 - r1, length, shapeParameter)
    elif type == "HAACK":
        return r1 + HAACK_radius(x, r2 - r1, length, shapeParameter)
    elif type == "OGIVE":
        return r1 + OGIVE_radius(x, r2 - r1, length, shapeParameter)
    elif type == "PARABOLIC":
        return r1 + PARABOLIC_radius(x, r2 - r1, length, shapeParameter)
    else:  # POWER
        return r1 + POWER_radius(x, r2 - r1, length, shapeParameter)


def ringCG(outerRadius, innerRadius, x1, x2, desity):
    return [(x1 + x2) / 2, 0, 0, ringMass(outerRadius, innerRadius, x2 - x1, desity)]


def ringMass(outerRadius, innerRadius, length, desity):
    return math.pi * max(math.pow(outerRadius, 2) - math.pow(innerRadius, 2), 0) * length * desity


def ringVolume(outerRadius, innerRadius, length):
    return ringMass(outerRadius, innerRadius, length, 1.0)


def ringLongitudinalUnitInertia(outerRadius, innerRadius, length):
    return (3 * (math.pow(innerRadius, 2) + math.pow(outerRadius, 2)) + math.pow(length, 2)) / 12


def ringRotationalUnitInertia(outerRadius, innerRadius):
    return (math.pow(innerRadius, 2) + math.pow(outerRadius, 2)) / 2


def save_params(param, filepath):
    # 将参数写入 JSON 文件
    os.makedirs(os.path.dirname(filepath), exist_ok=True)
    with open(filepath, "w") as f:
        json.dump(param, f)
