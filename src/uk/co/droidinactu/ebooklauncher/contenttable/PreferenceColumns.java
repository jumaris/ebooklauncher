package uk.co.droidinactu.ebooklauncher.contenttable;

import android.provider.BaseColumns;

public abstract interface PreferenceColumns extends BaseColumns {
    public static final String BINDING_DIRECTION = "binding_direction";
    public static final String BRIGHTNESS = "brightness";
    public static final String CONTENT_ID = "content_id";
    public static final String CONTRAST = "contrast";
    public static final String SHOW_NOTES = "show_notes";
    public static final String TONE_CURVE_TYPE = "tone_curve_type";
    public static final String TYPE_BINDING_DIRECTION = "TEXT";
    public static final String TYPE_BRIGHTNESS = "INTEGER";
    public static final String TYPE_CONTENT_ID = "INTEGER NOT NULL";
    public static final String TYPE_CONTRAST = "INTEGER";
    public static final String TYPE_ID = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final String TYPE_SHOW_NOTES = "INTEGER";
    public static final String TYPE_TONE_CURVE_TYPE = "TEXT";
}