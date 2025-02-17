import math
from utils.common_helper import average
rocket = None
def calculateCG(rootComponent) -> float:
    parentTransform = Transformation(translate=None, rotation=None)
    cg_structure = calculateStructure(rootComponent, parentTransform)
    init_cg = [0, 0, 0, 0]
    all_cg = merge(init_cg, cg_structure)
    cg_motor = calculateMotors(rootComponent, parentTransform)
    all_cg = merge(all_cg, cg_motor)
    return all_cg[0]
#计算当前组件的结构重心
def calculateStructure(rootComponent, parentTransform):
    instanceCount = rootComponent.instanceCount
    allInstanceOffsets = rootComponent.allInstanceOffsets
    allInstanceAngles = rootComponent.allInstanceAngles
    # 递归计算当前组件的子组件的cg
    children_cg = [0, 0, 0, 0]
    for i in range(instanceCount):
        currentInstanceOffset = allInstanceOffsets[i]
        currentInstanceOffset_list = [currentInstanceOffset.get('x'), currentInstanceOffset.get('y'),
                                      currentInstanceOffset.get('z'), currentInstanceOffset.get('weight')]
        offsetTransform = Transformation(translate=currentInstanceOffset_list)
        currentInstanceAngle = allInstanceAngles[i]
        angleTransform = Transformation.rotate_x(currentInstanceAngle)
        currentTransform = parentTransform.applyTransformation(offsetTransform).applyTransformation(angleTransform)
        for child in rootComponent.children:
            eachChild = child
            eachCg = calculateStructure(eachChild, currentTransform)
            children_cg = merge(children_cg, eachCg)
    ############### Begin ###############
    # 获取组件的重心
    compCM = [rootComponent.cg.get('x'), rootComponent.cg.get('y'), rootComponent.cg.get('z'),
              rootComponent.cg.get('weight')]
    # 获取组件的所在位置
    compPosition = [rootComponent.position.get('x'), rootComponent.position.get('y'), rootComponent.position.get('z')]
    #组件重心与组件位置相加
    compCM[0] = compCM[0] + compPosition[0]
    compCM[1] = compCM[1] + compPosition[1]
    compCM[2] = compCM[2] + compPosition[2]
    #使用父类组件 parentTransform对结果进行transform变换
    compCM = parentTransform.transform(compCM)
    #与子组件的重心合并
    cg = merge(compCM, children_cg)
    ############### End ###############
    return cg
def calculateMountData(compCM, motor, parentTransform):
    clusterLocalCM = [motor.motorXPosition + motor.mountXPosition + motor.cMx, 0, 0,
                      motor.eachMass * motor.instanceCount]
    results = parentTransform.transform(clusterLocalCM)
    return results
#计算当前组件的发动机重心
def calculateMotors(rootComponent, parentTransform):
    compCM = [0, 0, 0, 0]
    instanceCount = rootComponent.instanceCount
    instanceLocations = rootComponent.instanceLocations
    if rootComponent.isMotorMount:
        motor = rootComponent
        cg_motor = calculateMountData(compCM, motor, parentTransform)
        compCM = merge(compCM, cg_motor)
    children_cg = [0, 0, 0, 0]
    for i in range(instanceCount):
        currentLocation = instanceLocations[i]
        currentLocation_list = [currentLocation.get('x'), currentLocation.get('y'),
                                currentLocation.get('z'), currentLocation.get('weight')]
        currentTransform = parentTransform.applyTransformation(Transformation(translate=currentLocation_list))
        for child in rootComponent.children:
            eachChild = child
            eachCg = calculateMotors(eachChild, currentTransform)
            children_cg = merge(children_cg, eachCg)
    ############### Begin ###############
    #判断子组件的重心是否有效（>0.00000001）
    #有效则将当前组件的重心与子组件的重心合并
    if (children_cg[0] > 0.00000001):
        cg_motor = merge(compCM, children_cg)
        compCM = cg_motor
    ############### End ###############
    return compCM
def merge(a, b):
    if (a[3] < 0.00000001):
        a = b
    else:
        a = average(a, b)
    return a
def transform(orig):
    matrix = [[0, 0, 0], [0, 0, 0], [0, 0, 0]]
    matrix[0][0] = 1
    matrix[1][2] = 1
    matrix[1][2] = 1
    x = matrix[0][0] * orig[0] + matrix[0][1] * orig[0] + matrix[2][2] * orig[0]
    y = matrix[1][0] * orig[0] + matrix[1][1] * orig[0] + matrix[2][2] * orig[0]
    z = matrix[2][0] * orig[0] + matrix[2][1] * orig[0] + matrix[2][2] * orig[0]
    return [x, y, z, orig[3]]
class Transformation:
    def __init__(self, translate=None, rotation=None):
        if translate is None:
            translate = [0, 0, 0, 0]
        if rotation is None:
            rotation_matrix = [
                [1, 0, 0],
                [0, 1, 0],
                [0, 0, 1]
            ]
            rotation = rotation_matrix
        self.translate = translate
        self.rotation = rotation
    def rotate_x(theta):
        if 0.000000001 > abs(theta):
            return Transformation()
        rotation_matrix = [
            [1, 0, 0],
            [0, math.cos(theta), -math.sin(theta)],
            [0, math.sin(theta), math.cos(theta)]
        ]
        return Transformation(rotation=rotation_matrix)
    def applyTransformation(self, other):
        portion = self.linerTransform(other.translate)
        translate = self.translate
        rotation = self.rotation
        translation = [portion[0] + translate[0], portion[1] + translate[1],
                       portion[2] + translate[2], portion[3] + translate[3]]
        combined = Transformation(translate=translation)
        for i in range(3):
            x = rotation[i][0]
            y = rotation[i][1]
            z = rotation[i][2]
            combined.rotation[i][0] = x * other.rotation[0][0] + y * other.rotation[1][0] + z * other.rotation[2][0]
            combined.rotation[i][1] = x * other.rotation[0][1] + y * other.rotation[1][1] + z * other.rotation[2][1]
            combined.rotation[i][2] = x * other.rotation[0][2] + y * other.rotation[1][2] + z * other.rotation[2][2]
        return combined
    def linerTransform(self, orig):
        rotation = self.rotation
        x = rotation[0][0] * orig[0] + rotation[0][1] * orig[1] + rotation[0][2] * orig[2]
        y = rotation[1][0] * orig[0] + rotation[1][1] * orig[1] + rotation[1][2] * orig[2]
        z = rotation[2][0] * orig[0] + rotation[2][1] * orig[1] + rotation[2][2] * orig[2]
        return x, y, z, orig[3]
    def transform(self, orig):
        rotation = self.rotation
        translate = self.translate
        x = rotation[0][0] * orig[0] + rotation[0][1] * orig[1] + rotation[0][2] * orig[2] + translate[0]
        y = rotation[1][0] * orig[0] + rotation[1][1] * orig[1] + rotation[1][2] * orig[2] + translate[1]
        z = rotation[2][0] * orig[0] + rotation[2][1] * orig[1] + rotation[2][2] * orig[2] + translate[2]
        return x, y, z, orig[3]