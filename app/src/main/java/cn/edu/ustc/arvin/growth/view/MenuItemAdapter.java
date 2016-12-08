package cn.edu.ustc.arvin.growth.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.edu.ustc.arvin.growth.R;
import cn.edu.ustc.arvin.growth.model.MenuItem;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by amazing on 2016/4/8.
 */
public class MenuItemAdapter extends BaseAdapter {
    private static final String TAG = "MenuItemAdapter";
    private static final String ASSET_PATH = "file:///android_asset/";
    private LayoutInflater inflater;
    private List<MenuItem> mMenuItems = null;

    public MenuItemAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<MenuItem> menus) {
        this.mMenuItems = menus;
    }

    @Override
    public int getCount() {
        if (mMenuItems == null) {
            return 0;
        }
        return mMenuItems.size();
    }

    @Override
    public Object getItem(int position) {
        if (mMenuItems == null || mMenuItems.get(position) == null) {
            return null;
        }
        return mMenuItems.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private int getRscId(String icon) {
        int rscId = R.mipmap.book;
        Field filed;
        if (icon != null) {
            try {
                R.mipmap tmp = new R.mipmap();
                filed = R.mipmap.class.getField(icon);
                rscId = filed.getInt(tmp);
            } catch (NoSuchFieldException e) {
                Log.i(TAG, "No Such Field in R.mipmap: " + icon);
            } catch (IllegalAccessException e) {
                Log.i(TAG, "Illegal Access to R.mipmap: " + icon);
            }

        }

        return rscId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.menu_item, parent, false);
        }

        MenuItem menuItem = mMenuItems.get(position);

        if (menuItem != null) {
            ((ImageView) convertView.findViewById(R.id.menu_icon)).setImageResource(getRscId(menuItem.getIcon()));
            ((TextView) convertView.findViewById(R.id.menu_name)).setText(menuItem.getName());
        }

        return convertView;
    }
}
