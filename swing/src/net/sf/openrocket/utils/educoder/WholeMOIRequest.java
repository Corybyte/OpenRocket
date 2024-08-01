package net.sf.openrocket.utils.educoder;

import net.sf.openrocket.util.Coordinate;

import java.util.List;

public class WholeMOIRequest extends Request {
    private WholeMOIDTO wholeMOIDTO;

    public WholeMOIDTO getWholeMOIDTO() {
        return wholeMOIDTO;
    }

    public void setWholeMOIDTO(WholeMOIDTO wholeMOIDTO) {
        this.wholeMOIDTO = wholeMOIDTO;
    }

    @Override
    public String toString() {
        return "WholeMOIRequest{" +
                "wholeMOIDTO=" + wholeMOIDTO +
                ", answer=" + answer +
                '}';
    }
}
