package net.sf.openrocket.utils.educoder;

import com.google.gson.annotations.SerializedName;
import net.sf.openrocket.rocketcomponent.Transition;

public class NoseConeCgRequest {
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


    private Double foreShoulderLength;
    private Double foreShoulderRadius;
    private Double aftShoulderRadius;
    private Double foreShoulderThickness;
    private Double aftShoulderLength;
    private Double aftShoulderThickness;
    private Boolean aftShoulderCapped;
    private Boolean foreShoulderCapped;

    /**
     * 正确结果
     */
    private Double answer;

    public void setTransitionType(Transition.Shape transitionType) {
        this.transitionType = transitionType;
    }

    public Double getForeShoulderLength() {
        return foreShoulderLength;
    }

    public void setForeShoulderLength(Double foreShoulderLength) {
        this.foreShoulderLength = foreShoulderLength;
    }

    public Double getForeShoulderRadius() {
        return foreShoulderRadius;
    }

    public void setForeShoulderRadius(Double foreShoulderRadius) {
        this.foreShoulderRadius = foreShoulderRadius;
    }

    public Double getAftShoulderRadius() {
        return aftShoulderRadius;
    }

    public void setAftShoulderRadius(Double aftShoulderRadius) {
        this.aftShoulderRadius = aftShoulderRadius;
    }

    public Double getForeShoulderThickness() {
        return foreShoulderThickness;
    }

    public void setForeShoulderThickness(Double foreShoulderThickness) {
        this.foreShoulderThickness = foreShoulderThickness;
    }

    public Double getAftShoulderLength() {
        return aftShoulderLength;
    }

    public void setAftShoulderLength(Double aftShoulderLength) {
        this.aftShoulderLength = aftShoulderLength;
    }

    public Double getAftShoulderThickness() {
        return aftShoulderThickness;
    }

    public void setAftShoulderThickness(Double aftShoulderThickness) {
        this.aftShoulderThickness = aftShoulderThickness;
    }

    public Boolean getIsForeShoulderCapped() {
        return foreShoulderCapped;
    }

    public void setIsForeShoulderCapped(Boolean isForeShoulderCapped) {
        this.foreShoulderCapped = isForeShoulderCapped;
    }

    public Boolean getIsAftShoulderCapped() {
        return aftShoulderCapped;
    }

    public void setIsAftShoulderCapped(Boolean isAftShoulderCapped) {
        this.aftShoulderCapped = isAftShoulderCapped;
    }

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

    public String getTransitionType() {
        return transitionType.name();
    }

    public void setTransitionType(String transitionType) {
        this.transitionType = Transition.Shape.valueOf(transitionType);
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

    public Double getAnswer() {
        return answer;
    }

    public void setAnswer(Double answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "NoseConeCgRequest{" +
                "length=" + length +
                ", filled=" + filled +
                ", thickness=" + thickness +
                ", DIVISIONS=" + DIVISIONS +
                ", transitionLength=" + transitionLength +
                ", transitionShapeParameter=" + transitionShapeParameter +
                ", transitionType='" + transitionType + '\'' +
                ", transitionForeRadius=" + transitionForeRadius +
                ", transitionAftRadius=" + transitionAftRadius +
                ", answer=" + answer +
                '}';
    }
}
