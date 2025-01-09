from calculateGlideCharacter.calculate import calculateCharacter


def calculateGlideCharacterhelper(param):
    cl_max = param['max_cl']
    n_max = cl_max / param['w']
    refArea = param['refArea']
    w = param['w']
    return calculateCharacter(cl_max, n_max, refArea,w)
