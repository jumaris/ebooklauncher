/*
 * Copyright 2012 Andy Aspell-Clark
 *
 * This file is part of eBookLauncher.
 * 
 * eBookLauncher is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * eBookLauncher is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with eBookLauncher. If not, see http://www.gnu.org/licenses/.
 *
 */
 package uk.co.droidinactu.ebooklauncher.data.calibre.contentprovider;

import uk.co.droidinactu.ebooklauncher.data.calibre.Author;
import uk.co.droidinactu.ebooklauncher.data.calibre.AuthorEbookLink;
import uk.co.droidinactu.ebooklauncher.data.calibre.CalibreBook;
import uk.co.droidinactu.ebooklauncher.data.calibre.Tag;
import uk.co.droidinactu.ebooklauncher.data.calibre.TagEbookLink;
import android.net.Uri;
import android.provider.BaseColumns;

public class CalibreDPData {

    /**
     * AuthorBookLink table contract
     */
    public static final class AuthorBookLink implements BaseColumns {

        /**
         * Path part for the Author URI
         */
        private static final String _PATH_AUTHORBOOKLINKS = "/authorbooklink";

        /**
         * Path part for the Author ID URI
         */
        private static final String _PATH_AUTHORBOOKLINKS_ID = "/authorbooklink/";

        /**
         * 0-relative position of an EBook ID segment in the path part of an
         * Author ID URI
         */
        public static final int AUTHORBOOKLINK_ID_PATH_POSITION = 1;

        /**
         * The content URI base for a single Author. Callers must append a
         * numeric Author id to this Uri to retrieve a single EBook
         */
        public static final Uri CONTENT_ID_URI_BASE = Uri.parse("content://" + AUTHORITY + _PATH_AUTHORBOOKLINKS_ID);

        /**
         * The content URI match pattern for a single Author, specified by its
         * ID. Use this to match incoming URIs or to construct an Intent.
         */
        public static final Uri CONTENT_ID_URI_PATTERN = Uri.parse("content://" + AUTHORITY + _PATH_AUTHORBOOKLINKS_ID
                + "/#");

        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single
         * Author.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/uk.co.droidinactu.inlibrislibertas.contentprovider.authorbooklink";

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of
         * Author.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.eads.minimrms.mobileclient.authorbooklink";

        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + _PATH_AUTHORBOOKLINKS);

        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = AuthorEbookLink.FIELD_NAME_EBOOK_ID + " ASC";

        /**
         * The table name offered by this provider
         */
        public static final String TABLE_NAME = AuthorEbookLink.DATABASE_TABLE_NAME;

