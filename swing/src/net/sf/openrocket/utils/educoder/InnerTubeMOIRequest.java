package net.sf.openrocket.utils.educoder;

import java.util.Arrays;

public class InnerTubeMOIRequest  {
    Double outerRadius; //getOuterRadius
    Double innerRadius; //getInnerRadius
    Double length;

    Double[] answer;

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
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

    public Double[] getAnswer() {
        return answer;
    }

    public void setAnswer(Double[] answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "InnerTubeCgRequest{" +
                ", outerRadius=" + outerRadius +
                ", innerRadius=" + innerRadius +
                ", answer=" + answer +
                '}';
    }
}
