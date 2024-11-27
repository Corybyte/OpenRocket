import math
import step1.solution
from common_helper import safe_sqrt, equals

def CONICAL_radius(x, radius, length, param):
    return radius * x / length

def ELLIPSOID_radius(x, radius, length, param):
    x = x * radius / length
    return safe_sqrt(2 * radius * x - x * x)

def HAACK_radius(x, radius, length, param):
    theta = math.acos(1 - 2 * x / length)
    if equals(param, 0):
        return radius * safe_sqrt((theta - math.sin(2 * theta) / 2) / math.pi)
    return radius * safe_sqrt((theta - math.sin(2 * theta) / 2 + param * math.pow(math.sin(theta), 3)) / math.pi)

def OGIVE_radius(x, radius, length, param):
    if length < radius:
        x = x * radius / length
        length = radius
    if param < 0.001:
        return CONICAL_radius(x, radius, length, param)
    R = safe_sqrt((math.pow(length, 2) + math.pow(radius, 2)) * (math.pow((2 - param) * length, 2) + math.pow(param * radius, 2)) / (4 * math.pow(param * radius, 2)))
    L = length / param
    y0 = safe_sqrt(R * R - L * L)
    return safe_sqrt(R * R - (L - x) * (L - x)) - y0

def PARABOLIC_radius(x, radius, length, param):
    return radius * ((2 * x / length - param * math.pow(x / length, 2)) / (2 - param))

def POWER_radius(x, radius, length, param):
    if param <= 0.00001:
        if x <= 0.00001:
            return 0
        else:
            return radius
    return radius * math.pow(x / length, param)

def get_radius(x, type, length, foreRadius, aftRadius, shapeParameter):
    if x < 0:
        return foreRadius
    if x >= length:
        return aftRadius
    
    r1 = foreRadius
    r2 = aftRadius
    
    if r1 == r2:
        return r1
    
    if r1 > r2:
        x = length - x
        tmp = r1
        r1 = r2
        r2 = tmp
    
    if type == "CONICAL":
        return r1 + CONICAL_radius(x, r2 - r1, length, shapeParameter)
    elif type == "ELLIPSOID":
        return r1 + ELLIPSOID_radius(x, r2 - r1, length, shapeParameter)
    elif type == "HAACK":
        return r1 + HAACK_radius(x, r2 - r1, length, shapeParameter)
    elif type == "OGIVE":
        return r1 + OGIVE_radius(x, r2 - r1, length, shapeParameter)
    elif type == "PARABOLIC":
        return r1 + PARABOLIC_radius(x, r2 - r1, length, shapeParameter)
    else: # POWER
        return r1 + POWER_radius(x, r2 - r1, length, shapeParameter)

def calculateUnitRotMOI(r1, r2):
    # check for cylinder special case
    if abs(r1 - r2) < 0.00000001:
        return 10.0 * math.pow(r1, 2) / 6.0
    return (math.pow(r2, 5) - math.pow(r1, 5)) / (math.pow(r2, 3) - math.pow(r1, 3))

def calculateLongMOICone(h, r):
    m = math.pow(r, 2) * h
    Ixx = 3 * m * (math.pow(r, 2) / 20.0 + math.pow(h, 2) / 5.0)
    return Ixx

def calculateLongMOI(l, r1, r2, cg):
    # check for cylinder special case
    moi = 0
    if abs(r1 - r2) < 0.00000001:
        # compute MOI of cylinder relative to CG of cylinder
        moi = cg[1] * (3 * math.pow(r1, 2) + math.pow(l, 2)) / 12.0
        return moi
    
    # is the frustum "small end forward" or "small end aft"?
    shiftCG = cg[0]
    if r1 > r2:
        tmp = r1
        r1 = r2
        r2 = tmp
        shiftCG = l - cg[0]
        
    # Find the heights of the two cones. Note that the h1 and h2 being calculated here
    # are NOT the x1 and x2 used in calculateProperties()
    h2 = l * r2 / (r2 - r1)
    h1 = h2 * r1 / r2
    
    moi1 = calculateLongMOICone(h1, r1)
    moi2 = calculateLongMOICone(h2, r2)
    
    # compute MOI relative to tip of cones (they share the same tip, of course)
    moi = moi2 - moi1
    
    # use parallel axis theorem to move MOI to be relative to CG  of frustum.
    moi = moi - math.pow(h1 + shiftCG, 2) * cg[1]
    
    return moi

