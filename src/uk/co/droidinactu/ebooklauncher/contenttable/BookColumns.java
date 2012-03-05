package uk.co.droidinactu.ebooklauncher.contenttable;

public abstract interface BookColumns extends PeriodicalColumns {
    public static final String AUTHOR = "author";
    public static final String AUTHOR_KEY = "author_key";
    public static final String BOOK_STATE = "book_state";
    public static final String BOOK_STATE_DOWNLOAD = "download";
    public static final String BOOK_STATE_NEW = "new";
    public static final String BOOK_STATE_NORMAL = "normal";
    public static final String BORROWED = "borrowed";
    public static final String CORRUPTED = "corrupted";
    public static final String EXPIRATION_DATE = "expiration_date";
    public static final String KANA_AUTHOR = "kana_author";
    public static final String KANA_TITLE = "kana_title";
    public static final String PURCHASED_DATE = "purchased_date";
    public static final String READING_TIME = "reading_time";
    public static final String SONY_ID = "sony_id";
    public static final String THUMBNAIL = "thumbnail";
    public static final String TITLE = "title";
    public static final String TITLE_KEY = "title_key";
    public static final String TYPE_AUTHOR = "TEXT";
    public static final String TYPE_AUTHOR_KEY = "TEXT";
    public static final String TYPE_BOOK_STATE = "TEXT";
    public static final String TYPE_BORROWED = "INTEGER";
    public static final String TYPE_CORRUPTED = "INTEGER";
    public static final String TYPE_EXPIRATION_DATE = "INTEGER";
    public static final String TYPE_ID = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final String TYPE_KANA_AUTHOR = "TEXT";
    public static final String TYPE_KANA_TITLE = "TEXT";
    public static final String TYPE_PURCHASED_DATE = "INTEGER";
    public static final String TYPE_READING_TIME = "INTEGER";
    public static final String TYPE_SONY_ID = "TEXT";
    public static final String TYPE_THUMBNAIL = "TEXT";
    public static final String TYPE_TITLE = "TEXT";
    public static final String TYPE_TITLE_KEY = "TEXT";
}