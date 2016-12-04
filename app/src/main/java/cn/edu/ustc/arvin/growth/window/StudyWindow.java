package cn.edu.ustc.arvin.growth.window;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.edu.ustc.arvin.growth.R;
import com.amazing.growth.activity.ChineseActivity;
import com.amazing.growth.model.Filter;
import cn.edu.ustc.arvin.growth.model.Han;
import cn.edu.ustc.arvin.growth.view.ViewListener;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by amazing on 2016/4/10.
 */
public class StudyWindow extends PopupWindow implements View.OnClickListener{
    private static final String TAG = "StudyWindow";
    private HanAdapter mAdapter = null;
    private GridView mGridView = null;
    private View mContentView = null;
    private Context mContext = null;
    private Realm mRealm;
    RealmResults<Han> mHans;
    private Han mHanCurrent;
    private Han mHanPrevious;
    private Han mHanNext;
    private int mCount;
    private int mCurrent;
    private Typeface mTypefaceKaiti = null;
    private Typeface mTypefaceXiaozhuan = null;
    private Boolean mIsKai = true;
    private TextView mTextViewHan = null;
    private TextView mTextViewHanIndex = null;
    private TextView mTextViewHanCount = null;
    private ImageView mImageViewGood = null;
    private ImageView mImageViewBad = null;
    private TextView mTextViewGoodCount = null;
    private TextView mTextViewBadCount = null;
    private ImageView mImageViewFavorivate = null;
    private ImageView mImageViewLeft = null;
    private ImageView mImageViewRight = null;
    private Filter mFilter = null;


    public StudyWindow(Context context, Filter filter, int width, int height) {
        mFilter = filter;

        mContentView = LayoutInflater.from(context).inflate(
                R.layout.study_listitem, null);
        this.setContentView(mContentView);

        mContext = context;

        filterHans();

        if (context instanceof ChineseActivity) {
            mTypefaceKaiti = ((ChineseActivity) context).getTypefaceByType("楷体");
            mTypefaceXiaozhuan = ((ChineseActivity) context).getTypefaceByType("小篆");
        }

        mTextViewHanIndex = (TextView) mContentView.findViewById(R.id.study_han_index);
        mTextViewHanCount = (TextView) mContentView.findViewById(R.id.study_han_count);

        mTextViewGoodCount = (TextView) mContentView.findViewById(R.id.study_good_count);
        mTextViewBadCount = (TextView) mContentView.findViewById(R.id.study_bad_count);

        mImageViewGood = (ImageView) mContentView.findViewById(R.id.study_good);
        mImageViewBad = (ImageView) mContentView.findViewById(R.id.study_bad);
        mImageViewFavorivate = (ImageView) mContentView.findViewById(R.id.study_favorite);
        mImageViewGood.setOnClickListener(this);
        mImageViewBad.setOnClickListener(this);
        mImageViewFavorivate.setOnClickListener(this);

        mImageViewLeft = (ImageView) mContentView.findViewById(R.id.study_left);
        mImageViewRight = (ImageView) mContentView.findViewById(R.id.study_right);
        mImageViewLeft.setOnClickListener(this);
        mImageViewRight.setOnClickListener(this);

        mTextViewHan = (TextView) mContentView.findViewById(R.id.study_han_text);
        mTextViewHan.setTypeface(mIsKai ? mTypefaceKaiti : mTypefaceXiaozhuan);

        setNextHan(0);

        this.setFocusable(true);
        this.setOutsideTouchable(false);
        this.setTouchable(true);
        this.setTouchInterceptor(new MyViewListener(context));

        this.setWidth(width);
        this.setHeight(height);

        ColorDrawable dw = new ColorDrawable(0xFF999999);
        this.setBackgroundDrawable(dw);
    }

    public StudyWindow(Context context, String json, int width, int height) {
        this(context, width, height);
    }

    public StudyWindow(Context context, int width, int height) {
        this(context,  new Filter(), width, height);
    }

    private void filterHans() {
        mRealm = Realm.getDefaultInstance();

        if (mFilter.getID() == Filter.FILTER_ALL) {
            mHans = mRealm.where(Han.class).findAll();
        } else if (mFilter.getID() == Filter.FILTER_FAVORITE) {
            mHans = mRealm.where(Han.class).isNotNull("favorite").findAll();
        } else if (mFilter.getID() == Filter.FILTER_BOOK) {
            mHans = mRealm.where(Han.class).equalTo("book.ISBN", mFilter.getBook().getISBN()).findAll();
        } else if (mFilter.getID() == Filter.FILTER_LESSON) {
            mHans = mRealm.where(Han.class).equalTo("lesson.ID", mFilter.getLesson().getID()).findAll();
        } else if (mFilter.getID() == Filter.FILTER_NEW) {
            mHans = mRealm.where(Han.class).isNull("learned").findAll();
            mHans.sort("bad", Sort.DESCENDING);
        } else if (mFilter.getID() == Filter.FILTER_DONE) {
            mHans = mRealm.where(Han.class).isNotNull("learned").findAll();
            mHans.sort("learned", Sort.DESCENDING);
        }

        mCount = mHans.size();
        mCurrent = 0;
    }

