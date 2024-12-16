import math


def calculateMoment(instanceList, cnaList, cpList, aoa, refLength, nextDouble, finSetFlags, PitchDampingMoment,
                    YawDampingMoment, centerOfMass, tubeFinSetFlags, cRollDamps, cRollForces):
    print(cRollForces)
    PITCH_YAW_RANDOM = 0.0005
    totalCM = 0
    totalCN = 0
    totalCyaw = 0
    totalCside = 0
    totalCRoll = 0
    for i in range(len(instanceList)):
        # 当前组件的个数以及参数
        componentCM, componentCN, componentCRoll = calculateComponentNonAxialForces(instanceList[i], cnaList[i],
                                                                                    cpList[i], aoa,
                                                                                    refLength, finSetFlags[i],
                                                                                    cRollForces[i],
                                                                                    cRollDamps[i], tubeFinSetFlags[i])
        totalCM += componentCM
        totalCRoll += componentCRoll
    totalCM -= PitchDampingMoment
    totalCyaw -= YawDampingMoment
    # totalCRoll += totalCN
    totalCyaw = totalCyaw + (PITCH_YAW_RANDOM * 2 * (nextDouble - 0.5))
    totalCM = totalCM + (PITCH_YAW_RANDOM * 2 * (nextDouble - 0.5))

    cm = totalCM - totalCN * centerOfMass.x / refLength
    cyaw = totalCyaw - totalCside * centerOfMass.x / refLength
    print(totalCRoll)
    return cm, cyaw,totalCRoll


# 计算当前组件所有实例的cm
def calculateComponentNonAxialForces(contextNum, instancesCNas, instancesCPs, aoa, refLength, isFinset, cRollForces,
                                     CRollDamps, isTubeFinSet):
    instancesCM = 0
    instancesCN = 0
    instancesCroll = 0
    STALL_ANGLE = (20 * math.pi / 180)
    for i in range(contextNum):
        if isFinset[i]:
            CN_instanced = instancesCNas[i] * min(aoa, STALL_ANGLE)
        else:
            CN_instanced = instancesCNas[i] * aoa
        CRoll_instanced = cRollForces[i] - CRollDamps[i]

        CP_instanced = instancesCPs[i]
        instanceCM = CN_instanced * CP_instanced / refLength
        instancesCM += instanceCM
        instancesCN += CN_instanced
        instancesCroll += CRoll_instanced
    return instancesCM, instancesCN, instancesCroll
