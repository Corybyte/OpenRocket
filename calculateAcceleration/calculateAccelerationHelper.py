from calculateAcceleration.solution import calculateAcceleration
from calculateAxialCD.solution import calculateAxialCD
from demo_dir.utils import RigidBody, Coordinate


def calculateAccelerationHelper(param):
    AtmosphericDensity = param['density']
    AtmosphericVelocity = param['velocity']
    CDaxial = param['CDaxial']
    refArea = param['refArea']
    refLength = param['refLength']
    CN = param['CN']

    rocketMass = param["rocketMass"]
    cm_data = rocketMass["cm"]
    Ixx = rocketMass["Ixx"]
    Iyy = rocketMass["Iyy"]
    Izz = rocketMass["Izz"]
    # 去掉 'length' 键
    cm_data.pop('length', None)
    cm = Coordinate(**cm_data)

    rocketMass = RigidBody(cm=cm, Ixx=Ixx, Iyy=Iyy, Izz=Izz)
    Cside = param['Cside']
    thrustForce = param['thrustForce']
    sin = param['sin']
    cos = param['cos']
    orientationQuaternion = param['orientationQuaternion']
    orientationQuaternion = Coordinate(orientationQuaternion['x'], orientationQuaternion['y'],
                                        orientationQuaternion['z'], orientationQuaternion['weight'])
    Cm = param['Cm']
    Cyaw = param['Cyaw']
    Croll = param['Croll']
    isLaunchRodCleared = param['isLaunchRodCleared']
    launchRodDirection = param['launchRodDirection']
    launchRodDirection = Coordinate(launchRodDirection['x'], launchRodDirection['y'],
                                        launchRodDirection['z'], launchRodDirection['weight'])
    gravity = param['gravity']
    coriolisAcceleration = param['coriolisAcceleration']

    coriolisAcceleration = Coordinate(coriolisAcceleration['x'], coriolisAcceleration['y'],
                                        coriolisAcceleration['z'], coriolisAcceleration['weight'])
    return calculateAcceleration(
        AtmosphericDensity,
        AtmosphericVelocity,
        CDaxial,
        refArea,
        refLength,
        CN,
        rocketMass,
        Cside,
        thrustForce,
        sin,
        cos,
        orientationQuaternion,
        Cm,
        Cyaw,
        Croll,
        isLaunchRodCleared,
        launchRodDirection,
        gravity,
        coriolisAcceleration
    )
