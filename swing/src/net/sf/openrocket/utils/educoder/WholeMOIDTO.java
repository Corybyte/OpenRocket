package net.sf.openrocket.utils.educoder;

import net.sf.openrocket.motor.Motor;
import net.sf.openrocket.motor.MotorConfiguration;
import net.sf.openrocket.rocketcomponent.*;
import net.sf.openrocket.util.ArrayList;
import net.sf.openrocket.util.Coordinate;

import java.util.Arrays;
import java.util.List;

public class WholeMOIDTO {
    List<WholeMOIDTO> children;

    Coordinate cg;
    Coordinate position;

    int instanceCount;

    Coordinate[] allInstanceOffsets;
    double[] allInstanceAngles;
    Coordinate[] instanceLocations;

    Double rotationalUnitInertia;

    Double longitudinalUnitInertia;

    boolean isMotorMount;

    //innerTube config
    double eachMass;
    double cMx;
    double motorXPosition;
    double mountXPosition;
    double config_longInertia;
    double config_inertia;

    public double getEachMass() {
        return eachMass;
    }

    public void setEachMass(double eachMass) {
        this.eachMass = eachMass;
    }

    public double getcMx() {
        return cMx;
    }

    public void setcMx(double cMx) {
        this.cMx = cMx;
    }

    public double getMotorXPosition() {
        return motorXPosition;
    }

    public void setMotorXPosition(double motorXPosition) {
        this.motorXPosition = motorXPosition;
    }

    public double getMountXPosition() {
        return mountXPosition;
    }

    public void setMountXPosition(double mountXPosition) {
        this.mountXPosition = mountXPosition;
    }

    public Coordinate[] getInstanceLocations() {
        return instanceLocations;
    }

    public void setInstanceLocations(Coordinate[] instanceLocations) {
        this.instanceLocations = instanceLocations;
    }

    public int getInstanceCount() {
        return instanceCount;
    }

    public void setInstanceCount(int instanceCount) {
        this.instanceCount = instanceCount;
    }

    public Coordinate[] getAllInstanceOffsets() {
        return allInstanceOffsets;
    }

    public void setAllInstanceOffsets(Coordinate[] allInstanceOffsets) {
        this.allInstanceOffsets = allInstanceOffsets;
    }

    public double[] getAllInstanceAngles() {
        return allInstanceAngles;
    }

    public void setAllInstanceAngles(double[] allInstanceAngles) {
        this.allInstanceAngles = allInstanceAngles;
    }

    public boolean isMotorMount() {
        return isMotorMount;
    }

    public void setMotorMount(boolean motorMount) {
        isMotorMount = motorMount;
    }

    @Override
    public String toString() {
        return "WholeMOIDTO{" +
                "children=" + children +
                ", cg=" + cg +
                ", position=" + position +
                ", instanceCount=" + instanceCount +
                ", allInstanceOffsets=" + Arrays.toString(allInstanceOffsets) +
                ", allInstanceAngles=" + Arrays.toString(allInstanceAngles) +
                ", instanceLocations=" + Arrays.toString(instanceLocations) +
                ", rotationalUnitInertia=" + rotationalUnitInertia +
                ", longitudinalUnitInertia=" + longitudinalUnitInertia +
                ", isMotorMount=" + isMotorMount +
                ", eachMass=" + eachMass +
                ", cMx=" + cMx +
                ", motorXPosition=" + motorXPosition +
                ", mountXPosition=" + mountXPosition +
                ", config_longInertia=" + config_longInertia +
                ", config_inertia=" + config_inertia +
                ", componentName='" + componentName + '\'' +
                '}';
    }

    public void copyValues(RocketComponent other, FlightConfiguration configuration) {
        this.componentName = other.getComponentName();
        this.cg = other.getCG();
        this.instanceCount = other.getInstanceCount();
        this.allInstanceOffsets = other.getInstanceLocations();
        this.allInstanceAngles = other.getInstanceAngles();
        this.position = other.getPosition();
        this.instanceLocations = other.getInstanceLocations();
        this.isMotorMount = other.isMotorMount();
        this.rotationalUnitInertia = other.getRotationalUnitInertia();
        this.longitudinalUnitInertia = other.getLongitudinalUnitInertia();
        if (isMotorMount && (other instanceof InnerTube || other instanceof BodyTube)) {
            MotorMount mount = (MotorMount) other;
            MotorConfiguration motorConfig = mount.getMotorConfig(configuration.getId());
            Motor motor = motorConfig.getMotor();
            double cMx = motor.getCMx(0);
            double motorXPosition = motorConfig.getX();
            double mountXPosition = other.getPosition().x;
            double eachMass = motor.getTotalMass(0);
            this.cMx = cMx;
            this.motorXPosition = motorXPosition;
            this.mountXPosition = mountXPosition;
            this.eachMass = eachMass;
            this.config_longInertia = motorConfig.getUnitLongitudinalInertia();
            this.config_inertia=  motorConfig.getUnitRotationalInertia();
        }

        // Recursively copy children
        this.children = new ArrayList<>();
        for (RocketComponent child : other.getChildren()) {
            WholeMOIDTO newChild = new WholeMOIDTO();
            newChild.copyValues(child, configuration); // Recursive call to copy child components
            this.children.add(newChild);
        }
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    String componentName;

    public void setChildren(List<WholeMOIDTO> children) {
        this.children = children;
    }


    public Coordinate getCg() {
        return cg;
    }

    public void setCg(Coordinate cg) {
        this.cg = cg;
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }

    public List<WholeMOIDTO> getChildren() {
        return children;
    }

}
