package net.sf.openrocket.utils.educoder;

public class BodyTubeCgRequest {
    /**
     * 组件长度
     */
    private Double length;
    /**
     * 正确结果
     */
    private Double answer;

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getAnswer() {
        return answer;
    }

    public void setAnswer(Double answer) {
        this.answer = answer;
    }

    public BodyTubeCgRequest(Double length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "BodyTubeCgRequest{" +
                "length=" + length +
                ", answer=" + answer +
                '}';
    }
}
