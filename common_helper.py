import math


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
