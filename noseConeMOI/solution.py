import math

def calculateLongMOICone(h, r):
    ############### Begin #############
    # 计算圆锥的质量 m（这里假设密度为单位值）
    m = pow(r, 2) * h

    # 计算圆锥的沿其高度方向的转动惯量 Ixx
    Ixx = 3 * m * (pow(r, 2) / 20 + pow(h, 2) / 5)
    return Ixx
    ############### End ###############


def calculate_unit_rot_moi(r1, r2, m):
    """
    计算圆台或圆柱的单位转动惯量。

    Args:
        r1 (float): 圆台的一端半径。
        r2 (float): 圆台的另一端半径。
        m (float): 物体的质量。

    Returns:
        float: 单位转动惯量。
    """
    ############### Begin #############
    EPSILON = 0.00000001

    # 判断是否为特殊情况：圆柱（两端半径相等）
    if abs(r1 - r2) < EPSILON:
        return 10.0 * pow(r1, 2) / 6.0

    # 计算一般情况的单位转动惯量
    return (math.pow(r2, 5) - math.pow(r1, 5)) / (math.pow(r2, 3) - math.pow(r1, 3)) * 3 * m / 10
    ############### End ###############


def calculate_Long_moi(l, r1, r2, cg):
    """
    计算圆台或圆柱沿其长度方向的转动惯量。

    Args:
        l (float): 物体的长度。
        r1 (float): 一端的半径。
        r2 (float): 另一端的半径。
        cg (tuple): 质心信息 (cg_x, cg_mass)，其中 cg_x 为质心位置，cg_mass 为质量。

    Returns:
        float: 计算得到的转动惯量。
    """
    EPSILON = 0.00000001

    ############### Begin ############
    # 如果是圆柱（两端半径相等），直接使用圆柱的转动惯量公式
    if abs(r1 - r2) < EPSILON:
        moi = cg[1] * (3 * pow(r1, 2) + pow(l, 2)) / 12.0
        return moi

    # 计算质心相对于较小端的位置
    shiftCG = cg[0]

    # 确保 r1 始终是较小的半径
    if r1 > r2:
        tmp = r1
        r1 = r2
        r2 = tmp
        shiftCG = l - cg[0]  # 更新质心位置

    # 计算圆锥的高度 h1 和 h2
    h2 = l * r2 / (r2 - r1)
    h1 = h2 * r1 / r2

    # 计算较小圆锥（底半径 r1，高度 h1）的转动惯量
    moi1 = calculateLongMOICone(h1, r1)

    # 计算较大圆锥（底半径 r2，高度 h2）的转动惯量
    moi2 = calculateLongMOICone(h2, r2)

    # 计算圆台的转动惯量（大圆锥 - 小圆锥）
    moi = moi2 - moi1

    # 考虑质心偏移对转动惯量的影响
    moi = moi - pow((h1 + shiftCG), 2) * cg[1]

    return moi
    ############### End ###############