def calculateCG(param):
    wetArea = 0
    planArea = 0
    planCenter = 0
    fullVolume = 0
    volume = 0
    longitudinalUnitInertia = 0
    rotationalUnitInertia = 0
    
    cgx = 0
    
    if param['length'] <= 0:
        return
    
    # Integrate for volume, CG, wetted area, planform area, and moments of inertia
    for n in range(param['divisions']):
        # x1 and x2 are the bounds on this division
        # hyp is the length of the hypotenuse from r1 to r2
        # height is the y-axis height of the component if not filled
        # r1o and r2o are the outer radii
        # r1i and r2i are the inner radii
        x1 = n * param['length'] / param['divisions']
        x2 = (n + 1) * param['length'] / param['divisions']
        
        l = x2 - x1
        
        r1o = get_radius(x1, param['transitionType'], param['length'], param['transitionForeRadius'], param['transitionAftRadius'], param['transitionShapeParameter'])
        r2o = get_radius(x2, param['transitionType'], param['length'], param['transitionForeRadius'], param['transitionAftRadius'], param['transitionShapeParameter'])
        
        hyp = math.hypot(r2o - r1o, l)
        
        height = param['thickness'] * hyp / l
        
        # get inner radii.
        r1i = 0
        r2i = 0
        
        if param['filled']:
            r1i = 0
            r2i = 0
        else:
            # Tiny inaccuracy is introduced on a division where one end is closed and other is open.
            r1i = max(r1o - height, 0)
            r2i = max(r2o - height, 0)

        fullCG = step1.solution.calculateCG(l, r1o, r2o)
        innerCG = step1.solution.calculateCG(l, r1i, r2i)

        dFullV = fullCG[1]
        dV = fullCG[1] - innerCG[1]

        dCG = (fullCG[0] * fullCG[1] - innerCG[0] * innerCG[1]) / dV
        dCGx = dV * (x1 + dCG)
        
        Ixxo = calculateUnitRotMOI(r1o, r2o)
        Ixxi = calculateUnitRotMOI(r1i, r2i)

        Ixx = Ixxo * fullCG[1] - Ixxi * innerCG[1]
        
        # longitudinal moment of inertia -- axis through CG of division
        Iyy = calculateLongMOI(l, r1o, r2o, fullCG) - calculateLongMOI(l, r1i, r2i, innerCG)
        
        # move to axis through forward end of component
        Iyy += dV * math.pow(x1 + dCG, 2)
        
        # Add to the volume-related components
        volume += dV
        fullVolume += dFullV
        cgx += dCGx
        rotationalUnitInertia += Ixx
        longitudinalUnitInertia += Iyy
        
        wetArea += (r1o + r2o) * math.sqrt(math.pow(r1o - r2o, 2) + math.pow(l, 2))
        
        dA = l * (r1o + r2o)
        planArea += dA
        planMoment = dA * x1 + 2.0 * math.pow(l, 2) * (r1o / 6.0 + r2o / 3.0)
        planCenter += planMoment
        
    if planArea > 0:
        planCenter /= planArea
        
    # get unit moments of inertia
    rotationalUnitInertia /= volume
    longitudinalUnitInertia /= volume
    
    # Correct for deferred constant factors
    volume *= math.pi / 3.0
    fullVolume *= math.pi / 3.0
    cgx *= math.pi / 3.0
    wetArea *= math.pi
    rotationalUnitInertia *= 3.0 / 10.0
    
    if volume < 0.0000000001: # 0.1 mm^3
        volume = 0
        cg = (param['length'] / 2, 0, 0, 0)
    else:
        # the mass of this shape is the material density * volume.
        # it cannot come from super.getComponentMass() since that 
        # includes the shoulders
        cg = (cgx / volume, 0, 0, param['density'] * volume)
        
    # a component so small it has no volume can't contribute to moment of inertia
    if equals(volume, 0):
        rotationalUnitInertia = 0
        longitudinalUnitInertia = 0
        return cg
    
    # Shift longitudinal inertia to CG
    longitudinalUnitInertia = longitudinalUnitInertia - math.pow(cg[0], 2)
    return cg[0]