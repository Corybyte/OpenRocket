import math
from demo_dir import utils


def calculate(param):
    isTube = False
    cnaCache = 0
    cpCache = 0
    (
        client_CnaCache, client_ForeRadius, client_AftRadius, client_Mach, client_AOA, client_RefArea,
        client_FullVolume,
        client_SincAOA, client_Length, client_PlanformCenter, client_BODY_LIFT_K, client_PlanformArea, client_SinAOA
    ) = utils.get_required_params(
        param)

    if math.isnan(client_CnaCache) or client_CnaCache == 0.0:
        # 获取前后半径
        r0 = client_ForeRadius
        r1 = client_AftRadius
        # 如果前半径和后半径相等，则认为是圆柱形状
        if r0 == r1:
            isTube = True  # 标记为圆柱体
            cnaCache = 0  # 对于圆柱体，cnaCache 设为 0
        else:
            # 如果前后半径不相等，则是一个非圆柱形状
            isTube = False  # 标记为非圆柱体

            # 计算前后截面积 A0 和 A1
            A0 = math.pi * r0 ** 2  # 圆的面积公式：A = π * r^2
            A1 = math.pi * r1 ** 2  # 使用 r0 和 r1 计算前后面积

            # 计算 cnaCache，这里是一个非圆柱体的形状
            cnaCache = 2 * (A1 - A0)  # 根据公式计算 cnaCache
            # 计算 cpCache，这个是与体积和面积相关的缓存值
            cpCache = (client_Length * A1 - client_FullVolume) / (A1 - A0)
    else:
        print(f"client_CnaCache not NaN")

    if isTube != 0:
        cp = utils.getLiftCP(
            Mach=client_Mach,
            AOA=client_AOA,
            planformCenter=client_PlanformCenter,
            BODY_LIFT_K=client_BODY_LIFT_K,
            planformArea=client_PlanformArea,
            RefArea=client_RefArea,
            SinAOA=client_SinAOA,
            SincAOA=client_SincAOA,
        )
    else:
        cp = utils.Coordinate(
            cpCache, 0, 0, cnaCache * client_SincAOA / client_RefArea
        ).average(
            utils.getLiftCP(
                Mach=client_Mach,
                AOA=client_AOA,
                planformCenter=client_PlanformCenter,
                BODY_LIFT_K=client_BODY_LIFT_K,
                planformArea=client_PlanformArea,
                RefArea=client_RefArea,
                SinAOA=client_SinAOA,
                SincAOA=client_SincAOA,
            )
        )
    print(cp.to_dict())
    ret_cn = cp.to_dict()['weight'] * client_AOA
    ret_json = {
        'weight': cp.to_dict()['weight'],
        'aoa': client_AOA,
        'ret_cn': ret_cn
    }
    return ret_cn
