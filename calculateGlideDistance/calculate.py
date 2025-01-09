def calculateGlideDis(height, c_l, c_d):
    tan = 1 / (c_l / c_d)
    return height / tan
