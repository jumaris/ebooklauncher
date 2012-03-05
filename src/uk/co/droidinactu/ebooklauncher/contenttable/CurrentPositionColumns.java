package uk.co.droidinactu.ebooklauncher.contenttable;

import android.provider.BaseColumns;

public abstract interface CurrentPositionColumns extends BaseColumns {
    public static final String CONTENT_ID = "content_id";
    public static final String CROP_AREA_BOTTOM = "crop_area_bottom";
    public static final String CROP_AREA_LEFT = "crop_area_left";
    public static final String CROP_AREA_RIGHT = "crop_area_right";
    public static final String CROP_AREA_TOP = "crop_area_top";
    public static final String CROP_MODE = "crop_mode";
    public static final String DPI = "dpi";
    public static final String FONT_SIZE = "font_size";
    public static final String FONT_STYLE = "font_style";
    public static final String HEIGHT = "height";
    public static final String MARK = "mark";
    public static final String ORIENTATION = "orientation";
    public static final String PAGE_STYLE = "page_style";
    public static final String REFLOW = "reflow";
    public static final String SPLIT_INDEX = "split_index";
    public static final String TEXT_ENCODING = "text_encoding";
    public static final String TYPE_CONTENT_ID = "INTEGER";
    public static final String TYPE_CROP_AREA_BOTTOM = "INTEGER";
    public static final String TYPE_CROP_AREA_LEFT = "INTEGER";
    public static final String TYPE_CROP_AREA_RIGHT = "INTEGER";
    public static final String TYPE_CROP_AREA_TOP = "INTEGER";
    public static final String TYPE_CROP_MODE = "INTEGER";
    public static final String TYPE_DPI = "INTEGER";
    public static final String TYPE_FONT_SIZE = "INTEGER";
    public static final String TYPE_FONT_STYLE = "TEXT";
    public static final String TYPE_HEIGHT = "INTEGER";
    public static final String TYPE_ID = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final String TYPE_MARK = "BLOB";
    public static final String TYPE_ORIENTATION = "INTEGER";
    public static final String TYPE_PAGE_STYLE = "INTEGER";
    public static final String TYPE_REFLOW = "INTEGER";
    public static final String TYPE_SPLIT_INDEX = "INTEGER";
    public static final String TYPE_TEXT_ENCODING = "TEXT";
    public static final String TYPE_VERSION = "INTEGER";
    public static final String TYPE_WIDTH = "INTEGER";
    public static final String VERSION = "version";
    public static final String WIDTH = "width";
}