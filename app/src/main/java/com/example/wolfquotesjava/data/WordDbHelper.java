package com.example.wolfquotesjava.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.wolfquotesjava.data.WordsContract.WordStorage;

public class WordDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = WordDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "words.db";

    private static final int DATABASE_VERSION = 1;

    public WordDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Строка для создания таблицы
        String SQL_CREATE_WORDS_TABLE = "CREATE TABLE IF NOT EXISTS " + WordStorage.TABLE_NAME + " ("
                + WordStorage._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WordStorage.COLUMN_VALUE + " TEXT NOT NULL, "
                + WordStorage.COLUMN_TYPE + " TEXT NOT NULL, "
                + WordStorage.COLUMN_WEIGHT + " INTEGER NOT NULL DEFAULT 5, "

                + WordStorage.COLUMN_IS_SAFE + " INTEGER DEFAULT 1);";
        //System.out.println(SQL_CREATE_WORDS_TABLE);
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_WORDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
