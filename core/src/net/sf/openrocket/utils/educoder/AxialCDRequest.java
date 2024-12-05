package net.sf.openrocket.utils.educoder;

import net.sf.openrocket.util.ArrayList;

import java.util.List;

public class AxialCDRequest {
    private Double aoa;
    private Double cd;
    private double[] axialDragPoly1;
    private  double[] axialDragPoly2;

    private long timestamp;

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Double getAoa() {
        return aoa;
    }

    public void setAoa(Double aoa) {
        this.aoa = aoa;
    }

    public void setAxialDragPoly1(double[] axialDragPoly1) {
        this.axialDragPoly1 = axialDragPoly1;
    }

    public void setAxialDragPoly2(double[] axialDragPoly2) {
        this.axialDragPoly2 = axialDragPoly2;
    }

    public void setCd(Double cd) {
        this.cd = cd;
    }

    public static ArrayList<Double> client_cn = new ArrayList<Double>();
    public  static ArrayList<Double> server_cn = new ArrayList<Double>();
}
