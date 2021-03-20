package com.example.wolfquotesjava.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.wolfquotesjava.data.HistoryContract.History;

public class HistoryDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = HistoryDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "history.db";

    private static final int DATABASE_VERSION = 1;

    public HistoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Строка для создания таблицы
        String SQL_CREATE_HISTORY_TABLE = "CREATE TABLE " + History.TABLE_NAME + " ("
                + History._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + History.COLUMN_VALUE + " TEXT NOT NULL, "
                + History.COLUMN_DATE + " TEXT NOT NULL);";

        //System.out.println(SQL_CREATE_WORDS_TABLE);
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_HISTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
