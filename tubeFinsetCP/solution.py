def calculateCP(outerRadius, chord, ar,
                poly, mach, beta):
    if outerRadius < 0.001:
        return 0
    cp = calculateCPPos(mach, beta, ar, poly) * chord
    return cp


def calculateCPPos(mach, beta, ar, poly):
    ############### Begin ###############
    # 如果马赫数小于等于0.5，返回常量0.25，因为在低马赫数下CP位置的变化较小
    if mach <= 0.5:
        return 0.25
        # 如果马赫数大于等于2，使用特定公式计算CP位置，通常适用于超音速情况
    if mach >= 2:
        return (ar * beta - 0.67) / (2 * ar * beta - 1)

        # 初始化x为1.0，x将用于计算多项式的各项
    x = 1.0
    # 初始化val为0，用于累加多项式计算结果
    val = 0
    # 遍历多项式系数数组，计算多项式的值
    for i in range(len(poly)):
        # 将每一项多项式系数与x的当前幂相乘，累加到val中
        val += poly[i] * x
        # 将x乘以马赫数，用于计算下一个马赫数次幂
        x *= mach
        # 返回通过多项式计算得出的CP位置值
    return val
    ############### End ###############
