package net.sf.openrocket.utils.educoder;

public class Result2 {
    private final Integer code;
    private final String msg;
    private final Double[] result;

    public Result2(Integer code, String msg, Double[] result) {
        this.code = code;
        this.msg = msg;
        this.result = result;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Double[] getResult() {
        return result;
    }
}
