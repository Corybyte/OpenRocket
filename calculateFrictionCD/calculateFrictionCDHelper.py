from calculateFrictionCD.solution import calculateFrictionCD
import re

def calculateFriectionCDHelper(param):
    mach = param['mach']
    velocity = param['velocity']
    lengthAerodynamic = param['lengthAerodynamic']
    kinematicViscosity = param['kinematicViscosity']
    isPerfectFinish = param['isPerfectFinish']

    aeroDynamic = param['aeroDynamic']
    refArea = param['refArea']
    finish_ordinal = param['finish_ordinal']
    roughnessSize = param['roughnessSize']
    componentName = [process_class_name(name) for name in param['componentName']]
    otherComponentFrictionCD = param['otherComponentFrictionCD']
    componentInstanceCount = param['componentInstanceCount']
    wetArea = param['wetArea']
    thickness = param['thickness']
    macLength = param['macLength']
    finArea = param['finArea']
    axialOffset = param['axialOffset']
    length = param['length']
    foreRadius = param['foreRadius']
    aftRadius = param['aftRadius']

    return calculateFrictionCD(mach, velocity, lengthAerodynamic, kinematicViscosity, isPerfectFinish, aeroDynamic,refArea,
                               componentName, finish_ordinal, roughnessSize, wetArea, thickness, macLength, finArea,
                               otherComponentFrictionCD, componentInstanceCount, axialOffset, length, foreRadius,
                               aftRadius)

# 提取类名并处理
def process_class_name(class_name):
    # 提取类名中的最后一部分，去掉 "Calc"
    match = re.search(r'\.([A-Za-z]+)Calc$', class_name)
    if match:
        name = match.group(1)
        # 将首字母大写并返回
        return name.capitalize()
    return None
