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
    processed_a = truncate_to_5_decimal_places(a)
    processed_b = truncate_to_5_decimal_places(b)
    return Counter(processed_a) == Counter(processed_b)


def check_list2(client_list, server_list) -> bool:
    # 将 client_list 和 server_list 的每个字典的 'x', 'y', 'z' 转换成一个元组
    client_coords = [
        (truncate_to_5_decimal(client['x']),
         truncate_to_5_decimal(client['y']),
         truncate_to_5_decimal(client['z']))
        for client in client_list
    ]
    server_coords = [
        (truncate_to_5_decimal(server['x']),
         truncate_to_5_decimal(server['y']),
         truncate_to_5_decimal(server['z']))
        for server in server_list
    ]
    # 使用 Counter 进行对比
    return Counter(client_coords) == Counter(server_coords)

def truncate_to_5_decimal_places(numbers):
    """截取数字列表中的每个数字为5位小数"""
    return [round(num, 5) for num in numbers]
def truncate_to_5_decimal(num):
    """将数字保留到 5 位小数"""
    return round(num, 5)
