import math
def calculate_unit_rot_moi(innerRadius, outerRadius, bodyRadius,
                           fins):
    ############### Begin #############
    icentermass = (math.pow(innerRadius, 2) + math.pow(outerRadius, 2)) / 2
    if fins == 1:
        return icentermass
    else:
        return fins * (icentermass + math.pow(outerRadius, 2) + bodyRadius)
    ############### End ###############
def calculate_Long_moi(innerRadius, outerRadius, fins, length, axialOffset):
    ############### Begin #############
    inertia = (3 * (pow(outerRadius, 2) + pow(innerRadius, 2)) + pow(length, 2)) / 12
    if fins == 1:
        return inertia
    totalInertia = 0.0
    for i in range(fins):
        totalInertia += inertia + pow(axialOffset, 2)
    return totalInertia
    ############### End ###############