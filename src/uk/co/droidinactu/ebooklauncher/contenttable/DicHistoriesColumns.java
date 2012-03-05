package uk.co.droidinactu.ebooklauncher.contenttable;

import android.provider.BaseColumns;

public abstract interface DicHistoriesColumns extends BaseColumns {
    public static final String ADDED_DATE = "added_date";
    public static final String CONTENT_ID = "content_id";
    public static final String DIC_CONTENT_ID = "dic_content_id";
    public static final String DIC_CONTENT_NAME = "dic_content_name";
    public static final String DIC_SEARCH_NO = "dic_search_no";
    public static final String DIC_SEARCHWORD = "dic_searchword";
    public static final String TYPE_ADDED_DATE = "TEXT NOT NULL";
    public static final String TYPE_CONTENT_ID = "INTEGER";
    public static final String TYPE_DIC_CONTENT_ID = "INTEGER";
    public static final String TYPE_DIC_CONTENT_NAME = "TEXT NOT NULL";
    public static final String TYPE_DIC_SEARCH_NO = "INTEGER";
    public static final String TYPE_DIC_SEARCHWORD = "TEXT NOT NULL";
    public static final String TYPE_ID = "INTEGER PRIMARY KEY AUTOINCREMENT";
}