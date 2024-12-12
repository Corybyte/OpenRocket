import math


def calculateMoment(instanceList, cnaList, cpList, aoa, refLength, nextDouble, finSetFlags, PitchDampingMoment,
                    YawDampingMoment, centerOfMass, tubeFinSetFlags):
    PITCH_YAW_RANDOM = 0.0005
    totalCM = 0
    totalCN = 0
    totalCyaw = 0
    totalCside = 0
    for i in range(len(instanceList)):
        # 当前组件的个数以及参数
        componentCM, componentCN = calculateComponentNonAxialForces(instanceList[i], cnaList[i], cpList[i], aoa,
                                                                    refLength,finSetFlags[i])
        totalCM += componentCM
        totalCN += componentCN
    totalCM -= PitchDampingMoment
    totalCyaw -= YawDampingMoment

    totalCyaw = totalCyaw + (PITCH_YAW_RANDOM * 2 * (nextDouble - 0.5))
    totalCM = totalCM + (PITCH_YAW_RANDOM * 2 * (nextDouble - 0.5))

    cm = totalCM - totalCN * centerOfMass.x / refLength
    cyaw = totalCyaw - totalCside * centerOfMass.x / refLength

    return cm, cyaw


# 计算当前组件所有实例的cm
def calculateComponentNonAxialForces(contextNum, instancesCNas, instancesCPs, aoa, refLength, isFinset):
    instancesCM = 0
    instancesCN = 0
    STALL_ANGLE = (20 * math.pi / 180)
    for i in range(contextNum):
        if isFinset[i]:
            CN_instanced = instancesCNas[i] * min(aoa, STALL_ANGLE)
        else:
            CN_instanced = instancesCNas[i] * aoa
        CP_instanced = instancesCPs[i]
        instanceCM = CN_instanced * CP_instanced / refLength
        instancesCM += instanceCM
        instancesCN += CN_instanced
    return instancesCM, instancesCN
