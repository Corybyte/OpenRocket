package net.sf.openrocket.utils.educoder;

import java.util.Arrays;

public class TubeFinSetCpRequest extends Request{
    private Double outerRadius;
    private  Double chord;
    private Double ar;
    private double[] poly;
    private Double mach;
    private Double beta;



    public Double getOuterRadius() {
        return outerRadius;
    }

    public void setOuterRadius(Double outerRadius) {
        this.outerRadius = outerRadius;
    }

    public Double getChord() {
        return chord;
    }

    public void setChord(Double chord) {
        this.chord = chord;
    }

    public Double getAr() {
        return ar;
    }

    public void setAr(Double ar) {
        this.ar = ar;
    }



    public double[] getPloy() {
        return poly;
    }

    public void setPloy(double[] poly) {
        this.poly = poly;
    }

    public Double getMach() {
        return mach;
    }

    public void setMach(Double mach) {
        this.mach = mach;
    }

    public Double getBeta() {
        return beta;
    }

    public void setBeta(Double beta) {
        this.beta = beta;
    }

    @Override
    public String toString() {
        return "TubeFinSetCpRequest{" +
                "outerRadius=" + outerRadius +
                ", chord=" + chord +
                ", ar=" + ar +
                ", poly=" + Arrays.toString(poly) +
                ", mach=" + mach +
                ", beta=" + beta +
                ", answer=" + answer +
                '}';
    }
}
