package com.amazing.growth.chinese;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.amazing.growth.model.Book;
import com.amazing.growth.model.Han;
import com.amazing.growth.model.Lesson;
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
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by amazing on 2016/4/10.
 */
public class ChineseHandler extends Handler {
    private static final String TAG = "ChineseHandler";

    public static final int BOOKLIST_ADD = 1;
    public static final int BOOKLIST_DELETE = 2;

    public static final int HAN_GOOD = 3;
    public static final int HAN_BAD = 4;
    public static final int HAN_FAVORITE = 5;
    public static final int HAN_UNFAVORITE = 6;
    public static final int LESSON_SYNC = 7;

    public static final String ACTION = "action";
    public static final String JSON = "json";
    public static final String TYPE = "type";

    public static final String GOOD = "good";
    public static final String BAD = "bad";
    public static final String FAVORITE = "favorite";

    private Realm mRealm;
    private Context mContext;

    public ChineseHandler(Context context, Realm realm) {
        this.mContext = context;
        this.mRealm = realm;
    }

    @Override
    public void handleMessage(Message msg) {
        final Bundle bundle = msg.getData();

        final int action = bundle.getInt(ACTION);
        final String json = bundle.getString(JSON);
        final String type = bundle.getString(TYPE);

        switch (action) {
            case BOOKLIST_ADD:
                addBookList(json);
                break;
            case BOOKLIST_DELETE:

                break;
            case HAN_GOOD:
                goodHan(json);
                break;
            case HAN_BAD:
                badHan(json);
                break;
            case HAN_FAVORITE:
                favoriteHan(json, type);
                break;
            case HAN_UNFAVORITE:
                unfavoriteHan(json);
                break;
            case LESSON_SYNC:
                break;

        }
    }

    private void goodHan(String text) {
        Han h = mRealm.where(Han.class).equalTo("text", text).findFirst();

        mRealm.beginTransaction();
        h.GOOD();
        if (h.getGood() == 5) {
            if (h.getLesson().isReady(1)) {
                h.getBook().isReady(1);
            }
        }
        mRealm.copyToRealmOrUpdate(h);
        mRealm.commitTransaction();
        Log.i(TAG, "good " + h);
    }

    private void badHan(String text) {

        Han h = mRealm.where(Han.class).equalTo("text", text).findFirst();

        mRealm.beginTransaction();
        h.BAD();
        mRealm.copyToRealmOrUpdate(h);
        mRealm.commitTransaction();
        Log.i(TAG, "bad " + h);
    }

    private void favoriteHan(String text, String group) {

        Han h = mRealm.where(Han.class).equalTo("text", text).findFirst();

        mRealm.beginTransaction();
        h.FAVORITE(group);
        mRealm.copyToRealmOrUpdate(h);
        mRealm.commitTransaction();
        Log.i(TAG, "favorite " + h + " into group " + group);
    }

    private void unfavoriteHan(String text) {

        Han h = mRealm.where(Han.class).equalTo("text", text).findFirst();

        mRealm.beginTransaction();
        h.UNFAVORITE();
        mRealm.copyToRealmOrUpdate(h);
        mRealm.commitTransaction();
        Log.i(TAG, "unfavorite " + h);
    }

    private void syncLesson() {

    }

    private void addBookList(String json) {
        List<Book> books = _loadJson(json);
        List<Book> imported = mRealm.where(Book.class).findAll();
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

        for (Book b : newBooks) {
            _addBook(b);
        }

        RealmResults<Book> results = mRealm.where(Book.class).findAll();
        List<String> ab = new ArrayList<String>();

        for (Book b : results) {
            ab.add(b.getName());
        }

        for (String bn : ab) {
            _syncBook(bn);
        }
    }

    private void _addBook(Book book) {
        RealmList<Lesson> lessons = book.loadLesson(mContext);
        book.syncLesson();

        for (Lesson ls: lessons) {
            mRealm.beginTransaction();
            RealmList<Han> hans = ls.loadHan();
            mRealm.copyToRealmOrUpdate(ls);
            mRealm.copyToRealmOrUpdate(hans);
            ls.syncHan();
            mRealm.commitTransaction();
        }

        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(book);
        mRealm.commitTransaction();
        Log.i(TAG, "add book " + book + ".");
    }

    private void _syncBook(String name) {
        Book book =  mRealm.where(Book.class).equalTo("name", name).findFirst();
        RealmList<Lesson> lessons = book.getLessons();
        int lesson_ready = 0;
        for (Lesson ls: lessons) {
            int ready = 0;
            RealmList<Han> hans = ls.getHans();

            for (Han h: hans) {
                if (h.getProgress() >= 100) {
                    ready++;
                }
            }

            mRealm.beginTransaction();
            ls.setReady(ready);
            if(ls.isReady(0)) {
                lesson_ready++;
            }
            mRealm.copyToRealmOrUpdate(ls);
            mRealm.commitTransaction();
        }

        mRealm.beginTransaction();
        book.setReady(lesson_ready);
        book.isReady(0);
        mRealm.copyToRealmOrUpdate(book);
        mRealm.commitTransaction();
        Log.i(TAG, "sync book " + book + ".");
    }


    private List<Book> _loadJson(String file) {
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
