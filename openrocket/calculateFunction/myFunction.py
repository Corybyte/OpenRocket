import math
def check_continuous(low, high):
    for i in range(len(low) - 1):
        if high[i] != low[i + 1]:
            return False
    return True
def functions() -> tuple[list[float], list[float]]:
    # 定义字符串形式的函数列表
    # 定义字符串形式的函数列表
    functions = ["x", "-x", "math.sin(x)"]

    # low 和 high 列表分别代表 x 的取值范围
    low = [0, 10, 20]  # 每个函数对应的 x 的最小值
    high = [10, 20, 30]  # 每个函数对应的 x 的最大值

    if low[0] is not 0:
        print("起始时间不为0")
    if check_continuous(low, high):
        print("区间不连续")

    # 计算值, 假设我们在每个区间中取 5 个点进行计算
    num_points = 5
    x_list = []  # 用于存储所有的 x 值
    y_list = []  # 用于存储对应的 y 值

    for i, f in enumerate(functions):
        # 获取当前函数的 x 的取值区间
        x_values = [low[i] + j * (high[i] - low[i]) / (num_points - 1) for j in range(num_points)]
        # 计算当前函数在区间内的 y 值
        y_values = [eval(f) for x in x_values]

        # 将 x 和 y 的值分别添加到各自的列表中
        x_list.extend(x_values)
        y_list.extend(y_values)
    print(x_list)
    print(y_list)
    return x_list, y_list
functions()

