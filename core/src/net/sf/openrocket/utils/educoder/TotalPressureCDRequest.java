package net.sf.openrocket.utils.educoder;


import java.util.List;
import net.sf.openrocket.util.ArrayList;

public class TotalPressureCDRequest {

    public double mach;
    public double refArea;


    public List<Double> length;
    public List<Double> foreRadiuss;
    public List<Double> aftRadiuss;
    public List<Integer> componentInstanceCount;
    public List<Boolean> isSymmetricComponent;
    public List<Double> prevAftRadius;
    public List<Boolean> hasPreviousSymmetricComponent;
    public List<Boolean> isComponentActives;
    public List<Double> componentCD;

    public static ArrayList client_cn = new net.sf.openrocket.util.ArrayList();
    public static ArrayList server_cn = new ArrayList();
}
