#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time    : 2024/12/12 10:54
# @Author  : Switch616
# @File    : custom_utils.py
# @Description:
import math


def get_calculate_nonaxial_forces_required_params(param):
    """
    从参数字典中获取必须的参数，如果缺少任何参数则抛出异常。

    :param param: 包含所有必要参数的字典
    :return: 获取到的各个参数
    :raises ValueError: 如果缺少任何必需的参数
    """
    # required_keys = [
    #     'Tau', 'Cna', 'Cna1', 'MacLead', 'MacLength', 'MacSpen', 'BodyRadius',
    #     'CantAngle', 'FlightConditions_Mach', 'FlightConditions_Beta',
    #     'FlightConditions_RefLength', 'FlightConditions_AOA', 'STALL_ANGLE', 'AerodynamicForces_CrollForce',
    #     # 需要新增
    #     'FinSetCalc_theta', 'interferenceFinCount', 'FinSetCalc_span', 'FinSetCalc_finArea', 'FinSetCalc_cosGamma',
    #     'STALL_ANGLE'
    # ]

    required_keys = [
        'Tau', 'Cna', 'Cna1', 'MacLead', 'MacLength', 'MacSpen', 'BodyRadius',
        'CantAngle', 'FlightConditions_Mach', 'FlightConditions_Beta',
        'FlightConditions_RefLength', 'FlightConditions_AOA', 'STALL_ANGLE', 'AerodynamicForces_CrollForce',
        # 需要新增
        'FinSetCalc_theta', 'interferenceFinCount', 'FinSetCalc_span', 'FinSetCalc_finArea', 'FinSetCalc_cosGamma',
        'STALL_ANGLE','angle'
    ]

    # 遍历每个必需的键，检查是否在 param 中
    for key in required_keys:
        if key not in param:
            raise ValueError(f"Missing required parameter: {key}")

    # 如果所有必需的键都存在，返回它们的值
    tau = param['Tau']
    cna = param['Cna']
    cna1 = param['Cna1']
    mac_lead = param['MacLead']
    mac_length = param['MacLength']
    mac_spen = param['MacSpen']
    body_radius = param['BodyRadius']
    cant_angle = param['CantAngle']
    flight_conditions_mach = param['FlightConditions_Mach']
    flight_conditions_beta = param['FlightConditions_Beta']
    flight_conditions_ref_length = param['FlightConditions_RefLength']
    flight_conditions_aoa = param['FlightConditions_AOA']
    stall_angle = param['STALL_ANGLE']
    aerodynamic_forces_croll_force = param['AerodynamicForces_CrollForce']
    # 需要新增
    theta = param['FinSetCalc_theta']
    interferenceFinCount = param['interferenceFinCount']
    span = param['FinSetCalc_span']
    fin_area = param['FinSetCalc_finArea']
    cos_gamma = param['FinSetCalc_cosGamma']
    STALL_ANGLE = param['STALL_ANGLE']
    angle = param['angle']

    return (
        tau, cna, cna1, mac_lead, mac_length, mac_spen, body_radius, cant_angle,
        flight_conditions_mach, flight_conditions_beta, flight_conditions_ref_length,
        flight_conditions_aoa, stall_angle, aerodynamic_forces_croll_force,
        theta, interferenceFinCount, span, fin_area, cos_gamma, STALL_ANGLE,
        angle
    )
