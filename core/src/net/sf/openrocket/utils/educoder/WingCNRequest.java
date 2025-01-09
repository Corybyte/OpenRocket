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
    public  double angle;
    public static ArrayList Client_CN = new ArrayList();
    public static ArrayList Server_CN = new ArrayList();

    public WingCNRequest(ArrayList client_cn, ArrayList server_cn) {
        Client_CN = client_cn;
        Server_CN = server_cn;
    }

    public String toString() {
        return "{" +
                "\"angle\":" + angle + "," +
                "\"Tau\":" + Tau + "," +
                "\"Cna\":" + Cna + "," +
                "\"Cna1\":" + Cna1 + "," +
                "\"MacLead\":" + MacLead + "," +
                "\"MacLength\":" + MacLength + "," +
                "\"MacSpen\":" + MacSpen + "," +
                "\"BodyRadius\":" + BodyRadius + "," +
                "\"CantAngle\":" + CantAngle + "," +
                "\"FlightConditions_Mach\":" + FlightConditions_Mach + "," +
                "\"FlightConditions_Beta\":" + FlightConditions_Beta + "," +
                "\"FlightConditions_RefLength\":" + FlightConditions_RefLength + "," +
                "\"FlightConditions_AOA\":" + FlightConditions_AOA + "," +
                "\"STALL_ANGLE\":" + STALL_ANGLE + "," +
                "\"AerodynamicForces_CrollForce\":" + AerodynamicForces_CrollForce + "," +
                "\"FlightConditions_RefArea\":" + FlightConditions_RefArea + "," +
                "\"FinSetCalc_finArea\":" + FinSetCalc_finArea + "," +
                "\"FinSetCalc_cosGamma\":" + FinSetCalc_cosGamma + "," +
                "\"FinSetCalc_span\":" + FinSetCalc_span + "," +
                "\"interferenceFinCount\":" + interferenceFinCount + "," +
                "\"FinSetCalc_theta\":" + FinSetCalc_theta + "," +
                "\"result_CN\":" + result_CN + "," +
                "\"Client_CN\":" + Client_CN + "," +
                "\"Server_CN\":" + Server_CN +
                "}";
    }



}
