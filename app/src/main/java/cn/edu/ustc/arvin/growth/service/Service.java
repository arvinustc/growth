package cn.edu.ustc.arvin.growth.service;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by amazing on 2016/10/23.
 */
public class Service {
    private ServiceManager mServiceManager;

    public Service() {
        mServiceManager = ServiceManager.getInstance();
    }

    public final Looper getLooper() {
        return mServiceManager.getLooper();
    }

    public Result process(Request request) {
        return new Result(Result.CODE_NO_ERROR);
    }
}
