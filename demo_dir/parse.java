protected Coordinate getLiftCP(FlightConditions conditions, WarningSet warnings) {

    // 计算升力中心（Center of Pressure, CP）时的修正因子
    /*
     * 在火箭的飞行过程中，只有在转向顶点（即飞行达到 apogee 时）才会遇到不稳定现象。
     * 这时，火箭会横向振荡。为避免这种情况，需要使用额外的修正因子。该修正因子
     * 仅在迎角 (AOA) 大于 45 度且火箭的速度小于 15 米/秒时才会生效。
     *
     * TODO: MEDIUM: 这个修正因子会导致飞行结果在 apogee 时 CP 跳变（异常现象）。
     */
    double mul = 1;  // 默认为 1，即不进行修正
    if ((conditions.getMach() < 0.05) && (conditions.getAOA() > Math.PI / 4)) {
        // 如果马赫数小于 0.05 且迎角大于 45 度，计算修正因子
        mul = pow2(conditions.getMach() / 0.05);  // pow2() 是一个用于计算平方的函数
    }

    // 创建并返回升力中心坐标对象
    // 返回的坐标对象表示升力中心的坐标值，坐标 x 为 planformCenter（翼型中心）
    // y 和 z 坐标分别为 0，weight（升力）在 z 轴方向。
    return new Coordinate(
        planformCenter,  // x 坐标（翼型中心位置）
        0,              // y 坐标（不影响此场景）
        0,              // z 坐标（不影响此场景）
        mul * BODY_LIFT_K * planformArea / conditions.getRefArea() *  // 修正因子与常量乘积
        conditions.getSinAOA() * conditions.getSincAOA()            // 计算 sin(迎角) 和 sin(迎角) 的平方的乘积
    );
    // 这里的公式可以解释为：计算升力中心的相对升力（即 CP）的值。
    // 具体来说，它是通过以下方式得出的：
    // 1. 使用修正因子（mul）来调整升力值。
    // 2. 乘以常量 BODY_LIFT_K 和翼型面积（planformArea）。
    // 3. 与参考面积（conditions.getRefArea()）进行标准化。
    // 4. 乘以迎角的正弦值（sin(aoa)）和 sin(aoa) 的平方。
}


@Override
public void calculateNonaxialForces(FlightConditions conditions, Transformation transform,
			AerodynamicForces forces, WarningSet warnings) {
    /**
     * 计算非轴向力
     * 在这个方法中，我们根据飞行条件、转换信息、气动力信息以及警告集来计算和设置相应的气动力。
     */

    // 创建一个新的HullCGRequest对象
    HullCGRequest hullCGRequest = new HullCGRequest();

    // 将计算所需的参数赋值到hullCGRequest对象中
    hullCGRequest.client_CnaCache = 0;  // 设置 CnaCache 值，默认是 0
    hullCGRequest.client_ForeRadius = foreRadius;  // 设置前半径 (foreRadius)
    hullCGRequest.client_AftRadius = aftRadius;  // 设置后半径 (aftRadius)
    hullCGRequest.client_FullVolume = fullVolume;  // 设置总容积 (fullVolume)
    hullCGRequest.client_Mach = conditions.getMach();  // 设置马赫数 (Mach)，来自飞行条件
    hullCGRequest.client_AOA = conditions.getAOA();  // 设置迎角 (AOA)，来自飞行条件
    hullCGRequest.client_RefArea = conditions.getRefArea();  // 设置参考面积 (RefArea)，来自飞行条件

    // 使用 eduCoderService 调用 demo 接口发送请求，处理计算
    OpenRocket.eduCoderService.demo(hullCGRequest).enqueue(new Callback<Result>() {
        @Override
        public void onResponse(Call<Result> call, Response<Result> response) {
            // 请求成功时，输出返回的结果
            System.out.println(response.body());
        }

        @Override
        public void onFailure(Call<Result> call, Throwable throwable) {
            // 请求失败时，输出错误信息
            System.out.println(throwable.getMessage());
        }
    });

    // 检查 cnaCache 是否为 NaN，如果是，则进行进一步计算
    if (Double.isNaN(cnaCache)) {
        // 获取前后半径
        final double r0 = foreRadius;
        final double r1 = aftRadius;

        // 如果前半径和后半径相等，则认为是圆柱形状
        if (MathUtil.equals(r0, r1)) {
            isTube = true;  // 标记为圆柱体
            cnaCache = 0;  // 对于圆柱体，cnaCache 设为 0
        } else {
            // 如果前后半径不相等，则是一个非圆柱形状
            isTube = false;  // 标记为非圆柱体

            // 计算前后截面积 A0 和 A1
            final double A0 = Math.PI * pow2(r0);  // 圆的面积公式：A = π * r^2
            final double A1 = Math.PI * pow2(r1);  // 使用 r0 和 r1 计算前后面积

            // 计算 cnaCache，这里是一个非圆柱体的形状
            cnaCache = 2 * (A1 - A0);  // 根据公式计算 cnaCache
            // 计算 cpCache，这个是与体积和面积相关的缓存值
            cpCache = (length * A1 - fullVolume) / (A1 - A0);
        }
    }

    Coordinate cp;  // 声明一个坐标对象 cp，用于存储升力中心

    // 如果前后半径相等（即圆柱体），则只考虑机身升力
    if (isTube) {
        cp = getLiftCP(conditions, warnings);  // 计算升力中心
    } else {
        // 如果前后半径不相等，计算加权平均的升力中心
        cp = new Coordinate(cpCache, 0, 0, cnaCache * conditions.getSincAOA() /
                conditions.getRefArea()).average(getLiftCP(conditions, warnings));
    }

    // 将计算得到的升力中心（cp）设置到 forces 对象中
    forces.setCP(cp);  // 设置升力中心
    forces.setCNa(cp.weight);  // 设置 cna (气动力系数)，与升力中心的权重相关
    forces.setCN(forces.getCNa() * conditions.getAOA());  // 计算总的气动力系数 CN，等于 Cna 乘以迎角

    // 如果 CNa 和 AOA 的乘积不为 0，输出 CNa * AOA 的值
    if (forces.getCNa() * conditions.getAOA() != 0 ) {
        System.out.println(forces.getCNa() * conditions.getAOA());  // 打印 CNa 和 AOA 的乘积
    }

    // 计算力矩（m），这个是基于气动力系数 CN 和升力中心的 x 坐标以及参考长度来计算的
    forces.setCm(forces.getCN() * cp.x / conditions.getRefLength());

    // 以下是与滚转力矩、侧向力矩等无关的力的初始化
    forces.setCroll(0);  // 设置滚转力矩为 0
    forces.setCrollDamp(0);  // 设置滚转阻尼为 0
    forces.setCrollForce(0);  // 设置滚转力为 0
    forces.setCside(0);  // 设置侧向力为 0
    forces.setCyaw(0);  // 设置偏航力矩为 0

    // 如果马赫数大于 1.1（超音速飞行），添加一个超音速警告
    if (conditions.getMach() > 1.1) {
        warnings.add(Warning.SUPERSONIC);  // 添加超音速警告
    }
}
