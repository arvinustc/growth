package cn.edu.ustc.arvin.growth.window;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.edu.ustc.arvin.growth.R;
import cn.edu.ustc.arvin.growth.model.Han;

import java.lang.reflect.Field;
import java.util.List;

import cn.edu.ustc.arvin.growth.service.Request;
import cn.edu.ustc.arvin.growth.service.Result;
import cn.edu.ustc.arvin.growth.service.Service;
import cn.edu.ustc.arvin.growth.service.ServiceManager;
import io.realm.RealmResults;

/**
 * Created by amazing on 2016/4/10.
 */
public class LessonAdapter extends BaseAdapter {
    private static final String TAG = "LessonAdapter";
    private static final String ASSET_PATH = "file:///android_asset/";
    private LayoutInflater inflater;
    private List<Han> mHans = null;
    private ProgressBar mProgrssBar;
    private Typeface mTypefaceKaiti = null;
    private Typeface mTypefaceXiaozhuan = null;

    public LessonAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Service sf = ServiceManager.getInstance().getService(ServiceManager.SERVICE_FONT, context);
        Result ret = sf.process(new Request(Request.FETCH, "楷体"));
        mTypefaceKaiti = (Typeface) ret.getReply();
        ret = sf.process(new Request(Request.FETCH, "小篆"));
        mTypefaceXiaozhuan = (Typeface) ret.getReply();
    }

    public void setData(RealmResults<Han> hans) {
        this.mHans = hans;
    }

    @Override
    public int getCount() {
        if (mHans == null) {
            return 0;
        }
        return mHans.size();
    }

    @Override
    public Object getItem(int position) {
        if (mHans == null || mHans.get(position) == null) {
            return null;
        }
        return mHans.get(position);
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

        Han h = mHans.get(position);

        if (h != null) {
            ((TextView) convertView.findViewById(R.id.han_name)).setText(h.getText());
            ((TextView) convertView.findViewById(R.id.han_name)).setTypeface(mTypefaceKaiti);
            ((ProgressBar)convertView.findViewById(R.id.han_progress)).setProgress(h.getProgress());
            ((TextView) convertView.findViewById(R.id.han_progress_num)).setText(String.valueOf(h.getProgress())+"%");
        }

        return convertView;
    }
}
