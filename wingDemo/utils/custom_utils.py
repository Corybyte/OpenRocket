#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time    : 2024/12/12 10:54
# @Author  : Switch616
# @File    : custom_utils.py
# @Description:
import math

DIVISIONS = 48
CNA_SUBSONIC = 4.9
CNA_SUPERSONIC = 1.5
# 初始化系数数组
n = int((5.0 - CNA_SUPERSONIC) * 10)
X = [0.0] * n
K1 = [0.0] * n
K2 = [0.0] * n
K3 = [0.0] * n
# 初始化弦长数组
chord_length = [0.0] * DIVISIONS
roll_sum = 0.0
# 计算超音速CNA的常数B
CNA_SUPERSONIC_B = math.pow(math.pow(CNA_SUPERSONIC, 2) - 1, 1.5)


def get_calculate_nonaxial_forces_required_params(param):
    """
    从参数字典中获取必须的参数，如果缺少任何参数则抛出异常。

    :param param: 包含所有必要参数的字典
    :return: 获取到的各个参数
    :raises ValueError: 如果缺少任何必需的参数
    """
    required_keys = [
        'Tau', 'Cna', 'Cna1', 'MacLead', 'MacLength', 'MacSpen', 'BodyRadius',
        'CantAngle', 'FlightConditions_Mach', 'FlightConditions_Beta',
        'FlightConditions_RefLength', 'FlightConditions_AOA', 'STALL_ANGLE',
        'AerodynamicForces_CrollForce'
    ]

    # 遍历每个必需的键，检查是否在 param 中
    for key in required_keys:
        if key not in param:
            raise ValueError(f"Missing required parameter: {key}")

    # 如果所有必需的键都存在，返回它们的值
    Tau = param['Tau']
    Cna = param['Cna']
    Cna1 = param['Cna1']
    MacLead = param['MacLead']
    MacLength = param['MacLength']
    MacSpen = param['MacSpen']
    BodyRadius = param['BodyRadius']
    CantAngle = param['CantAngle']
    FlightConditions_Mach = param['FlightConditions_Mach']
    FlightConditions_Beta = param['FlightConditions_Beta']
    FlightConditions_RefLength = param['FlightConditions_RefLength']
    FlightConditions_AOA = param['FlightConditions_AOA']
    STALL_ANGLE = param['STALL_ANGLE']
    AerodynamicForces_CrollForce = param['AerodynamicForces_CrollForce']

    return (
        Tau, Cna, Cna1, MacLead, MacLength, MacSpen, BodyRadius, CantAngle,
        FlightConditions_Mach, FlightConditions_Beta, FlightConditions_RefLength,
        FlightConditions_AOA, STALL_ANGLE, AerodynamicForces_CrollForce
    )


def calculate_damping_moment(conditions):
    """
    计算阻尼力矩

    :param conditions: 飞行条件对象，包含横滚率、速度等信息
    :return: 阻尼力矩
    """

    roll_rate = conditions.get_roll_rate()

    if abs(roll_rate) < 0.1:
        return 0

    mach = conditions.get_mach()
    abs_rate = abs(roll_rate)

    body_radius = conditions.get_body_radius()
    span = conditions.get_span()
    velocity = conditions.get_velocity()
    ref_area = conditions.get_ref_area()
    ref_length = conditions.get_ref_length()

    # 在低速和较大的横滚率情况下，考虑翼尖单独旋转的影响
    if abs_rate * (body_radius + span) / velocity > 15 * math.pi / 180:
        sum_moment = 0
        for i in range(DIVISIONS):
            dist = body_radius + span * i / DIVISIONS
            aoa = min(abs_rate * dist / velocity, 15 * math.pi / 180)
            sum_moment += chord_length[i] * dist * aoa

        return (math.copysign(1, roll_rate) *
                sum_moment * (span / DIVISIONS) * 2 * math.pi / conditions.get_beta() /
                (ref_area * ref_length))

    # 亚音速时的计算公式
    if mach <= CNA_SUBSONIC:
        return (2 * math.pi * roll_rate * roll_sum /
                (ref_area * ref_length * velocity * conditions.get_beta()))

    # 超音速时的计算公式
    if mach >= CNA_SUPERSONIC:
        vel = velocity
        k1 = interpolate(K1, mach)
        k2 = interpolate(K2, mach)
        k3 = interpolate(K3, mach)

        sum_moment = 0
        for i in range(DIVISIONS):
            y = i * span / (DIVISIONS - 1)
            angle = roll_rate * (body_radius + y) / vel
            sum_moment += (k1 * angle + k2 * angle ** 2 + k3 * angle ** 3) * chord_length[i] * (body_radius + y)

        return (sum_moment * span / (DIVISIONS - 1) /
                (ref_area * ref_length))

    # 跨音速，进行线性插值
    cond = conditions.clone()
    cond.set_mach(CNA_SUBSONIC - 0.01)
    subsonic = calculate_damping_moment(cond)
    cond.set_mach(CNA_SUPERSONIC + 0.01)
    supersonic = calculate_damping_moment(cond)

    return (subsonic * (CNA_SUPERSONIC - mach) / (CNA_SUPERSONIC - CNA_SUBSONIC) +
            supersonic * (mach - CNA_SUBSONIC) / (CNA_SUPERSONIC - CNA_SUBSONIC))


def calculate_cp_pos(conditions):
    """
    计算气动中心(CP)的位置

    :param conditions: 飞行条件对象，包含马赫数、beta等信息
    :return: 气动中心位置
    """
    m = conditions.get_mach()
    poly = range(1, 6)  # 用于插值多项式的系数

    if m <= 0.5:
        # 亚音速时，气动中心位于四分之一弦长处
        return 0.25

    if m >= 2:
        # 超音速时，使用经验公式
        beta = conditions.get_beta()
        ar = conditions.get_aspect_ratio()  # 假设ar为展弦比
        return (ar * beta - 0.67) / (2 * ar * beta - 1)

    # 过渡区域，使用插值多项式
    x = 1.0
    val = 0
    for coeff in poly:  # poly是一个多项式系数数组
        val += coeff * x
        x *= m

    return val


def clamp(x, min_val, max_val):
    """
    限制值x在[min_val, max_val]范围内

    :param x: 输入值
    :param min_val: 最小值
    :param max_val: 最大值
    :return: 限制后的值
    """
    if x < min_val:
        return min_val
    if x > max_val:
        return max_val
    return x


def interpolate(values, mach):
    """
    插值计算，返回对应Mach数的值

    :param values: 包含Mach数和对应值的列表 [(mach1, value1), (mach2, value2), ...]
    :param mach: 当前Mach数
    :return: 插值后的值
    """
    for i in range(len(values) - 1):
        m1, v1 = values[i]
        m2, v2 = values[i + 1]
        if m1 <= mach <= m2:
            # 线性插值
            return v1 + (v2 - v1) * (mach - m1) / (m2 - m1)
    return values[-1][1] if mach > values[-1][0] else values[0][1]
