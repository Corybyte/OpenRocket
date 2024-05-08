package net.sf.openrocket.utils.educoder;

public class FinSetCpRequest {
    /**
     * 平均气动翼弦前缘位置 MAC leading edge position
     */
    private Double macLead;
    /**
     * 平均气动弦翼展位置 MAC spanwise position
     */
    private Double macLength;
    /**
     * 当前马赫速度
     */
    private Double mach;
    /**
     * 翅片长宽比
     */
    private Double ar;

    public Double getMacLead() {
        return macLead;
    }

    public void setMacLead(Double macLead) {
        this.macLead = macLead;
    }

    public Double getMacLength() {
        return macLength;
    }

    public void setMacLength(Double macLength) {
        this.macLength = macLength;
    }

    public Double getMach() {
        return mach;
    }

    public void setMach(Double mach) {
        this.mach = mach;
    }

    public Double getAr() {
        return ar;
    }

    public void setAr(Double ar) {
        this.ar = ar;
    }

    @Override
    public String toString() {
        return "FinSetCpRequest{" +
                "macLead=" + macLead +
                ", macLength=" + macLength +
                ", mach=" + mach +
                ", ar=" + ar +
                '}';
    }
}
