package net.sf.openrocket.utils.educoder;

import net.sf.openrocket.motor.Motor;
import net.sf.openrocket.motor.MotorConfiguration;
import net.sf.openrocket.rocketcomponent.*;
import net.sf.openrocket.util.ArrayList;
import net.sf.openrocket.util.Coordinate;

import java.util.Arrays;
import java.util.List;

public class MyComponent {
    List<net.sf.openrocket.utils.educoder.MyComponent> children;

    Coordinate cg;
    Coordinate position;

    int instanceCount;

    Coordinate[] allInstanceOffsets;
    double[] allInstanceAngles;
    Coordinate[] instanceLocations;

    boolean isMotorMount;

    //innerTube config
    double eachMass;
    double cMx;
    double motorXPosition;
    double mountXPosition;

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

    public void copyValues(RocketComponent other, FlightConfiguration configuration) {
        this.componentName = other.getComponentName();
        this.cg = other.getCG();
        this.instanceCount = other.getInstanceCount();
        this.allInstanceOffsets = other.getInstanceLocations();
        this.allInstanceAngles = other.getInstanceAngles();
        this.position = other.getPosition();
        this.instanceLocations = other.getInstanceLocations();
        this.isMotorMount = other.isMotorMount();
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
        }

        // Recursively copy children
        this.children = new ArrayList<>();
        for (RocketComponent child : other.getChildren()) {
            net.sf.openrocket.utils.educoder.MyComponent newChild = new net.sf.openrocket.utils.educoder.MyComponent();
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

    public void setChildren(List<net.sf.openrocket.utils.educoder.MyComponent> children) {
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

    public List<net.sf.openrocket.utils.educoder.MyComponent> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return "MyComponent{" +
                "children=" + children +
                ", cg=" + cg +
                ", position=" + position +
                ", instanceCount=" + instanceCount +
                ", allInstanceOffsets=" + Arrays.toString(allInstanceOffsets) +
                ", allInstanceAngles=" + Arrays.toString(allInstanceAngles) +
                ", instanceLocations=" + Arrays.toString(instanceLocations) +
                ", componentName='" + componentName + '\'' +
                '}';
    }
}
