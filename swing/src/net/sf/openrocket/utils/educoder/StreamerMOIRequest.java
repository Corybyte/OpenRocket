package net.sf.openrocket.utils.educoder;

public class StreamerMOIRequest {
    private Double radius;
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

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    @Override
    public String toString() {
        return "ParachuteMOIRequest{" +
                "radius=" + radius +
                ", answer=" + answer +
                '}';
    }
}
