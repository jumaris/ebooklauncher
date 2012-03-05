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
package uk.co.droidinactu.ebooklauncher.data;

import java.util.ArrayList;
import java.util.HashMap;

import uk.co.droidinactu.common.model.AbstractDataObj;
import uk.co.droidinactu.ebooklauncher.data.calibre.CalibreBook;
import android.content.ContentValues;
import android.database.Cursor;

/**
 * this object defines all aspects of a book as defined in the Sony PRS-T1
 * database.
 * 
 * @author andy aspell-clark
 * 
 */
public final class Book extends AbstractDataObj {

    /**
     * The name of the column in the database used to store the created
     * date/time.
     */
    public static final String COLUMN_NAME_ADDED_DATE = "added_date";
    public static final String COLUMN_NAME_AUTHOR = "author";
    public static final String COLUMN_NAME_AUTHOR_KEY = "author_key";
    public static final String COLUMN_NAME_CONFORMS_TO = "conforms_to";
    public static final String COLUMN_NAME_CORRUPTED = "corrupted";
    public static final String COLUMN_NAME_DESCRIPTION = "description";
    public static final String COLUMN_NAME_EXPIRATION_DATE = "expiration_date";
    public static final String COLUMN_NAME_FILE_NAME = "file_name";
    public static final String COLUMN_NAME_FILE_PATH = "file_path";
    public static final String COLUMN_NAME_FILE_SIZE = "file_size";
    public static final String COLUMN_NAME_KANA_AUTHOR = "kana_author";
    public static final String COLUMN_NAME_KANA_PERIODICAL_NAME = "kana_periodical_name";
    public static final String COLUMN_NAME_KANA_TITLE = "kana_title";
    public static final String COLUMN_NAME_LOGOS = "logos";
    public static final String COLUMN_NAME_MIME_TYPE = "mime_type";
    /**
     * The name of the column in the database used to store the modified
     * date/time.
     */
    public static final String COLUMN_NAME_MODIFIED_DATE = "modified_date";
    public static final String COLUMN_NAME_PERIODICAL_NAME = "periodical_name";
    public static final String COLUMN_NAME_PERIODICAL_NAME_KEY = "periodical_name_key";
    public static final String COLUMN_NAME_PREVENT_DELETE = "prevent_delete";
    public static final String COLUMN_NAME_PUBLICATION_DATE = "publication_date";
    public static final String COLUMN_NAME_PURCHASED_DATE = "purchased_date";
    public static final String COLUMN_NAME_READING_TIME = "reading_time";
    public static final String COLUMN_NAME_SONY_ID = "sony_id";
    public static final String COLUMN_NAME_SOURCE_ID = "source_id";
    public static final String COLUMN_NAME_THUMBNAIL = "thumbnail";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_TITLE_KEY = "title_key";

    public static final String DATABASE_TABLE_NAME = "books";

    private static final long serialVersionUID = 1L;

    public long added_date;
    public String author;
    public String author_key;
    public String conforms_to;
    public int corrupted;
    public String description;
    public int expiration_date;
    public String file_name;
    public String file_path;
    public long file_size;
    public String kana_author;
    public String kana_periodical_name;
    public String kana_title;
    public String logos;
    public String mime_type;
    public long modified_date;
    public String periodical_name;
    public String periodical_name_key;
    public int prevent_delete;
    public int publication_date;
    public int purchased_date;
    public int reading_time;
    public String sony_id;
    public long source_id;
    public String thumbnail;
    public String title;
    public String title_key;

    public Book() {
    }

    public Book(CalibreBook ebk) {
        this.uniqueIdentifier = -1;
        this.added_date = -1;
        this.modified_date = -1;
        this.author = ebk.authorString;
        this.author_key = "";
        this.conforms_to = "";
        this.corrupted = -1;
        this.description = ebk.comments;
        this.expiration_date = -1;
        this.file_name = ebk.lpath;
        this.file_path = ebk.lpath;
        this.file_size = -1;
        this.kana_author = "";
        this.kana_periodical_name = "";
        this.kana_title = "";
        this.logos = "";
        this.mime_type = ebk.mime;
        this.periodical_name = "";
        this.periodical_name_key = "";
        this.prevent_delete = -1;
        this.publication_date = -1;
        this.purchased_date = -1;
        this.reading_time = -1;
        this.sony_id = "";
        this.source_id = -1;
        this.thumbnail = "";
        this.title = ebk.title;
        this.title_key = "";
    }

