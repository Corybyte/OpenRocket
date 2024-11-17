package net.sf.openrocket.utils.educoder;

public class InnerComponentMOIRequest {
    Double outerRadius; //getOuterRadius
    Double innerRadius; //getInnerRadius
    Double[] answer;
    Double length;

    public Double getOuterRadius() {
        return outerRadius;
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
