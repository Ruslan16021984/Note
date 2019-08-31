package com.gmail3333333.note.Model;

import android.database.Cursor;
import android.view.View;

public interface ItemActionHandler<T> {
    void onListItemPtessed(T t, Cursor cursor);
}
