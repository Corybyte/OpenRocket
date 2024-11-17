package net.sf.openrocket.utils.educoder;

public class StageMOIRequest {
    private Double[] answer;

    public Double[] getAnswer() {
        return answer;
    }

    public void setAnswer(Double[] answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "StageCgRequest{" +
                "answer=" + answer +
                '}';
    }
}
