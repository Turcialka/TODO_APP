package com.example.todo_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

class ToDoBD extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "ToDo.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "tasks";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "task_title";
    private static final String COLUMN_DATE = "task_date";
    private static final String COLUMN_CATEGORY = "task_category";

    public long getFeed() {
        return feed;
    }

    public void setFeed(long feed) {
        this.feed = feed;
    }

    private long feed;

    public ToDoBD(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COLUMN_TITLE + " TEXT, "
                        + COLUMN_DATE + " DATE, "
                        + COLUMN_CATEGORY + " TEXT);";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public void addTask(String title, String date, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_DATE, String.valueOf(date));
        cv.put(COLUMN_CATEGORY, category);
        long result = db.insert(TABLE_NAME, null, cv);
        setFeed(result);

    }

    public Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
}
