package cn.edu.ustc.arvin.growth.window;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.edu.ustc.arvin.growth.R;
import com.amazing.growth.activity.ChineseActivity;
import cn.edu.ustc.arvin.growth.model.Han;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by amazing on 2016/4/10.
 */
public class StudyAdapter extends BaseAdapter {
    private static final String TAG = "StudyAdapter";
    private static final String ASSET_PATH = "file:///android_asset/";
    private Context mContext = null;
    private LayoutInflater inflater;
    private Typeface mTypefaceKaiti = null;
    private Typeface mTypefaceXiaozhuan = null;
    private List<Han> mHans = null;

    public StudyAdapter(Context context) {
        mContext = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (context instanceof ChineseActivity) {
            mTypefaceKaiti = ((ChineseActivity) context).getTypefaceByType("楷体");
            mTypefaceXiaozhuan = ((ChineseActivity) context).getTypefaceByType("小篆");
        }
    }

    public void setData(List<Han> hans) {
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
            convertView = inflater.inflate(R.layout.study_listitem, parent, false);
        }

        Han han = mHans.get(position);

        if (han != null) {
            ((TextView) convertView.findViewById(R.id.study_han_text)).setText(han.getText());
            ((TextView) convertView.findViewById(R.id.study_han_text)).setTypeface(mTypefaceKaiti);

            ((TextView) convertView.findViewById(R.id.study_han_index)).setText(new Integer(position+1).toString());
            ((TextView) convertView.findViewById(R.id.study_han_count)).setText(new Integer(mHans.size()).toString());
        }

        return convertView;
    }
}
