package com.example.amanda.academicdashboard;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Amanda on 5/1/2019.
 */

public class CourseCursorAdapter extends CursorAdapter {
    public CourseCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.listview_item, parent);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String termNameText = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_NAME));
        TextView tvListItem = (TextView)view.findViewById(R.id.tvListViewItem);
        tvListItem.setText(termNameText);

    }
}
