import math

def calculateCP(mach: float, AOA: float, planformCenter: float, planformArea: float, 
                refArea: float, sinAOA: float, sincAOA: float) -> tuple[float, float, float, float]:
    # Without this extra multiplier the rocket may become unstable at apogee
	# when turning around, and begin oscillating horizontally.  During the flight
	# of the rocket this has no effect.  It is effective only when AOA > 45 deg
	# and the velocity is less than 15 m/s.
		
    # TODO: MEDIUM:  This causes an anomaly to the flight results with the CP jumping at apogee
    mul = 1
    if mach < 0.05 and AOA > math.pi / 4:
        mul = math.pow(mach / 0.05, 2)
    return (planformCenter, 0, 0, mul * 1.1 * planformArea / refArea * sinAOA * sincAOA)