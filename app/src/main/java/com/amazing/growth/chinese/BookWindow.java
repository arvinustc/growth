package com.amazing.growth.chinese;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.amazing.growth.R;
import com.amazing.growth.model.Book;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by amazing on 2016/4/10.
 */
public class BookWindow extends PopupWindow implements AdapterView.OnItemClickListener {
    private static final String TAG = "BookWindow";
    private BookAdapter mAdapter = null;
    private GridView mGridView = null;
    private View mContentView = null;
    private Context mContext = null;
    private Realm mRealm;

    public BookWindow(Context context, String json, int width, int height) {
        mContentView = LayoutInflater.from(context).inflate(
                R.layout.book_list, null);
        this.setContentView(mContentView);

        mContext = context;
        mGridView = (GridView) mContentView.findViewById(R.id.book_list);
        mAdapter = new BookAdapter(context);
        mAdapter.notifyDataSetChanged();
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(BookWindow.this);

        mRealm = Realm.getDefaultInstance();
        RealmResults<Book> books = mRealm.where(Book.class).findAll();
        books.sort("ISBN", Sort.ASCENDING);
        mAdapter.setData(books);

        this.setWidth(width);
        this.setHeight(height/2);

        this.setFocusable(true);
        this.setOutsideTouchable(true);

        ColorDrawable dw = new ColorDrawable(0xFF999999);
        this.setBackgroundDrawable(dw);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Book book = (Book)mAdapter.getItem(position);

        Log.i(TAG, "Click book item: " + book + ", id=" + id);

        BookWindow.this.dismiss();
    }
}
