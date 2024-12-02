package net.sf.openrocket.utils.educoder;

import net.sf.openrocket.utils.educoder.Request;
import net.sf.openrocket.utils.educoder.WholeCpDTO;

import java.util.List;

public class WholeCpRequest extends Request {

    List<net.sf.openrocket.utils.educoder.WholeCpDTO> list;

    public List<net.sf.openrocket.utils.educoder.WholeCpDTO> getList() {
        return list;
    }

    public void setList(List<WholeCpDTO> list) {
        this.list = list;
    }


    @Override
    public String toString() {
        return "WholeCpRequest{" +
                "list=" + list +
                ", answer=" + answer +
                '}';
    }
}
