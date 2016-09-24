package com.amazing.growth.chinese;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.HashMap;

import io.realm.Realm;

/**
 * Created by amazing on 2016/4/10.
 */
public class ChineseThread extends Thread {
    private static final String TAG = "ChineseThread";
    private Context mContext = null;
    private boolean mReady = false;
    public Handler mChineseHandler = null;
    private HashMap<String, Typeface> mTypefaces = new HashMap<String, Typeface>(5);
    private Typeface mTypeFaceKaiti = null;
    private Typeface mTypeFaceXiaoZhuan = null;


    public ChineseThread(Context context) {
        this.mContext = context;
    }

    @Override
    public void run() {
        Realm realm = null;

        this.setName("ChineseThread");
        Log.i(TAG, "ChineseThread is running.");


        try {
            Looper.prepare();
            realm = Realm.getDefaultInstance();
            mChineseHandler = new ChineseHandler(mContext, realm);
            mReady = true;
            // load typeface
            loadTypeface();
            Looper.loop();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    public boolean getReady () {
        return mReady;
    }

    public Typeface getTypefaceByType(String type) {
        return mTypefaces.get(type);
    }

    private void loadTypeface() {
        _addTypeface("font/simkai.ttf", "楷体");
        _addTypeface("font/xiaozhuanti.ttf", "小篆");
    }

    private void _addTypeface(String file, String type) {
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), file);
        mTypefaces.put(type, tf);
        Log.i(TAG, "add typeface " + file + ", type is " + type + ".");
    }

}
