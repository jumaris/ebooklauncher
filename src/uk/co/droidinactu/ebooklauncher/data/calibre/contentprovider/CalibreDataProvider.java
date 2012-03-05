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

import java.util.HashMap;

import uk.co.droidinactu.common.model.AbstractDataObj;
import uk.co.droidinactu.ebooklauncher.data.calibre.Author;
import uk.co.droidinactu.ebooklauncher.data.calibre.CalibreBook;
import uk.co.droidinactu.ebooklauncher.data.calibre.Tag;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.LiveFolders;
import android.text.TextUtils;

public class CalibreDataProvider extends ContentProvider {

    // The incoming URI matches the Author ID URI pattern
    private static final int AUTHOR_ID = 12;
    // The incoming URI matches the Authors URI pattern
    private static final int AUTHORS = 11;
    // The incoming URI matches the EBook ID URI pattern
    private static final int EBOOK_ID = 2;
    // The incoming URI matches the EBooks URI pattern
    private static final int EBOOK_RECENT = 3;

    // The incoming URI matches the EBooks URI pattern
    private static final int EBOOKS = 1;
    // The incoming URI matches the Live Folder URI pattern
    private static final int LIVE_FOLDER_EBOOKS = 9;

    private static final String LOG_TAG = "InLibrisLibertasProvider";
    private static HashMap<String, String> sAuthorsProjectionMap = new HashMap<String, String>();

    /**
     * A projection map used to select columns from the database
     */
    private static HashMap<String, String> sEBooksProjectionMap = new HashMap<String, String>();
    private static HashMap<String, String> sLiveFolderProjectionMap = new HashMap<String, String>();
    private static HashMap<String, String> sTagsProjectionMap = new HashMap<String, String>();
    private static final UriMatcher sUriMatcher;

    // The incoming URI matches the Tag ID URI pattern
    private static final int TAG_ID = 22;

    // The incoming URI matches the Tags URI pattern
    private static final int TAGS = 21;