    public Book(final Cursor tmpCursor) {
        super(tmpCursor);
        try {
            this.added_date = tmpCursor.getLong(tmpCursor.getColumnIndex(Book.COLUMN_NAME_ADDED_DATE));
        } catch (final Exception e) {
        }
        try {
            this.modified_date = tmpCursor.getLong(tmpCursor.getColumnIndex(Book.COLUMN_NAME_MODIFIED_DATE));
        } catch (final Exception e) {
        }
        try {
            this.author = tmpCursor.getString(tmpCursor.getColumnIndex(Book.COLUMN_NAME_AUTHOR));
        } catch (final Exception e) {
        }
        try {
            this.author_key = tmpCursor.getString(tmpCursor.getColumnIndex(Book.COLUMN_NAME_AUTHOR_KEY));
        } catch (final Exception e) {
        }
        try {
            this.conforms_to = tmpCursor.getString(tmpCursor.getColumnIndex(Book.COLUMN_NAME_CONFORMS_TO));
        } catch (final Exception e) {
        }
        this.corrupted = tmpCursor.getInt(tmpCursor.getColumnIndex(Book.COLUMN_NAME_CORRUPTED));
        this.description = tmpCursor.getString(tmpCursor.getColumnIndex(Book.COLUMN_NAME_DESCRIPTION));
        try {
            this.expiration_date = tmpCursor.getInt(tmpCursor.getColumnIndex(Book.COLUMN_NAME_EXPIRATION_DATE));
        } catch (final Exception e) {
        }
        this.file_name = tmpCursor.getString(tmpCursor.getColumnIndex(Book.COLUMN_NAME_FILE_NAME));
        this.file_path = tmpCursor.getString(tmpCursor.getColumnIndex(Book.COLUMN_NAME_FILE_PATH));
        try {
            this.file_size = tmpCursor.getLong(tmpCursor.getColumnIndex(Book.COLUMN_NAME_FILE_SIZE));
        } catch (final Exception e) {
        }
        this.kana_author = tmpCursor.getString(tmpCursor.getColumnIndex(Book.COLUMN_NAME_KANA_AUTHOR));
        this.kana_periodical_name = tmpCursor
                .getString(tmpCursor.getColumnIndex(Book.COLUMN_NAME_KANA_PERIODICAL_NAME));
        this.kana_title = tmpCursor.getString(tmpCursor.getColumnIndex(Book.COLUMN_NAME_KANA_TITLE));
        this.logos = tmpCursor.getString(tmpCursor.getColumnIndex(Book.COLUMN_NAME_LOGOS));
        this.mime_type = tmpCursor.getString(tmpCursor.getColumnIndex(Book.COLUMN_NAME_MIME_TYPE));
        this.periodical_name = tmpCursor.getString(tmpCursor.getColumnIndex(Book.COLUMN_NAME_PERIODICAL_NAME));
        this.periodical_name_key = tmpCursor.getString(tmpCursor.getColumnIndex(Book.COLUMN_NAME_PERIODICAL_NAME_KEY));
        try {
            this.prevent_delete = tmpCursor.getInt(tmpCursor.getColumnIndex(Book.COLUMN_NAME_PREVENT_DELETE));
        } catch (final Exception e) {
        }
        try {
            this.publication_date = tmpCursor.getInt(tmpCursor.getColumnIndex(Book.COLUMN_NAME_PUBLICATION_DATE));
        } catch (final Exception e) {
        }
        this.purchased_date = tmpCursor.getInt(tmpCursor.getColumnIndex(Book.COLUMN_NAME_PURCHASED_DATE));
        try {
            this.reading_time = tmpCursor.getInt(tmpCursor.getColumnIndex(Book.COLUMN_NAME_READING_TIME));
        } catch (final Exception e) {
        }
        this.sony_id = tmpCursor.getString(tmpCursor.getColumnIndex(Book.COLUMN_NAME_SONY_ID));
        try {
            this.source_id = tmpCursor.getInt(tmpCursor.getColumnIndex(Book.COLUMN_NAME_SOURCE_ID));
        } catch (final Exception e) {
        }
        this.thumbnail = tmpCursor.getString(tmpCursor.getColumnIndex(Book.COLUMN_NAME_THUMBNAIL));
        this.title = tmpCursor.getString(tmpCursor.getColumnIndex(Book.COLUMN_NAME_TITLE));
        this.title_key = tmpCursor.getString(tmpCursor.getColumnIndex(Book.COLUMN_NAME_TITLE_KEY));
    }

