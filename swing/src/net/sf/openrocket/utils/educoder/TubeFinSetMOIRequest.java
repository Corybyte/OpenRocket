package net.sf.openrocket.utils.educoder;

public class TubeFinSetMOIRequest {

    private Double thickness;

    private int fins;

    private Double bodyRadius;

    private Double innerRadius;

    private Double outerRadius;
    private Double length;
    private Double axialOffset;
    private Double[] answer;

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getAxialOffset() {
        return axialOffset;
    }

    public void setAxialOffset(Double axialOffset) {
        this.axialOffset = axialOffset;
    }

    public Double getThickness() {
        return thickness;
    }

    public void setThickness(Double thickness) {
        this.thickness = thickness;
    }

    public int getFins() {
        return fins;
    }

    public void setFins(int fins) {
        this.fins = fins;
    }

    public Double getBodyRadius() {
        return bodyRadius;
    }

    public void setBodyRadius(Double bodyRadius) {
        this.bodyRadius = bodyRadius;
    }

    public Double getInnerRadius() {
        return innerRadius;
    }

    public void setInnerRadius(Double innerRadius) {
        this.innerRadius = innerRadius;
    }

    public Double getOuterRadius() {
        return outerRadius;
    }

    public void setOuterRadius(Double outerRadius) {
        this.outerRadius = outerRadius;
    }

    public Double[] getAnswer() {
        return answer;
    }

    public void setAnswer(Double[] answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "TubeFinSetMOIRequest{" +
                "thickness=" + thickness +
                ", fins=" + fins +
                ", bodyRadius=" + bodyRadius +
                ", innerRadius=" + innerRadius +
                ", outerRadius=" + outerRadius +
                ", answer=" + answer +
                '}';
    }
}
