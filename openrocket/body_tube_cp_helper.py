import step6.solution

def calculateCP(param):
    ans = step6.solution.calculateCP(param['mach'], param['AOA'], param['planformCenter'], 
                                     param['planformArea'], param['refArea'], param['sinAOA'],
                                     param['sincAOA'])
    return ans[0]