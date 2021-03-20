package com.example.wolfquotesjava.data;

import android.provider.BaseColumns;

public class HistoryContract {

    private HistoryContract() {
    };

    public static final class History implements BaseColumns {
        public final static String TABLE_NAME = "history";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_VALUE = "value";
        public final static String COLUMN_DATE = "date";


    }
}
