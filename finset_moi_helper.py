import step9.solution



def calculate_moi(param):
    moi1 = step9.solution.calculate_unit_rot_moi(param['length'], param['span'], param['finArea'], param['bodyRadius'], param['finCount'])
    moi2 = step9.solution.calculate_Long_moi(param['length'], param['span'], param['singlePlanformArea'], param['finCount'], param['bodyRadius'])
    return [moi1,moi2]
