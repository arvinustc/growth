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

import io.realm.RealmObject;

/**
 * Created by amazing on 2016/4/11.
 */
public class BookImportList {
    protected Context mContext = null;
    protected BookAdapter mAdapter;
    private String mFile = null;
    protected GridView mGridView = null;

    public BookImportList(Context context, GridView gridView, AdapterView.OnItemClickListener listener) {
        mContext = context;
        mGridView = gridView;
        mAdapter = new BookAdapter(context);

        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(listener);
        mAdapter.notifyDataSetChanged();
    }

    public BookAdapter load(String json, List<Book> imported) {
        List<Book> books = loadJson(json);
        List<Book> newBooks = new ArrayList<Book>();
        Boolean isNew = true;
        for (Book b: books) {
            isNew = true;
            for (Book i : imported) {
                if (b.equals(i)){
                    isNew = false;
                    break;
                }
            }

            if (true == isNew) {
                newBooks.add(b);
            }
        }
        mAdapter.setData(newBooks);

        return mAdapter;
    }

    private List<Book> loadJson(String file) {
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
        List<Book> books = gson.fromJson(json, new TypeToken<List<Book>>() {
        }.getType());

        return new ArrayList<Book>(books);
    }
}
