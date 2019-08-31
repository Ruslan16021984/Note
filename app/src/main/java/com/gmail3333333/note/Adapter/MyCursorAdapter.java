package com.gmail3333333.note.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.gmail3333333.note.MainActivity;
import com.gmail3333333.note.Model.Note;
import com.gmail3333333.note.R;
import com.gmail3333333.note.database.SQLiteConnector;

public class MyCursorAdapter extends CursorAdapter {
    private MainActivity mainActivity;
    private LayoutInflater layoutInflater;
    public MyCursorAdapter(MainActivity context, Cursor c, int flags) {
        super(context, c, flags);
        this.mainActivity = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return layoutInflater.inflate(R.layout.item_lst, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String title = cursor.getString(cursor.getColumnIndex(SQLiteConnector.TITLE));
        String date = cursor.getString(cursor.getColumnIndex(SQLiteConnector.DATE));
        Note note = new Note(title, date);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvDate = view.findViewById(R.id.tvDate);
        tvDate.setText(note.getDate());
        tvTitle.setText(note.getNote());
        ConstraintLayout constraintLayout = view.findViewById(R.id.cLayout);
        constraintLayout.setOnClickListener(e -> {

            mainActivity.onListItemPtessed(note, cursor);
        });

    }
}
