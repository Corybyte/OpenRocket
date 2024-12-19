#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time    : 2024/12/12 10:36
# @Author  : Switch616
# @File    : calculateCN.py
# @Description:
import warnings

from wingDemo.utils import *
import math


def calculateFinCNa1(conditions):
    """
      根据飞行条件计算鳍片的 CNa1
      :param conditions: 字典，包含飞行条件（mach, ref_area, aoa 等）
      :return: 鳍片的 CNa1 值
    """
    mach = conditions['FlightConditions_Mach']
    ref = conditions['FlightConditions_RefArea']

    # 需要传递的参数
    span = conditions['FinSetCalc_span']
    fin_area = conditions['FinSetCalc_finArea']
    cos_gamma = conditions['FinSetCalc_cosGamma']

    return 2 * math.pi * pow2(span) / (1 + safeSqrt(1 + (1 - pow2(mach)) *
                                                    pow2(pow2(span) / (fin_area * cos_gamma)))) / ref


def calculate_nonaxial_forces(param):
    (
        tau, cna, cna1, mac_lead, mac_length, mac_spen, body_radius, cant_angle,
        flight_conditions_mach, flight_conditions_beta, flight_conditions_ref_length,
        flight_conditions_aoa, stall_angle, aerodynamic_forces_croll_force,
        theta, interferenceFinCount, span, fin_area, cos_gamma, STALL_ANGLE
    ) = get_calculate_nonaxial_forces_required_params(param)
    cna1 = calculateFinCNa1(param)
    # 计算基本的 CNa（不考虑干扰效应）
    # 基本的 CNa 值通过正弦函数计算，考虑了theta与angle的差异
    cna = cna1 * pow2(math.sin(theta - cant_angle))
    # 根据鳍片数量调整 CNa，考虑鳍片间的干扰效应
    if interferenceFinCount == 5:
        cna *= 0.948  # 对于5片鳍片，应用系数0.948
    elif interferenceFinCount == 6:
        cna *= 0.913  # 对于6片鳍片，应用系数0.913
    elif interferenceFinCount == 7:
        cna *= 0.854  # 对于7片鳍片，应用系数0.854
    elif interferenceFinCount == 8:
        cna *= 0.81  # 对于8片鳍片，应用系数0.81
    elif interferenceFinCount >= 9:
        # 对于超过8片鳍片，假设效率为75%
        cna *= 0.75
        warnings.warn("PARALLEL_FINS")

    # 计算机身与鳍片之间的干扰效应
    r = body_radius  # 机身半径
    tau = r / (span + r)  # 计算干扰效应比例

    # 如果tau值无效（为NaN或无穷大），则设定tau为0
    if math.isnan(tau) or math.isinf(tau):
        tau = 0

    # 应用机身与鳍片的干扰效应
    cna *= (1 + tau)  # Barrowman公式，用于计算机身与鳍片的干扰效应

    cn = cna * min(flight_conditions_aoa, STALL_ANGLE)

    return {'cn': cn}
