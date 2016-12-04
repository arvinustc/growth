package cn.edu.ustc.arvin.growth.window;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.GridView;

import io.realm.Realm;

/**
 * Created by amazing on 2016/4/11.
 */
public class BookList {
    protected Context mContext = null;
    protected BookAdapter mAdapter;
    protected GridView mGridView = null;
    private Realm mRealm;

    public BookList(Context context, GridView gridView, AdapterView.OnItemClickListener listener) {
        mContext = context;
        mGridView = gridView;
        mAdapter = new BookAdapter(context);

        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(listener);
        mAdapter.notifyDataSetChanged();
        mRealm = Realm.getDefaultInstance();
    }
    }
