package cn.edu.ustc.arvin.growth.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import cn.edu.ustc.arvin.growth.R;
import cn.edu.ustc.arvin.growth.model.Filter;
import cn.edu.ustc.arvin.growth.model.Han;
import cn.edu.ustc.arvin.growth.model.Lesson;
import cn.edu.ustc.arvin.growth.view.ViewListener;
import cn.edu.ustc.arvin.growth.window.LessonAdapter;
import cn.edu.ustc.arvin.growth.window.WordWindow;
import io.realm.Realm;
import io.realm.RealmResults;

public class LessonActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "LessonActivity";

    private static final int LESSON_STATUS_NORMAL = 0;
    private static final int LESSON_STATUS_IN_EDIT = 1;

    private LessonAdapter mAdapter = null;
    private GridView mGridView = null;
    private TextView mTVLesson=null;
    private TextView mTVBook= null;

    private ImageView mImageViewRefresh = null;
    private ImageView mImageViewEdit = null;
    private ImageView mImageViewSearch = null;

    private View mContentView = null;
    private Realm mRealm;
    private Lesson mCurrentLesson;
    private RealmResults<Han> mHans;
    private RealmResults<Lesson> mLessons;
    private int mCount;
    private int mCurrent;
    private int mCurrentMode;
    private int mInEdit = LESSON_STATUS_NORMAL;

    private void lesson_init() {
        mContentView.setKeepScreenOn(true);

        mTVBook = (TextView)mContentView.findViewById(R.id.book_name);
        mTVLesson = (TextView)mContentView.findViewById(R.id.lesson_name);

        mImageViewRefresh = (ImageView) mContentView.findViewById(R.id.lesson_refresh);
        mImageViewEdit = (ImageView)mContentView.findViewById(R.id.lesson_edit);
        mImageViewSearch = (ImageView)mContentView.findViewById(R.id.lesson_search);

        MyClickListener mcl = new MyClickListener();

        mImageViewRefresh.setOnClickListener(mcl);
        mImageViewEdit.setOnClickListener(mcl);
        mImageViewSearch.setOnClickListener(mcl);
        ((ImageView)mContentView.findViewById(R.id.return_back)).setOnClickListener(mcl);

        mCurrentMode = Filter.FILTER_NEW;
        mRealm = Realm.getDefaultInstance();

        mGridView = (GridView) mContentView.findViewById(R.id.lesson_list);
        mAdapter = new LessonAdapter(this);
        mAdapter.notifyDataSetChanged();
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(LessonActivity.this);
        mGridView.setFocusable(true);

        lesson_load();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentView = LayoutInflater.from(this).inflate(
                R.layout.activity_lesson, null);
        this.setContentView(mContentView);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        lesson_init();
    }

    private int getIndexByOffset(int offset) {
        if (0 == mCount)
            return -1;
        else
            return (mCurrent+mCount+offset) % mCount;
    }

    private void lesson_load() {
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
        lesson_data_load(0);
    }

    private void lesson_data_load(int offset) {
        if (mLessons.size() > 0) {
            mCurrent = getIndexByOffset(offset);
            mCurrentLesson = mLessons.get(mCurrent);

            if(mCurrentLesson != null) {
                mHans = mRealm.where(Han.class).equalTo("lesson.ID", mCurrentLesson.getID()).findAll();
                mAdapter.setData(mHans);
                mAdapter.notifyDataSetInvalidated();

                mTVBook.setText(mCurrentLesson.getBook().getName());
                mTVLesson.setText(mCurrentLesson.getName());
            }
        } else {
            mAdapter.setData(null);
            mAdapter.notifyDataSetChanged();
            mTVBook.setText("-");
            mTVLesson.setText("-");
        }
    }

    private void lesson_data_refresh() {
        if (mLessons.size() > 0) {
            if(mCurrentLesson != null) {
                mRealm.beginTransaction();
                mCurrentLesson.setProgress(mHans.sum("progress").intValue() / mHans.size());
                mRealm.copyToRealmOrUpdate(mCurrentLesson);
                mRealm.commitTransaction();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Han h = (Han)mAdapter.getItem(position);

        Log.i(TAG, "Click han item: " + h + ", id=" + id);

        if (1 == mInEdit) {
            View root = mContentView.getRootView();
            WordWindow ww = new WordWindow(this, h, root.getWidth(), root.getHeight());
            ww.show(root);
        }
    }

    private class MyClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (R.id.return_back == v.getId()) {
                Log.i(TAG, "Click go back: ");
                finish();
            } else if(R.id.lesson_refresh == v.getId()) {
                Log.i(TAG, "Click refresh: ");
                lesson_data_refresh();
                lesson_load();
                mAdapter.notifyDataSetInvalidated();
                mContentView.invalidate();
            } else if(R.id.lesson_edit == v.getId()) {
                Log.i(TAG, "Click edit: ");
                if (LESSON_STATUS_NORMAL == mInEdit) {
                    mImageViewEdit.setImageResource(R.mipmap.edit_ok);
                    mInEdit = LESSON_STATUS_IN_EDIT;
                } else {
                    mImageViewEdit.setImageResource(R.mipmap.edit);
                    mInEdit = LESSON_STATUS_NORMAL;
                    mAdapter.notifyDataSetInvalidated();
                }
                mContentView.invalidate();
            } else if(R.id.lesson_search == v.getId()) {
                Log.i(TAG, "Click search: ");

            }
        }
    }
}
