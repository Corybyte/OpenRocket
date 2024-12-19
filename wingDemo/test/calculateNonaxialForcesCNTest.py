#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time    : 2024/12/19 10:04
# @Author  : Switch616
# @File    : calculateNonaxialForcesCNTest.py
# @Description:
import pytest
from wingDemo.calculateNonaxialForcesCN import *


class TestCalculateNonaxialForces:

    @pytest.mark.testgroup("Basic Test Group")
    @pytest.mark.testcase("Basic Test Case 1")
    def testCalculateFinCNa1(self):
        print("\n")
        data = {
            "Tau": 0.2941176470588236,
            "Cna": 4.938876408107876,
            "Cna1": 5.088539595082702,
            "MacLead": 0.012699999999999996,
            "MacLength": 0.05079999999999996,
            "MacSpen": 0.014999999999999996,
            "BodyRadius": 0.0125,
            "CantAngle": 0.0,
            "FlightConditions_Mach": 0.300,  # 从控制台数据提取 mach=0.300
            "FlightConditions_Beta": 0.9989304850678329,  # 这里你可以根据需要设置 Beta
            "FlightConditions_RefLength": 0.025,  # 从控制台数据提取 refLength=0.025
            "FlightConditions_AOA": 0.00,  # 从控制台数据提取 aoa=0.00°
            "STALL_ANGLE": 0.3490658503988659,  # 如果需要设置角度，可以调整
            "AerodynamicForces_CrollForce": 0.0,
            "FlightConditions_RefArea": 0.0004908738521234052,  # 如果有相关参考值，替换此处
            "FinSetCalc_finArea": 0.0015239999999999997,
            "FinSetCalc_cosGamma": 0.7631932598976461,
            "FinSetCalc_span": 0.03,
            "interferenceFinCount": 3.0,  # 如果需要设置该值
            "FinSetCalc_theta": 1.00,  # 从控制台数据提取 theta=1.00°
            "result_CN": 0.5371673027739671  # 如果有相关值，可以更新
        }
        cna1 = calculateFinCNa1(data)
        print(f"cna1: {cna1}")
        theta = 0.017453292519943295
        cant_angle = 0.0
        cna = cna1 * pow2(math.sin(theta - cant_angle))
        r = 0.0125
        tau = 0.2941176470588236
        cna *= 1 + tau
        print(f"cna: {cna}")
        aoa = 0.00
        STALL_ANGLE = 0.3490658503988659
        cn = cna * min(aoa,STALL_ANGLE)
        print(f"cn: {cn}")


    def test2(self):
        print("\n")
        print(22)
