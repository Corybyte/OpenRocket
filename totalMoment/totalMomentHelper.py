from totalMoment.solution import calculateMoment
from calculateTubeFinSetHullCG.utils import RigidBody, Coordinate


def calculateTotalMomentHelper(param):
    componentInstance = param['componentInstance']
    cnaLists = param['cnaLists']
    cpLists = param['cpLists']
    aoa = param['aoa']
    refLength = param['refLength']
    randomDoubles = param['randomDoubles']
    finSetFlags = param['flags']
    PitchDampingMoment = param['PitchDampingMoment']
    YawDampingMoment = param['YawDampingMoment']
    tubeFinSetFlags = param['tubeFinsetList']
    cRollDamps = param['cRollDamps']
    cRollForces = param['cRollForces']
    cm = param['cm']
    centerOfMass = Coordinate(cm['x'], cm['y'], cm['z'], 0)
    return calculateMoment(componentInstance, cnaLists, cpLists, aoa, refLength, randomDoubles, finSetFlags,
                           PitchDampingMoment, YawDampingMoment, centerOfMass, tubeFinSetFlags, cRollDamps, cRollForces)
