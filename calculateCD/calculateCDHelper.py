from calculateCD.solution import calculateBaseCD


def calculateCD(param):
    return calculateBaseCD(param['instanceCount'],param['mach'],param['refArea'],
                           param['foreRadius'],param['aftRadius'],param['length'],param['nextComponents'],param['nextRadius'],param['isComponentActives'])