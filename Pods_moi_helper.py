import step54.solution


def calculateMOI(param):
    moi1 = step54.solution.calculate_unit_rot_moi()
    moi2 = step54.solution.calculate_Long_moi()
    return [moi1, moi2]
