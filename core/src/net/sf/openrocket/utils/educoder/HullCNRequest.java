package net.sf.openrocket.utils.educoder;

import net.sf.openrocket.util.ArrayList;

public class HullCNRequest {
    public  double client_CnaCache;
    public  double client_ForeRadius;
    public  double client_AftRadius;
    public  double client_Mach;
    public  double client_AOA;
    public  double client_RefArea;
    public  double client_FullVolume;
    public  double client_Length;
    public  double client_SinAOA;
    public  double client_SincAOA;
    public  double client_PlanformCenter;
    public  double client_PlanformArea;
    public  final double client_BODY_LIFT_K = 1.1;
    public  long timestap;
    public  double result_cn;
    public  double result_cna;

    public static   ArrayList Client_cn = new ArrayList();
    public  static ArrayList Server_cn = new ArrayList();

    public HullCNRequest(ArrayList client_cn, ArrayList server_cn) {
        Client_cn = client_cn;
        Server_cn = server_cn;
    }
}
