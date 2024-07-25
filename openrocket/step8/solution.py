from common_helper import safe_sqrt

def calculateCP(macLead: float, macLength: float, mach: float,
                ar: float, poly: list) -> float:
    m = mach
    if m <= 0.5:
		# At subsonic speeds CP at quarter chord
        return macLead + 0.25 * macLength
    if m >= 2:
        beta = safe_sqrt(1 - mach * mach)
		# At supersonic speeds use empirical formula
        return macLead + ((ar * beta - 0.67) / (2 * ar * beta - 1)) * macLength
		
	# In between use interpolation polynomial
    x = 1.0
    val = 0
	
    for i in range(len(poly)):
        val += poly[i] * x
        x *= m

    print(f"macLead = {macLead}")
    print(f"calculateCPPos = {val}")
    print(f"macLength = {macLength}")

    return macLead + val * macLength