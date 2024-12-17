#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time    : 2024/12/12 10:36
# @Author  : Switch616
# @File    : calculateCN.py
# @Description:
from wingDemo.utils import *
import math


def calculate_nonaxial_forces(param):
    (
        tau, cna, cna1, mac_lead, mac_length, mac_span, bodyRadius, cant_angle,
        FlightConditions_Mach, FlightConditions_Beta, FlightConditions_RefLength,
        FlightConditions_AOA, STALL_ANGLE, AerodynamicForces_CrollForce
    ) = get_calculate_nonaxial_forces_required_params(param)

    # 缺失参数
    aoa = 0
    roll_sum = 0
    #
    r = bodyRadius
    if math.isnan(tau) or math.isinf(tau):
        tau = 0
    cna *= 1 + tau  # 使用经典Barrowman公式计算cna

    # 计算气动中心位置
    x = mac_lead + calculate_cp_pos(param) * mac_length
    croll_force = (mac_span + r) * cna1 * (1 + tau) * cant_angle / param.get_ref_length()
    if aoa > STALL_ANGLE:
        croll_force = croll_force * \
                      clamp(1 - (aoa - STALL_ANGLE) / (STALL_ANGLE / 2), 0, 1)

    croll_damp = calculate_damping_moment(param)
    # 设置阻尼力矩
    # croll = croll_force - croll_damp
    # 设置法向力系数
    cn = cna * min(aoa, STALL_ANGLE)
    print("croll_force: ", croll_force, "croll_damp: ", croll_damp, "cn: ", cn)

    return 0.0
