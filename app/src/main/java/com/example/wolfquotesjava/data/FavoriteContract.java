package com.example.wolfquotesjava.data;

import android.provider.BaseColumns;

public class FavoriteContract {

    private FavoriteContract() {
    };

    public static final class Favorite implements BaseColumns {
        public final static String TABLE_NAME = "favorite";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_VALUE = "value";
        public final static String COLUMN_DATE = "date";


    }
}
