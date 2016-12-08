package cn.edu.ustc.arvin.growth.service;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

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

import cn.edu.ustc.arvin.growth.model.Book;
import cn.edu.ustc.arvin.growth.model.Han;
import cn.edu.ustc.arvin.growth.model.Lesson;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by amazing on 2016/10/20.
 */
public class ServiceData extends Service{
    private static String TAG = "ServiceData";
    private static Context sContext = null;
    private static ServiceData sServiceData = null;

    private Realm mRealm;
    private ServiceDataHandler mHandler;

    // Create Instance of ServiceData
    public static synchronized ServiceData createInstance(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Invalid context argument");
        }
        sContext = context;
        return getInstance();
    }

    // Get Instance of ServiceData
    public static synchronized ServiceData getInstance() {
        if (null == sServiceData) {
            sServiceData = new ServiceData(sContext);
        }
        return sServiceData;
    }

    private ServiceData(Context context) {
        super();
        Log.i(TAG, "new ServiceData");
        // REALM initialization
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(context).build();
        // Clear the realm from last time
        //Realm.deleteRealm(realmConfig);

        Realm.setDefaultConfiguration(realmConfig);
        mRealm = Realm.getDefaultInstance();

        mHandler = new ServiceDataHandler(getLooper(), context, mRealm);
    }

    protected void finalize() {
        mRealm.close();
    }

    @Override
    public Result process(Request r) {
        Message message;
        Result ret = new Result(Result.CODE_NO_ERROR);
        if (Request.LOAD == r.getAction()) {
            Log.i(TAG, "add " + r.getData());
            message = buildMessage(ServiceDataHandler.LOAD, r.getData());
            mHandler.sendMessage(message);
        } else if (Request.INCREASE == r.getAction()) {
            Log.i(TAG, "increase " + r.getData());
            message = buildMessage(ServiceDataHandler.INCREASE, r.getData());
            mHandler.sendMessage(message);
        } else if (Request.DECREASE == r.getAction()) {
            Log.i(TAG, "decrease " + r.getData());
            message = buildMessage(ServiceDataHandler.DECREASE, r.getData());
            mHandler.sendMessage(message);
        } else if (Request.FAVORITE == r.getAction()) {
            Log.i(TAG, "favorite " + r.getData());
            message = buildMessage(ServiceDataHandler.FAVORITE, r.getData(), r.getGroup());
            mHandler.sendMessage(message);
        } else if (Request.UNFAVORITE == r.getAction()) {
            Log.i(TAG, "unfavorite " + r.getData());
            message = buildMessage(ServiceDataHandler.UNFAVORITE, r.getData());
            mHandler.sendMessage(message);
        } else if (Request.FILTER == r.getAction()) {
            Log.i(TAG, "filter " + r.getData());
            message = buildMessage(ServiceDataHandler.FILTER, r.getData());
            mHandler.sendMessage(message);
        }

        return ret;
    }

    private static Message buildMessage(int action, String data) {
        return buildMessage(action, data, "");
    }

    private static Message buildMessage(int action, String data, String group) {
        Bundle bundle = new Bundle(3);
        bundle.putInt(ServiceDataHandler.ACTION, action);
        bundle.putString(ServiceDataHandler.DATA, data);
        bundle.putString(ServiceDataHandler.GROUP, group);
        Message message = new Message();
        message.setData(bundle);
        return message;
    }

    private static final class ServiceDataHandler extends Handler {
        private static String TAG = "ServiceDataHandler";
        private Context mContext;

        public static final int LOAD = 1;

        public static final int INCREASE = 3;
        public static final int DECREASE = 4;
        public static final int FAVORITE = 5;
        public static final int UNFAVORITE = 6;
        public static final int FILTER = 7;

        public static final String ACTION = "action";
        public static final String DATA = "json";
        public static final String GROUP = "type";

        private Realm mRealm;

        ServiceDataHandler(Looper looper, Context context, Realm realm) {
            super(looper);
            mContext = context;
            mRealm = realm;
            Log.i(TAG, "new ServiceDataHandler");
        }

        @Override
        public void handleMessage(Message msg) {
            final Bundle bundle = msg.getData();

            final int action = bundle.getInt(ACTION);
            final String data = bundle.getString(DATA);
            final String group = bundle.getString(GROUP);

            switch (action) {
                case LOAD:
                    load(data);
                    break;
                case INCREASE:
                    increase(data);
                    break;
                case DECREASE:
                    decrease(data);
                    break;
                case FAVORITE:
                    favorite(data, group);
                    break;
                case UNFAVORITE:
                    unfavorite(data);
                    break;
            }
        }

        private void increase(String text) {
            Han h = mRealm.where(Han.class).equalTo("text", text).findFirst();

            mRealm.beginTransaction();
            h.increase();
            if (h.getGood() == 5) {
                if (h.getLesson().isReady(1)) {
                    h.getBook().isReady(1);
                }
            }
            mRealm.copyToRealmOrUpdate(h);
            mRealm.commitTransaction();
            Log.i(TAG, "increase " + h);
        }

        private void decrease(String text) {

            Han h = mRealm.where(Han.class).equalTo("text", text).findFirst();

            mRealm.beginTransaction();
            h.decrease();
            mRealm.copyToRealmOrUpdate(h);
            mRealm.commitTransaction();
            Log.i(TAG, "decrease " + h);
        }

        private void favorite(String text, String group) {

            Han h = mRealm.where(Han.class).equalTo("text", text).findFirst();

            mRealm.beginTransaction();
            h.favorite(group);
            mRealm.copyToRealmOrUpdate(h);
            mRealm.commitTransaction();
            Log.i(TAG, "favorite " + h + " into group " + group);
        }

        private void unfavorite(String text) {

            Han h = mRealm.where(Han.class).equalTo("text", text).findFirst();

            mRealm.beginTransaction();
            h.unfavorite();
            mRealm.copyToRealmOrUpdate(h);
            mRealm.commitTransaction();
            Log.i(TAG, "unfavorite " + h);
        }

        private void syncLesson() {

        }

        private void load(String json) {
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
}
