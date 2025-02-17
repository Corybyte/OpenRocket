import math
import noseConeCG.solution
from noseConeCG.solution import calculateCG
import utils.common_helper as common_helper
from utils.common_helper import *
def calculateNoseConeCG(param):
  fullVolume = 0
  volume = 0
  cgx = 0
  if param['length'] <= 0:
      return
  # 对组件长度进行分段处理，计算重心,湿面积，平面面积，旋转惯量
  for n in range(param['divisions']):
      # x1 和 x2 是分割区间的边界。在代码中，这些变量用于确定每个分割部分的长度。
      # hyp 是从 r1 到 r2 的斜边长度，通常是通过两点之间的欧几里得距离计算得到的。
      # height 是组件在未填充的情况下沿着 y 轴的高度。具体来说，它代表了未填充部分的高度。
      # r1o 和 r2o 是外部半径。在代码中，这些变量用于表示每个分割部分的外部半径。
      # r1i 和 r2i 是内部半径。在代码中，这些变量用于表示每个分割部分的内部半径。
      # 分割区间的左边界
      x1 = n * param['length'] / param['divisions']
      # 分割区间的右边界
      x2 = (n + 1) * param['length'] / param['divisions']
      # 计算每个分割区间的长度
      l = x2 - x1
      # 分割区间的外部边界半径
      r1o = common_helper.get_radius(x1, param['transitionType'], param['length'], param['transitionForeRadius'],
                       param['transitionAftRadius'], param['transitionShapeParameter'])
      # 分割区间的内部边界半径
      r2o = common_helper.get_radius(x2, param['transitionType'], param['length'], param['transitionForeRadius'],
                       param['transitionAftRadius'], param['transitionShapeParameter'])
      # 斜边的长度
      hyp = math.hypot(r2o - r1o, l)
      # 环的高度
      height = param['thickness'] * hyp / l
      # 判断当前分割区间是否被填充
      r1i = 0
      r2i = 0
      if param['filled']:
          r1i = 0
          r2i = 0
      else:
          # 计算每个分割区间中环的内部半径
          r1i = max(r1o - height, 0)
          r2i = max(r2o - height, 0)
  ############### Begin ###############
      # 计算外部半径的梯形台体的重心
      fullCG = calculateCG(l, r1o, r2o)
      # 计算内部半径的梯形台体的重心
      innerCG = calculateCG(l, r1i, r2i)
      # 存储梯形台体的总体积，它的值等于外部半径梯形台体的重心的权重
      dFullV = fullCG[1]
      # 存储梯形台体内部的体积，它的值等于外部半径梯形台体的体积减去内部半径梯形台体的体积
      dV = fullCG[1] - innerCG[1]
      # 存储梯形台体的重心在 x 轴上的位置，它的值通过计算外部半径梯形台体重心在 x 轴上的位置与内部半径梯形台体重心在 x 轴上的位置的加权平均值得到。
      dCG = (fullCG[0] * fullCG[1] - innerCG[0] * innerCG[1]) / dV
      # 存储第一矩的值。
      # 它的计算方式是将体积差值 dV 乘以外部半径梯形台体的重心在 x 轴上的位置与内部半径梯形台体的重心在 x 轴上的位置的加权平均值，即 (x1 + dCG)。
      dCGx = dV * (x1 + dCG)
      # 计算得到的各个部分贡献的体积相关组件
      volume += dV
      fullVolume += dFullV
      cgx += dCGx
  ############### End #################
  # Correct for deferred constant factors
  fullVolume *= math.pi / 3.0
  if volume < 0.0000000001:  # 0.1 mm^3
      volume = 0
      cg = (param['length'] / 2, 0, 0, 0)
  else:
      # the mass of this shape is the material density * volume.
      # it cannot come from super.getComponentMass() since that
      # includes the shoulders
      cg = (cgx / volume, 0, 0, param['density'] * volume)
  # a component so small it has no volume can't contribute to moment of inertia
  if equals(volume, 0):
      return cg
  # calculate transition:
  ######################
  foreShoulderLength = param['foreShoulderLength']
  foreShoulderRadius = param['foreShoulderRadius']
  MINFEATURE = 0.001
  aftShoulderRadius = param['aftShoulderRadius']
  foreShoulderThickness = param['foreShoulderThickness']
  aftShoulderLength = param['aftShoulderLength']
  aftShoulderThickness = param['aftShoulderThickness']
  isForeShoulderCapped = param['foreShoulderCapped']
  isAftShoulderCapped = param['aftShoulderCapped']
  if (foreShoulderLength > MINFEATURE) or (aftShoulderRadius > MINFEATURE):
      transVolume = volume
      transCG = cg
      foreCapVolume = 0.0
      foreCapCG = [0, 0, 0, 0]
      if (isForeShoulderCapped):
          ir = max(foreShoulderRadius - foreShoulderThickness, 0)
          foreCapCG = common_helper.ringCG(ir, 0, -foreShoulderLength, foreShoulderThickness - foreShoulderLength, param['density'])
          foreCapVolume = common_helper.ringVolume(ir, 0, foreShoulderThickness)
      foreShoulderVolume = 0.0
      foreShoulderCG = [0, 0, 0, 0]
      if foreShoulderLength > MINFEATURE:
          orr = foreShoulderRadius
          ir = max(foreShoulderRadius - foreShoulderThickness, 0)
          foreShoulderCG = common_helper.ringCG(foreShoulderRadius, ir, -foreShoulderLength, 0, param['density'])
          foreShoulderVolume = common_helper.ringVolume(orr, ir, foreShoulderLength)
      aftShoulderVolume = 0.0
      aftShoulderCG = [0, 0, 0, 0]
      if aftShoulderLength > MINFEATURE:
          orr = aftShoulderRadius
          ir = max(aftShoulderRadius - aftShoulderThickness, 0)
          aftShoulderCG = common_helper.ringCG(aftShoulderRadius, ir, param['length'], param['length'] + aftShoulderLength,
                                 param['density'])
          aftShoulderVolume = common_helper.ringVolume(orr, ir, aftShoulderLength)
      aftCapVolume = 0.0
      aftCapCG = [0, 0, 0, 0]
      if isAftShoulderCapped:
          ir = max(aftShoulderRadius - aftShoulderThickness, 0)
          aftCapCG = common_helper.ringCG(ir, 0, param['length'] + aftShoulderLength - aftShoulderThickness,
                            param['length'] + aftShoulderLength, param['density'])
          aftCapVolume = common_helper.ringVolume(ir, 0, aftShoulderThickness)
      cgx = foreCapCG[0] * foreCapCG[3] + foreShoulderCG[0] * foreShoulderCG[3] + transCG[0] * transCG[3] + \
            aftShoulderCG[0] * aftShoulderCG[3] + aftCapCG[0] * aftCapCG[3]
      mass = foreCapCG[3] + foreShoulderCG[3] + transCG[3] + aftShoulderCG[3] + aftCapCG[3]
      cg = [cgx / mass, 0, 0, mass]
  return cg[0]