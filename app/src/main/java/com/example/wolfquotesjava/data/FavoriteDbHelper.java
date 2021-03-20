package com.example.wolfquotesjava.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.wolfquotesjava.data.FavoriteContract.Favorite;

public class FavoriteDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = FavoriteDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "favorite.db";

    private static final int DATABASE_VERSION = 1;

    public FavoriteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Строка для создания таблицы
        String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + Favorite.TABLE_NAME + " ("
                + Favorite._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Favorite.COLUMN_VALUE + " TEXT UNIQUE NOT NULL , "
                + Favorite.COLUMN_DATE + " TEXT NOT NULL);";

        //System.out.println(SQL_CREATE_WORDS_TABLE);
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
