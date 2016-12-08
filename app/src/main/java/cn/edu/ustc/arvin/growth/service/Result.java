package cn.edu.ustc.arvin.growth.service;

/**
 * Created by amazing on 2016/10/25.
 */
public class Result {
    public static final int CODE_NO_ERROR = 0;
    public static final int CODE_ERROR = 1;
    private int code;
    private Object reply;

    public Result(int code) {
        this.code = code;
    }

    public Result(int code, Object reply) {
        this.code = code;
        this.reply = reply;
    }

    public Result() {
        this.code = CODE_NO_ERROR;
    }

    public Object getReply() {
        return reply;
    }

    public void setReply(Object reply) {
        this.reply = reply;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
