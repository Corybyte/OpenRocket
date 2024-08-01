import math
from common_helper import average
import sys

from step55.solution import Transformation, merge
from Component import Component


def calculateMOI(rootComponent: Component) -> float:
    parentTransform = Transformation(translate=None, rotation=None)
    inertia_list = []
    inertia_list2 = []
    centerofMass = calculateStructure(rootComponent, parentTransform, inertia_list)
    rigidBody = calculateMomentInertia(inertia_list, centerofMass)

    centerofMass2 = calculateMotors(rootComponent, parentTransform, inertia_list2)
    list = inertia_list + inertia_list2
    all_centerofMass = average(centerofMass, centerofMass2)

    rigidBody = calculateMomentInertia(list, all_centerofMass)

    return rigidBody[1]


def calculateStructure(rootComponent, parentTransform, inertia_list):
    instanceCount = rootComponent.instanceCount

    allInstanceOffsets = rootComponent.allInstanceOffsets

    allInstanceAngles = rootComponent.allInstanceAngles

    # 计算每个子组件的cg
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

            eachCg = calculateStructure(eachChild, currentTransform, inertia_list)
            children_cg = merge(children_cg, eachCg)

    # 计算每个组件的cg

    compCM = [rootComponent.cg.get('x'), rootComponent.cg.get('y'), rootComponent.cg.get('z'),
              rootComponent.cg.get('weight')]
    compPosition = [rootComponent.position.get('x'), rootComponent.position.get('y'), rootComponent.position.get('z')]

    compCM[0] = compCM[0] + compPosition[0]
    compCM[1] = compCM[1] + compPosition[1]
    compCM[2] = compCM[2] + compPosition[2]
    compCM = parentTransform.transform(compCM)

    compIx = rootComponent.rotationalUnitInertia * compCM[3]
    compIt = rootComponent.longitudinalUnitInertia * compCM[3]
    componentInertia = [compCM, compIx, compIt, compIt]

    inertia_list.append(componentInertia)
    cg = merge(compCM, children_cg)

    return cg


def calculateMountData(compCM, motor, parentTransform, inertia_list2):
    clusterIt = motor.config_longInertia * motor.instanceCount * motor.eachMass

    clusterBaseIr = motor.config_inertia * motor.instanceCount * motor.eachMass
    clusterLocalCM = [motor.motorXPosition + motor.mountXPosition + motor.cMx, 0, 0,
                      motor.eachMass * motor.instanceCount]
    results = parentTransform.transform(clusterLocalCM)

    clusterIr = clusterBaseIr
    if motor.instanceCount > 1:
        for i in range(len(motor.allInstanceOffsets)):
            coord = motor.allInstanceOffsets[i]
            distance = math.hypot(coord[2], coord[3])
            clusterIr += motor.eachMass * math.pow(distance, 2)

    clusterMOI = [results, clusterIr, clusterIt, clusterIt]
    inertia_list2.append(clusterMOI)
    return results


def calculateMotors(rootComponent, parentTransform, inertia_list2):
    compCM = [0, 0, 0, 0]
    instanceCount = rootComponent.instanceCount
    instanceLocations = rootComponent.instanceLocations

    if rootComponent.isMotorMount:
        motor = rootComponent
        cg_motor = calculateMountData(compCM, motor, parentTransform, inertia_list2)
        compCM = merge(compCM, cg_motor)

    children_cg = [0, 0, 0, 0]

    for i in range(instanceCount):
        currentLocation = instanceLocations[i]
        currentLocation_list = [currentLocation.get('x'), currentLocation.get('y'),
                                currentLocation.get('z'), currentLocation.get('weight')]

        currentTransform = parentTransform.applyTransformation(Transformation(translate=currentLocation_list))

        for child in rootComponent.children:
            eachChild = child
            eachCg = calculateMotors(eachChild, currentTransform, inertia_list2)
            children_cg = merge(children_cg, eachCg)

    if (children_cg[0] > 0.00000001):
        cg_motor = merge(compCM, children_cg)
        compCM = cg_motor
    return compCM


def calculateMomentInertia(inertia_list, centerOfMass):
    centerOfMass = centerOfMass
    Ir = 0
    It = 0
    for eachLocal in inertia_list:
        eachLocal = rebase(eachLocal, centerOfMass)
        Ir = Ir + eachLocal[1]
        It = It + eachLocal[2]
    return [centerOfMass, Ir, It, It]


def rebase(location, newLocation):
    # sub
    cm = location[0]  # cg
    delta = [cm[0] - newLocation[0], cm[1] - newLocation[1], cm[2] - newLocation[2], cm[3] - newLocation[3]]
    delta[3] = 0
    x2 = delta[0] * delta[0]
    y2 = delta[1] * delta[1]
    z2 = delta[2] * delta[2]

    newIxx = location[1] + cm[3] * (y2 + z2)
    newIyy = location[2] + cm[3] * (x2 + z2)
    newIzz = location[3] + cm[3] * (x2 + y2)

    return [newLocation, newIxx, newIyy, newIzz]