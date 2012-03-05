package uk.co.droidinactu.ebooklauncher.contenttable;

import android.provider.BaseColumns;

public abstract interface CollectionColumns extends BaseColumns {
    public static final String KANA_TITLE = "kana_title";
    public static final String SOURCE_ID = "source_id";
    public static final String TITLE = "title";
    public static final String TYPE_ID = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final String TYPE_KANA_TITLE = "TEXT";
    public static final String TYPE_SOURCE_ID = "INTEGER";
    public static final String TYPE_TITLE = "TEXT";
    public static final String TYPE_UUID = "TEXT";
    public static final String UUID = "uuid";
}