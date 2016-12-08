package cn.edu.ustc.arvin.growth.window;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.HashMap;

import cn.edu.ustc.arvin.growth.R;
import cn.edu.ustc.arvin.growth.model.Han;
import cn.edu.ustc.arvin.growth.service.Request;
import cn.edu.ustc.arvin.growth.service.Result;
import cn.edu.ustc.arvin.growth.service.Service;
import cn.edu.ustc.arvin.growth.service.ServiceManager;
import io.realm.Realm;

/**
 * Created by amazing on 2016/11/29.
 */
public class WordWindow extends PopupWindow implements View.OnClickListener{
    private static final String TAG = "WordWindow";
    private View mContentView = null;
    private Context mContext = null;
    private Realm mRealm;
    private TextView mTVLesson=null;
    private TextView mTVBook= null;
    private TextView mTVWord= null;
    private ImageView mImageViewScore_0;
    private ImageView mImageViewScore_1;
    private ImageView mImageViewScore_2;
    private ImageView mImageViewScore_3;
    private ImageView mImageViewScore_4;
    private ImageView mImageViewScore_5;

    private HashMap<Integer, ImageView> mImageViewScores = new HashMap<Integer, ImageView>(8);

    private String mWord;
    private Han mHan;
    private int mGood = -1;

    private Typeface mTypefaceKaiti = null;
    private Typeface mTypefaceXiaozhuan = null;

    private void typeface_init() {
        Service sf = ServiceManager.getInstance().getService(ServiceManager.SERVICE_FONT, mContext);
        Result ret = sf.process(new Request(Request.FETCH, "楷体"));
        mTypefaceKaiti = (Typeface) ret.getReply();
        ret = sf.process(new Request(Request.FETCH, "小篆"));
        mTypefaceXiaozhuan = (Typeface) ret.getReply();
    }

    private void word_init() {
        typeface_init();

        mRealm = Realm.getDefaultInstance();

        mContentView = LayoutInflater.from(mContext).inflate(
                R.layout.word_list, null);
        this.setContentView(mContentView);
        mContentView.setKeepScreenOn(true);

        ((ImageView)mContentView.findViewById(R.id.return_back)).setOnClickListener(this);
        mTVBook = (TextView)mContentView.findViewById(R.id.book_name);
        mTVLesson = (TextView)mContentView.findViewById(R.id.lesson_name);
        mTVWord = (TextView)mContentView.findViewById(R.id.word_text);

        mImageViewScore_0 = (ImageView)mContentView.findViewById(R.id.word_score_0);
        mImageViewScore_1 = (ImageView)mContentView.findViewById(R.id.word_score_1);
        mImageViewScore_2 = (ImageView)mContentView.findViewById(R.id.word_score_2);
        mImageViewScore_3 = (ImageView)mContentView.findViewById(R.id.word_score_3);
        mImageViewScore_4 = (ImageView)mContentView.findViewById(R.id.word_score_4);
        mImageViewScore_5 = (ImageView)mContentView.findViewById(R.id.word_score_5);

        mImageViewScores.put(R.id.word_score_0, mImageViewScore_0);
        mImageViewScores.put(R.id.word_score_1, mImageViewScore_1);
        mImageViewScores.put(R.id.word_score_2, mImageViewScore_2);
        mImageViewScores.put(R.id.word_score_3, mImageViewScore_3);
        mImageViewScores.put(R.id.word_score_4, mImageViewScore_4);
        mImageViewScores.put(R.id.word_score_5, mImageViewScore_5);

        mImageViewScore_0.setOnClickListener(this);
        mImageViewScore_1.setOnClickListener(this);
        mImageViewScore_2.setOnClickListener(this);
        mImageViewScore_3.setOnClickListener(this);
        mImageViewScore_4.setOnClickListener(this);
        mImageViewScore_5.setOnClickListener(this);


        mTVWord.setText(mWord);
        mTVWord.setTypeface(mTypefaceKaiti);

        this.setFocusable(true);
        this.setOutsideTouchable(false);
        this.setTouchable(true);

        ColorDrawable dw = new ColorDrawable(0xFF999999);
        this.setBackgroundDrawable(dw);

        word_load();
    }

    private int word_score_map(int rsc_id) {
        int result = -1;
        int index;

        int score_iv_id [] = {
                R.id.word_score_0,
                R.id.word_score_1,
                R.id.word_score_2,
                R.id.word_score_3,
                R.id.word_score_4,
                R.id.word_score_5};

        for (index = 0; index < 6; index++) {
            if (score_iv_id[index] == rsc_id) {
                result = index;
                break;
            }
        }

        Log.i(TAG, "word score map: " + result);

        return result;
    }

    private void word_score_load(int good, boolean force) {
        int score_rsc_id [] = {
                R.mipmap.num_0,
                R.mipmap.num_1,
                R.mipmap.num_2,
                R.mipmap.num_3,
                R.mipmap.num_4,
                R.mipmap.num_5};

        int score_rsc_id_hi [] = {
                R.mipmap.num_0_hi,
                R.mipmap.num_1_hi,
                R.mipmap.num_2_hi,
                R.mipmap.num_3_hi,
                R.mipmap.num_4_hi,
                R.mipmap.num_5_hi};

        int score_iv_id [] = {
                R.id.word_score_0,
                R.id.word_score_1,
                R.id.word_score_2,
                R.id.word_score_3,
                R.id.word_score_4,
                R.id.word_score_5};

            if(force || mGood != good ) {
                ImageView ivn = mImageViewScores.get(score_iv_id[good]);
                ivn.setImageResource(score_rsc_id_hi[good]);

                if ( mGood != good && mGood != -1) {
                    ImageView ivo = mImageViewScores.get(score_iv_id[mGood]);
                    ivo.setImageResource(score_rsc_id[mGood]);
                }

                if (mGood != good)
                    mGood = good;
            }
    }

    private void word_load() {
        mHan = mRealm.where(Han.class).equalTo("text", mWord).findFirst();
        mTVBook.setText(mHan.getBook().getName());
        mTVLesson.setText(mHan.getLesson().getName());

        word_score_load(mHan.getGood(), true);
    }

    private void word_save() {
        mRealm.beginTransaction();
        if (-1 != mGood) {
            mHan.setGood(mGood);
            mHan.setProgress(mGood*100/5);
        }
        mRealm.copyToRealmOrUpdate(mHan);
        mRealm.commitTransaction();
    }

    private void word_exit() {
        word_save();
        mRealm.close();
    }

    private void word_refresh() {
        mContentView.invalidate();
    }

    public WordWindow(Context context, Han han, int width, int height) {
        mContext = context;
        mHan = han;
        mWord = mHan.getText();

        word_init();

        this.setWidth(width);
        this.setHeight(height);
    }

    public WordWindow(Context context, String word, int width, int height) {
        mContext = context;
        mWord = word;

        word_init();

        this.setWidth(width);
        this.setHeight(height);
    }

    @Override
    public void onClick(View v) {
        if (R.id.return_back == v.getId()) {
            Log.i(TAG, "Click go back: ");
            WordWindow.this.dismiss();
        } else {
            int good = word_score_map(v.getId());

            if (good > -1 && good < 6) {
                word_score_load(good, false);
                mContentView.invalidate();
            }
        }
    }

    @Override
    public void dismiss() {
        word_exit();
        super.dismiss();
    }

    public void show(View parent) {
        Log.i(TAG, "show: parent=" + parent);
        if (!this.isShowing()) {
            this.showAtLocation(parent, Gravity.CENTER, 0, 0);
        } else {
            this.dismiss();
        }
    }
}
