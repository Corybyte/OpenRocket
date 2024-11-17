package net.sf.openrocket.utils.educoder;

public class LaunchLugMOIRequest {

    private Double outerRadius;
    private Double innerRadius;
    private Double length;
    private Double[] answer;

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
