package net.sf.openrocket.utils.educoder;

public class BodyTubeCgRequest {
    /**
     * 组件长度
     */
    private Double length;

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public BodyTubeCgRequest(Double length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "BodyTubeCgRequest{" +
                "length=" + length +
                '}';
    }
}
