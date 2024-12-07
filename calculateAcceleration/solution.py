from openrocket.demo_dir import utils


def calculateAcceleration(AtmosphericDensity, AtmosphericVelocity, CDaxial, refArea, refLength, CN, rocketMass, Cside,
                          thrustForce, sin, cos,
                          orientationQuaternion, Cm, Cyaw, Croll, isLaunchRodCleared, launchRodDirection,
                          gravity, coriolisAcceleration):
    # Cm, Cyaw = calculafeForces(Cm, Cyaw)
    # Calculate the forces from the aerodynamic coefficients

    dynP = 0.5 * AtmosphericDensity * pow(AtmosphericVelocity, 2)
    dragForce = CDaxial * dynP * refArea
    # cn和cside merge....
    fN = CN * dynP * refArea
    fSide = Cside * dynP * refArea
    forceZ = thrustForce - dragForce
    linearAcceleration = utils.Coordinate(
        -fN / rocketMass.cm.weight, -fSide / rocketMass.cm.weight, forceZ / rocketMass.cm.weight, 0)
    linearAcceleration = rotateZ(linearAcceleration, sin, cos)


    linearAcceleration = rotate(orientationQuaternion, linearAcceleration)

    # Convert into rocket world coordinates
    linearAcceleration = linearAcceleration.sub(0, 0, gravity).add(coriolisAcceleration)
    if not isLaunchRodCleared:
        dotProduct = linearAcceleration.dot(launchRodDirection)
        linearAcceleration.multiply(dotProduct)
        angularAcceleration = utils.Coordinate(0, 0, 0, 0)
    else:
        Cm = Cm - CN * rocketMass.cm.x / refLength
        Cyaw = Cyaw - Cside * rocketMass.cm.x / refLength

        momX = - Cyaw * dynP * refArea * refLength
        momY = Cm * dynP * refArea * refLength
        momZ = Croll * dynP * refArea * refLength

        angularAcceleration = utils.Coordinate(momX / rocketMass.Iyy, momY / rocketMass.Iyy, momZ / rocketMass.Ixx,
                                               0)
        angularAcceleration = rotateZ(angularAcceleration, sin=sin, cos=cos)
        angularAcceleration = rotate(orientationQuaternion, angularAcceleration)
    return [linearAcceleration, angularAcceleration]


def calculateStructureMass():
    return 0


def rotateZ(c, sin, cos):
    return utils.Coordinate(cos * c.x - sin * c.y, cos * c.y + sin * c.x, c.z, c.weight)


def rotate(q, coord):
    # q 群来的
    a = -q.y * coord.x - q.z * coord.y - q.weight * coord.z
    b = q.x * coord.x + q.z * coord.z - q.weight * coord.y
    c = q.x * coord.y - q.y * coord.z + q.weight * coord.x
    d = q.x * coord.z + q.y * coord.y - q.z * coord.x
    return utils.Coordinate(
        -a * q.y + b * q.x - c * q.weight + d * q.z,
        -a * q.z + b * q.weight + c * q.x - d * q.y,
        -a * q.weight - b * q.z + c * q.y + d * q.x,
        coord.weight
    )
