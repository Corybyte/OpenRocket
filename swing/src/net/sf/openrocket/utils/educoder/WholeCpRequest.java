package net.sf.openrocket.utils.educoder;

import net.sf.openrocket.rocketcomponent.RocketComponent;

import java.util.List;
import java.util.Map;

public class WholeCpRequest extends Request{

    List<WholeCpDTO> list;

    public List<WholeCpDTO> getList() {
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
