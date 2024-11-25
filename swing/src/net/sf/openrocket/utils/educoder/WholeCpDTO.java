package net.sf.openrocket.utils.educoder;

import net.sf.openrocket.rocketcomponent.InstanceContext;
import net.sf.openrocket.util.Coordinate;
import net.sf.openrocket.util.Transformation;

import java.util.List;

public class WholeCpDTO {
    String componentName;
    Coordinate cp[];
    Boolean rocketComponentCalc;


    public Coordinate[] getCp() {
        return cp;
    }

    public void setCp(Coordinate[] cp) {
        this.cp = cp;
    }

    public Boolean getRocketComponentCalc() {
        return rocketComponentCalc;
    }

    public void setRocketComponentCalc(Boolean rocketComponentCalc) {
        this.rocketComponentCalc = rocketComponentCalc;
    }


    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    @Override
    public String toString() {
        return "WholeCpDTO{" +
                "componentName='" + componentName + '\'' +
                ", cp=" + cp +
                ", rocketComponentCalc=" + rocketComponentCalc +
                '}';
    }
}
