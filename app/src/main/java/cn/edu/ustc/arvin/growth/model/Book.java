package cn.edu.ustc.arvin.growth.model;

import android.content.Context;
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

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by amazing on 2016/4/10.
 */
public class Book extends RealmObject {
    private static final String TAG = "Book";
    @PrimaryKey
    private long ISBN;
    private String name;
    private String file;
    private String icon;
    private int progress;
    private int ready;
    private int count;
    private RealmList<Lesson> lessons;

    public RealmList<Lesson> loadLesson(Context context) {
        InputStream stream;
        try {
            stream = context.getAssets().open(file);
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
        lessons = gson.fromJson(json, new TypeToken<RealmList<Lesson>>() {
        }.getType());

        return lessons;
    }

    public void syncLesson() {
        for (Lesson lesson : lessons) {
            if (null == lesson.getBook()) {
                lesson.setBook(Book.this);
            }
        }
        this.count = lessons.size();
        this.ready = 0;
    }

    public int getReady() {
        return ready;
    }

    public Boolean isReady(int increase) {
        this.ready += increase;
        if (0 != this.count) {
            this.progress = (this.ready*100) / this.count;
            Log.i(TAG, "isReady " + increase + "/" + count + ", progress=" + progress);
        }

        return (this.ready == this.count);
    }

    public void setReady(int ready) {
        this.ready = ready;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getISBN() {
        return ISBN;
    }

    public void setISBN(long ISBN) {
        this.ISBN = ISBN;
    }

    public RealmList<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(RealmList<Lesson> lessons) {
        this.lessons = lessons;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "Book{" +
                "ISBN=" + ISBN +
                ", name='" + name + '\'' +
                ", file='" + file + '\'' +
                ", progress=" + progress +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        Book b = (Book) o;
        if (b.getISBN() == this.getISBN()) {
            return true;
        } else {
            return false;
        }
    }
}