    /**
     * A block that instantiates and sets static objects
     */
    static {
        /*
         * Creates and initializes the URI matcher
         */
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // Add a pattern that routes URIs terminated with "EBooks" to an
        // EBOOKS operation
        sUriMatcher.addURI(CalibreDPData.AUTHORITY, "ebooks", EBOOKS);

        // Add a pattern that routes URIs terminated with "EBooks" plus an
        // integer to a EBOOK ID operation
        sUriMatcher.addURI(CalibreDPData.AUTHORITY, "ebooks/#", EBOOK_ID);

        // Add a pattern that routes URIs terminated with "EBooks" plus an
        // integer to a EBOOK ID operation
        sUriMatcher.addURI(CalibreDPData.AUTHORITY, "recentebook", EBOOK_RECENT);

        // Add a pattern that routes URIs terminated with live_folders/EBooks
        // to a live folder operation
        sUriMatcher.addURI(CalibreDPData.AUTHORITY, "live_folders/ebooks", LIVE_FOLDER_EBOOKS);

        // Add a pattern that routes URIs terminated with "Authors" to an
        // Authors operation
        sUriMatcher.addURI(CalibreDPData.AUTHORITY, "authors", AUTHORS);

        // Add a pattern that routes URIs terminated with "Authors" plus an
        // integer to a author ID operation
        sUriMatcher.addURI(CalibreDPData.AUTHORITY, "authors/#", AUTHOR_ID);

        // Add a pattern that routes URIs terminated with "Tags" to an
        // Tags operation
        sUriMatcher.addURI(CalibreDPData.AUTHORITY, "tags", TAGS);

        // Add a pattern that routes URIs terminated with "Tags" plus an
        // integer
        // to a tag ID operation
        sUriMatcher.addURI(CalibreDPData.AUTHORITY, "tags/#", TAG_ID);

        // Creates a new projection map instance. The map returns a column name
        // given a string. The two are usually equal.
        sEBooksProjectionMap.put(BaseColumns._ID, BaseColumns._ID);
        sEBooksProjectionMap.put(CalibreBook.FIELD_NAME_APPLICATION_ID, CalibreBook.FIELD_NAME_APPLICATION_ID);
        sEBooksProjectionMap.put(CalibreBook.FIELD_NAME_author_sort, CalibreBook.FIELD_NAME_author_sort);
        sEBooksProjectionMap.put(CalibreBook.FIELD_NAME_book_producer, CalibreBook.FIELD_NAME_book_producer);
        sEBooksProjectionMap.put(CalibreBook.FIELD_NAME_BOOK_TITLE, CalibreBook.FIELD_NAME_BOOK_TITLE);
        sEBooksProjectionMap.put(CalibreBook.FIELD_NAME_category, CalibreBook.FIELD_NAME_category);
        sEBooksProjectionMap.put(CalibreBook.FIELD_NAME_comments, CalibreBook.FIELD_NAME_comments);
        sEBooksProjectionMap.put(CalibreBook.FIELD_NAME_cover, CalibreBook.FIELD_NAME_cover);
        sEBooksProjectionMap.put(CalibreBook.FIELD_NAME_db_id, CalibreBook.FIELD_NAME_db_id);
        sEBooksProjectionMap.put(CalibreBook.FIELD_NAME_ddc, CalibreBook.FIELD_NAME_ddc);
        sEBooksProjectionMap.put(CalibreBook.FIELD_NAME_isbn, CalibreBook.FIELD_NAME_isbn);
        sEBooksProjectionMap.put(CalibreBook.FIELD_NAME_language, CalibreBook.FIELD_NAME_language);
        sEBooksProjectionMap.put(CalibreBook.FIELD_NAME_lcc, CalibreBook.FIELD_NAME_lcc);
        sEBooksProjectionMap.put(CalibreBook.FIELD_NAME_lccn, CalibreBook.FIELD_NAME_lccn);
        sEBooksProjectionMap.put(CalibreBook.FIELD_NAME_lpath, CalibreBook.FIELD_NAME_lpath);
        sEBooksProjectionMap.put(CalibreBook.FIELD_NAME_mime, CalibreBook.FIELD_NAME_mime);
        sEBooksProjectionMap.put(CalibreBook.FIELD_NAME_pubdate, CalibreBook.FIELD_NAME_pubdate);
        sEBooksProjectionMap.put(CalibreBook.FIELD_NAME_publication_type, CalibreBook.FIELD_NAME_publication_type);
        sEBooksProjectionMap.put(CalibreBook.FIELD_NAME_publisher, CalibreBook.FIELD_NAME_publisher);
        sEBooksProjectionMap.put(CalibreBook.FIELD_NAME_rating, CalibreBook.FIELD_NAME_rating);
        sEBooksProjectionMap.put(CalibreBook.FIELD_NAME_rights, CalibreBook.FIELD_NAME_rights);
        sEBooksProjectionMap.put(CalibreBook.FIELD_NAME_series, CalibreBook.FIELD_NAME_series);
        sEBooksProjectionMap.put(CalibreBook.FIELD_NAME_series_index, CalibreBook.FIELD_NAME_series_index);
        sEBooksProjectionMap.put(CalibreBook.FIELD_NAME_size, CalibreBook.FIELD_NAME_size);
        sEBooksProjectionMap.put(CalibreBook.FIELD_NAME_timestamp, CalibreBook.FIELD_NAME_timestamp);
        sEBooksProjectionMap.put(CalibreBook.FIELD_NAME_title_sort, CalibreBook.FIELD_NAME_title_sort);
        sEBooksProjectionMap.put(CalibreBook.FIELD_NAME_uuid, CalibreBook.FIELD_NAME_uuid);

        sEBooksProjectionMap.put(CalibreBook.FIELD_NAME_thumbnail_data, CalibreBook.FIELD_NAME_thumbnail_data);
        sEBooksProjectionMap.put(CalibreBook.FIELD_NAME_thumbnail_height, CalibreBook.FIELD_NAME_thumbnail_height);
        sEBooksProjectionMap.put(CalibreBook.FIELD_NAME_thumbnail_width, CalibreBook.FIELD_NAME_thumbnail_width);

        sEBooksProjectionMap.put(AbstractDataObj.FIELD_NAME_LAST_UPDATED, AbstractDataObj.FIELD_NAME_LAST_UPDATED);

        /*
         * Creates an initializes a projection map for handling Live Folders
         */
        // Maps "_ID" to "_ID AS _ID" for a live folder
        sLiveFolderProjectionMap.put(BaseColumns._ID, BaseColumns._ID + " AS " + BaseColumns._ID);

        // Maps "NAME" to "title AS NAME"
        sLiveFolderProjectionMap.put(LiveFolders.NAME, CalibreBook.FIELD_NAME_BOOK_TITLE + " AS " + LiveFolders.NAME);

        sAuthorsProjectionMap.put(BaseColumns._ID, BaseColumns._ID);
        sAuthorsProjectionMap.put(Author.FIELD_NAME_AUTHOR_NAME, Author.FIELD_NAME_AUTHOR_NAME);
        sAuthorsProjectionMap.put(Author.FIELD_NAME_AUTHOR_SORT, Author.FIELD_NAME_AUTHOR_SORT);
        sAuthorsProjectionMap.put(AbstractDataObj.FIELD_NAME_LAST_UPDATED, AbstractDataObj.FIELD_NAME_LAST_UPDATED);

        sTagsProjectionMap.put(BaseColumns._ID, BaseColumns._ID);
        sTagsProjectionMap.put(Tag.FIELD_NAME_TAG_NAME, Tag.FIELD_NAME_TAG_NAME);
        sTagsProjectionMap.put(Tag.FIELD_NAME_TAG_SORT, Tag.FIELD_NAME_TAG_SORT);
        sTagsProjectionMap.put(AbstractDataObj.FIELD_NAME_LAST_UPDATED, AbstractDataObj.FIELD_NAME_LAST_UPDATED);
    }

