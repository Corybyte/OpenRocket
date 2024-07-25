import math
import step6.solution
from nose_cone_cg_helper import get_radius
from common_helper import safe_sqrt, equals


def calculate_moi(param):
    if(param['filled']):
        innerRadius =  0
    else:
        innerRadius = max(param['outerRadius'] - param['thickness'], 0)
    moi = step6.solution.calculateMOI(innerRadius, param['outerRadius'])
    return moi

# if __name__ == '__main__':
#     print(calculate_moi(0.002, 0.025))
