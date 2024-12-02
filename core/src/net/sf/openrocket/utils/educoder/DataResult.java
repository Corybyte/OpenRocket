package net.sf.openrocket.utils.educoder;

import java.util.List;

public class DataResult {
    private final Integer code;
    private final String msg;
    private final List<List<Double>> result;

    public DataResult(Integer code, String msg, List<List<Double>> result) {
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

    public List<List<Double>> getResult() {
        return result;
    }
}
