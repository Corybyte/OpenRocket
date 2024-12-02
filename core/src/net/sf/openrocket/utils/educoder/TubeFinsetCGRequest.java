package net.sf.openrocket.utils.educoder;

import net.sf.openrocket.utils.educoder.Request;

public class TubeFinsetCGRequest extends Request {
    private Double length;

    private Double volume;
    private Double density; //material

    private Double outerRadius;

    private Double innerRadius;

    private Double thickness;

    private Double bodyRadius;

    private int fins;

    public Double getLength() {
        return length;
    }

    public Double getInnerRadius() {
        return innerRadius;
    }

    public void setInnerRadius(Double innerRadius) {
        this.innerRadius = innerRadius;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getDensity() {
        return density;
    }

    public void setDensity(Double density) {
        this.density = density;
    }

    public Double getOuterRadius() {
        return outerRadius;
    }

    public void setOuterRadius(Double outerRadius) {
        this.outerRadius = outerRadius;
    }

    public Double getThickness() {
        return thickness;
    }

    public void setThickness(Double thickness) {
        this.thickness = thickness;
    }

    public Double getBodyRadius() {
        return bodyRadius;
    }

    public void setBodyRadius(Double bodyRadius) {
        this.bodyRadius = bodyRadius;
    }

    public int getFins() {
        return fins;
    }

    public void setFins(int fins) {
        this.fins = fins;
    }

    @Override
    public String toString() {
        return "TubeFinsetCGRequest{" +
                "length=" + length +
                ", volume=" + volume +
                ", density=" + density +
                ", outerRadius=" + outerRadius +
                ", innerRadius=" + innerRadius +
                ", thickness=" + thickness +
                ", bodyRadius=" + bodyRadius +
                ", fins=" + fins +
                ", answer=" + answer +
                '}';
    }
}

