package com.amazing.growth.view;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.amazing.growth.R;
import com.amazing.growth.model.Menu;

/**
 * Created by amazing on 2016/4/9.
 */
public class PopupMenuView extends PopupWindow {
    private Menu mMenu = null;
    private MenuItemAdapter mAdapter = null;
    private GridView mGridView = null;
    private View mContentView = null;

    public PopupMenuView(Context context, String json, int width, int height) {
        mContentView = LayoutInflater.from(context).inflate(
                R.layout.popup_menu, null);
        this.setContentView(mContentView);

        mGridView = (GridView) mContentView.findViewById(R.id.popup_menu_list);
        mMenu = new Menu(context, mGridView, null);
        mAdapter = mMenu.load(json);

        this.setWidth(width);
        this.setHeight(height/2);

        this.setFocusable(true);
        this.setOutsideTouchable(true);

        ColorDrawable dw = new ColorDrawable(0xFF999999);
        this.setBackgroundDrawable(dw);


    }

    public void show(View parent) {
        if (!this.isShowing()) {
            this.showAtLocation(parent, Gravity.CENTER, 0, 0);
        } else {
            this.dismiss();
        }
    }
}
