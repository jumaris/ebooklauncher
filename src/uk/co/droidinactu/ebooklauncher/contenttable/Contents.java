package uk.co.droidinactu.ebooklauncher.contenttable;

import android.net.Uri;
import android.provider.BaseColumns;

public class Contents {
    public static final class Book implements BookColumns {
        public static final class CurrentPosition implements CurrentPositionColumns {
            public static final Uri CONTENT_URI = Uri.parse("content://com.sony.drbd.ebook.provider/books/position");
        }

        public static final class History implements HistoryColumns {
            public static final Uri CONTENT_URI = Uri.parse("content://com.sony.drbd.ebook.provider/books/history");
        }

        public static final class LayoutCache implements LayoutCacheColumns {
            public static final Uri CONTENT_URI = Uri.parse("content://com.sony.drbd.ebook.provider/books/layoutcache");
        }

        public static final class Preference implements PreferenceColumns {
            public static final Uri CONTENT_URI = Uri.parse("content://com.sony.drbd.ebook.provider/books/preference");
        }

        public static final Uri ALL_BOOKS_URI;
        public static final Uri BOOKS_HISTORY_URI;
        public static final Uri BOOKS_LAYOUTCACHE_URI;
        public static final Uri BOOKS_POSITION_URI;
        public static final Uri BOOKS_PREFERENCE_URI;

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.sony.drbd.ebook.book";

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.sony.drbd.ebook.book";

        public static final Uri CONTENT_URI = Uri.parse("content://com.sony.drbd.ebook.provider/books");

        public static final String DEFAULT_SORT_ORDER = "title ASC";

        static {
            ALL_BOOKS_URI = CONTENT_URI;
            BOOKS_PREFERENCE_URI = Preference.CONTENT_URI;
            BOOKS_POSITION_URI = CurrentPosition.CONTENT_URI;
            BOOKS_LAYOUTCACHE_URI = LayoutCache.CONTENT_URI;
            BOOKS_HISTORY_URI = History.CONTENT_URI;
        }
    }

    public static class Collection implements CollectionColumns {
        public static final class Collections implements CollectionsColumns {
            public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.sony.drbd.ebook.collections";
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.sony.drbd.ebook.collections";
            public static final Uri CONTENT_URI = Uri.parse("content://com.sony.drbd.ebook.provider/collections");
        }

        public static final Uri ALL_COLLECTIONS_URI;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.sony.drbd.ebook.collection";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.sony.drbd.ebook.collection";

        public static final Uri CONTENT_URI = Uri.parse("content://com.sony.drbd.ebook.provider/collection");

        static {
            ALL_COLLECTIONS_URI = Collections.CONTENT_URI;
        }
    }

    public static class DeletedFile implements ContentColumns {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.sony.drbd.ebook.deleted";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.sony.drbd.ebook.deleted";
        public static final Uri CONTENT_URI = Uri.parse("content://com.sony.drbd.ebook.provider.deleted");
        public static final Uri EXTERNAL_CONTENT_URI;
        public static final Uri INTERNAL_CONTENT_URI = Uri
                .parse("content://com.sony.drbd.ebook.provider.deleted/internal");

        static {
            EXTERNAL_CONTENT_URI = Uri.parse("content://com.sony.drbd.ebook.provider.deleted/external");
        }
    }

    public static final class Dictionary implements BaseColumns {
        public static final class DicHistories implements DicHistoriesColumns {
            public static final Uri CONTENT_URI = Uri
                    .parse("content://com.sony.drbd.ebook.provider/dictionary/histories");
        }
    }

    public static class Markups implements MarkupsColumns {
        public static final class Annotation implements AnnotationColumns {
            public static final Uri CONTENT_URI = Uri.parse("content://com.sony.drbd.ebook.provider/books/annotation");
            public static final int MARKUPTYPE_NOTE_HANDWRITING = 12;
            public static final int MARKUPTYPE_NOTE_NONE = 10;
            public static final int MARKUPTYPE_NOTE_TEXT = 11;
        }

        public static final class Bookmark implements BookmarkColumns {
            public static final Uri CONTENT_URI = Uri.parse("content://com.sony.drbd.ebook.provider/books/bookmark");
            public static final int MARKUPTYPE_NOTE_HANDWRITING = 2;
            public static final int MARKUPTYPE_NOTE_NONE = 0;
            public static final int MARKUPTYPE_NOTE_TEXT = 1;
        }

        public static final class Freehand implements FreehandColumns {
            public static final Uri CONTENT_URI = Uri.parse("content://com.sony.drbd.ebook.provider/books/freehand");
            public static final int MARKUPTYPE_NOTE_NONE = 20;
        }

        public static final Uri ALL_MARKUPS_URI;

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.sony.drbd.ebook.markup";

        public static final Uri CONTENT_URI = Uri.parse("content://com.sony.drbd.ebook.provider/markups");

        static {
            ALL_MARKUPS_URI = CONTENT_URI;
        }
    }

    public static final class Notepad implements NotepadColumns {
        public static final class Handwriting implements NotepadColumns {
            public static final String CONTENT_ITEM_TYPE = "application/x-sony-notepad-svg";
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/x-sony-notepad-svg";
            public static final Uri CONTENT_URI = Uri.parse("content://com.sony.drbd.ebook.provider/handwriting");
        }

        public static final class TextMemo implements NotepadColumns {
            public static final String CONTENT_ITEM_TYPE = "application/x-sony-notepad-text";
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/x-sony-notepad-text";
            public static final Uri CONTENT_URI = Uri.parse("content://com.sony.drbd.ebook.provider/textmemo");
        }

        public static final Uri ALL_HANDWRITINGS_URI;
        public static final Uri ALL_TEXTMEMOS_URI;

        public static final Uri CONTENT_URI = Uri.parse("content://com.sony.drbd.ebook.provider/notepad");

        public static final String DEFAULT_SORT_ORDER = "created_date ASC";

        static {
            ALL_TEXTMEMOS_URI = TextMemo.CONTENT_URI;
            ALL_HANDWRITINGS_URI = Handwriting.CONTENT_URI;
        }
    }

    public static class Periodical implements PeriodicalColumns {
        public static final class Periodicals implements PeriodicalsColumns {
            public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.sony.drbd.ebook.periodicals";
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.sony.drbd.ebook.periodicals";
            public static final Uri CONTENT_URI = Uri.parse("content://com.sony.drbd.ebook.provider/periodicals");
        }

        public static final Uri ALL_PERIODICALS_URI;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.sony.drbd.ebook.periodical";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.sony.drbd.ebook.periodical";

        public static final Uri CONTENT_URI = Uri.parse("content://com.sony.drbd.ebook.provider/periodical");

        static {
            ALL_PERIODICALS_URI = CONTENT_URI;
        }
    }

    public static class StandbyPicture implements StandbyPictureColumns {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.sony.drbd.ebook.standby.picture";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.sony.drbd.ebook.standby.picture";
        public static final Uri CONTENT_URI = Uri.parse("content://com.sony.drbd.ebook.provider.standby/pictures");
    }

    public static final String AUTHORITY = "com.sony.drbd.ebook.provider";

    public static final String DELETED_AUTHORITY = "com.sony.drbd.ebook.provider.deleted";

    public static final String STANDBY_AUTHORITY = "com.sony.drbd.ebook.provider.standby";
}