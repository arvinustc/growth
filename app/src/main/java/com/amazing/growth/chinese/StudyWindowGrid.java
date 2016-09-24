package com.amazing.growth.chinese;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.amazing.growth.R;
import com.amazing.growth.model.Han;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by amazing on 2016/4/10.
 */
public class StudyWindowGrid extends PopupWindow implements AdapterView.OnItemClickListener {
    private static final String TAG = "StudyWindowGrid";
    private StudyAdapter mAdapter = null;
    private GridView mGridView = null;
    private View mContentView = null;
    private Context mContext = null;
    private Realm mRealm;

    public StudyWindowGrid(Context context, String json, int width, int height) {
        mContentView = LayoutInflater.from(context).inflate(
                R.layout.study_list, null);
        this.setContentView(mContentView);

        mContext = context;
        mGridView = (GridView) mContentView.findViewById(R.id.study_list);
        mAdapter = new StudyAdapter(context);
        mAdapter.notifyDataSetChanged();
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(StudyWindowGrid.this);

        mRealm = Realm.getDefaultInstance();
        RealmResults<Han> hans = mRealm.where(Han.class).findAll();
        //hans.sort("UTF8", Sort.ASCENDING);
        mAdapter.setData(hans);

        this.setWidth(width);
        this.setHeight(height-50);

        this.setFocusable(true);
        this.setOutsideTouchable(false);

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
        Han han = (Han)mAdapter.getItem(position);

        Log.i(TAG, "Click study item: " + han + ", id=" + id);

        StudyWindowGrid.this.dismiss();
    }
}
