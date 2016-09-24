package com.amazing.growth.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.amazing.growth.model.Menu;

import com.amazing.growth.R;
import com.amazing.growth.model.MenuItem;
import com.amazing.growth.view.MenuItemAdapter;

public class RootActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "RootActivity";
    private Menu mMenu = null;
    private MenuItemAdapter mAdapter = null;
    private GridView mGridView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        mGridView = (GridView) findViewById(R.id.menuitem_list);
        mMenu = new Menu(this, mGridView, this);
        mAdapter = mMenu.load("json/root.json");
        mGridView.setKeepScreenOn(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MenuItem menuItem = (MenuItem)mAdapter.getItem(position);

        Log.i(TAG, "Click menu item: " + menuItem + ", id="+id);

        if (menuItem.getName().equals("退出")) {
            finish();
        } else if (menuItem.getName().equals("语文")) {
            Intent intent = new Intent(RootActivity.this, ChineseActivity.class);
            startActivity(intent);
        }

        if (menuItem.getFile().equals("null.json")) {
            Log.i(TAG, "null.json file is detected, just ignore it.");
        } else if (menuItem.getFile() != null) {

        }
    }
}
