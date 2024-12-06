package net.sf.openrocket.utils.educoder;

import net.sf.openrocket.masscalc.RigidBody;
import net.sf.openrocket.util.ArrayList;
import net.sf.openrocket.util.Coordinate;

public class AccelerationRequest {
    private Double density;
    private Double velocity;

    private Double CDaxial;

    private Double refArea;

    private Double refLength;

    private Double CN;

    private Double Cside;

    private Double thrustForce;
    private RigidBody rocketMass;

    private Double sin;
    private Double cos;
    private Coordinate orientationQuaternion;

    private Double Cm;

    private Double Cyaw;

    private Double Croll;

    private Boolean isLaunchRodCleared;

    private Coordinate launchRodDirection;

    private Double gravity;

    private Coordinate coriolisAcceleration;

    public Long timestamp;
    public static ArrayList<Double> client_cn = new ArrayList<Double>();
    public  static ArrayList<Coordinate> server_cn = new ArrayList<Coordinate>();
    public  static ArrayList<Coordinate> server_cn2 = new ArrayList<Coordinate>();

    public void setDensity(Double density) {
        this.density = density;
    }

    public void setVelocity(Double velocity) {
        this.velocity = velocity;
    }

    public void setCDaxial(Double CDaxial) {
        this.CDaxial = CDaxial;
    }

    public void setRefArea(Double refArea) {
        this.refArea = refArea;
    }

    public void setRefLength(Double refLength) {
        this.refLength = refLength;
    }

    public void setCN(Double CN) {
        this.CN = CN;
    }

    public void setCside(Double cside) {
        Cside = cside;
    }

    public void setThrustForce(Double thrustForce) {
        this.thrustForce = thrustForce;
    }

    public void setRocketMass(RigidBody rocketMass) {
        this.rocketMass = rocketMass;
    }

    public void setSin(Double sin) {
        this.sin = sin;
    }

    public void setCos(Double cos) {
        this.cos = cos;
    }

    public void setOrientationQuaternion(Coordinate orientationQuaternion) {
        this.orientationQuaternion = orientationQuaternion;
    }

    public void setCm(Double cm) {
        Cm = cm;
    }

    public void setCyaw(Double cyaw) {
        Cyaw = cyaw;
    }

    public void setCroll(Double croll) {
        Croll = croll;
    }

    public void setLaunchRodCleared(Boolean launchRodCleared) {
        isLaunchRodCleared = launchRodCleared;
    }

    public void setLaunchRodDirection(Coordinate launchRodDirection) {
        this.launchRodDirection = launchRodDirection;
    }

    public void setGravity(Double gravity) {
        this.gravity = gravity;
    }

    public void setCoriolisAcceleration(Coordinate coriolisAcceleration) {
        this.coriolisAcceleration = coriolisAcceleration;
    }

    @Override
    public String toString() {
        return "AccelerationRequest{" +
                "density=" + density +
                ", velocity=" + velocity +
                ", CDaxial=" + CDaxial +
                ", refArea=" + refArea +
                ", refLength=" + refLength +
                ", CN=" + CN +
                ", Cside=" + Cside +
                ", thrustForce=" + thrustForce +
                ", rocketMass=" + rocketMass +
                ", sin=" + sin +
                ", cos=" + cos +
                ", orientationQuaternion=" + orientationQuaternion +
                ", Cm=" + Cm +
                ", Cyaw=" + Cyaw +
                ", Croll=" + Croll +
                ", isLaunchRodCleared=" + isLaunchRodCleared +
                ", launchRodDirection=" + launchRodDirection +
                ", gravity=" + gravity +
                ", coriolisAcceleration=" + coriolisAcceleration +
                '}';
    }
}
