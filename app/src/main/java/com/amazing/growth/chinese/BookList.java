package com.amazing.growth.chinese;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.GridView;

import com.amazing.growth.model.Book;
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

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by amazing on 2016/4/11.
 */
public class BookList {
    protected Context mContext = null;
    protected BookAdapter mAdapter;
    protected GridView mGridView = null;
    private Realm mRealm;

    public BookList(Context context, GridView gridView, AdapterView.OnItemClickListener listener) {
        mContext = context;
        mGridView = gridView;
        mAdapter = new BookAdapter(context);

        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(listener);
        mAdapter.notifyDataSetChanged();
        mRealm = Realm.getDefaultInstance();
    }
    }
