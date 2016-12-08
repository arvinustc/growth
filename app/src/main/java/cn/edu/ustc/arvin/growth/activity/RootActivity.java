package cn.edu.ustc.arvin.growth.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import cn.edu.ustc.arvin.growth.R;
import cn.edu.ustc.arvin.growth.model.Filter;
import cn.edu.ustc.arvin.growth.model.Lesson;
import cn.edu.ustc.arvin.growth.service.Request;
import cn.edu.ustc.arvin.growth.service.Service;
import cn.edu.ustc.arvin.growth.service.ServiceManager;

import cn.edu.ustc.arvin.growth.model.Menu;
import cn.edu.ustc.arvin.growth.model.MenuItem;
import cn.edu.ustc.arvin.growth.view.MenuItemAdapter;
import cn.edu.ustc.arvin.growth.window.LessonWindow;
import cn.edu.ustc.arvin.growth.window.OverviewWindow;
import cn.edu.ustc.arvin.growth.window.StudyWindow;

public class RootActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "RootActivity";
    private Menu mMenu = null;
    private MenuItemAdapter mAdapter = null;
    private GridView mGridView = null;
    private ServiceManager mServiceManager = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_root);
        mGridView = (GridView) findViewById(R.id.menuitem_list);
        mMenu = new Menu(this, mGridView, this);
        mAdapter = mMenu.load("window/root.json");
        mGridView.setKeepScreenOn(true);

        mServiceManager = ServiceManager.createInstance(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MenuItem m = (MenuItem)mAdapter.getItem(position);

        Log.i(TAG, "Click menu item: " + m + ", id="+id);

        if (m.getName().equals("退出")) {
            finish();
        } else if (m.getName().equals("导入")) {
            Service s = mServiceManager.getService(ServiceManager.SERVICE_DATA, this);
            s.process(new Request(Request.LOAD, m.getFile()));
        } else if (m.getName().equals("统计")) {
            View root = mGridView;//view.getRootView();
            OverviewWindow ow = new OverviewWindow(this, m.getFile(), root.getWidth(), root.getHeight() - 100);
            ow.show(root);
        } else if (m.getName().equals("课程")) {
            View root = view.getRootView();
            LessonWindow sw = new LessonWindow(this, m.getFile(),root.getWidth(), root.getHeight());
            sw.show(root);
        }  else if (m.getName().equals("复习")) {
            View root = view.getRootView();
            StudyWindow sw = new StudyWindow(this, new Filter(Filter.FILTER_LESSON),root.getWidth(), root.getHeight());
            sw.show(root);
        } else if (m.getName().equals("学习")) {
            Intent intent = new Intent(RootActivity.this, LessonActivity.class);
            startActivity(intent);
        }

        if (m.getFile().equals("null.json")) {
            Log.i(TAG, "null.json file is detected, just ignore it.");
        } else if (m.getFile() != null) {

        }
    }
}
