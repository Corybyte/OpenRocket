package net.sf.openrocket.utils.educoder;

import com.google.gson.annotations.SerializedName;
import net.sf.openrocket.rocketcomponent.Transition;

public class NoseConeMOIRequest {

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
    private Double density;

    private Double foreShoulderLength;
    private Double foreShoulderRadius;
    private Double aftShoulderRadius;
    private Double foreShoulderThickness;
    private Double aftShoulderLength;
    private Double aftShoulderThickness;
    private Boolean aftShoulderCapped;
    private Boolean foreShoulderCapped;

    public Double getForeShoulderLength() {
        return foreShoulderLength;
    }

    public Double getForeShoulderRadius() {
        return foreShoulderRadius;
    }

    public Double getAftShoulderRadius() {
        return aftShoulderRadius;
    }

    public Double getForeShoulderThickness() {
        return foreShoulderThickness;
    }

    public Double getAftShoulderLength() {
        return aftShoulderLength;
    }

    public Double getAftShoulderThickness() {
        return aftShoulderThickness;
    }

    public Boolean getAftShoulderCapped() {
        return aftShoulderCapped;
    }

    public Boolean getForeShoulderCapped() {
        return foreShoulderCapped;
    }

    public void setForeShoulderLength(Double foreShoulderLength) {
        this.foreShoulderLength = foreShoulderLength;
    }

    public void setForeShoulderRadius(Double foreShoulderRadius) {
        this.foreShoulderRadius = foreShoulderRadius;
    }

    public void setAftShoulderRadius(Double aftShoulderRadius) {
        this.aftShoulderRadius = aftShoulderRadius;
    }

    public void setForeShoulderThickness(Double foreShoulderThickness) {
        this.foreShoulderThickness = foreShoulderThickness;
    }

    public void setAftShoulderLength(Double aftShoulderLength) {
        this.aftShoulderLength = aftShoulderLength;
    }

    public void setAftShoulderThickness(Double aftShoulderThickness) {
        this.aftShoulderThickness = aftShoulderThickness;
    }

    public void setAftShoulderCapped(Boolean aftShoulderCapped) {
        this.aftShoulderCapped = aftShoulderCapped;
    }

    public void setForeShoulderCapped(Boolean foreShoulderCapped) {
        this.foreShoulderCapped = foreShoulderCapped;
    }

    public Double getDensity() {
        return density;
    }

    public void setDensity(Double density) {
        this.density = density;
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

    private Double[] answer;

    public Double[] getAnswer() {
        return answer;
    }

    public void setAnswer(Double[] answer) {
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
                ", density=" + density +
                ", answer=" + answer +
                '}';
    }
}
