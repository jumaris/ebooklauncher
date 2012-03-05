package uk.co.droidinactu.ebooklauncher.contenttable;

import android.provider.BaseColumns;

public abstract interface CollectionsColumns extends BaseColumns {
    public static final String ADDED_ORDER = "added_order";
    public static final String COLLECTION_ID = "collection_id";
    public static final String CONTENT_ID = "content_id";
    public static final String TYPE_ADDED_ORDER = "INTEGER";
    public static final String TYPE_COLLECTION_ID = "INTEGER";
    public static final String TYPE_CONTENT_ID = "INTEGER";
    public static final String TYPE_ID = "INTEGER PRIMARY KEY AUTOINCREMENT";
}