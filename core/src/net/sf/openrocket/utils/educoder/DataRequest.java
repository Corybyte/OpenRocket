package net.sf.openrocket.utils.educoder;

import net.sf.openrocket.util.ArrayList;

public class DataRequest {
    public ArrayList   Client_List;
    public  ArrayList  Server_List;
    public  DataRequest(ArrayList client_List,ArrayList server_List){
        Client_List=client_List;
        Server_List=server_List;
    }
}
