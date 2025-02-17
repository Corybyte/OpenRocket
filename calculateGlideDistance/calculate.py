def calculateGlideDis(height, c_l, c_d):
    ############### Begin #############
    tan = 1 / (c_l / c_d)
    return height / tan
    ############### End ###############