package net.sf.openrocket.utils.educoder;

public class ShockCordMOIRequest extends Request{
    private Double radius;

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
