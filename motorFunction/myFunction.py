import math
import numpy as np
class ForceCalculate1:
    def __init__(self):
        # 损失系数sigma
        self.sigma = 0.2
        # 水火箭瓶身截面积(m2)
        self.S0 = (81 / 2 * 0.001) ** 2 * math.pi
        # 喷口截面积(m2)（后面乘0.2为粘性的近似修正）
        self.S1 = (18 / 2 * 0.001) ** 2 * math.pi * self.sigma
        # 水火箭瓶身容量(m3)
        self.V0 = 9.0972 * 10 ** (-3)
        # 气体初始压强(Pa)
        self.P1 = 2.8 * 10 ** 6
        # 初始液体体积
        self.Vl = 3.5 * 10 ** (-3)
        # 液体密度(kg/m3)
        self.rho = 1 * 10 ** 3
        # 装入液体质量(kg)
        self.ml = self.Vl * self.rho
        # 初始气体体积
        self.Vg = self.V0 - self.Vl
        # 大气压强(Pa)
        self.P0 = 10130
        # 重力加速度
        self.g = 9.81
        # 空气的常数Cp/Cv
        self.gamma = 1.4
        ################################
        # 该部分根据数据需要进行修改
        # 最大时间长度
        self.t_max = 9000
        # 时间步长
        self.dt = 1e-5
    def calculate_onestep(self, t, V, dV):
        # 取回数值
        P1 = self.P1
        Vg = self.Vg
        gamma = self.gamma
        S0 = self.S0
        S1 = self.S1
        rho = self.rho
        g = self.g
        V0 = self.V0
        P0 = self.P0
        ###########################################################
        # 四个系数
        C0 = P1 * (Vg ** gamma)
        C1 = (S0 ** 2 - S1 ** 2) * rho / (2 * S1 ** 2 * S0 ** 2)
        k = rho * g / S0
        C2 = rho * g * V0 / S0 - P0
        # 体积变化量，计算公式已化简到仅与V有关
        dV = ((C0 * V ** -gamma - k * V + C2) / C1) ** 0.5
        ddV = (-gamma * C0 * V ** (-gamma - 1) - k) / (2 * C1)
        ##########################################################
        return dV, ddV
    def RK4(self, t, Vt, dVt):
        # RK4积分单步
        dt = self.dt
        dV1, ddV0 = self.calculate_onestep(t, Vt, dVt)
        dV1, ddV1 = self.calculate_onestep(t, Vt, dV1)
        dV2, ddV0 = self.calculate_onestep(t + dt / 2, Vt + dV1 / 2 * dt, dVt + ddV1 / 2 * dt)
        dV2, ddV2 = self.calculate_onestep(t + dt / 2, Vt + dV1 / 2 * dt, dV2 + ddV1 / 2 * dt)
        dV3, ddV0 = self.calculate_onestep(t + dt / 2, Vt + dV2 / 2 * dt, dVt + ddV2 / 2 * dt)
        dV3, ddV3 = self.calculate_onestep(t + dt / 2, Vt + dV2 / 2 * dt, dV3 + ddV2 / 2 * dt)
        dV4, ddV0 = self.calculate_onestep(t + dt, Vt + dV3 * dt, dVt + ddV3 * dt)
        dV4, ddV4 = self.calculate_onestep(t + dt, Vt + dV3 * dt, dV4 + ddV3 * dt)
        dV_all = (dV1 + 2 * dV2 + 2 * dV3 + dV4) / 6
        ddV_all = (ddV1 + 2 * ddV2 + 2 * ddV3 + ddV4) / 6
        return dV_all, ddV_all
    def calculate(self):
        #
        # 定义待循环变量
        # V
        # 需要数据初始化
        # 迭代初始
        # 初始气体体积
        V = self.Vg
        # 初始时间
        t = 0
        # 步长
        dt = self.dt
        # 初始质量
        m = self.ml
        # 用于存储数据的list
        # 所有气体体积
        Vs = [V]
        # 时间list
        ts = [t]
        # 推力list
        Fs = []
        # 初始条件
        dV = 0
        while V < self.V0:
            # RK4积分
            Vt = V
            dVt = dV
            dV_all, ddV_all = self.RK4(t, Vt, dVt)
            # 数据迭代
            V = V + dV_all * dt
            dV = dV_all
            t = t + dt
            ############### Begin ###############
            # 推力数据，直接带公式
            v_up = 1 / self.S0 * dV
            v_out = v_up * self.S0 / self.S1
            dv_up = 1 / self.S0 * ddV_all
            dv_out = dv_up * self.S0 / self.S1
            dm = self.rho * dV
            F = dm * (v_out - v_up) + self.rho * (self.V0 - V) * dv_up
            ############### End #################
            # 数据存储
            Vs.append(V)
            ts.append(t)
            Fs.append(F)
        return ts, Vs, Fs
def sample_list(lst, target_length=10000):
    # 计算采样间隔
    interval = len(lst) // target_length
    # 每隔interval个元素取一个，直到取到target_length个
    sampled_list = lst[::interval]
    # 如果取样后列表长度超过了目标长度，截取前target_length个
    if len(sampled_list) > target_length:
        sampled_list = sampled_list[:target_length]
    return sampled_list
def functions():
    M = ForceCalculate1()
    ts, Vs, Fs = M.calculate()
    Fs = np.array(Fs)
    ts = np.array(ts)
    ############### Begin ###############
    # 质量、直径、长度、设计者姓名、发动机名称
    weight = 0
    diameter = 0
    length = 0
    designName = 'myName'
    commonName = 'myMotor'
    ############### End #################
    print(len(sample_list(ts[1:].tolist())))
    return sample_list(ts[1:].tolist()), sample_list(Fs.tolist()), [weight], [diameter], [length], [designName], [commonName]