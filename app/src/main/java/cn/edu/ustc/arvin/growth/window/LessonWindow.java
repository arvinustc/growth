package cn.edu.ustc.arvin.growth.window;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.edu.ustc.arvin.growth.R;
import cn.edu.ustc.arvin.growth.model.Filter;
import cn.edu.ustc.arvin.growth.model.Han;
import cn.edu.ustc.arvin.growth.model.Lesson;

import cn.edu.ustc.arvin.growth.view.ViewListener;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by amazing on 2016/4/10.
 */
public class LessonWindow extends PopupWindow implements AdapterView.OnItemClickListener {
    private static final String TAG = "LessonWindow";
    private LessonAdapter mAdapter = null;
    private GridView mGridView = null;
    private TextView mTVLesson=null;
    private TextView mTVBook= null;
    private TextView mTVOrder= null;
    private ImageView mImageViewMode = null;

    private View mContentView = null;
    private Context mContext = null;
    private Realm mRealm;
    private Lesson mCurrentLesson;
    private RealmResults<Han> mHans;
    private RealmResults<Lesson> mLessons;
    private int mCount;
    private int mCurrent;
    private int mCurrentMode;
    private int mInEdit = 0;

    public LessonWindow(Context context, String json, int width, int height) {
        mContentView = LayoutInflater.from(context).inflate(
                R.layout.lesson_list, null);
        this.setContentView(mContentView);

        mContext = context;
        mGridView = (GridView) mContentView.findViewById(R.id.lesson_list);
        mTVBook = (TextView)mContentView.findViewById(R.id.book_name);
        mTVLesson = (TextView)mContentView.findViewById(R.id.lesson_name);
        mTVOrder = (TextView)mContentView.findViewById(R.id.lesson_order);

        mImageViewMode = (ImageView)mContentView.findViewById(R.id.lesson_mode);

        MyClickListener mcl = new MyClickListener();
        mImageViewMode.setOnClickListener(mcl);

        ((ImageView)mContentView.findViewById(R.id.return_back)).setOnClickListener(mcl);

        mAdapter = new LessonAdapter(context);
        mAdapter.notifyDataSetChanged();
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(LessonWindow.this);

        mCurrentMode = Filter.FILTER_ALL;
        mRealm = Realm.getDefaultInstance();
        loadLesson();

        this.setWidth(width);
        this.setHeight(height);

        this.setFocusable(true);
        this.setOutsideTouchable(false);
        this.setTouchable(true);
        this.setTouchInterceptor(new MyViewListener(context));

        ColorDrawable dw = new ColorDrawable(0xFF999999);
        this.setBackgroundDrawable(dw);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Han h = (Han)mAdapter.getItem(position);

        Log.i(TAG, "Click han item: " + h + ", id=" + id);

        //LessonWindow.this.dismiss();

        if (1 == mInEdit) {
            View root = mContentView.getRootView();
            WordWindow ww = new WordWindow(mContext, h.getText(), this.getWidth(), this.getHeight());
            this.dismiss();
            ww.show(root);
        }
    }

    private int getIndexByOffset(int offset) {
        if (0 == mCount)
            return -1;
        else
            return (mCurrent+mCount+offset) % mCount;
    }

    public void loadLesson() {
        if (Filter.FILTER_ALL == mCurrentMode) {
            mLessons = mRealm.where(Lesson.class).findAll();
            mCount = mLessons.size();
            if (mCurrent > mCount - 1) {
                mCurrent = mCount - 1;
            }
        } else if(Filter.FILTER_DONE == mCurrentMode) {
            mLessons = mRealm.where(Lesson.class).greaterThanOrEqualTo("progress", 100).findAll();
            mCount = mLessons.size();
            if (mCurrent > mCount - 1) {
                mCurrent = mCount - 1;
            }
        } else if(Filter.FILTER_NEW == mCurrentMode) {
            mLessons = mRealm.where(Lesson.class).lessThan("progress", 100).findAll();
            mCount = mLessons.size();
            if (mCurrent > mCount - 1) {
                mCurrent = mCount - 1;
            }
        }
        loadHans(0);
    }

    private void loadHans(int offset) {
        if (mLessons.size() > 0) {
            mCurrent = getIndexByOffset(offset);
            mCurrentLesson = mLessons.get(mCurrent);

            if(mCurrentLesson != null) {
                mHans = mRealm.where(Han.class).equalTo("lesson.ID", mCurrentLesson.getID()).findAll();
                mAdapter.setData(mHans);
                mAdapter.notifyDataSetInvalidated();

                mTVBook.setText(mCurrentLesson.getBook().getName());
                mTVLesson.setText(mCurrentLesson.getName());
                String s = String.format("%03d / %03d", mCurrent+1, mCount);
                mTVOrder.setText(s);
            }
        } else {
            mAdapter.setData(null);
            mAdapter.notifyDataSetChanged();
            mTVBook.setText("-");
            mTVLesson.setText("-");
            mTVOrder.setText("000 / 000");
        }
    }

    private class MyViewListener extends ViewListener {
        public MyViewListener(Context context) {
            super(context);
        }

        @Override
        public boolean turnLeft() {
            Log.i(TAG, "turn left");
            loadHans(1);
            mContentView.invalidate();
            return super.turnLeft();
        }

        @Override
        public boolean turnRight() {
            Log.i(TAG, "turn right");
            loadHans(-1);
            mContentView.invalidate();
            return super.turnRight();
        }
    }

    private class MyClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (R.id.lesson_mode == v.getId()) {
                Log.i(TAG, "Click view mode: ");

                if (Filter.FILTER_ALL == mCurrentMode) {
                    mCurrentMode = Filter.FILTER_DONE;
                    mImageViewMode.setImageResource(R.mipmap.lesson_done);
                } else if (Filter.FILTER_DONE == mCurrentMode) {
                    mCurrentMode = Filter.FILTER_NEW;
                    mImageViewMode.setImageResource(R.mipmap.lesson_todo);
                } else if (Filter.FILTER_NEW == mCurrentMode) {
                    mCurrentMode = Filter.FILTER_ALL;
                    mImageViewMode.setImageResource(R.mipmap.lesson_all);
                }
                loadLesson();
                mContentView.invalidate();
            } else if (R.id.return_back == v.getId()) {
                Log.i(TAG, "Click go back: ");
                LessonWindow.this.dismiss();
            }
        }
    }

}
