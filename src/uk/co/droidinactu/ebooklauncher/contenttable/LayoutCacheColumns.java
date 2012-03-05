package uk.co.droidinactu.ebooklauncher.contenttable;

import android.provider.BaseColumns;

public abstract interface LayoutCacheColumns extends BaseColumns {
    public static final String CONTENT_ID = "content_id";
    public static final String DPI = "dpi";
    public static final String ENCODING = "encoding";
    public static final String FILE_PATH = "file_path";
    public static final String FONT_SIZE = "font_size";
    public static final String FONT_STYLE = "font_style";
    public static final String HEIGHT = "height";
    public static final String LAYOUT_VERSION = "layout_version";
    public static final String REFLOW = "reflow";
    public static final String STATE = "state";
    public static final String TYPE_CONTENT_ID = "INTEGER";
    public static final String TYPE_DPI = "INTEGER";
    public static final String TYPE_ENCODING = "TEXT";
    public static final String TYPE_FILE_PATH = "TEXT";
    public static final String TYPE_FONT_SIZE = "INTEGER";
    public static final String TYPE_FONT_STYLE = "TEXT";
    public static final String TYPE_HEIGHT = "INTEGER";
    public static final String TYPE_ID = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final String TYPE_LAYOUT_VERSION = "INTERGER";
    public static final String TYPE_REFLOW = "INTEGER";
    public static final String TYPE_STATE = "INTEGER";
    public static final String TYPE_WIDTH = "INTEGER";
    public static final String WIDTH = "width";
}