    @Override
    public void dismiss() {
        mRealm.close();
        super.dismiss();
    }

    public void show(View parent) {
        if (!this.isShowing()) {
            this.showAtLocation(parent, Gravity.CENTER, 0, 0);
        } else {
            this.dismiss();
        }
    }

    private void commentHan(String text, String good, String group) {
        if (mContext instanceof ChineseActivity) {
            ((ChineseActivity) mContext).commentHan(text, good, group);
        }
    }

    private void commentHan(String text, String good) {
        if (mContext instanceof ChineseActivity) {
            ((ChineseActivity) mContext).commentHan(text, good);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.study_good) {
            Log.i(TAG, "comment text=" + mHanCurrent.getText() + " good.");
            commentHan(mHanCurrent.getText(), "good");
        } else if (v.getId() == R.id.study_bad) {
            Log.i(TAG, "comment text=" + mHanCurrent.getText() + " bad.");
            commentHan(mHanCurrent.getText(), "bad");
        } else if (v.getId() == R.id.study_favorite) {
            if (mFilter.getID() != Filter.FILTER_FAVORITE) {
                Log.i(TAG, "comment text=" + mHanCurrent.getText() + " favorite.");
                if (null == mHanCurrent.getFavorite()) {
                    commentHan(mHanCurrent.getText(), "favorite", "喜爱");
                    mImageViewFavorivate.setImageResource(R.mipmap.favorite_yes);
                } else {
                    commentHan(mHanCurrent.getText(), "unfavorite");
                    mImageViewFavorivate.setImageResource(R.mipmap.favorite_no);
                }
                v.getRootView().invalidate();
            }
        } else if (v.getId() == R.id.study_left) {
            Log.i(TAG, "goto previous text=" + mHanPrevious.getText());
            setNextHan(-1);
        } else if (v.getId() == R.id.study_right) {
            Log.i(TAG, "goto next text=" + mHanNext.getText());
            setNextHan(1);
        }

        v.getRootView().invalidate();
    }

    public void setNextFont(int offset) {
        mIsKai = ! mIsKai;

        mTextViewHan.setTypeface(mIsKai ? mTypefaceKaiti : mTypefaceXiaozhuan);
    }

    private int getIndexByOffset(int offset) {
        return (mCurrent+mCount+offset) % mCount;
    }

    public void setNextHan(int offset) {
        if (1 == offset) {
            mHanPrevious = mHanCurrent;
            mHanCurrent = mHanNext;
            mHanNext = mHans.get(getIndexByOffset(2));
            mCurrent = getIndexByOffset(1);
        } else if (-1 == offset) {
            mHanNext = mHanCurrent;
            mHanCurrent = mHanPrevious;
            mHanPrevious = mHans.get(getIndexByOffset(-2));
            mCurrent = getIndexByOffset(-1);
        }
        if (mHans.size() > 0) {
            if (null == mHanCurrent) {
                mHanCurrent = mHans.get(mCurrent);
                mHanPrevious = mHans.get(getIndexByOffset(-1));
                mHanNext = mHans.get(getIndexByOffset(1));
            }

            mTextViewHan.setText(mHanCurrent.getText());
            mTextViewHanIndex.setText(new Integer(mCurrent + 1).toString());
            mTextViewHanCount.setText(new Integer(mCount).toString());

            mTextViewGoodCount.setText("("+mHanCurrent.getGood()+")");
            mTextViewBadCount.setText("("+mHanCurrent.getBad()+")");

            if (null == mHanCurrent.getFavorite()
                    || "" == mHanCurrent.getFavorite()) {
                mImageViewFavorivate.setImageResource(R.mipmap.favorite_no);
            } else {
                mImageViewFavorivate.setImageResource(R.mipmap.favorite_yes);
            }
        }
    }

    private class MyViewListener extends ViewListener {
        public MyViewListener(Context context) {
            super(context);
        }

        @Override
        public boolean turnLeft() {
            Log.i(TAG, "turn left");
            setNextHan(1);
            mContentView.invalidate();
            return super.turnLeft();
        }

        @Override
        public boolean turnRight() {
            Log.i(TAG, "turn right");
            setNextHan(-1);
            mContentView.invalidate();
            return super.turnRight();
        }

        @Override
        public boolean turnDown() {
            Log.i(TAG, "turn down");
            setNextFont(-1);
            mContentView.invalidate();
            return super.turnRight();
        }

        @Override
        public boolean turnUp() {
            Log.i(TAG, "turn up");
            setNextFont(1);
            mContentView.invalidate();
            return super.turnRight();
        }
    }
}
