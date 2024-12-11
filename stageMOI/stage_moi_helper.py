import stageMOI.solution


def calculateMOI(param):
    moi1 = stageMOI.solution.calculate_unit_rot_moi()
    moi2 = stageMOI.solution.calculate_Long_moi()
    return [moi1, moi2]
