package net.sf.openrocket.utils.educoder;

public class RailButtonCgRequest extends Request{
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

    private Double instanceSeparation;
    private Integer instanceCount;

    @Override
    public String toString() {
        return "RailButtonCgRequest{" +
                "instanceSeparation=" + instanceSeparation +
                ", instanceCount=" + instanceCount +
                ", answer=" + answer +
                '}';
    }
}
