package uk.co.droidinactu.ebooklauncher.contenttable;

import android.provider.BaseColumns;

public abstract interface ContentColumns extends BaseColumns {
    public static final String ADDED_DATE = "added_date";
    public static final String FILE_NAME = "file_name";
    public static final String FILE_PATH = "file_path";
    public static final String FILE_SIZE = "file_size";
    public static final String MIME_TYPE = "mime_type";
    public static final String MODIFIED_DATE = "modified_date";
    public static final String PREVENT_DELETE = "prevent_delete";
    public static final String SOURCE_ID = "source_id";
    public static final String TYPE_ADDED_DATE = "INTEGER";
    public static final String TYPE_FILE_NAME = "TEXT";
    public static final String TYPE_FILE_PATH = "TEXT";
    public static final String TYPE_FILE_SIZE = "INTEGER";
    public static final String TYPE_ID = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final String TYPE_MIME_TYPE = "TEXT";
    public static final String TYPE_MODIFIED_DATE = "INTEGER";
    public static final String TYPE_PREVENT_DELETE = "INTEGER";
    public static final String TYPE_SOURCE_ID = "INTEGER";
}