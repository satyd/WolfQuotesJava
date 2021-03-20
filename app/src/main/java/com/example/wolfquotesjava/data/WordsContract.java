package com.example.wolfquotesjava.data;

import android.provider.BaseColumns;

public class WordsContract {

    private WordsContract() {
    };

    public static final class WordStorage implements BaseColumns {
        public final static String TABLE_NAME = "words";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_VALUE = "value";
        public final static String COLUMN_TYPE = "type";
        public final static String COLUMN_IS_SAFE= "is_safe";
        public final static String COLUMN_WEIGHT = "weight";

        public static final int NOT_SAFE = 0;
        public static final int IS_SAFE = 1;
    }
}
