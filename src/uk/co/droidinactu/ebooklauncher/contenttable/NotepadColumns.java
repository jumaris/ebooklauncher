package uk.co.droidinactu.ebooklauncher.contenttable;

public abstract interface NotepadColumns extends ContentColumns {
    public static final String CREATED_DATE = "created_date";
    public static final String THUMBNAIL = "thumbnail";
    public static final String TITLE = "title";
    public static final String TYPE_CREATED_DATE = "INTEGER";
    public static final String TYPE_ID = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final String TYPE_THUMBNAIL = "TEXT";
    public static final String TYPE_TITLE = "TEXT";
}