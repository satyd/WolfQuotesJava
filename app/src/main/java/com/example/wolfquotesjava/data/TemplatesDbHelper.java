package com.example.wolfquotesjava.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.wolfquotesjava.data.TemplatesContract.Templates;

public class TemplatesDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = TemplatesDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "templates.db";

    private static final int DATABASE_VERSION = 1;

    public TemplatesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Строка для создания таблицы
        String SQL_CREATE_Templates_TABLE = "CREATE TABLE " + Templates.TABLE_NAME + " ("
                + Templates._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Templates.COLUMN_NUMBER + " INTEGER UNIQUE NOT NULL , "
                + Templates.COLUMN_WEIGHT + " INTEGER NOT NULL);";

        //System.out.println(SQL_CREATE_WORDS_TABLE);
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_Templates_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
