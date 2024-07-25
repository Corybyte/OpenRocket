package net.sf.openrocket.utils.educoder;

public class BodyTubeMOIRequest extends Request{


    private Double thickness;

    private Double outerRadius;

    private Boolean filled;

    public Boolean getFilled() {
        return filled;
    }

    public void setFilled(Boolean filled) {
        this.filled = filled;
    }

    public Double getOuterRadius() {
        return outerRadius;
    }

    public void setOuterRadius(Double outerRadius) {
        this.outerRadius = outerRadius;
    }

    public Double getThickness() {
        return thickness;
    }

    public void setThickness(Double thickness) {
        this.thickness = thickness;
    }

    @Override
    public String toString() {
        return "BodyTubeMOIRequest{" +
                "thickness=" + thickness +
                ", outerRadius=" + outerRadius +
                ", filled=" + filled +
                ", answer=" + answer +
                '}';
    }
}
