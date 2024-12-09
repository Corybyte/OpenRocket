from calculateAxialCD.solution import calculateAxialCD


def calculateAixalCDHelper(param):
    return calculateAxialCD(param['aoa'], param['cd'], param['axialDragPoly1'], param['axialDragPoly2'])
