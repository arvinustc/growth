package cn.edu.ustc.arvin.growth.window;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.edu.ustc.arvin.growth.R;
import cn.edu.ustc.arvin.growth.model.Book;
import cn.edu.ustc.arvin.growth.model.Han;
import cn.edu.ustc.arvin.growth.model.Lesson;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by amazing on 2016/4/10.
 */
public class OverviewWindow extends PopupWindow implements View.OnClickListener {
    private static final String TAG = "OverviewWindow";
    private View mContentView = null;
    private TextView mTextViewBook;
    private TextView mTextViewLesson;
    private TextView mTextViewHan;
    private ProgressBar mProgressBarBook = null;
    private ProgressBar mProgressBarLesson = null;
    private ProgressBar mProgressBarHan = null;
    private TextView mTextViewBookProgress;
    private TextView mTextViewLessonProgress;
    private TextView mTextViewHanProgress;
    private Context mContext = null;
    private Realm mRealm;
    private OverviewInfo mOverviewInfo = new OverviewInfo();

    public OverviewWindow(Context context,String json, int width, int height) {
        mContentView = LayoutInflater.from(context).inflate(
                R.layout.overview, null);
        this.setContentView(mContentView);


        mContentView.setOnClickListener(OverviewWindow.this);
        mTextViewBook = (TextView) mContentView.findViewById(R.id.id_book_info);
        mTextViewLesson = (TextView) mContentView.findViewById(R.id.id_lesson_info);
        mTextViewHan = (TextView) mContentView.findViewById(R.id.id_han_info);

        mProgressBarBook = (ProgressBar) mContentView.findViewById(R.id.book_progress);
        mProgressBarLesson = (ProgressBar) mContentView.findViewById(R.id.lesson_progress);
        mProgressBarHan = (ProgressBar) mContentView.findViewById(R.id.han_progress);

        mTextViewBookProgress  = (TextView) mContentView.findViewById(R.id.book_progress_num);
        mTextViewLessonProgress  = (TextView) mContentView.findViewById(R.id.lesson_progress_num);
        mTextViewHanProgress = (TextView) mContentView.findViewById(R.id.han_progress_num);

        mRealm = Realm.getDefaultInstance();

        mOverviewInfo.sync();

        if (0 <  mOverviewInfo.getBookAll()) {
            mTextViewBook.setText(mOverviewInfo.getBookDone() + " / " + mOverviewInfo.getBookAll());
            mProgressBarBook.setProgress((int) (100 * mOverviewInfo.getBookDone() / mOverviewInfo.getBookAll()));
            mTextViewBookProgress.setText((int) (100 * mOverviewInfo.getBookDone() / mOverviewInfo.getBookAll()) + "%");
        }

        if (0 <  mOverviewInfo.getLessonAll()) {
            mTextViewLesson.setText(mOverviewInfo.getLessonDone() + " / " + mOverviewInfo.getLessonAll());
            mProgressBarLesson.setProgress((int) (100 * mOverviewInfo.getLessonDone() / mOverviewInfo.getLessonAll()));
            mTextViewLessonProgress.setText((int) (100*mOverviewInfo.getLessonDone()/mOverviewInfo.getLessonAll()) + "%");
        }

        if (0 <  mOverviewInfo.getHanAll()) {
            mTextViewHan.setText(mOverviewInfo.getHanDone() + " / " + mOverviewInfo.getHanAll());
            mProgressBarHan.setProgress((int) (100 * mOverviewInfo.getHanDone() / mOverviewInfo.getHanAll()));
            mTextViewHanProgress.setText((int) (100*mOverviewInfo.getHanDone() / mOverviewInfo.getHanAll()) + "%");
        }

        this.setWidth(width/2);
        this.setHeight(height);

        this.setFocusable(true);
        this.setOutsideTouchable(true);

        ColorDrawable dw = new ColorDrawable(0xFF999999);
        this.setBackgroundDrawable(dw);
    }

    private class OverviewInfo {
        private long book_all;
        private long book_done;
        private long lesson_all;
        private long lesson_done;
        private long han_all;
        private long han_done;

        public long getBookAll(Boolean sync) {
            if (true == sync) {
                RealmResults<Book> books = mRealm.where(Book.class).findAll();
                book_all = books.size();
            }
            return book_all;
        }

        public void setBook_all(long book_all) {
            this.book_all = book_all;
        }

        public long getBookDone(Boolean sync) {
            if (true == sync) {
                RealmResults<Book> books = mRealm.where(Book.class).equalTo("progress", 100).findAll();
                book_done = books.size();
            }
            return book_done;
        }

        public void setBook_done(long book_done) {
            this.book_done = book_done;
        }

        public long getLessonAll(Boolean sync) {
            if (true == sync) {
                RealmResults<Lesson> lessons = mRealm.where(Lesson.class).findAll();
                lesson_all = lessons.size();
            }
            return lesson_all;
        }

        public void setLesson_all(long lesson_all) {
            this.lesson_all = lesson_all;
        }

        public long getLessonDone(Boolean sync) {
            if (true == sync) {
                RealmResults<Lesson> lessons = mRealm.where(Lesson.class).equalTo("progress", 100).findAll();
                lesson_done = lessons.size();
            }
            return lesson_done;
        }

        public void setLesson_done(long lesson_done) {
            this.lesson_done = lesson_done;
        }

        public long getHanAll(Boolean sync) {
            if (true == sync) {
                RealmResults<Han> hans = mRealm.where(Han.class).findAll();
                han_all = hans.size();
            }
            return han_all;
        }

        public void setHan_all(long han_all) {
            this.han_all = han_all;
        }

        public long getHanDone(Boolean sync) {
            if (true == sync) {
                RealmResults<Han> hans = mRealm.where(Han.class).equalTo("progress", 100).findAll();
                han_done = hans.size();
            }
            return han_done;
        }

        public void setHan_done(long han_done) {
            this.han_done = han_done;
        }

        public long getBookAll() {
            return book_all;
        }

        public long getBookDone() {
            return book_done;
        }

        public long getLessonAll() {
            return lesson_all;
        }

        public long getLessonDone() {
            return lesson_done;
        }

        public long getHanAll() {
            return han_all;
        }

        public long getHanDone() {
            return han_done;
        }

        public void sync() {
            this.getBookAll(true);
            this.getBookDone(true);

            this.getLessonAll(true);
            this.getLessonDone(true);

            this.getHanAll(true);
            this.getHanDone(true);
        }
    }

    @Override
    public void dismiss() {
        mRealm.close();
        super.dismiss();
    }

    public void show(View parent) {
        if (!this.isShowing()) {
            this.showAtLocation(parent, Gravity.CENTER, 0, 0);
        } else {
            this.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        OverviewWindow.this.dismiss();
    }
}
