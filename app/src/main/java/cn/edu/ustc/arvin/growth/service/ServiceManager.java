package cn.edu.ustc.arvin.growth.service;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

/**
 * Created by amazing on 2016/10/20.
 */
public class ServiceManager {
    private static final String sServiceManagerThreadName = "ServiceManager";
    private static ServiceManager sServiceManager = null;
    private static Context sContext = null;
    private static boolean sGetInstanceAllowed = false;
    private ServiceManagerThread mSMThread;

    public static final int SERVICE_FONT = 101;
    public static final int SERVICE_DATA = 102;
    public static final int SERVICE_VOICE = 103;

    private ServiceManager() {
        mSMThread = new ServiceManagerThread();
        mSMThread.start();
    }

    public static synchronized ServiceManager getInstance() {
        checkInstanceIsAllowed();
        if (null == sServiceManager) {
            sServiceManager = new ServiceManager();
        }
        return sServiceManager;
    }

    public static synchronized ServiceManager createInstance(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Invalid context argument");
        }
        setGetInstanceIsAllowed();
        sContext = context;
        return getInstance();
    }

    static void setGetInstanceIsAllowed() {
        sGetInstanceAllowed = true;
    }

    private static void checkInstanceIsAllowed() {
        if (!sGetInstanceAllowed) {
            throw new IllegalStateException(
                    "ServiceManager::createInstance() needs to be called "
                            + "before ServiceManager::getInstance()");
        }
    }

    private static final class ServiceManagerThread extends HandlerThread {

        public ServiceManagerThread() {
            super(sServiceManagerThreadName);
        }

        protected void onLooperPrepared() {
            Log.i("ServiceManager", "onLooperPrepared()");
            ServiceData.createInstance(sContext);
            ServiceVoice.createInstance(sContext);
            ServiceFont.createInstance(sContext);
        }
    }

    public Service getService(int type, Context context) {
        Service ret;

        switch (type) {
            case SERVICE_DATA:
                ret = ServiceData.getInstance();
                break;
            case SERVICE_FONT:
                ret = ServiceFont.getInstance();
                break;
            case SERVICE_VOICE:
                ret = ServiceVoice.getInstance();
                break;
            default:
                ret = null;
        }
        return ret;
    }

    public Looper getLooper() {
        return mSMThread.getLooper();
    }


}
