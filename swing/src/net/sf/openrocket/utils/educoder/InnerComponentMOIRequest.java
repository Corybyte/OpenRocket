package net.sf.openrocket.utils.educoder;

public class InnerComponentMOIRequest extends Request{
    Double outerRadius; //getOuterRadius
    Double innerRadius; //getInnerRadius

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
        return "InnerComponentMOIRequest{" +
                "outerRadius=" + outerRadius +
                ", innerRadius=" + innerRadius +
                ", answer=" + answer +
                '}';
    }
}
