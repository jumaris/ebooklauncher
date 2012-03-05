package uk.co.droidinactu.ebooklauncher.contenttable;

public abstract interface FreehandColumns extends MarkupsColumns {
    public static final String SVG_FILE = "svg_file";
    public static final String THUMBNAIL = "thumbnail";
    public static final String TYPE_MARKUP_TYPE = "INTEGER DEFAULT 20";
    public static final String TYPE_SVG_FILE = "TEXT";
    public static final String TYPE_THUMBNAIL = "TEXT";
}