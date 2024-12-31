package net.sf.openrocket.utils.educoder;

import net.sf.openrocket.util.ArrayList;

import java.lang.reflect.Array;

public class WingCNRequest {
    public  double Tau;
    public  double Cna;
    public  double Cna1;
    public  double MacLead;
    public  double MacLength;
    public  double MacSpen;
    public  double BodyRadius;
    public  double CantAngle;
    public  double FlightConditions_Mach;
    public  double FlightConditions_Beta;
    public  double FlightConditions_RefLength;
    public  double FlightConditions_AOA;
    public  double STALL_ANGLE;
    public  double AerodynamicForces_CrollForce;
    public  double FlightConditions_RefArea;
    public  double FinSetCalc_finArea;
    public  double FinSetCalc_cosGamma;
    public  double FinSetCalc_span;
    public  double interferenceFinCount;
    public  double FinSetCalc_theta;
    public  double result_CN;
    public static ArrayList Client_CN = new ArrayList();
    public static ArrayList Server_CN = new ArrayList();

    public WingCNRequest(ArrayList client_cn, ArrayList server_cn) {
        Client_CN = client_cn;
        Server_CN = server_cn;
    }



}
