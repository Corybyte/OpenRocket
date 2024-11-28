#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time    : 2024/11/28 15:33
# @Author  : Switch616
# @File    : custom_utils.py
# @Description:
import math


def get_required_params(param):
    """
    从参数字典中获取必须的参数，如果缺少任何参数则抛出异常。

    :param param: 包含所有必要参数的字典
    :return: 获取到的各个参数
    :raises ValueError: 如果缺少任何必需的参数
    """
    required_keys = [
        'client_CnaCache', 'client_ForeRadius', 'client_AftRadius', 'client_Mach',
        'client_AOA', 'client_RefArea', 'client_FullVolume', 'client_SincAOA',
        'client_Length', 'client_PlanformCenter', 'client_BODY_LIFT_K',
        'client_PlanformArea', 'client_SinAOA'
    ]

    # 遍历每个必需的键，检查是否在 param 中
    for key in required_keys:
        if key not in param:
            print(f"Missing required parameter: {key}")
            raise ValueError(f"Missing required parameter: {key}")

    # 如果所有必需的键都存在，返回它们的值
    client_CnaCache = param['client_CnaCache']
    client_ForeRadius = param['client_ForeRadius']
    client_AftRadius = param['client_AftRadius']
    client_Mach = param['client_Mach']
    client_AOA = param['client_AOA']
    client_RefArea = param['client_RefArea']
    client_FullVolume = param['client_FullVolume']
    client_SincAOA = param['client_SincAOA']
    client_Length = param['client_Length']
    client_PlanformCenter = param['client_PlanformCenter']
    client_BODY_LIFT_K = param['client_BODY_LIFT_K']
    client_PlanformArea = param['client_PlanformArea']
    client_SinAOA = param['client_SinAOA']

    return (
        client_CnaCache, client_ForeRadius, client_AftRadius, client_Mach, client_AOA, client_RefArea,
        client_FullVolume,
        client_SincAOA, client_Length, client_PlanformCenter, client_BODY_LIFT_K, client_PlanformArea, client_SinAOA
    )


class Coordinate:
    def __init__(self, x, y, z, weight):
        """
        Coordinate 类用于表示三维坐标和一个权重值（如升力）。

        :param x: x 坐标
        :param y: y 坐标
        :param z: z 坐标
        :param weight: 代表坐标点的权重（例如，升力值）
        """
        self.x = x
        self.y = y
        self.z = z
        self.weight = weight

    def __repr__(self):
        """
        """
        return f"Coordinate(x={self.x}, y={self.y}, z={self.z}, weight={self.weight})"

    def average(self, other):
        """计算当前 Coordinate 和另一个 Coordinate 对象的加权平均坐标"""
        EPSILON = 0.00000001

        if other is None:
            return self

        # 计算加权平均
        w1 = self.weight + other.weight

        # 如果权重和接近 0，则返回坐标的算术平均，并将权重设置为 0
        if abs(w1) < math.pow(EPSILON, 2):
            x1 = (self.x + other.x) / 2
            y1 = (self.y + other.y) / 2
            z1 = (self.z + other.z) / 2
            w1 = 0
        else:
            x1 = (self.x * self.weight + other.x * other.weight) / w1
            y1 = (self.y * self.weight + other.y * other.weight) / w1
            z1 = (self.z * self.weight + other.z * other.weight) / w1

        return Coordinate(x1, y1, z1, w1)

    def to_dict(self):
        """
        将 Coordinate 对象的属性转换为字典。

        :return: 字典格式的坐标数据
        """
        return {
            "x": self.x,
            "y": self.y,
            "z": self.z,
            "weight": self.weight
        }


def getLiftCP(Mach, AOA, planformCenter, BODY_LIFT_K, planformArea, RefArea, SinAOA, SincAOA, warnings=None):
    """
    计算升力中心坐标 (CP) ，根据给定的飞行条件、机体参数等。

    参数:
        Mach (float): 当前马赫数
        AOA (float): 攻角（弧度）
        planformCenter (float): 机翼中心位置
        BODY_LIFT_K (float): 升力常数
        planformArea (float): 机翼平面面积
        RefArea (float): 参考面积
        SinAOA (float): sin(aoa)，即攻角的正弦值
        SincAOA (float): sin(aoa)^2 / aoa（升力曲线修正项）
        warnings (list): 可选参数，用于记录警告信息

    返回:
        Coordinate: 返回计算出的升力中心 (CP) 坐标
    """
    # 初始 multiplier 为 1
    mul = 1

    # 如果马赫数小于 0.05 且攻角大于 45 度（π/4 弧度），则应用额外的修正因子
    if Mach < 0.05 and AOA > math.pi / 4:
        mul = math.pow((Mach / 0.05), 2)

    # 使用给定的参数计算升力中心 (CP)
    lift = mul * BODY_LIFT_K * planformArea / RefArea * SinAOA * SincAOA

    # 返回一个 Coordinate 对象，代表升力中心坐标
    return Coordinate(planformCenter, 0, 0, lift)
