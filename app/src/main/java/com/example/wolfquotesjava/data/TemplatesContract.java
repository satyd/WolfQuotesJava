package com.example.wolfquotesjava.data;

import android.provider.BaseColumns;

public class TemplatesContract {

    private TemplatesContract() {
    };

    public static final class Templates implements BaseColumns {
        public final static String TABLE_NAME = "templates";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NUMBER = "number";
        public final static String COLUMN_WEIGHT = "weight";


    }
}
