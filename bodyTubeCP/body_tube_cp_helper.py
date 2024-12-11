import bodyTubeCP.solution

def calculateCP(param):
    ans = bodyTubeCP.solution.calculateCP(param['mach'], param['AOA'], param['length'], param['divisions'], param['outerRadius'], param['refArea'], param['sinAOA'], param['sincAOA'])
    return ans[0]
