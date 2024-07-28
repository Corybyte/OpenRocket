package net.sf.openrocket.utils.educoder;

public class MassComponentCgRequest extends Request{
    private Double length;

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "ParachuteCgRequest{" +
                "length=" + length +
                ", answer=" + answer +
                '}';
    }
}
