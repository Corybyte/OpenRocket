import podsMOI.solution


def calculateMOI(param):
    moi1 = podsMOI.solution.calculate_unit_rot_moi()
    moi2 = podsMOI.solution.calculate_Long_moi()
    return [moi1, moi2]