        // This class cannot be instantiated
        private AuthorBookLink() {
        }
    }

    /**
     * Author table contract
     */
    public static final class Authors implements BaseColumns {

        /**
         * Path part for the Author URI
         */
        private static final String _PATH_AUTHORS = "/authors";

        /**
         * Path part for the Author ID URI
         */
        private static final String _PATH_AUTHORS_ID = "/authors/";

        /**
         * Path part for the Live Folder URI
         */
        private static final String _PATH_LIVE_FOLDER = "/live_folders/authors";

        /**
         * 0-relative position of an EBook ID segment in the path part of an
         * Author ID URI
         */
        public static final int AUTHORS_ID_PATH_POSITION = 1;

        /**
         * The content URI base for a single Author. Callers must append a
         * numeric Author id to this Uri to retrieve a single EBook
         */
        public static final Uri CONTENT_ID_URI_BASE = Uri.parse("content://" + AUTHORITY + _PATH_AUTHORS_ID);

        /**
         * The content URI match pattern for a single Author, specified by its
         * ID. Use this to match incoming URIs or to construct an Intent.
         */
        public static final Uri CONTENT_ID_URI_PATTERN = Uri.parse("content://" + AUTHORITY + _PATH_AUTHORS_ID + "/#");

        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single
         * Author.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/uk.co.droidinactu.inlibrislibertas.contentprovider.author";

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of
         * Author.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.eads.minimrms.mobileclient.author";

        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + _PATH_AUTHORS);

        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = Author.FIELD_NAME_AUTHOR_NAME + " ASC";

        /**
         * The content Uri pattern for a Author listing for live folders
         */
        public static final Uri LIVE_FOLDER_URI = Uri.parse("content://" + AUTHORITY + _PATH_LIVE_FOLDER);

        /**
         * The table name offered by this provider
         */
        public static final String TABLE_NAME = Author.DATABASE_TABLE_NAME;

        // This class cannot be instantiated
        private Authors() {
        }
    }

    /**
     * EBook table contract
     */
    public static final class EBooks implements BaseColumns {

        /**
         * Path part for the EBooks URI
         */
        private static final String _PATH_EBOOKS = "/ebooks";

        /**
         * Path part for the Ebook ID URI
         */
        private static final String _PATH_EBOOKS_ID = "/ebooks/";

        /**
         * Path part for the Ebook ID URI
         */
        private static final String _PATH_EBOOKS_RECENT = "/recentebook";

        /**
         * Path part for the Live Folder URI
         */
        private static final String _PATH_LIVE_FOLDER = "/live_folders/ebooks";

        /**
         * The content URI base for a single EBook. Callers must append a
         * numeric ebook id to this Uri to retrieve a single EBook
         */
        public static final Uri CONTENT_ID_URI_BASE = Uri.parse("content://" + AUTHORITY + _PATH_EBOOKS_ID);

        /**
         * The content URI match pattern for a single EBook, specified by its
         * ID. Use this to match incoming URIs or to construct an Intent.
         */
        public static final Uri CONTENT_ID_URI_PATTERN = Uri.parse("content://" + AUTHORITY + _PATH_EBOOKS_ID + "/#");

        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single
         * EBook.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/uk.co.droidinactu.inlibrislibertas.contentprovider.ebook";

        /**
         * The content URI match pattern for a single EBook, specified by its
         * ID. Use this to match incoming URIs or to construct an Intent.
         */
        public static final Uri CONTENT_RECENT_URI = Uri.parse("content://" + AUTHORITY + _PATH_EBOOKS_RECENT);

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of
         * EBooks.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.eads.minimrms.mobileclient.ebook";

        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + _PATH_EBOOKS);

        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = CalibreBook.FIELD_NAME_BOOK_TITLE + " ASC";

        /**
         * 0-relative position of an EBook ID segment in the path part of an
         * Ebook ID URI
         */
        public static final int EBOOKS_ID_PATH_POSITION = 1;

        /**
         * The content Uri pattern for a notes listing for live folders
         */
        public static final Uri LIVE_FOLDER_URI = Uri.parse("content://" + AUTHORITY + _PATH_LIVE_FOLDER);

        /**
         * The table name offered by this provider
         */
        public static final String TABLE_NAME = CalibreBook.DATABASE_TABLE_NAME;

        // This class cannot be instantiated
        private EBooks() {
        }

    }

    /**
     * AuthorBookLink table contract
     */
    public static final class TagBookLink implements BaseColumns {

        /**
         * Path part for the Author URI
         */
        private static final String _PATH_TAGBOOKLINKS = "/tagbooklink";

        /**
         * Path part for the Author ID URI
         */
        private static final String _PATH_TAGBOOKLINKS_ID = "/tagbooklink/";

        /**
         * The content URI base for a single Author. Callers must append a
         * numeric Author id to this Uri to retrieve a single EBook
         */
        public static final Uri CONTENT_ID_URI_BASE = Uri.parse("content://" + AUTHORITY + _PATH_TAGBOOKLINKS_ID);

        /**
         * The content URI match pattern for a single Author, specified by its
         * ID. Use this to match incoming URIs or to construct an Intent.
         */
        public static final Uri CONTENT_ID_URI_PATTERN = Uri.parse("content://" + AUTHORITY + _PATH_TAGBOOKLINKS_ID
                + "/#");

        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single
         * Author.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/uk.co.droidinactu.inlibrislibertas.contentprovider.tagbooklink";

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of
         * Author.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.eads.minimrms.mobileclient.tagbooklink";

        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + _PATH_TAGBOOKLINKS);

        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = TagEbookLink.FIELD_NAME_EBOOK_ID + " ASC";

        /**
         * The table name offered by this provider
         */
        public static final String TABLE_NAME = TagEbookLink.DATABASE_TABLE_NAME;

        /**
         * 0-relative position of an EBook ID segment in the path part of an
         * Author ID URI
         */
        public static final int TAGBOOKLINK_ID_PATH_POSITION = 1;

        // This class cannot be instantiated
        private TagBookLink() {
        }
    }

    /**
     * Tag table contract
     */
    public static final class Tags implements BaseColumns {

        /**
         * Path part for the Live Folder URI
         */
        private static final String _PATH_LIVE_FOLDER = "/live_folders/authors";

        /**
         * Path part for the Author URI
         */
        private static final String _PATH_TAGS = "/tags";

        /**
         * Path part for the Author ID URI
         */
        private static final String _PATH_TAGS_ID = "/tags/";

        /**
         * The content URI base for a single Author. Callers must append a
         * numeric Author id to this Uri to retrieve a single EBook
         */
        public static final Uri CONTENT_ID_URI_BASE = Uri.parse("content://" + AUTHORITY + _PATH_TAGS_ID);

        /**
         * The content URI match pattern for a single Author, specified by its
         * ID. Use this to match incoming URIs or to construct an Intent.
         */
        public static final Uri CONTENT_ID_URI_PATTERN = Uri.parse("content://" + AUTHORITY + _PATH_TAGS_ID + "/#");

        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single
         * Author.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/uk.co.droidinactu.inlibrislibertas.contentprovider.author";

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of
         * Author.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.eads.minimrms.mobileclient.author";

        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + _PATH_TAGS);

        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = Tag.FIELD_NAME_TAG_NAME + " ASC";

        /**
         * The content Uri pattern for a Author listing for live folders
         */
        public static final Uri LIVE_FOLDER_URI = Uri.parse("content://" + AUTHORITY + _PATH_LIVE_FOLDER);

        /**
         * The table name offered by this provider
         */
        public static final String TABLE_NAME = Tag.DATABASE_TABLE_NAME;

        /**
         * 0-relative position of an EBook ID segment in the path part of an
         * Author ID URI
         */
        public static final int TAGS_ID_PATH_POSITION = 1;

        // This class cannot be instantiated
        private Tags() {
        }
    }

    public static final String AUTHORITY = "uk.co.droidinactu.inlibrislibertas.contentprovider";

    // This class cannot be instantiated
    private CalibreDPData() {
    }

}
