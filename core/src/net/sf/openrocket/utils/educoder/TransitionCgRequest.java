package net.sf.openrocket.utils.educoder;

import com.google.gson.annotations.SerializedName;
import net.sf.openrocket.rocketcomponent.Transition;
import net.sf.openrocket.utils.educoder.Request;

public class TransitionCgRequest extends Request {
    /**
     * 组件长度
     */
    private Double length;
    /**
     * 组件材料密度
     */
    private Double density;
    /**
     * 组件是否被填充
     */
    private Boolean filled;
    /**
     * 组件厚度
     */
    private Double thickness;
    /**
     * 形状分成的数量
     */
    @SerializedName("divisions")
    private Integer DIVISIONS;

    private Double transitionLength;

    private Double transitionShapeParameter;

    private Transition.Shape transitionType;

    private Double transitionForeRadius;

    private Double transitionAftRadius;

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getDensity() {
        return density;
    }

    public void setDensity(Double density) {
        this.density = density;
    }

    public Boolean getFilled() {
        return filled;
    }

    public void setFilled(Boolean filled) {
        this.filled = filled;
    }

    public Double getThickness() {
        return thickness;
    }

    public void setThickness(Double thickness) {
        this.thickness = thickness;
    }

    public Integer getDIVISIONS() {
        return DIVISIONS;
    }

    public void setDIVISIONS(Integer DIVISIONS) {
        this.DIVISIONS = DIVISIONS;
    }

    public Double getTransitionLength() {
        return transitionLength;
    }

    public void setTransitionLength(Double transitionLength) {
        this.transitionLength = transitionLength;
    }

    public Double getTransitionShapeParameter() {
        return transitionShapeParameter;
    }

    public void setTransitionShapeParameter(Double transitionShapeParameter) {
        this.transitionShapeParameter = transitionShapeParameter;
    }

    public Transition.Shape getTransitionType() {
        return transitionType;
    }

    public void setTransitionType(Transition.Shape transitionType) {
        this.transitionType = transitionType;
    }

    public Double getTransitionForeRadius() {
        return transitionForeRadius;
    }

    public void setTransitionForeRadius(Double transitionForeRadius) {
        this.transitionForeRadius = transitionForeRadius;
    }

    public Double getTransitionAftRadius() {
        return transitionAftRadius;
    }

    public void setTransitionAftRadius(Double transitionAftRadius) {
        this.transitionAftRadius = transitionAftRadius;
    }

    @Override
    public String toString() {
        return "TransitionCgRequest{" +
                "length=" + length +
                ", density=" + density +
                ", filled=" + filled +
                ", thickness=" + thickness +
                ", DIVISIONS=" + DIVISIONS +
                ", transitionLength=" + transitionLength +
                ", transitionShapeParameter=" + transitionShapeParameter +
                ", transitionType=" + transitionType +
                ", transitionForeRadius=" + transitionForeRadius +
                ", transitionAftRadius=" + transitionAftRadius +
                ", answer=" + answer +
                '}';
    }
}
