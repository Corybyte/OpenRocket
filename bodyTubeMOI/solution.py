def calculate_unit_rot_moi(innerRadius, outerRadius):
    ############### Begin #############
    return (pow(innerRadius, 2) + pow(outerRadius, 2)) / 2
    ############### End ###############
def calculate_Long_moi(outerRadius, innerRadius, length):
    ############### Begin #############
    return (3 * (pow(outerRadius, 2) + pow(innerRadius, 2)) + pow(length, 2)) / 12
    ############### End ###############