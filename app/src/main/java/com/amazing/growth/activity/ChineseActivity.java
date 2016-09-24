package com.amazing.growth.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.amazing.growth.R;
import com.amazing.growth.chinese.ChineseThread;
import com.amazing.growth.chinese.ChineseHandler;
import com.amazing.growth.chinese.BookWindow;
import com.amazing.growth.chinese.HanWindow;
import com.amazing.growth.chinese.LessonWindow;
import com.amazing.growth.chinese.OverviewWindow;
import com.amazing.growth.chinese.StudyWindow;
import com.amazing.growth.model.Filter;
import com.amazing.growth.model.Menu;
import com.amazing.growth.model.MenuItem;
import com.amazing.growth.view.MenuItemAdapter;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ChineseActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "ChineseActivity";
    private Menu mMenu = null;
    private MenuItemAdapter mAdapter = null;
    private GridView mGridView = null;

    private ChineseThread mChineseThread;
    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinese);

        // REALM initialization
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        // Clear the realm from last time
        //Realm.deleteRealm(realmConfig);

        Realm.setDefaultConfiguration(realmConfig);
        mRealm = Realm.getDefaultInstance();

        // Start a background thread for long-time tasks, like load typefaces, REALM operations.
        mChineseThread = new ChineseThread(ChineseActivity.this);
        mChineseThread.start();

        // GridView and Menu items
        mGridView = (GridView) findViewById(R.id.chinese_list);
        mMenu = new Menu(this, mGridView, this);
        mAdapter = mMenu.load("json/chinese.json");
        mGridView.setKeepScreenOn(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mChineseThread.mChineseHandler) {
            mChineseThread.mChineseHandler.getLooper().quit();
        }
        mRealm.close(); // Remember to close Realm when done.
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MenuItem menuItem = (MenuItem)mAdapter.getItem(position);

        Log.i(TAG, "Click chinese item: " + menuItem + ", id=" + id);

        if (menuItem.getName().equals("返回")) {
            finish();
        } else if (menuItem.getName().equals("导入")) {
            addBookList(menuItem.getFile());
        } else if (menuItem.getName().equals("课本")) {
            View root = mGridView;//view.getRootView();
            BookWindow cw = new BookWindow(this, menuItem.getFile(), root.getWidth(), root.getHeight() - 30);
            cw.show(root);
        } else if (menuItem.getName().equals("课程")) {
            View root = mGridView;//view.getRootView();
            LessonWindow lw = new LessonWindow(this, menuItem.getFile(), root.getWidth(), root.getHeight() - 30);
            lw.show(root);
        } else if (menuItem.getName().equals("汉字")) {
            View root = mGridView;//view.getRootView();
            HanWindow hw = new HanWindow(this, menuItem.getFile(), root.getWidth(), root.getHeight() - 30);
            hw.show(root);
        } else if (menuItem.getName().equals("学习")) {
            View root = view.getRootView();
            StudyWindow sw = new StudyWindow(this, new Filter(Filter.FILTER_NEW),root.getWidth(), root.getHeight());
            sw.show(root);
        } else if (menuItem.getName().equals("喜爱")) {
            View root = view.getRootView();
            StudyWindow sw = new StudyWindow(this, new Filter("喜爱"), root.getWidth(), root.getHeight());
            sw.show(root);
        } else if (menuItem.getName().equals("统计")) {
            View root = mGridView;//view.getRootView();
            OverviewWindow ow = new OverviewWindow(this, menuItem.getFile(), root.getWidth(), root.getHeight() - 30);
            ow.show(root);
        } else if (menuItem.getName().equals("记录")) {
            View root = view.getRootView();
            StudyWindow sw = new StudyWindow(this, new Filter(Filter.FILTER_DONE),root.getWidth(), root.getHeight());
            sw.show(root);
        } else if (menuItem.getName().equals("设置")) {
            Intent intent = new Intent(ChineseActivity.this, ChineseSettingActivity.class);
            startActivity(intent);
        }
    }

    private static Message buildMessage(int action, String json) {
        return buildMessage(action, json, "");
    }

    private static Message buildMessage(int action, String json, String type) {
        Bundle bundle = new Bundle(3);
        bundle.putInt(ChineseHandler.ACTION, action);
        bundle.putString(ChineseHandler.JSON, json);
        bundle.putString(ChineseHandler.TYPE, type);
        Message message = new Message();
        message.setData(bundle);
        return message;
    }

    private void addBookList(String json) {
        Message message = buildMessage(ChineseHandler.BOOKLIST_ADD, json);
        if (null != mChineseThread.mChineseHandler) {
            mChineseThread.mChineseHandler.sendMessage(message);
        }
    }

    public Typeface getTypefaceByType(String type) {
        return mChineseThread.getTypefaceByType(type);
    }

    public void commentHan(String text, String action, String group) {
        Message msg = null;
        if (action.equalsIgnoreCase("good")) {
            msg = buildMessage(ChineseHandler.HAN_GOOD, text);
        } else if (action.equalsIgnoreCase("bad")) {
            msg = buildMessage(ChineseHandler.HAN_BAD, text);
        } else if (action.equalsIgnoreCase("favorite")) {
            msg = buildMessage(ChineseHandler.HAN_FAVORITE, text, group);
        } else if (action.equalsIgnoreCase("unfavorite")) {
            msg = buildMessage(ChineseHandler.HAN_UNFAVORITE, text);
        }

        if (null != msg &&
                null != mChineseThread.mChineseHandler) {
            mChineseThread.mChineseHandler.sendMessage(msg);
        }
    }

    public void commentHan(String text, String action) {
        commentHan(text, action, "");
    }
}
