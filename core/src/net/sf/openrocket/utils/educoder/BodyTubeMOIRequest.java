package net.sf.openrocket.utils.educoder;

public class BodyTubeMOIRequest {


    private Double thickness;

    private Double outerRadius;

    private Boolean filled;

    private Double innerRadius;

    private Double length;

    private Double[] answer;

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

    public Double getInnerRadius() {
        return innerRadius;
    }

    public void setInnerRadius(Double innerRadius) {
        this.innerRadius = innerRadius;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double[] getAnswer() {
        return answer;
    }

    public void setAnswer(Double[] answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "BodyTubeMOIRequest{" +
                "thickness=" + thickness +
                ", outerRadius=" + outerRadius +
                ", filled=" + filled +
                ", innerRadius=" + innerRadius +
                ", length=" + length +
                ", answer=" + answer +
                '}';
    }
}
