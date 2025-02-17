import math


def calculate_unit_rot_moi(w: float, h: float, finArea: float, bodyRadius: float, finCount: int) -> float:
    """
    计算单个尾翼的单位旋转惯性矩（Rotational Moment of Inertia）。

    Args:
        w (float): 尾翼的宽度（Chord Length）。
        h (float): 尾翼的高度（Span）。
        finArea (float): 单个尾翼的平面面积。
        bodyRadius (float): 火箭主体的半径。
        finCount (int): 尾翼的数量。

    Returns:
        float: 单个尾翼的单位旋转惯性矩。
    """

    ############### Begin #############
    # 计算等效高度 h2：如果 w 或 h 为 0，则用 finArea 近似高度
    if (w * h == 0):
        h2 = finArea
    else:
        h2 = h * finArea / w

    # 计算惯性矩的基本分量
    inertia = h2 / 12

    # 如果只有 1 个尾翼，则直接返回计算值
    if (finCount == 1):
        return inertia

    # 多个尾翼的情况，需要考虑到尾翼相对于火箭主体的旋转偏移量
    return inertia + math.pow(safeSqrt(h2) / 2 + bodyRadius, 2)
    ############### End ###############


def calculate_Long_moi(length, span, singlePlanformArea, finCount, bodyRadius):
    """
    计算尾翼的纵向惯性矩（Longitudinal Moment of Inertia）。

    Args:
        length (float): 尾翼的长度（Chord Length）。
        span (float): 尾翼的跨度（Span）。
        singlePlanformArea (float): 单个尾翼的平面面积。
        finCount (int): 尾翼的数量。
        bodyRadius (float): 火箭主体的半径。

    Returns:
        float: 尾翼的纵向惯性矩。
    """

    ############### Begin #############
    w = length  # 取尾翼长度
    h = span  # 取尾翼跨度

    # 计算等效宽度和高度，如果 length 或 span 为 0，则直接用 singlePlanformArea 近似
    if length * span == 0:
        w2 = singlePlanformArea
        h2 = singlePlanformArea
    else:
        w2 = w * singlePlanformArea / h
        h2 = h * singlePlanformArea / w

    # 计算基本的惯性矩
    inertia = (h2 + 2 * w2) / 24

    # 如果只有一个尾翼，则直接返回计算值
    if finCount == 1:
        return inertia

    # 多个尾翼时，考虑旋转惯性影响
    return inertia + pow(safeSqrt(h2) / 2 + bodyRadius, 2) / 2
    ############### End ###############


def safeSqrt(d):
    """
    计算安全的平方根，避免负数输入导致错误。

    Args:
        d (float): 输入值。

    Returns:
        float: 平方根结果，如果 d 过小（接近 0），则返回 0。
    """
    if d < 0:
        if d < 0.01:  # 处理小于 0.01 的负数情况，近似为 0
            return 0
    return math.sqrt(d)
