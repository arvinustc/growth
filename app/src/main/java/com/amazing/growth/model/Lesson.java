package com.amazing.growth.model;

import android.util.Log;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by amazing on 2016/4/10.
 */
public class Lesson extends RealmObject {
    private static final String TAG = "Lesson";
    private Book book;
    @PrimaryKey
    private long ID;
    private String lesson;
    private String strings;
    private String icon;
    private int progress;
    private int count;
    private int ready;
    private RealmList<Han> hans = new RealmList<Han>();

    public RealmList<Han> loadHan() {
        int count = strings.length();
        char text;
        int i;
        for (i = 0; i < count; i++) {
            text = strings.charAt(i);

            Han h = new Han();
            h.setText(text);
            h.setUTF8((long) text);
            h.setCreated(new Date(System.currentTimeMillis()));

            hans.add(h);
        }

        return hans;
    }

    public void syncHan() {
        for (Han h : hans) {
            if (null == h.getLesson()) {
                h.setLesson(Lesson.this);
            }

            if (null == h.getBook()) {
                h.setBook(Lesson.this.getBook());
            }
        }
        this.count = hans.size();
        this.ready = 0;
    }

    public int getReady() {
        return ready;
    }

    public void setReady(int ready) {
        this.ready = ready;
    }

    public Boolean isReady(int increase) {
        this.ready += increase;
        if (0 != this.count) {
            this.progress = (this.ready*100) / this.count;
            Log.i(TAG, "isReady " + ready + "/" + count + ", progress=" + progress);
        }

        return (this.ready == this.count);
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

    public RealmList<Han> getHans() {
        return hans;
    }

    public void setHans(RealmList<Han> hans) {
        this.hans = hans;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public String getStrings() {
        return strings;
    }

    public void setStrings(String strings) {
        this.strings = strings;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "lesson='" + lesson + '\'' +
                ", strings='" + strings + '\'' +
                ", ready[" + ready + "/" + count + "]" +
                '}';
    }
}
