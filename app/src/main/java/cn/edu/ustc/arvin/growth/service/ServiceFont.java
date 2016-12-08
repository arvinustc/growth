package cn.edu.ustc.arvin.growth.service;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by amazing on 2016/10/20.
 */
public class ServiceFont extends Service {
    private static String TAG = "ServiceFont";
    private static Context sContext = null;
    private static ServiceFont sServiceFont = null;
    private HashMap<String, Typeface> mTypefaces = new HashMap<String, Typeface>(5);

    public static synchronized ServiceFont createInstance(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Invalid context argument");
        }
        sContext = context;
        return getInstance();
    }

    public static synchronized ServiceFont getInstance() {
        if (null == sServiceFont) {
            sServiceFont = new ServiceFont(sContext);
        }
        return sServiceFont;
    }

    private ServiceFont(Context context) {
        super();
        Log.i(TAG, "new ServiceFont");

        Typeface tf = Typeface.createFromAsset(context.getAssets(), "font/simkai.ttf");
        mTypefaces.put("楷体", tf);

        tf = Typeface.createFromAsset(context.getAssets(), "font/xiaozhuanti.ttf");
        mTypefaces.put("小篆", tf);
    }

    public Typeface getTypefaceByType(String type) {
        return mTypefaces.get(type);
    }

    public Result process(Request r) {
        Result ret = new Result(Result.CODE_NO_ERROR);
        if (Request.FETCH == r.getAction()) {
            ret.setReply(mTypefaces.get(r.getData()));
        }
        return ret;
    }
}
