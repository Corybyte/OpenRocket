import step5.solution

def calculateCP(param):
    ans = step5.solution.calculateCP(param['mach'], param['AOA'], param['length'], param['divisions'],param['outerRadius'], param['refArea'], param['sinAOA'],param['sincAOA'])
    return ans[0]
