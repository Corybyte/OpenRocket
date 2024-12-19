#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time    : 2024/12/17 10:42
# @Author  : Switch616
# @File    : math_utils.py
# @Description:
import math


def safeSqrt(d):
    """
    安全的计算平方根，避免负数导致错误。
    """
    if d < 0:
        if d < -0.01:
            print(f"Warning: Attempting to compute sqrt({d})")
        return 0
    return math.sqrt(d)


def min_val(x, y, z):
    """
    返回 x, y, z 中的最小值。
    """
    return min(x, y, z)


def pow(x, y):
    """
    计算 x 的 y 次方。
    Args:
        x:
        y:

    Returns:

    """
    return math.pow(x, y)


def pow2(x):
    """
    计算 x 的 2 次方。
    Args:
        x:

    Returns:

    """
    return math.pow(x, 2)
