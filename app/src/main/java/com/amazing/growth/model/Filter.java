package com.amazing.growth.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by amazing on 2016/4/19.
 */
public class Filter extends RealmObject {
    @PrimaryKey
    private int ID;
    private Book book;
    private Lesson lesson;
    private String favorite;
    private int order;
    private int count;
    private int index;

    public static int FILTER_ALL = 0x00000001;
    public static int FILTER_FAVORITE = 0x00000002;
    public static int FILTER_BOOK = 0x00000003;
    public static int FILTER_LESSON = 0x00000004;
    public static int FILTER_NEW = 0x00000005;
    public static int FILTER_DONE = 0x00000006;


    public Book getBook() {
        return book;
    }

    public void setBook(Book mBook) {
        this.book = mBook;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson mLesson) {
        this.lesson = mLesson;
    }

    public String getGroup() {
        return favorite;
    }

    public void setGroup(String mGroup) {
        this.favorite = mGroup;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int mOrder) {
        this.order = mOrder;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int mCount) {
        this.count = mCount;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int mIndex) {
        this.index = mIndex;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Filter() {
        ID = FILTER_ALL;
    }

    public Filter (Book book) {
        ID = FILTER_BOOK;
        this.book = book;
    }

    public Filter (Lesson lesson) {
        ID = FILTER_LESSON;
        book = lesson.getBook();
        this.lesson = lesson;
    }

    public Filter (String group) {
        ID = FILTER_FAVORITE;
        favorite = group;
    }

    public Filter (int id) {
        ID = id;
    }
}
