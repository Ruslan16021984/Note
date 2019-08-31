package com.gmail3333333.note;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import com.autofit.et.lib.AutoFitEditText;
import com.gmail3333333.note.Adapter.MyCursorAdapter;
import com.gmail3333333.note.Model.ItemActionHandler;
import com.gmail3333333.note.Model.Note;
import com.gmail3333333.note.database.SQLiteConnector;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ItemActionHandler<Note>, TextWatcher {
    @BindView(R.id.lvNote)
    ListView lvNote;
    @BindView(R.id.etNote)
    AutoFitEditText etNote;
    @BindView(R.id.btnAdd)
    Button btnAdd;
    private ArrayList<Note> noteArrayList;
    private MyCursorAdapter noteAdapter;
    private Cursor cursor;

    private SQLiteConnector sqLiteConnector;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sqLiteConnector = new SQLiteConnector(this, SQLiteConnector.DATABASE, 1);
        database = sqLiteConnector.getWritableDatabase();
        noteArrayList = new ArrayList<>();
        noteAdapter = new MyCursorAdapter(this, cursor, 0);
        lvNote.setAdapter(noteAdapter);
        changeCursor();
        etNote.addTextChangedListener(this);
    }

    @OnClick(R.id.btnAdd)
    public void onClick(View view) {
        Note note = new Note();
        note.setNote(etNote.getText().toString());
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
        String strDate = dateFormat.format(date);
        note.setDate(strDate);
        switch (view.getId()) {
            case R.id.btnAdd: {

                ContentValues contentValues = new ContentValues();
                contentValues.put(SQLiteConnector.TITLE, note.getNote());
                contentValues.put(SQLiteConnector.DATE, note.getDate());
                long rowId = database.insert(SQLiteConnector.DATABASE_TABLE, null, contentValues);
                noteAdapter.changeCursor(cursor);
                changeCursor();
                etNote.setText("");
                Toast.makeText(this, "Have been add " + rowId, Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.globalSearch);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnCloseListener(() -> {
            Log.d("TAG", "onClose");
            lvNote.setAdapter(noteAdapter);
            changeCursor();
            return false;
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                cursor = getNoteListByKeyword(text);
                if (cursor != null) {
                    noteAdapter.swapCursor(cursor);
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.searchDate:
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                    Log.d("Tag", "" + year + month + dayOfMonth);
//                        tvData.setText("" + dayOfMonth +"-"+ (month+1) + "-"+ year);
                }, Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                Log.d("Tag", "Data");
                break;
        }
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (etNote.getText().toString() == null) {
            btnAdd.setEnabled(false);
        } else {
            btnAdd.setEnabled(true);
        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (etNote.getText().toString() == null) {
            btnAdd.setEnabled(false);
        } else {
            btnAdd.setEnabled(true);
        }
    }

    private void changeCursor() {
        cursor = database.rawQuery("SELECT * FROM note", null);
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(SQLiteConnector.TITLE));
            String date = cursor.getString(cursor.getColumnIndex(SQLiteConnector.DATE));
            Note data = new Note(title, date);
            noteArrayList.add(data);
        }
        noteAdapter.changeCursor(cursor);
    }


    @Override
    public void onListItemPtessed(Note note, Cursor cursor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ваша заметка");
        builder.setMessage(note.getNote() + " " + note.getDate());
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", (dialog, which) -> {

        });
        builder.show();
    }

    private Cursor getNoteListByKeyword(String text) {
        SQLiteDatabase db = sqLiteConnector.getReadableDatabase();
        String selectQuery = "SELECT rowid as " + "_id,"+
                SQLiteConnector.TITLE + "," +
                SQLiteConnector.DATE +
                " FROM " + SQLiteConnector.DATABASE_TABLE +
                " WHERE " + SQLiteConnector.TITLE + "  LIKE  '%" + text + "%' ";


        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("TAG", selectQuery +" --------" + cursor.toString());
        // looping through all rows and adding to list
        if (cursor == null) {
            Log.d("TAG", " cursor == null" );
            return null;
        } else if (!cursor.moveToFirst()) {
            Log.d("TAG", "!cursor.moveToFirst()" );
            cursor.close();
            return null;
        }

        return cursor;
    }
}
