package cn.edu.ustc.arvin.growth.service;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;

/**
 * Created by amazing on 2016/9/29.
 */
public class ServiceVoice {
    private static String TAG = "ServiceVoice";
    private boolean mInit = false;
    private SpeechSynthesizer mTTS = null;

    private SynthesizerListener mSynListener = new SynthesizerListener() {

        public void onCompleted(SpeechError error) {
            Log.i(TAG, "onCompleted");
        }

        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
            Log.i(TAG, "onBufferProgress");
        }

        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            Log.i(TAG, "onSpeakProgress, percent=" + percent);
        }

        public void onSpeakBegin() {
            Log.i(TAG, "onSpeakBegin");
        }

        public void onSpeakResumed() {
            Log.i(TAG, "onSpeakResumed");
        }

        public void onSpeakPaused() {
            Log.i(TAG, "onSpeakPaused");
        }

        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
            Log.i(TAG, "onEvent");
        }

    };

    public ServiceVoice(Context context) {
        Log.i(TAG, "new ServiceVoice");

        SpeechUtility.createUtility(context, SpeechConstant.APPID + "=57df9552");

        mTTS = SpeechSynthesizer.createSynthesizer(context, null);
        mTTS.setParameter(SpeechConstant.VOICE_NAME, "vixf");
        mTTS.setParameter(SpeechConstant.SPEED, "50");
        mTTS.setParameter(SpeechConstant.VOLUME, "80");
        mTTS.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        //mTTS.startSpeaking("汉字启蒙", mSynListener);

        mInit = true;
    }

    public void speak(String text) {
        if (mInit) {
            mTTS.startSpeaking(text, mSynListener);
        }
    }
}
