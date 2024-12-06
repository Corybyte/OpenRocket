package net.sf.openrocket.utils.educoder;

import de.javagl.obj.Obj;

public class Result {
    private final Integer code;
    private final String msg;
    private final Object result;

    public Result(Integer code, String msg, Double result) {
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

    public Object getResult() {
        return result;
    }
}
