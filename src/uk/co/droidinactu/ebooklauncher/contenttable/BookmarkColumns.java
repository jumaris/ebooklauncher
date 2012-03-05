package uk.co.droidinactu.ebooklauncher.contenttable;

public abstract interface BookmarkColumns extends MarkupsColumns {
    public static final String FILE_PATH = "file_path";
    public static final String MIME_TYPE = "mime_type";
    public static final String TYPE_FILE_PATH = "TEXT";
    public static final String TYPE_MARKUP_TYPE = "INTEGER DEFAULT 0";
    public static final String TYPE_MIME_TYPE = "TEXT";
}