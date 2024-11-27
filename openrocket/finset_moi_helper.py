import math

import step9.solution
import step6.solution
from nose_cone_cg_helper import get_radius
from common_helper import safe_sqrt, equals


def calculate_moi(param):
    return step9.solution.calculateMOI(param['length'], param['span'], param['finArea'], param['bodyRadius'], param['finCount'])