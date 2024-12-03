import numpy as np
from matplotlib import pyplot as plt
import step56.solution


def parse_cp(cp_dict):
    return (cp_dict['x'], cp_dict['y'], cp_dict['z'], cp_dict['weight'])


def split_components(cp_list):
    """
    交替将相同名称的组件分配到两组。
    """
    group_1 = []
    group_2 = []
    flag = True

    for item in cp_list:
        if flag:
            group_1.append(item)
            flag = False
        else:
            group_2.append(item)
            flag = True
    return group_1, group_2


def calculateCP(param):
    components_group1, components_group2 = split_components(param['list'])
    cp = process_cp_list(components_group1)
    process_cp_list2(components_group2)
    return cp


def process_cp_list(cp_list):
    # 获取 cp_list 中的最大长度，用于确定循环次数
    max_len = max(len(item['cp']) for item in cp_list)
    # 循环 n 次，每次取出对应的 cp[i] 和 calc
    result_cp = []
    for i in range(max_len):
        component_list = []  # 存储每次传递的参数列表
        for item in cp_list:
            name = item['componentName']
            cp_value = item['cp'][i]  # 获取当前的 cp 值

            if cp_value is None:
                cp_value = {'x': 0.0, 'y': 0.0, 'z': 0.0, 'weight': 0.0, 'length': -1.0}
            calc = item['rocketComponentCalc']
            # 构建 WholeCpDTO 实例
            param = {
                'componentName': name,
                'cp': cp_value,
                'rocket_component_calc': calc
            }
            component_list.append(
                WholeCpDTO(param['componentName'], parse_cp(param['cp']), param['rocket_component_calc']))

        result = step56.solution.calculateCP(component_list)
        result_cp.append(result[0])
    mach = np.arange(0, 1, 0.01)
    plt.plot(mach, result_cp)
    plt.title('Mach vs CPX')  # 设置标题
    plt.xlabel('Mach')  # 设置 x 轴标签
    plt.ylabel('CPX')  # 设置 y 轴标签
    plt.savefig('/data/workspace/myshixun/step56/mach.png', dpi=300, bbox_inches='tight')
    plt.close()

    return result_cp[30]


def process_cp_list2(cp_list):
    # 获取 cp_list 中的最大长度，用于确定循环次数
    max_len = max(len(item['cp']) for item in cp_list)
    # 循环 n 次，每次取出对应的 cp[i] 和 calc
    result_cp = []
    for i in range(max_len):
        component_list = []  # 存储每次传递的参数列表
        for item in cp_list:
            name = item['componentName']
            cp_value = item['cp'][i]  # 获取当前的 cp 值
            if cp_value is None:
                cp_value = {'x': 0.0, 'y': 0.0, 'z': 0.0, 'weight': 0.0, 'length': -1.0}
            calc = item['rocketComponentCalc']

            # 构建 WholeCpDTO 实例
            param = {
                'componentName': name,
                'cp': cp_value,
                'rocket_component_calc': calc
            }
            component_list.append(
                WholeCpDTO(param['componentName'], parse_cp(param['cp']), param['rocket_component_calc']))
        result = step56.solution.calculateCP(component_list)
        result_cp.append(result[0])
    AOA = np.arange(-90, 90, 1)
    plt.plot(AOA, result_cp)
    plt.title('AOA vs CPX')  # 设置标题
    plt.xlabel('AOA')  # 设置 x 轴标签
    plt.ylabel('CPX')  # 设置 y 轴标签
    plt.savefig('/data/workspace/myshixun/step56/aoa.png', dpi=300, bbox_inches='tight')
    plt.close()


class WholeCpDTO:
    def __init__(self, component_name, cp, rocket_component_calc, w=None):
        self.component_name = component_name
        self.cp = cp
        self.rocket_component_calc = rocket_component_calc

    def __repr__(self):
        return f"WholeCpDTO(component_name='{self.component_name}', cp={self.cp}, rocket_component_calc={self.rocket_component_calc})"


class WholeCpRequest:
    def __init__(self, components):
        self.components = components

    def __repr__(self):
        return f"WholeCpRequest(components={self.components})"
