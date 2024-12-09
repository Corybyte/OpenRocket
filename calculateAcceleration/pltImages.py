import matplotlib.pyplot as plt
import math


def haversine(lat1, lon1, lat2, lon2):
    """
    计算两点之间的地表距离（单位：公里）
    """
    # 将纬度和经度从度转换为弧度
    lat1, lon1, lat2, lon2 = map(math.radians, [lat1, lon1, lat2, lon2])

    # 计算纬度和经度差
    dlat = lat2 - lat1
    dlon = lon2 - lon1

    # Haversine公式
    a = math.sin(dlat / 2) ** 2 + math.cos(lat1) * math.cos(lat2) * math.sin(dlon / 2) ** 2
    c = 2 * math.asin(math.sqrt(a))

    # 地球平均半径（单位：公里）
    r = 6371.0
    return c * r


def AccelerationPlt(coordinates):
    """
    绘制火箭飞行轨迹的二维图
    x轴：地表距离
    y轴：高度（海拔）
    """
    # 提取纬度、经度和海拔数据
    latitudes = [coord.x for coord in coordinates]
    longitudes = [coord.y for coord in coordinates]
    altitudes = [coord.z for coord in coordinates]

    # 起点的纬度和经度
    start_lat = latitudes[0]
    start_lon = longitudes[0]

    # 计算从起点到每个点的地表距离
    distances = [0]  # 第一个点到自己的距离是0
    for i in range(1, len(coordinates)):
        dist = haversine(start_lat, start_lon, latitudes[i], longitudes[i])
        distances.append(dist)

    # 创建二维图
    plt.figure(figsize=(10, 6))
    plt.plot(distances, altitudes, label="Rocket Trajectory", color="blue", marker='o')

    # 设置图形标签
    plt.xlabel("Surface Distance (km)")
    plt.ylabel("Altitude (m)")

    # 添加标题
    plt.title("Rocket Flight Trajectory - Distance vs Altitude")

    # 显示图例
    plt.legend()

    # 保存图片
    plt.savefig('./rocket_trajectory.png', dpi=500)

    # 显示图形
    plt.show()

