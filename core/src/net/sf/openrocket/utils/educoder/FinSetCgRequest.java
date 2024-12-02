package net.sf.openrocket.utils.educoder;

import net.sf.openrocket.rocketcomponent.FinSet;
import net.sf.openrocket.util.Coordinate;

public class FinSetCgRequest {
    /**
     * 单机翼质心x
     */
    private Double singlePlanformX;
    /**
     * 单机翼质心y
     */
    private Double singlePlanformY;
    /**
     * 单机翼质心z
     */
    private Double singlePlanformZ;
    /**
     * 单机翼质心面积
     */
    private Double singlePlanformArea;
    /**
     * 单Tab质心x
     */
    private Double tabX;
    /**
     * 单Tab质心y
     */
    private Double tabY;
    /**
     * 单Tab质心z
     */
    private Double tabZ;
    /**
     * 单Tab质心面积
     */
    private Double tabArea;
    /**
     * 单Fillet质心x
     */
    private Double filletX;
    /**
     * 单Fillet质心y
     */
    private Double filletY;
    /**
     * 单Fillet质心z
     */
    private Double filletZ;
    /**
     * 单Fillet质心面积
     */
    private Double filletArea;
    /**
     * 翅片厚度
     */
    private Double thickness;
    /**
     * 翅片横截面的相对体积
     */
    private Double crossSectionRelativeVolume;
    /**
     * 组件材料密度
     */
    private Double materialDensity;
    /**
     * 翅片材料密度
     */
    private Double filletMaterialDensity;
    /**
     * 翅片数量
     */
    private Integer finCount;
    /**
     * 正确结果
     */
    private Double answer;

    public FinSetCgRequest(Coordinate wettedCentroid, Coordinate tabCentroid, Coordinate filletCentroid,
                           Double thickness, FinSet.CrossSection crossSection, Double materialDensity,
                           Double filletMaterialDensity, Integer finCount, Double answer) {
        this.singlePlanformX = wettedCentroid.x;
        this.singlePlanformY = wettedCentroid.y;
        this.singlePlanformZ = wettedCentroid.z;
        this.singlePlanformArea = wettedCentroid.weight;
        this.tabX = tabCentroid.x;
        this.tabY = tabCentroid.y;
        this.tabZ = tabCentroid.z;
        this.tabArea = tabCentroid.weight;
        this.filletX = filletCentroid.x;
        this.filletY = filletCentroid.y;
        this.filletZ = filletCentroid.z;
        this.filletArea = filletCentroid.weight;
        this.thickness = thickness;
        this.crossSectionRelativeVolume = crossSection.getRelativeVolume();
        this.materialDensity = materialDensity;
        this.filletMaterialDensity = filletMaterialDensity;
        this.finCount = finCount;
        this.answer = answer;
    }

    public Double getSinglePlanformX() {
        return singlePlanformX;
    }

    public void setSinglePlanformX(Double singlePlanformX) {
        this.singlePlanformX = singlePlanformX;
    }

    public Double getSinglePlanformY() {
        return singlePlanformY;
    }

    public void setSinglePlanformY(Double singlePlanformY) {
        this.singlePlanformY = singlePlanformY;
    }

    public Double getSinglePlanformZ() {
        return singlePlanformZ;
    }

    public void setSinglePlanformZ(Double singlePlanformZ) {
        this.singlePlanformZ = singlePlanformZ;
    }

    public Double getSinglePlanformArea() {
        return singlePlanformArea;
    }

    public void setSinglePlanformArea(Double singlePlanformArea) {
        this.singlePlanformArea = singlePlanformArea;
    }

    public Double getTabArea() {
        return tabArea;
    }

    public void setTabArea(Double tabArea) {
        this.tabArea = tabArea;
    }

    public Double getTabX() {
        return tabX;
    }

    public void setTabX(Double tabX) {
        this.tabX = tabX;
    }

    public Double getTabY() {
        return tabY;
    }

    public void setTabY(Double tabY) {
        this.tabY = tabY;
    }

    public Double getTabZ() {
        return tabZ;
    }

    public void setTabZ(Double tabZ) {
        this.tabZ = tabZ;
    }

    public Double getFilletArea() {
        return filletArea;
    }

    public void setFilletArea(Double filletArea) {
        this.filletArea = filletArea;
    }

    public Double getFilletX() {
        return filletX;
    }

    public void setFilletX(Double filletX) {
        this.filletX = filletX;
    }

    public Double getFilletY() {
        return filletY;
    }

    public void setFilletY(Double filletY) {
        this.filletY = filletY;
    }

    public Double getFilletZ() {
        return filletZ;
    }

    public void setFilletZ(Double filletZ) {
        this.filletZ = filletZ;
    }

    public Double getThickness() {
        return thickness;
    }

    public void setThickness(Double thickness) {
        this.thickness = thickness;
    }

    public Double getCrossSectionRelativeVolume() {
        return crossSectionRelativeVolume;
    }

    public void setCrossSectionRelativeVolume(Double crossSectionRelativeVolume) {
        this.crossSectionRelativeVolume = crossSectionRelativeVolume;
    }

    public Double getMaterialDensity() {
        return materialDensity;
    }

    public void setMaterialDensity(Double materialDensity) {
        this.materialDensity = materialDensity;
    }

    public Double getFilletMaterialDensity() {
        return filletMaterialDensity;
    }

    public void setFilletMaterialDensity(Double filletMaterialDensity) {
        this.filletMaterialDensity = filletMaterialDensity;
    }

    public Integer getFinCount() {
        return finCount;
    }

    public void setFinCount(Integer finCount) {
        this.finCount = finCount;
    }

    public Double getAnswer() {
        return answer;
    }

    public void setAnswer(Double answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "FinSetCgRequest{" +
                "singlePlanformX=" + singlePlanformX +
                ", singlePlanformY=" + singlePlanformY +
                ", singlePlanformZ=" + singlePlanformZ +
                ", singlePlanformArea=" + singlePlanformArea +
                ", tabX=" + tabX +
                ", tabY=" + tabY +
                ", tabZ=" + tabZ +
                ", tabArea=" + tabArea +
                ", filletX=" + filletX +
                ", filletY=" + filletY +
                ", filletZ=" + filletZ +
                ", filletArea=" + filletArea +
                ", thickness=" + thickness +
                ", crossSectionRelativeVolume=" + crossSectionRelativeVolume +
                ", materialDensity=" + materialDensity +
                ", filletMaterialDensity=" + filletMaterialDensity +
                ", finCount=" + finCount +
                ", answer=" + answer +
                '}';
    }
}
