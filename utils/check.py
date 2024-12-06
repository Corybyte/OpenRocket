#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time    : 2024/12/3 16:10
# @Author  : Switch616
# @File    : check.py
# @Description:
from collections import Counter


def check_json(a, b) -> bool:
    """

    Args:
        a: Json a
        b: Json b

    Returns: True or False

    """

    return a == b




def check_list(a, b) -> bool:
    """
    Check if two lists contain the same elements, regardless of order.

    Args:
        a: List of elements
        b: List of elements

    Returns:
        bool: True if both lists have the same elements, False otherwise.
    """
    return Counter(a) == Counter(b)
