package cn.edu.ustc.arvin.growth.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by amazing on 2016/4/10.
 */
public class Han extends RealmObject {
    private Book book;
    private Lesson lesson;
    @PrimaryKey
    private long UTF8;
    private String text;
    private Date created;
    private Date learned;
    private int progress;
    private int good;
    private int bad;
    private String favorite;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public long getUTF8() {
        return UTF8;
    }

    public void setUTF8(long UTF8) {
        this.UTF8 = UTF8;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setText(char text) {
        char chars[] = {text};
        this.text = new String(chars);
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLearned() {
        return learned;
    }

    public void setLearned(Date learned) {
        this.learned = learned;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getGood() {
        return good;
    }

    public void setGood(int good) {
        this.good = good;
    }

    public int getBad() {
        return bad;
    }

    public void setBad(int bad) {
        this.bad = bad;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return "Han{" +
                "text='" + text + '\'' +
                ", created=" + created +
                ", learned=" + learned +
                ", progress=" + progress +
                ", good=" + good +
                ", bad=" + bad +
                '}';
    }

    public void GOOD() {
        good++;
        if (good <= 5) {
            progress = good * 20;
        }

        if (5 == good) {
            learned = new Date(System.currentTimeMillis());
        } else if (good >= 5 && progress < 100) {
            progress = 100;
            learned = new Date(System.currentTimeMillis());
        }

        if (100 < progress) {
            progress = 100;
        }
    }

    public void BAD() {
        bad++;
    }

    public void FAVORITE(String group) { favorite = group;}

    public void UNFAVORITE() { favorite = null;}
}
