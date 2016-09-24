package com.amazing.growth.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.amazing.growth.R;
import com.amazing.growth.chinese.BookImportWindow;
import com.amazing.growth.chinese.HanWindow;
import com.amazing.growth.chinese.LessonWindow;
import com.amazing.growth.model.Menu;
import com.amazing.growth.model.MenuItem;
import com.amazing.growth.view.MenuItemAdapter;
import com.amazing.growth.view.PopupMenuView;

public class ChineseSettingActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "ChineseSettingActivity";
    private Menu mMenu = null;
    private MenuItemAdapter mAdapter = null;
    private GridView mGridView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinese_setting);
        mGridView = (GridView) findViewById(R.id.chinese_learning_list);
        mMenu = new Menu(this, mGridView, this);
        mAdapter = mMenu.load("json/chinese_setting.json");
        mGridView.setKeepScreenOn(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MenuItem menuItem = (MenuItem)mAdapter.getItem(position);

        Log.i(TAG, "Click chinese setting item: " + menuItem + ", id=" + id);

        if (menuItem.getName().equals("返回")) {
            finish();
        } else if (menuItem.getName().equals("课本浏览")) {
            View root = mGridView;//view.getRootView();
            BookImportWindow ciw = new BookImportWindow(this, menuItem.getFile(), root.getWidth(), root.getHeight() - 30);
            ciw.show(root);
        } else if (menuItem.getName().equals("课程浏览")) {
            View root = mGridView;//view.getRootView();
            LessonWindow lw = new LessonWindow(this, menuItem.getFile(), root.getWidth(), root.getHeight() - 30);
            lw.show(root);
        } else if (menuItem.getName().equals("字库浏览")) {
            View root = mGridView;//view.getRootView();
            HanWindow hw = new HanWindow(this, menuItem.getFile(), root.getWidth(), root.getHeight() - 30);
            hw.show(root);
        } else if (menuItem.getFile().isEmpty() ||
                menuItem.getFile().equals("null.json") ) {
            // do nothing
        } else {
            View root = mGridView;//view.getRootView();
            PopupMenuView pmv = new PopupMenuView(this, menuItem.getFile(), root.getWidth() - 30, root.getHeight() - 30);
            pmv.show(root);
        }
    }
}
