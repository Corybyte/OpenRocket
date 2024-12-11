import finSetMOI.solution



def calculate_moi(param):
    moi1 = finSetMOI.solution.calculate_unit_rot_moi(param['length'], param['span'], param['finArea'], param['bodyRadius'], param['finCount'])
    moi2 = finSetMOI.solution.calculate_Long_moi(param['length'], param['span'], param['singlePlanformArea'], param['finCount'], param['bodyRadius'])
    return [moi1,moi2]
