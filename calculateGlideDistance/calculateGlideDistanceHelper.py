from calculateGlideDistance.calculate import calculateGlideDis


def calculateGlideDistancehelper(param):
    height = param['height']
    c_l = param['c_l']
    c_d = param['c_d']
    return calculateGlideDis(height, c_l, c_d)
