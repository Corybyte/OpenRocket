package net.sf.openrocket.utils.educoder;

import net.sf.openrocket.util.ArrayList;
import net.sf.openrocket.util.Coordinate;

public class componentForcesRequest {
    public  double othercn;
    public  double cn;


    public static ArrayList Client_CN = new ArrayList();
    public static ArrayList Server_CN = new ArrayList();
    public componentForcesRequest(ArrayList client_cn, ArrayList server_cn) {
        Client_CN =client_cn;
        Server_CN =server_cn;


    }

}
