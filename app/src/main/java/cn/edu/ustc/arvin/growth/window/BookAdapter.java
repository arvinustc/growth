package cn.edu.ustc.arvin.growth.window;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.edu.ustc.arvin.growth.R;
import cn.edu.ustc.arvin.growth.model.Book;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by amazing on 2016/4/10.
 */
public class BookAdapter extends BaseAdapter {
    private static final String TAG = "MenuItemAdapter";
    private static final String ASSET_PATH = "file:///android_asset/";
    private LayoutInflater inflater;
    private List<Book> books = null;

    public BookAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<Book> books) {
        this.books = books;
    }

    @Override
    public int getCount() {
        if (books == null) {
            return 0;
        }
        return books.size();
    }

    @Override
    public Object getItem(int position) {
        if (books == null || books.get(position) == null) {
            return null;
        }
        return books.get(position);
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
            convertView = inflater.inflate(R.layout.book_listitem, parent, false);
        }

        Book book = books.get(position);

        if (book != null) {
            ((ImageView) convertView.findViewById(R.id.lesson_icon)).setImageResource(getRscId(book.getIcon()));
            ((TextView) convertView.findViewById(R.id.han_name)).setText(book.getName());
        }

        return convertView;
    }
}