    @Override
    public String[] getColumnNames() {
        final String[] sprCols = super.getColumnNames();
        final String[] colNames = new String[sprCols.length + 26];
        for (int y = 0; y < sprCols.length; y++) {
            colNames[y] = sprCols[y];
        }
        int x = sprCols.length - 1;
        colNames[x++] = Book.COLUMN_NAME_ADDED_DATE;
        colNames[x++] = Book.COLUMN_NAME_MODIFIED_DATE;
        colNames[x++] = Book.COLUMN_NAME_AUTHOR;
        colNames[x++] = Book.COLUMN_NAME_AUTHOR_KEY;
        colNames[x++] = Book.COLUMN_NAME_CONFORMS_TO;
        colNames[x++] = Book.COLUMN_NAME_CORRUPTED;
        colNames[x++] = Book.COLUMN_NAME_DESCRIPTION;
        colNames[x++] = Book.COLUMN_NAME_EXPIRATION_DATE;
        colNames[x++] = Book.COLUMN_NAME_FILE_NAME;
        colNames[x++] = Book.COLUMN_NAME_FILE_PATH;
        colNames[x++] = Book.COLUMN_NAME_FILE_SIZE;
        colNames[x++] = Book.COLUMN_NAME_KANA_AUTHOR;
        colNames[x++] = Book.COLUMN_NAME_KANA_PERIODICAL_NAME;
        colNames[x++] = Book.COLUMN_NAME_KANA_TITLE;
        colNames[x++] = Book.COLUMN_NAME_LOGOS;
        colNames[x++] = Book.COLUMN_NAME_MIME_TYPE;
        colNames[x++] = Book.COLUMN_NAME_PERIODICAL_NAME;
        colNames[x++] = Book.COLUMN_NAME_PERIODICAL_NAME_KEY;
        colNames[x++] = Book.COLUMN_NAME_PREVENT_DELETE;
        colNames[x++] = Book.COLUMN_NAME_PUBLICATION_DATE;
        colNames[x++] = Book.COLUMN_NAME_PURCHASED_DATE;
        colNames[x++] = Book.COLUMN_NAME_READING_TIME;
        colNames[x++] = Book.COLUMN_NAME_SONY_ID;
        colNames[x++] = Book.COLUMN_NAME_SOURCE_ID;
        colNames[x++] = Book.COLUMN_NAME_THUMBNAIL;
        colNames[x++] = Book.COLUMN_NAME_TITLE;
        colNames[x++] = Book.COLUMN_NAME_TITLE_KEY;
        return colNames;
    }

