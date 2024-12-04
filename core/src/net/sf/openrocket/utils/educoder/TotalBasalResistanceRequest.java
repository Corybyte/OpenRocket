


package net.sf.openrocket.utils.educoder;

import net.sf.openrocket.util.ArrayList;

import java.util.List;

public class TotalBasalResistanceRequest extends Request {
    private Double mach;
    private Double refArea;

    private List<Double> foreRadius;
    private List<Double> aftRadius;
    private List<Double> length;
    private List<Integer> instanceCount;
    private List<Boolean> nextComponents;
    private List<Double> nextRadius;
    private List<Boolean> isComponentActives;
    private Long timestamp;

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public static ArrayList<Double> client_cn = new ArrayList<Double>();
    public  static ArrayList<Double> server_cn = new ArrayList<Double>();

    public List<Double> getNextRadius() {
        return nextRadius;
    }

    @Override
    public String toString() {
        return "TotalBasalResistanceRequest{" +
                "mach=" + mach +
                ", refArea=" + refArea +
                ", foreRadius=" + foreRadius +
                ", aftRadius=" + aftRadius +
                ", length=" + length +
                ", instanceCount=" + instanceCount +
                ", nextComponents=" + nextComponents +
                ", nextRadius=" + nextRadius +
                ", isComponentActives=" + isComponentActives +
                ", answer=" + answer +
                '}';
    }

    public List<Boolean> getNextComponents() {
        return nextComponents;
    }

    public void setNextComponents(List<Boolean> nextComponents) {
        this.nextComponents = nextComponents;
    }

    public void setNextRadius(List<Double> nextRadius) {
        this.nextRadius = nextRadius;
    }

    public List<Boolean> getIsComponentActives() {
        return isComponentActives;
    }

    public void setIsComponentActives(List<Boolean> isComponentActives) {
        this.isComponentActives = isComponentActives;
    }

    public Double getMach() {
        return mach;
    }

    public void setMach(Double mach) {
        this.mach = mach;
    }

    public Double getRefArea() {
        return refArea;
    }

    public void setRefArea(Double refArea) {
        this.refArea = refArea;
    }


    public List<Double> getForeRadius() {
        return foreRadius;
    }

    public void setForeRadius(List<Double> foreRadius) {
        this.foreRadius = foreRadius;
    }

    public List<Double> getAftRadius() {
        return aftRadius;
    }

    public void setAftRadius(List<Double> aftRadius) {
        this.aftRadius = aftRadius;
    }

    public List<Double> getLength() {
        return length;
    }

    public void setLength(List<Double> length) {
        this.length = length;
    }

    public List<Integer> getInstanceCount() {
        return instanceCount;
    }

    public void setInstanceCount(List<Integer> instanceCount) {
        this.instanceCount = instanceCount;
    }
}
