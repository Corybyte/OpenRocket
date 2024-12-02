package net.sf.openrocket.utils.educoder;

import net.sf.openrocket.utils.educoder.Request;

public class LaunchLugCgRequest extends Request {
    private Double length;
    private Double instanceSeparation;
    private Integer instanceCount;


    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getInstanceSeparation() {
        return instanceSeparation;
    }

    public void setInstanceSeparation(Double instanceSeparation) {
        this.instanceSeparation = instanceSeparation;
    }

    public Integer getInstanceCount() {
        return instanceCount;
    }

    public void setInstanceCount(Integer instanceCount) {
        this.instanceCount = instanceCount;
    }

    @Override
    public String toString() {
        return "LaunchLugCgRequest{" +
                "length=" + length +
                ", instanceSeparation=" + instanceSeparation +
                ", instanceCount=" + instanceCount +
                ", answer=" + answer +
                '}';
    }
}
