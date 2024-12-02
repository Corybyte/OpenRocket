package net.sf.openrocket.utils.educoder;

import net.sf.openrocket.utils.educoder.Request;

public class StreamerCgRequest extends Request {
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
