package com.amazing.growth.model;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.amazing.growth.view.MenuItemAdapter;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;

/**
 * Created by amazing on 2016/4/9.
 */
public class Menu {
    protected Context mContext = null;
    protected MenuItemAdapter mAdapter;
    private String mFile = null;
    protected GridView mGridView = null;

    private Menu() {
    }

    public Menu(Context context, GridView gridView, AdapterView.OnItemClickListener listener) {
        mContext = context;
        mGridView = gridView;
        mAdapter = new MenuItemAdapter(context);

        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(listener);
        mAdapter.notifyDataSetChanged();
    }

    public MenuItemAdapter load(String json) {
        List<MenuItem> menuItems = loadJson(json);
        MenuItemAdapter mia = (MenuItemAdapter)mAdapter;
        mia.setData(menuItems);

        return mAdapter;
    }

    private List<MenuItem> loadJson(String file) {
        InputStream stream;
        try {
            stream = mContext.getAssets().open(file);
        } catch (IOException e) {
            return null;
        }

        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();

        JsonElement json = new JsonParser().parse(new InputStreamReader(stream));
        List<MenuItem> menuItems = gson.fromJson(json, new TypeToken<List<MenuItem>>() {
        }.getType());

        return new ArrayList<MenuItem>(menuItems);
    }
}
