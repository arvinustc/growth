package cn.edu.ustc.arvin.growth.service;

/**
 * Created by amazing on 2016/10/23.
 */
public class Request {
    public static final int EMPTY = 0;
    public static final int LOAD = 0x1;
    public static final int RESET = 0x2;
    public static final int FILTER = 0x3;

    public static final int FETCH = 0x11;
    public static final int FIRST = 0x12;
    public static final int LAST = 0x13;
    public static final int NEXT = 0x14;
    public static final int SIZE = 0x15;

    public static final int INCREASE = 0x21;
    public static final int DECREASE = 0x22;
    public static final int FAVORITE = 0x23;
    public static final int UNFAVORITE = 0x24;

    public static final int MAX = 0x100;

    private int mAction;
    private String mData;
    private String mGroup;

    public Request() {
        this.mAction = EMPTY;
        this.mData = null;
        this.mGroup = null;
    }

    public Request(int action, String data) {
        this.mAction = action;
        this.mData = data;
        this.mGroup = null;
    }

    public int getAction() {
        return mAction;
    }

    public String getData() {
        return mData;
    }

    public String getGroup() {
        return mGroup;
    }

    public void setAction(int action) {
        this.mAction = action;
    }

    public void setData(String data) {
        this.mData = data;
    }

    public void setGroup(String group) {
        this.mGroup = group;
    }
}
