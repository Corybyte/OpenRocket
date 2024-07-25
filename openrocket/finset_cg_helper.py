import step7.solution

def calculateCG(param):
    wettedCentroid = (param['singlePlanformX'], param['singlePlanformY'], param['singlePlanformZ'], param['singlePlanformArea'])
    tabCentroid = (param['tabX'], param['tabY'], param['tabZ'], param['tabArea'])
    filletCentroid = (param['filletX'], param['filletY'], param['filletZ'], param['filletArea'])
    return step7.solution.calculateCG(wettedCentroid, tabCentroid, filletCentroid, param['thickness'], param['crossSectionRelativeVolume'],
                                      param['materialDensity'], param['filletMaterialDensity'], param['finCount'])