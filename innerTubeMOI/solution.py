import math
def calculate_unit_rot_moi(outerRadius, innerRadius):
    ############### Begin #############
    return (math.pow(outerRadius, 2) + math.pow(innerRadius, 2)) / 2
    ############### End ###############
def calculate_Long_moi(outerRadius, innerRadius, length):
    ############### Begin #############
    inertia = (3 * (pow(outerRadius, 2) + pow(innerRadius, 2)) + pow(length, 2)) / 12
    return inertia
    ############### End ###############