    @Override
    public ContentValues getContentValues() {
        final ContentValues map = super.getContentValues();
        map.put(Book.COLUMN_NAME_ADDED_DATE, this.added_date);
        map.put(Book.COLUMN_NAME_MODIFIED_DATE, this.modified_date);
        map.put(Book.COLUMN_NAME_AUTHOR, this.author);
        map.put(Book.COLUMN_NAME_AUTHOR_KEY, this.author_key);
        map.put(Book.COLUMN_NAME_CONFORMS_TO, this.conforms_to);
        map.put(Book.COLUMN_NAME_CORRUPTED, this.corrupted);
        map.put(Book.COLUMN_NAME_DESCRIPTION, this.description);
        map.put(Book.COLUMN_NAME_EXPIRATION_DATE, this.expiration_date);
        map.put(Book.COLUMN_NAME_FILE_NAME, this.file_name);
        map.put(Book.COLUMN_NAME_FILE_PATH, this.file_path);
        map.put(Book.COLUMN_NAME_FILE_SIZE, this.file_size);
        map.put(Book.COLUMN_NAME_KANA_AUTHOR, this.kana_author);
        map.put(Book.COLUMN_NAME_KANA_PERIODICAL_NAME, this.kana_periodical_name);
        map.put(Book.COLUMN_NAME_KANA_TITLE, this.kana_title);
        map.put(Book.COLUMN_NAME_LOGOS, this.logos);
        map.put(Book.COLUMN_NAME_MIME_TYPE, this.mime_type);
        map.put(Book.COLUMN_NAME_PERIODICAL_NAME, this.periodical_name);
        map.put(Book.COLUMN_NAME_PERIODICAL_NAME_KEY, this.periodical_name_key);
        map.put(Book.COLUMN_NAME_PREVENT_DELETE, this.prevent_delete);
        map.put(Book.COLUMN_NAME_PUBLICATION_DATE, this.publication_date);
        map.put(Book.COLUMN_NAME_PURCHASED_DATE, this.purchased_date);
        map.put(Book.COLUMN_NAME_READING_TIME, this.reading_time);
        map.put(Book.COLUMN_NAME_SONY_ID, this.sony_id);
        map.put(Book.COLUMN_NAME_SOURCE_ID, this.source_id);
        map.put(Book.COLUMN_NAME_THUMBNAIL, this.thumbnail);
        map.put(Book.COLUMN_NAME_TITLE, this.title);
        map.put(Book.COLUMN_NAME_TITLE_KEY, this.author);
        return map;
    }

    @Override
    public HashMap<Integer, ArrayList<String>> getFields() {
        final HashMap<Integer, ArrayList<String>> fields = super.getFields();

        int x = fields.size();
        fields.put(x++, this.getArrayList(Book.COLUMN_NAME_ADDED_DATE, "VARCHAR(255) NOT NULL"));
        fields.put(x++, this.getArrayList(Book.COLUMN_NAME_MODIFIED_DATE, "VARCHAR(255) NOT NULL"));
        fields.put(x++, this.getArrayList(Book.COLUMN_NAME_AUTHOR, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(Book.COLUMN_NAME_AUTHOR_KEY, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(Book.COLUMN_NAME_CONFORMS_TO, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(Book.COLUMN_NAME_CORRUPTED, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(Book.COLUMN_NAME_DESCRIPTION, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(Book.COLUMN_NAME_EXPIRATION_DATE, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(Book.COLUMN_NAME_FILE_NAME, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(Book.COLUMN_NAME_FILE_PATH, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(Book.COLUMN_NAME_FILE_SIZE, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(Book.COLUMN_NAME_KANA_AUTHOR, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(Book.COLUMN_NAME_KANA_PERIODICAL_NAME, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(Book.COLUMN_NAME_KANA_TITLE, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(Book.COLUMN_NAME_LOGOS, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(Book.COLUMN_NAME_MIME_TYPE, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(Book.COLUMN_NAME_PERIODICAL_NAME, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(Book.COLUMN_NAME_PERIODICAL_NAME_KEY, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(Book.COLUMN_NAME_PREVENT_DELETE, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(Book.COLUMN_NAME_PUBLICATION_DATE, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(Book.COLUMN_NAME_PURCHASED_DATE, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(Book.COLUMN_NAME_READING_TIME, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(Book.COLUMN_NAME_SONY_ID, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(Book.COLUMN_NAME_SOURCE_ID, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(Book.COLUMN_NAME_THUMBNAIL, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(Book.COLUMN_NAME_TITLE, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(Book.COLUMN_NAME_TITLE_KEY, "VARCHAR(255)"));
        return fields;
    }

    @Override
    public String[] getIndexSql() {
        final String idxs[] = new String[] { "create index author_index on " + Book.DATABASE_TABLE_NAME
                + " ("
                + Book.COLUMN_NAME_AUTHOR
                + " collate nocase);",
                "create index title_index on " + Book.DATABASE_TABLE_NAME
                        + " ("
                        + Book.COLUMN_NAME_TITLE
                        + " collate nocase);" };
        return idxs;
    }

    public String getSelectByTitleSql() {
        String sql = "";
        return sql;
    }

    @Override
    public String getTableName() {
        return Book.DATABASE_TABLE_NAME;
    }

    @Override
    public String toString() {
        return "book: " + this.title + " [by " + this.author + "]";
    }
}
