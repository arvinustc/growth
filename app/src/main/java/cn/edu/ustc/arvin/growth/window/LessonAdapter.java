package cn.edu.ustc.arvin.growth.window;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.edu.ustc.arvin.growth.R;
import cn.edu.ustc.arvin.growth.model.Lesson;

import java.lang.reflect.Field;
import java.util.List;

import io.realm.RealmResults;

/**
 * Created by amazing on 2016/4/10.
 */
public class LessonAdapter extends BaseAdapter {
    private static final String TAG = "LessonAdapter";
    private static final String ASSET_PATH = "file:///android_asset/";
    private LayoutInflater inflater;
    private List<Lesson> mLessons = null;
    private ProgressBar mProgrssBar;

    public LessonAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(RealmResults<Lesson> lessons) {
        this.mLessons = lessons;
    }

    @Override
    public int getCount() {
        if (mLessons == null) {
            return 0;
        }
        return mLessons.size();
    }

    @Override
    public Object getItem(int position) {
        if (mLessons == null || mLessons.get(position) == null) {
            return null;
        }
        return mLessons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private int getRscId(String icon) {
        int rscId = R.mipmap.book;
        Field filed;
        if (icon != null) {
            try {
                R.mipmap tmp = new R.mipmap();
                filed = R.mipmap.class.getField(icon);
                rscId = filed.getInt(tmp);
            } catch (NoSuchFieldException e) {
                Log.i(TAG, "No Such Field in R.mipmap: " + icon);
            } catch (IllegalAccessException e) {
                Log.i(TAG, "Illegal Access to R.mipmap: " + icon);
            }

        }

        return rscId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.lesson_listitem, parent, false);
        }

        Lesson lesson = mLessons.get(position);

        if (lesson != null) {
            ((ImageView) convertView.findViewById(R.id.lesson_icon)).setImageResource(getRscId(lesson.getIcon()));
            ((TextView) convertView.findViewById(R.id.lesson_name)).setText(lesson.getLesson());
            ((TextView) convertView.findViewById(R.id.book_name)).setText(lesson.getBook().getName());
            ((ProgressBar)convertView.findViewById(R.id.lesson_progress)).setProgress(lesson.getProgress());
            ((TextView) convertView.findViewById(R.id.lesson_progress_num)).setText(String.valueOf(lesson.getProgress())+"%");
        }

        return convertView;
    }
}
