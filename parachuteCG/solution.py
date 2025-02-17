import math
def calculate_unit_rot_moi(outerRadius):
    ############### Begin #############
    return pow(outerRadius, 2) / 2
    ############### End ###############
def calculate_Long_moi(outerRadius, length):
    ############### Begin #############
    return (3*pow(outerRadius,2)+pow(length,2))/12
    ############### End ###############