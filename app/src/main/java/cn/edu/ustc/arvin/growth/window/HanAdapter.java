package cn.edu.ustc.arvin.growth.window;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.edu.ustc.arvin.growth.R;
import cn.edu.ustc.arvin.growth.model.Han;

import java.util.List;

/**
 * Created by amazing on 2016/4/10.
 */
public class HanAdapter extends BaseAdapter {
    private static final String TAG = "HanAdapter";
    private static final String ASSET_PATH = "file:///android_asset/";
    private LayoutInflater inflater;
    private List<Han> hans = null;

    public HanAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<Han> hans) {
        this.hans = hans;
    }

    @Override
    public int getCount() {
        if (hans == null) {
            return 0;
        }
        return hans.size();
    }

    @Override
    public Object getItem(int position) {
        if (hans == null || hans.get(position) == null) {
            return null;
        }
        return hans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private int getRscId(int progress) {
        int rscId = R.mipmap.progress_0;

        if (progress >= 100) {
            rscId = R.mipmap.progress_4;
        } else if (progress >= 75) {
            rscId = R.mipmap.progress_3;
        } else if (progress >= 50) {
            rscId = R.mipmap.progress_2;
        } else if (progress >= 25) {
            rscId = R.mipmap.progress_1;
        } else if (progress >= 0) {
            rscId = R.mipmap.progress_0;
        }

        return rscId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.han_listitem, parent, false);
        }

        Han han = hans.get(position);

        if (han != null) {
            ((ImageView) convertView.findViewById(R.id.han_status)).setImageResource(getRscId(han.getProgress()));
            ((TextView) convertView.findViewById(R.id.han_text)).setText(han.getText());
        }

        return convertView;
    }
}