    // Handle to a new DatabaseHelper.
    private DatabaseHelper mOpenHelper;

    /**
     * This is called when a client calls
     * {@link android.content.ContentResolver#delete(Uri, String, String[])}.
     * Deletes records from the database. If the incoming URI matches the ebook
     * ID URI pattern, this method deletes the one record specified by the ID in
     * the URI. Otherwise, it deletes a a set of records. The record or records
     * must also match the input selection criteria specified by where and
     * whereArgs.
     * 
     * If rows were deleted, then listeners are notified of the change.
     * 
     * @return If a "where" clause is used, the number of rows affected is
     *         returned, otherwise 0 is returned. To delete all rows and get a
     *         row count, use "1" as the where clause.
     * @throws IllegalArgumentException
     *             if the incoming URI pattern is invalid.
     */
    @Override
    public int delete(final Uri uri, final String where, final String[] whereArgs) {

        // Opens the database object in "write" mode.
        final SQLiteDatabase db = this.mOpenHelper.getWritableDatabase();
        String finalWhere;

        int count;

        // Does the delete based on the incoming URI pattern.
        switch (sUriMatcher.match(uri)) {

        // If the incoming pattern matches the general pattern for ebooks, does
        // a delete based on the incoming "where" columns and arguments.
        case EBOOKS:
            count = db.delete(CalibreBook.DATABASE_TABLE_NAME, // The
                                                         // database
                                                         // table
                                                         // name
                    where, // The incoming where clause column names
                    whereArgs // The incoming where clause values
                    );
            break;

        // If the incoming URI matches a single ebook ID, does the delete based
        // on the
        // incoming data, but modifies the where clause to restrict it to the
        // particular ebook ID.
        case EBOOK_ID:
            /*
             * Starts a final WHERE clause by restricting it to the desired
             * ebook ID.
             */
            finalWhere = BaseColumns._ID + // The ID column name
                    " = " + // test for equality
                    uri.getPathSegments(). // the incoming ebook ID
                            get(CalibreDPData.EBooks.EBOOKS_ID_PATH_POSITION);

            // If there were additional selection criteria, append them to the
            // final WHERE clause
            if (where != null) {
                finalWhere = finalWhere + " AND " + where;
            }

            // Performs the delete.
            count = db.delete(CalibreBook.DATABASE_TABLE_NAME, // The database table
                                                         // name.
                    finalWhere, // The final WHERE clause
                    whereArgs // The incoming where clause values.
                    );
            break;

        // If the incoming pattern matches the general pattern for authors, does
        // a delete based on the incoming "where" columns and arguments.
        case AUTHORS:
            count = db.delete(Author.DATABASE_TABLE_NAME, // The
                                                          // database
                                                          // table
                                                          // name
                    where, // The incoming where clause column names
                    whereArgs // The incoming where clause values
                    );
            break;

        // If the incoming URI matches a single author ID, does the delete based
        // on the incoming data, but modifies the where clause to restrict it to
        // the particular author ID.
        case AUTHOR_ID:
            /*
             * Starts a final WHERE clause by restricting it to the desired
             * author ID.
             */
            finalWhere = BaseColumns._ID + // The ID column name
                    " = " + // test for equality
                    uri.getPathSegments(). // the incoming author ID
                            get(CalibreDPData.Authors.AUTHORS_ID_PATH_POSITION);

            // If there were additional selection criteria, append them to the
            // final WHERE clause
            if (where != null) {
                finalWhere = finalWhere + " AND " + where;
            }

            // Performs the delete.
            count = db.delete(Author.DATABASE_TABLE_NAME, // The database table
                                                          // name.
                    finalWhere, // The final WHERE clause
                    whereArgs // The incoming where clause values.
                    );
            break;

        // If the incoming pattern matches the general pattern for tags, does a
        // delete
        // based on the incoming "where" columns and arguments.
        case TAGS:
            count = db.delete(Tag.DATABASE_TABLE_NAME, // The
                                                       // database
                                                       // table
                                                       // name
                    where, // The incoming where clause column names
                    whereArgs // The incoming where clause values
                    );
            break;

        // If the incoming URI matches a single tag ID, does the delete based
        // on the
        // incoming data, but modifies the where clause to restrict it to the
        // particular tag ID.
        case TAG_ID:
            /*
             * Starts a final WHERE clause by restricting it to the desired tag
             * ID.
             */
            finalWhere = BaseColumns._ID + // The ID column name
                    " = " + // test for equality
                    uri.getPathSegments(). // the incoming tag ID
                            get(CalibreDPData.Tags.TAGS_ID_PATH_POSITION);

            // If there were additional selection criteria, append them to the
            // final
            // WHERE clause
            if (where != null) {
                finalWhere = finalWhere + " AND " + where;
            }

            // Performs the delete.
            count = db.delete(Tag.DATABASE_TABLE_NAME, // The database table
                                                       // name.
                    finalWhere, // The final WHERE clause
                    whereArgs // The incoming where clause values.
                    );
            break;

        // If the incoming pattern is invalid, throws an exception.
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        /*
         * Gets a handle to the content resolver object for the current context,
         * and notifies it that the incoming URI changed. The object passes this
         * along to the resolver framework, and observers that have registered
         * themselves for the provider are notified.
         */
        this.getContext().getContentResolver().notifyChange(uri, null);

        // Returns the number of rows deleted.
        return count;
    }

    /**
     * A test package can call this to get a handle to the database underlying
     * ebookPadProvider, so it can insert test data into the database. The test
     * case class is responsible for instantiating the provider in a test
     * context; {@link android.test.ProviderTestCase2} does this during the call
     * to setUp()
     * 
     * @return a handle to the database helper object for the provider's data.
     */
    DatabaseHelper getOpenHelperForTest() {
        return this.mOpenHelper;
    }

    /**
     * This is called when a client calls
     * {@link android.content.ContentResolver#getType(Uri)}. Returns the MIME
     * data type of the URI given as a parameter.
     * 
     * @param uri
     *            The URI whose MIME type is desired.
     * @return The MIME type of the URI.
     * @throws IllegalArgumentException
     *             if the incoming URI pattern is invalid.
     */
    @Override
    public String getType(final Uri uri) {

        /**
         * Chooses the MIME type based on the incoming URI pattern
         */
        switch (sUriMatcher.match(uri)) {

        // If the pattern is for ebooks or live folders, returns the general
        // content type.
        case EBOOKS:
        case LIVE_FOLDER_EBOOKS:
            return CalibreDPData.EBooks.CONTENT_TYPE;

            // If the pattern is for ebook IDs, returns the ebook ID content
            // type.
        case EBOOK_ID:
            return CalibreDPData.EBooks.CONTENT_ITEM_TYPE;

            // If the pattern is for authors or live folders, returns the
            // general
            // content type.
        case AUTHORS:
            return CalibreDPData.Authors.CONTENT_TYPE;

            // If the pattern is for author IDs, returns the author ID content
            // type.
        case AUTHOR_ID:
            return CalibreDPData.Authors.CONTENT_ITEM_TYPE;

            // If the pattern is for tags or live folders, returns the general
            // content type.
        case TAGS:
            return CalibreDPData.Tags.CONTENT_TYPE;

            // If the pattern is for tag IDs, returns the tag ID content type.
        case TAG_ID:
            return CalibreDPData.Tags.CONTENT_ITEM_TYPE;

            // If the URI pattern doesn't match any permitted patterns, throws
            // an exception.
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    /**
     * This is called when a client calls
     * {@link android.content.ContentResolver#insert(Uri, ContentValues)}.
     * Inserts a new row into the database. This method sets up default values
     * for any columns that are not included in the incoming map. If rows were
     * inserted, then listeners are notified of the change.
     * 
     * @return The row ID of the inserted row.
     * @throws SQLException
     *             if the insertion fails.
     */
    @Override
    public Uri insert(final Uri uri, final ContentValues initialValues) {

        // Validates the incoming URI. Only the full provider URI is allowed for
        // inserts.
        if (sUriMatcher.match(uri) == EBOOKS) {

            // A map to hold the new record's values.
            ContentValues values;

            // If the incoming values map is not null, uses it for the new
            // values.
            if (initialValues != null) {
                values = new ContentValues(initialValues);
            } else {
                // Otherwise, create a new value map
                values = new ContentValues();
            }

            // Gets the current system time in milliseconds
            final Long now = Long.valueOf(System.currentTimeMillis());

            // If the values map doesn't contain a last updated date, sets the
            // value
            // to the current time.
            // if (values.containsKey(AbstractDataObj.FIELD_NAME_LAST_UPDATED)
            // ==
            // false) {
            values.put(AbstractDataObj.FIELD_NAME_LAST_UPDATED, now);
            // }

            // TODO may need to set other defaults here

            // Opens the database object in "write" mode.
            final SQLiteDatabase db = this.mOpenHelper.getWritableDatabase();

            // Performs the insert and returns the ID of the new ebook.
            final long rowId = db.insert(CalibreBook.DATABASE_TABLE_NAME, // The table
                                                                    // to
                                                                    // insert
                                                                    // into.
                    CalibreBook.FIELD_NAME_series, // A hack, SQLite sets this column
                                             // value to null
                    // if values is empty.
                    values // A map of column names, and the values to insert
                           // into
                           // the columns.
                    );

            // If the insert succeeded, the row ID exists.
            if (rowId > 0) {
                // Creates a URI with the ebook ID pattern and the new row ID
                // appended to it.
                final Uri ebookUri = ContentUris.withAppendedId(CalibreDPData.EBooks.CONTENT_ID_URI_BASE, rowId);

                // Notifies observers registered against this provider that the
                // data
                // changed.
                this.getContext().getContentResolver().notifyChange(ebookUri, null);
                return ebookUri;
            }

            // If the insert didn't succeed, then the rowID is <= 0. Throws an
            // exception.
            throw new SQLException("Failed to insert row into " + uri);
        } else if (sUriMatcher.match(uri) == AUTHORS) {
            // A map to hold the new record's values.
            ContentValues values;

            // If the incoming values map is not null, uses it for the new
            // values.
            if (initialValues != null) {
                values = new ContentValues(initialValues);

            } else {
                // Otherwise, create a new value map
                values = new ContentValues();
            }

            // Gets the current system time in milliseconds
            final Long now = Long.valueOf(System.currentTimeMillis());

            // If the values map doesn't contain a last updated date, sets the
            // value
            // to the current time.
            // if (values.containsKey(AbstractDataObj.FIELD_NAME_LAST_UPDATED)
            // ==
            // false) {
            values.put(AbstractDataObj.FIELD_NAME_LAST_UPDATED, now);
            // }

            // TODO may need to set other defaults here

            // Opens the database object in "write" mode.
            final SQLiteDatabase db = this.mOpenHelper.getWritableDatabase();

            // Performs the insert and returns the ID of the new author.
            final long rowId = db.insert(Author.DATABASE_TABLE_NAME, // The
                                                                     // table to
                                                                     // insert
                                                                     // into.
                    Author.FIELD_NAME_AUTHOR_SORT, // A hack, SQLite sets this
                                                   // column
                    // value to null
                    // if values is empty.
                    values // A map of column names, and the values to
                           // insertinto
                           // the columns.
                    );

            // If the insert succeeded, the row ID exists.
            if (rowId > 0) {
                // Creates a URI with the author ID pattern and the new row ID
                // appended to it.
                final Uri authorUri = ContentUris.withAppendedId(CalibreDPData.Authors.CONTENT_ID_URI_BASE,
                        rowId);

                // Notifies observers registered against this provider that the
                // data
                // changed.
                this.getContext().getContentResolver().notifyChange(authorUri, null);
                return authorUri;
            }

            // If the insert didn't succeed, then the rowID is <= 0. Throws an
            // exception.
            throw new SQLException("Failed to insert row into " + uri);
        } else if (sUriMatcher.match(uri) == TAGS) {
            // A map to hold the new record's values.
            ContentValues values;

            // If the incoming values map is not null, uses it for the new
            // values.
            if (initialValues != null) {
                values = new ContentValues(initialValues);

            } else {
                // Otherwise, create a new value map
                values = new ContentValues();
            }

            // Gets the current system time in milliseconds
            final Long now = Long.valueOf(System.currentTimeMillis());

            // If the values map doesn't contain the creation date, sets the
            // value
            // to the current time.
            if (values.containsKey(AbstractDataObj.FIELD_NAME_LAST_UPDATED) == false) {
                values.put(AbstractDataObj.FIELD_NAME_LAST_UPDATED, now);
            }

            // TODO may need to set other defaults here

            // Opens the database object in "write" mode.
            final SQLiteDatabase db = this.mOpenHelper.getWritableDatabase();

            // Performs the insert and returns the ID of the new tag.
            final long rowId = db.insert(Tag.DATABASE_TABLE_NAME, // The table
                                                                  // to
                                                                  // insert
                                                                  // into.
                    Tag.FIELD_NAME_TAG_SORT, // A hack, SQLite sets this
                                             // column value to null if values
                                             // is empty.
                    values // A map of column names, and the values to insert
                           // into the columns.
                    );

            // If the insert succeeded, the row ID exists.
            if (rowId > 0) {
                // Creates a URI with the tag ID pattern and the new row ID
                // appended to it.
                final Uri tagUri = ContentUris.withAppendedId(CalibreDPData.Tags.CONTENT_ID_URI_BASE, rowId);

                // Notifies observers registered against this provider that the
                // data changed.
                this.getContext().getContentResolver().notifyChange(tagUri, null);
                return tagUri;
            }

            // If the insert didn't succeed, then the rowID is <= 0. Throws an
            // exception.
            throw new SQLException("Failed to insert row into " + uri);
        } else {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    /**
     * 
     * Initializes the provider by creating a new DatabaseHelper. onCreate() is
     * called automatically when Android creates the provider in response to a
     * resolver request from a client.
     */
    @Override
    public boolean onCreate() {

        // Creates a new helper object. ebook that the database itself isn't
        // opened until something tries to access it, and it's only created if
        // it doesn't already exist.
        this.mOpenHelper = new DatabaseHelper(this.getContext());

        // Assumes that any failures will be reported by a thrown exception.
        return true;
    }

    /**
     * This method is called when a client calls
     * {@link android.content.ContentResolver#query(Uri, String[], String, String[], String)}
     * . Queries the database and returns a cursor containing the results.
     * 
     * @return A cursor containing the results of the query. The cursor exists
     *         but is empty if the query returns no results or an exception
     *         occurs.
     * @throws IllegalArgumentException
     *             if the incoming URI pattern is invalid.
     */
    @Override
    public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs,
            final String sortOrder) {

        // Constructs a new query builder and sets its table name
        final SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(CalibreBook.DATABASE_TABLE_NAME);

        String orderBy = sortOrder;

        /**
         * Choose the projection and adjust the "where" clause based on URI
         * pattern-matching.
         */
        switch (sUriMatcher.match(uri)) {
        // If the incoming URI is for EBooks, chooses the EBooks
        // projection
        case EBOOKS:
            qb.setProjectionMap(sEBooksProjectionMap);
            // If no sort order is specified, uses the default
            if (TextUtils.isEmpty(sortOrder)) {
                orderBy = CalibreDPData.EBooks.DEFAULT_SORT_ORDER;
            }
            break;

        /*
         * If the incoming URI is for a single EBook identified by its ID,
         * chooses the EBook ID projection, and appends "_ID = <EBookID>" to the
         * where clause, so that it selects that single EBook
         */
        case EBOOK_ID:
            qb.setProjectionMap(sEBooksProjectionMap);
            qb.appendWhere(BaseColumns._ID + // the name of the ID column
                    "=" +
                    // the position of the EBook ID itself in the incoming URI
                    uri.getPathSegments().get(CalibreDPData.EBooks.EBOOKS_ID_PATH_POSITION));
            // If no sort order is specified, uses the default
            if (TextUtils.isEmpty(sortOrder)) {
                orderBy = CalibreDPData.EBooks.DEFAULT_SORT_ORDER;
            }
            break;

        case EBOOK_RECENT:
            qb.setProjectionMap(sEBooksProjectionMap);
            orderBy = AbstractDataObj.FIELD_NAME_LAST_UPDATED + " ASC";
            break;

        case LIVE_FOLDER_EBOOKS:
            // If the incoming URI is from a live folder, chooses the live
            // folder projection.
            qb.setProjectionMap(sLiveFolderProjectionMap);
            // If no sort order is specified, uses the default
            if (TextUtils.isEmpty(sortOrder)) {
                orderBy = CalibreDPData.EBooks.DEFAULT_SORT_ORDER;
            }
            break;

        // If the incoming URI is for Authors, chooses the Authors
        // projection
        case AUTHORS:
            qb.setProjectionMap(sAuthorsProjectionMap);
            // If no sort order is specified, uses the default
            if (TextUtils.isEmpty(sortOrder)) {
                orderBy = CalibreDPData.Authors.DEFAULT_SORT_ORDER;
            }
            break;

        /*
         * If the incoming URI is for a single Author identified by its ID,
         * chooses the Author ID projection, and appends "_ID = <AuthorID>" to
         * the where clause, so that it selects that single Author
         */
        case AUTHOR_ID:
            qb.setProjectionMap(sAuthorsProjectionMap);
            qb.appendWhere(BaseColumns._ID + // the name of the ID column
                    "=" +
                    // the position of the Author ID itself in the incoming URI
                    uri.getPathSegments().get(CalibreDPData.Authors.AUTHORS_ID_PATH_POSITION));
            // If no sort order is specified, uses the default
            if (TextUtils.isEmpty(sortOrder)) {
                orderBy = CalibreDPData.Authors.DEFAULT_SORT_ORDER;
            }
            break;

        // If the incoming URI is for Tags, chooses the Tags
        // projection
        case TAGS:
            qb.setProjectionMap(sTagsProjectionMap);
            // If no sort order is specified, uses the default
            if (TextUtils.isEmpty(sortOrder)) {
                orderBy = CalibreDPData.Tags.DEFAULT_SORT_ORDER;
            }
            break;

        /*
         * If the incoming URI is for a single Tag identified by its ID, chooses
         * the Tag ID projection, and appends "_ID = <TagID>" to the where
         * clause, so that it selects that single Tag
         */
        case TAG_ID:
            qb.setProjectionMap(sTagsProjectionMap);
            qb.appendWhere(BaseColumns._ID + // the name of the ID column
                    "=" +
                    // the position of the Tag ID itself in the incoming URI
                    uri.getPathSegments().get(CalibreDPData.Tags.TAGS_ID_PATH_POSITION));
            // If no sort order is specified, uses the default
            if (TextUtils.isEmpty(sortOrder)) {
                orderBy = CalibreDPData.Tags.DEFAULT_SORT_ORDER;
            }
            break;

        default:
            // If the URI doesn't match any of the known patterns, throw an
            // exception.
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // Opens the database object in "read" mode, since no writes need to be
        // done.
        final SQLiteDatabase db = this.mOpenHelper.getReadableDatabase();

        /*
         * Performs the query. If no problems occur trying to read the database,
         * then a Cursor object is returned; otherwise, the cursor variable
         * contains null. If no records were selected, then the Cursor object is
         * empty, and Cursor.getCount() returns 0.
         */
        final Cursor c = qb.query(db, // The database to query
                projection, // The columns to return from the query
                selection, // The columns for the where clause
                selectionArgs, // The values for the where clause
                null, // don't group the rows
                null, // don't filter by row groups
                orderBy // The sort order
                );

        switch (sUriMatcher.match(uri)) {
        case EBOOKS:
        case EBOOK_ID:

            // FIXME : how do we read in the authors and tags for the books ?
        }

        // Tells the Cursor what URI to watch, so it knows when its source data
        // changes
        c.setNotificationUri(this.getContext().getContentResolver(), uri);
        return c;
    }

    /**
     * This is called when a client calls
     * {@link android.content.ContentResolver#update(Uri,ContentValues,String,String[])}
     * Updates records in the database. The column names specified by the keys
     * in the values map are updated with new data specified by the values in
     * the map. If the incoming URI matches the ebook ID URI pattern, then the
     * method updates the one record specified by the ID in the URI; otherwise,
     * it updates a set of records. The record or records must match the input
     * selection criteria specified by where and whereArgs. If rows were
     * updated, then listeners are notified of the change.
     * 
     * @param uri
     *            The URI pattern to match and update.
     * @param values
     *            A map of column names (keys) and new values (values).
     * @param where
     *            An SQL "WHERE" clause that selects records based on their
     *            column values. If this is null, then all records that match
     *            the URI pattern are selected.
     * @param whereArgs
     *            An array of selection criteria. If the "where" param contains
     *            value placeholders ("?"), then each placeholder is replaced by
     *            the corresponding element in the array.
     * @return The number of rows updated.
     * @throws IllegalArgumentException
     *             if the incoming URI pattern is invalid.
     */
    @Override
    public int update(final Uri uri, final ContentValues values, final String where, final String[] whereArgs) {

        // Opens the database object in "write" mode.
        final SQLiteDatabase db = this.mOpenHelper.getWritableDatabase();
        int count;

        String finalWhere = "";

        // Does the update based on the incoming URI pattern
        switch (sUriMatcher.match(uri)) {

        // If the incoming URI matches the general EBooks pattern, does the
        // update based on the incoming data.
        case EBOOKS:

            // Does the update and returns the number of rows updated.
            count = db.update(CalibreBook.DATABASE_TABLE_NAME, values, // A map of
                                                                 // column names
                                                                 // and new
                                                                 // values to
                                                                 // use.
                    where, // The where clause column names.
                    whereArgs // The where clause column values to select on.
                    );
            break;

        // If the incoming URI matches a single EBook ID, does the update based
        // on the incoming data, but modifies the where clause to restrict it to
        // the particular ebook ID.
        case EBOOK_ID:
            // From the incoming URI, get the ebook ID
            final String ebookId = uri.getPathSegments().get(CalibreDPData.EBooks.EBOOKS_ID_PATH_POSITION);

            /*
             * Starts creating the final WHERE clause by restricting it to the
             * incoming EBook ID.
             */
            finalWhere = BaseColumns._ID + // The ID column name
                    "=" + // test for equality
                    ebookId;

            // If there were additional selection criteria, append them to the
            // final WHERE
            // clause
            if (where != null) {
                finalWhere = finalWhere + " AND " + where;
            }

            // Does the update and returns the number of rows updated.
            count = db.update(CalibreBook.DATABASE_TABLE_NAME, values, // A map of
                                                                 // column names
                                                                 // and new
                                                                 // values to
                                                                 // use.
                    finalWhere, // The final WHERE clause to use
                                // placeholders for whereArgs
                    whereArgs // The where clause column values to select on, or
                              // null if the values are in the where argument.
                    );
            break;

        // If the incoming URI matches the general Authors pattern, does the
        // update based on the incoming data.
        case AUTHORS:

            // Does the update and returns the number of rows updated.
            count = db.update(Author.DATABASE_TABLE_NAME, values, // A map of
                                                                  // column
                                                                  // names
                                                                  // and new
                                                                  // values to
                                                                  // use.
                    where, // The where clause column names.
                    whereArgs // The where clause column values to select on.
                    );
            break;

        // If the incoming URI matches a single Author ID, does the update based
        // on the incoming data, but modifies the where clause to restrict it to
        // the particular author ID.
        case AUTHOR_ID:
            // From the incoming URI, get the author ID
            final String authorId = uri.getPathSegments().get(CalibreDPData.Authors.AUTHORS_ID_PATH_POSITION);

            /*
             * Starts creating the final WHERE clause by restricting it to the
             * incoming Author ID.
             */
            finalWhere = BaseColumns._ID + // The ID column name
                    "=" + // test for equality
                    authorId;

            // If there were additional selection criteria, append them to the
            // final WHERE clause
            if (where != null) {
                finalWhere = finalWhere + " AND " + where;
            }

            // Does the update and returns the number of rows updated.
            count = db.update(Author.DATABASE_TABLE_NAME, values, // A map of
                                                                  // column
                                                                  // names
                                                                  // and new
                                                                  // values to
                                                                  // use.
                    finalWhere, // The final WHERE clause to use
                                // placeholders for whereArgs
                    whereArgs // The where clause column values to select on, or
                              // null if the values are in the where argument.
                    );
            break;

        // If the incoming URI matches the general Tags pattern, does the
        // update based on
        // the incoming data.
        case TAGS:

            // Does the update and returns the number of rows updated.
            count = db.update(Tag.DATABASE_TABLE_NAME, values, // A map of
                                                               // column
                                                               // names
                                                               // and new
                                                               // values to
                                                               // use.
                    where, // The where clause column names.
                    whereArgs // The where clause column values to select on.
                    );
            break;

        // If the incoming URI matches a single Tag ID, does the update
        // based on the incoming
        // data, but modifies the where clause to restrict it to the particular
        // tag ID.
        case TAG_ID:
            // From the incoming URI, get the tag ID
            final String tagId = uri.getPathSegments().get(CalibreDPData.Tags.TAGS_ID_PATH_POSITION);

            /*
             * Starts creating the final WHERE clause by restricting it to the
             * incoming Tag ID.
             */
            finalWhere = BaseColumns._ID + // The ID column name
                    "=" + // test for equality
                    tagId;

            // If there were additional selection criteria, append them to the
            // final WHERE clause
            if (where != null) {
                finalWhere = finalWhere + " AND " + where;
            }

            // Does the update and returns the number of rows updated.
            count = db.update(Tag.DATABASE_TABLE_NAME, values, // A map of
                                                               // column
                                                               // names
                                                               // and new
                                                               // values to
                                                               // use.
                    finalWhere, // The final WHERE clause to use
                                // placeholders for whereArgs
                    whereArgs // The where clause column values to select on, or
                              // null if the values are in the where argument.
                    );
            break;

        // If the incoming pattern is invalid, throws an exception.
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        /*
         * Gets a handle to the content resolver object for the current context,
         * and notifies it that the incoming URI changed. The object passes this
         * along to the resolver framework, and observers that have registered
         * themselves for the provider are notified.
         */
        this.getContext().getContentResolver().notifyChange(uri, null);

        // Returns the number of rows updated.
        return count;
    }

}
