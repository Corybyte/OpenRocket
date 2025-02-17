import math
def calculateLongMOICone(h, r):
    ############### Begin #############
    m = pow(r, 2) * h
    Ixx = 3 * m * (pow(r, 2) / 20 + pow(h, 2) / 5)
    return Ixx
    ############### End ###############
def calculate_unit_rot_moi(r1, r2):
    """
    Calculates the unit rotational moment of inertia for a frustum or a cylinder.
    Args:
        r1 (float): Radius of one end of the frustum.
        r2 (float): Radius of the other end of the frustum.
    Returns:
        float: The unit rotational moment of inertia.
    """
    # Define an epsilon value for floating point comparison
    ############### Begin #############
    EPSILON = 0.00000001
    # Check for cylinder special case
    if abs(r1 - r2) < EPSILON:
        return 10.0 * pow(r1, 2) / 6.0
    return (math.pow(r2, 5) - math.pow(r1, 5)) / (math.pow(r2, 3) - math.pow(r1, 3))*3/10
    ############### End ###############
def calculate_Long_moi(l, r1, r2, cg):
    EPSILON = 0.00000001
    ############### Begin ############
    if abs(r1 - r2) < EPSILON:
        moi = cg[1] * (3 * pow(r1, 2) + pow(l, 2)) / 12.0
        return moi
    shiftCG = cg[0]
    if r1 > r2:
        tmp = r1
        r1 = r2
        r2 = tmp
        shiftCG = l - cg[0]
    h2 = l * r2 / (r2 - r1)
    h1 = h2 * r1 / r2
    moi1 = calculateLongMOICone(h1, r1)
    moi2 = calculateLongMOICone(h2, r2)
    moi = moi2 - moi1
    moi = moi - pow((h1 + shiftCG), 2) * cg[1]
    return moi
    ############### End ###############