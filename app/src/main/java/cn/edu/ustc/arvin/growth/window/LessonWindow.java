package cn.edu.ustc.arvin.growth.window;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import cn.edu.ustc.arvin.growth.R;
import cn.edu.ustc.arvin.growth.model.Lesson;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by amazing on 2016/4/10.
 */
public class LessonWindow extends PopupWindow implements AdapterView.OnItemClickListener {
    private static final String TAG = "LessonWindow";
    private LessonAdapter mAdapter = null;
    private GridView mGridView = null;
    private View mContentView = null;
    private ProgressBar mProgressBar = null;
    private Context mContext = null;
    private Realm mRealm;

    public LessonWindow(Context context, String json, int width, int height) {
        mContentView = LayoutInflater.from(context).inflate(
                R.layout.lesson_list, null);
        this.setContentView(mContentView);

        mContext = context;
        mGridView = (GridView) mContentView.findViewById(R.id.lesson_list);
        mProgressBar = (ProgressBar)mContentView.findViewById(R.id.lesson_progress);
        mAdapter = new LessonAdapter(context);
        mAdapter.notifyDataSetChanged();
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(LessonWindow.this);

        mRealm = Realm.getDefaultInstance();
        RealmResults<Lesson> lessons = mRealm.where(Lesson.class).findAll();
        lessons.sort("ID", Sort.ASCENDING);
        mAdapter.setData(lessons);

        this.setWidth(width);
        this.setHeight(height/2);

        this.setFocusable(true);
        this.setOutsideTouchable(true);

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
        Lesson lesson = (Lesson)mAdapter.getItem(position);

        Log.i(TAG, "Click lesson item: " + lesson + ", id=" + id);

        LessonWindow.this.dismiss();
    }

}
