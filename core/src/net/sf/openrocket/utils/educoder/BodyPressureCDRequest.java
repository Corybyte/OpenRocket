package net.sf.openrocket.utils.educoder;

import net.sf.openrocket.util.ArrayList;

public class BodyPressureCDRequest extends Request {
    private Double forceRadius;
    private Double aftRadius;
    private Double length;

    ///////////////////////////////变量
    private Double frontalArea;
    private Double refArea;
    private Double interpolatorValue;

    private double fineness;
    private Double mach;

    public void setMach(Double mach) {
        this.mach = mach;
    }

    public static ArrayList<Double> client_cn = new ArrayList<Double>();
    public  static ArrayList<Double> server_cn = new ArrayList<Double>();

    public void setInterpolatorValue(Double interpolatorValue) {
        this.interpolatorValue = interpolatorValue;
    }

    public void setFineness(double fineness) {
        this.fineness = fineness;
    }

    public Double getForceRadius() {
        return forceRadius;
    }

    public void setForceRadius(Double forceRadius) {
        this.forceRadius = forceRadius;
    }

    public Double getAftRadius() {
        return aftRadius;
    }

    public void setAftRadius(Double aftRadius) {
        this.aftRadius = aftRadius;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }


    public void setFrontalArea(Double frontalArea) {
        this.frontalArea = frontalArea;
    }

    public Double getRefArea() {
        return refArea;
    }

    public void setRefArea(Double refArea) {
        this.refArea = refArea;
    }


}
