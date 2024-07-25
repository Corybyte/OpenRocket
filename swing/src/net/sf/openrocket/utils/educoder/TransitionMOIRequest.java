package net.sf.openrocket.utils.educoder;

import com.google.gson.annotations.SerializedName;
import net.sf.openrocket.rocketcomponent.Transition;

public class TransitionMOIRequest {

    /**
     * 组件长度
     */
    private Double length;

    /**
     * 形状分成的数量
     */
    @SerializedName("divisions")
    private Integer DIVISIONS;

    /**
     * 组件是否被填充
     */
    private Boolean filled;

    /**
     * 翅片厚度
     */
    private Double thickness;
    /**
     * 正确结果
     */

    private Double transitionShapeParameter;

    private Transition.Shape transitionType;

    private Double transitionForeRadius;

    private Double transitionAftRadius;


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

    private Double answer;

    public Double getAnswer() {
        return answer;
    }

    public void setAnswer(Double answer) {
        this.answer = answer;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Integer getDIVISIONS() {
        return DIVISIONS;
    }

    public void setDIVISIONS(Integer DIVISIONS) {
        this.DIVISIONS = DIVISIONS;
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

    @Override
    public String toString() {
        return "NoseConeMOIRequest{" +
                "length=" + length +
                ", DIVISIONS=" + DIVISIONS +
                ", filled=" + filled +
                ", thickness=" + thickness +
                ", transitionShapeParameter=" + transitionShapeParameter +
                ", transitionType=" + transitionType +
                ", transitionForeRadius=" + transitionForeRadius +
                ", transitionAftRadius=" + transitionAftRadius +
                ", answer=" + answer +
                '}';
    }
}
