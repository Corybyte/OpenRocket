import math
def calculateCG(length: float, instanceSeparation: float, instanceCount: int) -> float:
    ############### Begin ###############
    return length/2+(instanceSeparation*(instanceCount-1))/2
    ############### End ###############