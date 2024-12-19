#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time    : 2024/12/17 10:42
# @Author  : Switch616
# @File    : interpolator.py
# @Description:
import numpy as np



class PolyInterpolator:
    def __init__(self, *points):
        """
        初始化PolyInterpolator，points是一个包含多个约束条件（函数值、导数等）的元组。
        """
        self.count = sum(len(p) for p in points)
        self.interpolation_matrix = self._calculate_interpolation_matrix(points)

    def _calculate_interpolation_matrix(self, points):
        my_count = self.count
        interpolation_matrix = np.zeros((my_count, my_count))

        # 计算多项式插值矩阵
        row = 0
        for j, point in enumerate(points):
            mul = 1
            for i, value in enumerate(point):
                x = 1
                for col in range(my_count - 1 - j, -1, -1):
                    interpolation_matrix[row][col] = x * mul
                    x *= value
                row += 1
            mul *= (my_count - j - 1)

        # 计算矩阵的逆
        return np.linalg.inv(interpolation_matrix)

    def interpolator(self, *values):
        """
        生成插值多项式，返回多项式的系数。
        """
        if len(values) != self.count:
            raise ValueError(f"Expected {self.count} values, got {len(values)}")

        return np.dot(self.interpolation_matrix, values)

    def interpolate(self, x, *values):
        """
        在给定的x处插值，返回插值结果。
        """
        coefficients = self.interpolator(*values)
        return self._eval_polynomial(x, coefficients)

    def _eval_polynomial(self, x, coefficients):
        """
        计算多项式在x处的值。
        """
        result = 0
        v = 1
        for coef in reversed(coefficients):
            result += coef * v
            v *= x
        return result
