package net.sf.openrocket.utils.educoder;

import net.sf.openrocket.utils.educoder.WholeMOIDTO;

public class WholeMOIRequest {
    private net.sf.openrocket.utils.educoder.WholeMOIDTO wholeMOIDTO;
    private Double[] answer;
    public net.sf.openrocket.utils.educoder.WholeMOIDTO getWholeMOIDTO() {
        return wholeMOIDTO;
    }

    public void setWholeMOIDTO(WholeMOIDTO wholeMOIDTO) {
        this.wholeMOIDTO = wholeMOIDTO;
    }

    public Double[] getAnswer() {
        return answer;
    }

    public void setAnswer(Double[] answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "WholeMOIRequest{" +
                "wholeMOIDTO=" + wholeMOIDTO +
                ", answer=" + answer +
                '}';
    }
}
