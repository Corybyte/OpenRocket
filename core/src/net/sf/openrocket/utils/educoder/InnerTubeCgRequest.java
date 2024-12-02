package net.sf.openrocket.utils.educoder;

import net.sf.openrocket.utils.educoder.Request;

import java.util.Arrays;

public class InnerTubeCgRequest extends Request {
    Integer instanceCount; //getInstanceCount
    Double outerRadius; //getOuterRadius
    Double innerRadius; //getInnerRadius
    Double length;
    Double density;

    Double[][] instanceOffsets;

    public Double[][] getInstanceOffsets() {
        return instanceOffsets;
    }

    public void setInstanceOffsets(Double[][] instanceOffsets) {
        this.instanceOffsets = instanceOffsets;
    }

    public Integer getInstanceCount() {
        return instanceCount;
    }

    public void setInstanceCount(Integer instanceCount) {
        this.instanceCount = instanceCount;
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

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getDensity() {
        return density;
    }

    public void setDensity(Double density) {
        this.density = density;
    }

    @Override
    public String toString() {
        return "InnerTubeCgRequest{" +
                "instanceCount=" + instanceCount +
                ", outerRadius=" + outerRadius +
                ", innerRadius=" + innerRadius +
                ", length=" + length +
                ", density=" + density +
                ", instanceOffsets=" + Arrays.toString(instanceOffsets) +
                ", answer=" + answer +
                '}';
    }
}
