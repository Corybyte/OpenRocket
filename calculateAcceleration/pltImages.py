import matplotlib.pyplot as plt

def plot_rocket_from_json(json_data):
    # 解析 JSON 数据
    client_list = json_data.get('Client_List', [])

    # 提取 x, y, z 坐标
    x_coords = [client['x'] for client in client_list]
    y_coords = [client['y'] for client in client_list]
    z_coords = [client['z'] for client in client_list]

    # 创建 3D 图
    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')

    # 设置标题
    ax.set_title('3D Rocket Trajectory')

    # 绘制点
    ax.scatter(x_coords, y_coords, z_coords, c='r', marker='o', label='Rocket Points')

    # 绘制连接线形成轨迹
    ax.plot(x_coords, y_coords, z_coords, c='b', label='Rocket Path')

    # 设置轴标签
    ax.set_xlabel('X Axis')
    ax.set_ylabel('Y Axis')
    ax.set_zlabel('Z Axis')

    # 添加图例
    ax.legend()

    # 显示图形
    plt.savefig('/data/workspace/myshixun/calculateAcceleration/image/path.png')
