from utils.common_helper import average

def calculateCG(wettedCentroid, tabCentroid, filletCentroid,
                thickness, crossSectionRelativeVolume,
                materialDensity, filletMaterialDensity, finCount):
    """
    计算组件（如火箭尾翼）的质心（Center of Gravity, CG）。

    Args:
        wettedCentroid (tuple): 受湿区域的质心坐标 (x, y, z, 相对体积)。
        tabCentroid (tuple): 尾翼根部（tab）的质心坐标 (x, y, z, 相对体积)。
        filletCentroid (tuple): 过渡段（fillet）的质心坐标 (x, y, z, 相对体积)。
        thickness (float): 组件厚度。
        crossSectionRelativeVolume (float): 组件横截面相对体积比例。
        materialDensity (float): 主要材料密度。
        filletMaterialDensity (float): 过渡段材料密度。
        finCount (int): 组件的个数（通常是尾翼的数量）。

    Returns:
        float: 组件的整体质心在 x 轴上的位置。
    """

    ############### Begin ###############
    # 计算受湿部分的体积，并基于材料密度计算其质量
    wettedVolume = wettedCentroid[3] * thickness * crossSectionRelativeVolume
    finBulkMass = wettedVolume * materialDensity
    wettedCM = (wettedCentroid[0], wettedCentroid[1], wettedCentroid[2], finBulkMass)

    # 计算尾翼根部（tab）部分的体积和质量
    tabVolume = tabCentroid[3] * thickness
    tabMass = tabVolume * materialDensity
    tabCM = (tabCentroid[0], tabCentroid[1], tabCentroid[2], tabMass)

    # 计算过渡段（fillet）的体积和质量
    filletVolume = filletCentroid[3]
    filletMass = filletVolume * filletMaterialDensity
    filletCM = (filletCentroid[0], filletCentroid[1], filletCentroid[2], filletMass)

    # 计算单个尾翼的总质量
    eachFinMass = finBulkMass + tabMass + filletMass

    # 计算单个尾翼的质心
    eachFinCenterOfMass = average(average(wettedCM, tabCM), filletCM)
    eachFinCenterOfMass = (eachFinCenterOfMass[0], eachFinCenterOfMass[1], eachFinCenterOfMass[2], eachFinMass)

    # 计算整个组件（如尾翼组）的质心
    # y 轴坐标归零：如果是单个尾翼，则围绕父组件旋转；如果是多个尾翼，则对称性使得 y 轴分量平均为 0
    centerOfMass = (eachFinCenterOfMass[0], 0, eachFinCenterOfMass[2], eachFinMass * finCount)

    # 返回组件整体的 x 轴质心位置
    return centerOfMass[0]
    ############### End ###############
