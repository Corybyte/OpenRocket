from calculateTubeFinSetHullCG import utils


def calculateAcceleration(AtmosphericDensity, AtmosphericVelocity, CDaxial, refArea, refLength, CN, rocketMass, Cside,
                          thrustForce, sin, cos,
                          orientationQuaternion, Cm, Cyaw, Croll, isLaunchRodCleared, launchRodDirection,
                          gravity, coriolisAcceleration):
    # Cm, Cyaw = calculafeForces(Cm, Cyaw) # 如果需要，可以通过此行计算力

    # 计算空气动力学系数产生的力
    ############### Begin #############
    # 计算动态压力
    dynP = 0.5 * AtmosphericDensity * pow(AtmosphericVelocity, 2)
    # 计算空气阻力
    dragForce = CDaxial * dynP * refArea
    # 计算法向力和侧向力
    fN = CN * dynP * refArea
    fSide = Cside * dynP * refArea
    # 计算沿Z轴的力
    forceZ = thrustForce - dragForce

    # 计算线性加速度
    linearAcceleration = utils.Coordinate(
        -fN / rocketMass.cm.weight, -fSide / rocketMass.cm.weight, forceZ / rocketMass.cm.weight, 0)

    # 沿Z轴旋转线性加速度
    linearAcceleration = rotateZ(linearAcceleration, sin, cos)
    # 使用四元数旋转线性加速度
    linearAcceleration = rotate(orientationQuaternion, linearAcceleration)

    # 转换为火箭世界坐标系
    linearAcceleration = linearAcceleration.sub(0, 0, gravity).add(coriolisAcceleration)

    # 如果发射杆未清除
    if not isLaunchRodCleared:
        dotProduct = linearAcceleration.dot(launchRodDirection)
        linearAcceleration.multiply(dotProduct)
        angularAcceleration = utils.Coordinate(0, 0, 0, 0)
    else:
        # 如果发射杆已清除，计算俯仰力矩
        Cm = Cm - CN * rocketMass.cm.x / refLength
        Cyaw = Cyaw - Cside * rocketMass.cm.x / refLength
        momX = - Cyaw * dynP * refArea * refLength
        momY = Cm * dynP * refArea * refLength
        momZ = Croll * dynP * refArea * refLength
        # 计算角加速度
        angularAcceleration = utils.Coordinate(momX / rocketMass.Iyy, momY / rocketMass.Iyy, momZ / rocketMass.Ixx, 0)
        # 沿Z轴旋转角加速度
        angularAcceleration = rotateZ(angularAcceleration, sin=sin, cos=cos)
        # 使用四元数旋转角加速度
        angularAcceleration = rotate(orientationQuaternion, angularAcceleration)

    # 返回线性加速度和角加速度
    return [linearAcceleration, angularAcceleration]
    ############### End #############


def calculateStructureMass():
    return 0  # 该函数目前返回0，可以根据需要修改为计算结构质量的公式


def rotateZ(c, sin, cos):
    # 沿Z轴旋转坐标
    return utils.Coordinate(cos * c.x - sin * c.y, cos * c.y + sin * c.x, c.z, c.weight)


def rotate(q, coord):
    # 使用四元数旋转坐标
    a = -q.y * coord.x - q.z * coord.y - q.weight * coord.z
    b = q.x * coord.x + q.z * coord.z - q.weight * coord.y
    c = q.x * coord.y - q.y * coord.z + q.weight * coord.x
    d = q.x * coord.z + q.y * coord.y - q.z * coord.x
    # 返回旋转后的坐标
    return utils.Coordinate(
        -a * q.y + b * q.x - c * q.weight + d * q.z,
        -a * q.z + b * q.weight + c * q.x - d * q.y,
        -a * q.weight - b * q.z + c * q.y + d * q.x,
        coord.weight
    )
