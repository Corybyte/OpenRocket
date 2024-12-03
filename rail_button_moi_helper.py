import step21.solution


def calculateMOI(param):
    moi1 = step21.solution.calculate_unit_rot_moi()
    moi2 = step21.solution.calculate_Long_moi()
    return [moi1, moi2]
