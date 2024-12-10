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


def check_list2(client_list, server_list) -> bool:
    # 将 client_list 和 server_list 的每个字典的 'x', 'y', 'z' 转换成一个元组
    client_coords = [(client['x'], client['y'], client['z']) for client in client_list]
    server_coords = [(server['x'], server['y'], server['z']) for server in server_list]
    # 使用 Counter 进行对比
    return Counter(client_coords) == Counter(server_coords)


list1 = [{'x': -2.0083083141575817, 'y': 6.86050706500479e-07, 'z': 96.51035887188577, 'weight': 0.0, 'length': -1.0},
         {'x': -2.51651651, 'y': 6.86050706500479e-07, 'z': 96.51035887188577, 'weight': 0.0, 'length': -1.0}]
list2 = [{'x': -2.51651651, 'y': 6.86050706500479e-07, 'z': 96.51035887188577, 'weight': 0.0, 'length': -1.0},
         {'x': -2.0083083141575817, 'y': 6.86050706500479e-07, 'z': 96.51035887188577, 'weight': 0.0, 'length': -1.0}]

check_list2(list1,list2)