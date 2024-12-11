
import innerComponentCG.solution


def calculateCG(param):
    cg = InnerComponentCG.solution.calculateCG(param['instanceCount'], param['innerRadius'],
                                               param['outerRadius'], param['length'], param['density'], param['instanceOffsets'])
    return cg[0]
