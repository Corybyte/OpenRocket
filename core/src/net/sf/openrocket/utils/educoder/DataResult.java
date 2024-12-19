package net.sf.openrocket.utils.educoder;

import java.util.List;

public class DataResult {
    private final Integer code;
    private final String msg;
    private final List<List<Object>> result;

    public DataResult(Integer code, String msg, List<List<Object>> result) {
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

    public List<List<Object>> getResult() {
        return result;
    }
}
