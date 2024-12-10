package net.sf.openrocket.utils.educoder;

import net.sf.openrocket.util.ArrayList;

import java.util.List;

public class FrictionCDRequest {
    private Double mach;
    private Double velocity;
    private Double lengthAerodynamic;
    private Double kinematicViscosity;
    private Boolean isPerfectFinish;
    private Double aeroDynamic;
    private Double refArea;


    // Finish
//    private List<Double> instances;
    //组件对应的材料
    private List<Integer> finish_ordinal;
    private List<Double> roughnessSize;

    //需要计算组件的 frictionCd
    private List<String> componentName;

    private List<Double> otherComponentFrictionCD;
    private List<Integer> componentInstanceCount;

    private List<Double> wetArea;

    private List<Double> thickness;
    private List<Double> macLength;
    private List<Double> finArea;

    //对称组件的offset  其余设为Null
    private List<Double> axialOffset;
    //对称组件的length
    private List<Double> length;
    //foreRadius aftRadius
    private List<Double> foreRadius;

    private List<Double> aftRadius;

    private Long timestamp;

    public static ArrayList client_cn = new ArrayList();
    public  static ArrayList server_cn = new ArrayList();

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public void setMach(Double mach) {
        this.mach = mach;
    }

    public void setVelocity(Double velocity) {
        this.velocity = velocity;
    }


    public void setLengthAerodynamic(Double lengthAerodynamic) {
        this.lengthAerodynamic = lengthAerodynamic;
    }



    public void setKinematicViscosity(Double kinematicViscosity) {
        this.kinematicViscosity = kinematicViscosity;
    }



    public void setPerfectFinish(Boolean perfectFinish) {
        isPerfectFinish = perfectFinish;
    }



    public void setAeroDynamic(Double aeroDynamic) {
        this.aeroDynamic = aeroDynamic;
    }



    public void setRefArea(Double refArea) {
        this.refArea = refArea;
    }



    public void setComponentInstanceCount(List<Integer> componentInstanceCount) {
        this.componentInstanceCount = componentInstanceCount;
    }



    public void setFinish_ordinal(List<Integer> finish_ordinal) {
        this.finish_ordinal = finish_ordinal;
    }

    public void setRoughnessSize(List<Double> roughnessSize) {
        this.roughnessSize = roughnessSize;
    }



    public void setComponentName(List<String> componentName) {
        this.componentName = componentName;
    }



    public void setOtherComponentFrictionCD(List<Double> otherComponentFrictionCD) {
        this.otherComponentFrictionCD = otherComponentFrictionCD;
    }



    public void setWetArea(List<Double> wetArea) {
        this.wetArea = wetArea;
    }



    public void setThickness(List<Double> thickness) {
        this.thickness = thickness;
    }



    public void setMacLength(List<Double> macLength) {
        this.macLength = macLength;
    }


    public void setFinArea(List<Double> finArea) {
        this.finArea = finArea;
    }



    public void setAxialOffset(List<Double> axialOffset) {
        this.axialOffset = axialOffset;
    }

    public void setLength(List<Double> length) {
        this.length = length;
    }


    public void setForeRadius(List<Double> foreRadius) {
        this.foreRadius = foreRadius;
    }



    public void setAftRadius(List<Double> aftRadius) {
        this.aftRadius = aftRadius;
    }

    @Override
    public String toString() {
        return "FrictionCDRequest{" +
                "mach=" + mach +
                ", velocity=" + velocity +
                ", lengthAerodynamic=" + lengthAerodynamic +
                ", kinematicViscosity=" + kinematicViscosity +
                ", isPerfectFinish=" + isPerfectFinish +
                ", aeroDynamic=" + aeroDynamic +
                ", refArea=" + refArea +
                ", finish_ordinal=" + finish_ordinal +
                ", roughnessSize=" + roughnessSize +
                ", componentName=" + componentName +
                ", otherComponentFrictionCD=" + otherComponentFrictionCD +
                ", componentInstanceCount=" + componentInstanceCount +
                ", wetArea=" + wetArea +
                ", thickness=" + thickness +
                ", macLength=" + macLength +
                ", finArea=" + finArea +
                ", axialOffset=" + axialOffset +
                ", length=" + length +
                ", foreRadius=" + foreRadius +
                ", aftRadius=" + aftRadius +
                ", timestamp=" + timestamp +
                '}';
    }
}
