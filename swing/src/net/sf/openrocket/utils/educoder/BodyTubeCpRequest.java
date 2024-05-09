package net.sf.openrocket.utils.educoder;

public class BodyTubeCpRequest {
    /**
     * 机翼中心
     */
    private Double planformCenter;
    /**
     * 机翼面积
     */
    private Double planformArea;
    /**
     * 迎角的sinc值
     */
    private Double sincAOA;
    /**
     * 迎角的sin值
     */
    private Double sinAOA;
    /**
     * 计算中使用的参考面积
     */
    private Double refArea;
    /**
     * 当前马赫速度
     */
    private Double mach;
    /**
     * 迎角
     */
    private Double AOA;

    public Double getPlanformCenter() {
        return planformCenter;
    }

    public void setPlanformCenter(Double planformCenter) {
        this.planformCenter = planformCenter;
    }

    public Double getPlanformArea() {
        return planformArea;
    }

    public void setPlanformArea(Double planformArea) {
        this.planformArea = planformArea;
    }

    public Double getSincAOA() {
        return sincAOA;
    }

    public void setSincAOA(Double sincAOA) {
        this.sincAOA = sincAOA;
    }

    public Double getSinAOA() {
        return sinAOA;
    }

    public void setSinAOA(Double sinAOA) {
        this.sinAOA = sinAOA;
    }

    public Double getRefArea() {
        return refArea;
    }

    public void setRefArea(Double refArea) {
        this.refArea = refArea;
    }

    public Double getMach() {
        return mach;
    }

    public void setMach(Double mach) {
        this.mach = mach;
    }

    public Double getAOA() {
        return AOA;
    }

    public void setAOA(Double AOA) {
        this.AOA = AOA;
    }

    @Override
    public String toString() {
        return "BodyTubeCpRequest{" +
                "planformCenter=" + planformCenter +
                ", planformArea=" + planformArea +
                ", sincAOA=" + sincAOA +
                ", sinAOA=" + sinAOA +
                ", refArea=" + refArea +
                ", mach=" + mach +
                ", AOA=" + AOA +
                '}';
    }
}
