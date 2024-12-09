package net.sf.openrocket.utils.educoder;

import net.sf.openrocket.util.ArrayList;
import net.sf.openrocket.util.Coordinate;

public class StabilityRequest {
//    总体压心.x - 总体重心位置.x / 总体参考面积
    public double cp;
    public double position;
    public double area;
    public Long timestamp;
    public static ArrayList<Double> client_cn = new ArrayList<Double>();
    public  static ArrayList<Double> server_cn = new ArrayList<Double>();

    public StabilityRequest(double cp, double position, double area,long timestamp) {
        this.cp = cp;
        this.position = position;
        this.area = area;
        this.timestamp = timestamp;
    }
}
