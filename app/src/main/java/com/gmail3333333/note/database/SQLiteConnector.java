package com.gmail3333333.note.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteConnector extends SQLiteOpenHelper {
    public static final String TITLE = "title";
    public static final String DATE = "date";
    public static final String DATABASE = "NoteMessage";
    public static final String DATABASE_TABLE = "note";
    private Context context;

    public SQLiteConnector(Context context, String name, int version) {
        super(context, name, null, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE note(_id INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHARE(50), date VARCHARE(50))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {

        } else if (oldVersion == 2 && newVersion == 3) {

        } else if (oldVersion == 3 && newVersion == 4) {

        }
    }

    public int createNewNote(String id, String title, String date) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteConnector.TITLE, title);
        contentValues.put(SQLiteConnector.DATE, date);
        int rowAffected = database.update("users", contentValues, "_id=?", new String[]{id});
        return rowAffected;
    }
    public void deleteNote(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("note", "_id=?", new String[]{id});
        db.close();
    }
    public void update(String id, String title, String date){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteConnector.TITLE, title);
        contentValues.put(SQLiteConnector.DATE, date);
        int rowAffected = database.update("users", contentValues, "_id=?", new String[]{id});

    }
}
