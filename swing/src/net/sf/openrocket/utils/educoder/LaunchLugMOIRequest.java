package net.sf.openrocket.utils.educoder;

public class LaunchLugMOIRequest extends Request {

    private Double outerRadius;
    private Double innerRadius;

    public Double getOuterRadius() {
        return outerRadius;
    }

    public void setOuterRadius(Double outerRadius) {
        this.outerRadius = outerRadius;
    }

    public Double getInnerRadius() {
        return innerRadius;
    }

    public void setInnerRadius(Double innerRadius) {
        this.innerRadius = innerRadius;
    }
    @Override
    public String toString() {
        return "LaunchLugMOIRequest{" +
                "outerRadius=" + outerRadius +
                ", innerRadius=" + innerRadius +
                ", answer=" + answer +
                '}';
    }
}
