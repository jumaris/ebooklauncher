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
package uk.co.droidinactu.ebooklauncher.data.nook;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

/**
 * CREATE TABLE docs (_ id INTEGER PRIMARY KEY, ean TEXT,_data TEXT, _size
 * INTEGER, product_type INTEGER, mime_type TEXT, _display_name TEXT,title TEXT,
 * authors TEXT, mainAuthorFirstName TEXT, mainAuthorMiddleName TEXT DEFAULT
 * NULL, mainAuthorLastName TEXT, publisher TEXT, date_added INTEGER,
 * date_modified INTEGER, date_published INTEGER, date_last_accessed INTEGER
 * DEFAULT 0, valid INTEGER, thumb_image TEXT, cover_image TEXT, rating INTEGER
 * DEFAULT 0, user_rating INTEGER DEFAULT 0, storage_location INTEGER DEFAULT 0,
 * category TEXT, page_count INTEGER, launcher_type TEXT, volume_id INTEGER
 * DEFAULT 0)
 * 
 * @author andy aspell-clark
 * 
 */
public class NookBook {

    public static final String COLUMN_NAME_authors = "authors";
    public static final String COLUMN_NAME_category = "category";
    public static final String COLUMN_NAME_cover_image = "cover_image";
    public static final String COLUMN_NAME_data = "_data";
    public static final String COLUMN_NAME_date_added = "date_added";
    public static final String COLUMN_NAME_date_last_accessed = "date_last_accessed";
    public static final String COLUMN_NAME_date_modified = "date_modified";
    public static final String COLUMN_NAME_date_published = "date_published";
    public static final String COLUMN_NAME_display_name = "_display_name";
    public static final String COLUMN_NAME_ean = "ean";
    public static final String COLUMN_NAME_launcher_type = "launcher_type";
    public static final String COLUMN_NAME_mainAuthorFirstName = "mainAuthorFirstName";
    public static final String COLUMN_NAME_mainAuthorLastName = "mainAuthorLastName";
    public static final String COLUMN_NAME_mainAuthorMiddleName = "mainAuthorMiddleName";
    public static final String COLUMN_NAME_MIME_TYPE = "mime_type";
    public static final String COLUMN_NAME_page_count = "page_count";
    public static final String COLUMN_NAME_product_type = "product_type";
    public static final String COLUMN_NAME_publisher = "publisher";
    public static final String COLUMN_NAME_rating = "rating";
    public static final String COLUMN_NAME_size = "_size";
    public static final String COLUMN_NAME_storage_location = "storage_location";
    public static final String COLUMN_NAME_thumb_image = "thumb_image";
    public static final String COLUMN_NAME_title = "title";
    public static final String COLUMN_NAME_user_rating = "user_rating";
    public static final String COLUMN_NAME_VALID = "valid";
    public static final String COLUMN_NAME_VOLUME_ID = "volume_id";

    public static final String DATABASE_TABLE_NAME = "docs";

    public long _id;
    public String authors;
    public String category;
    public String _data;
    public long date_added;
    public long date_last_accessed;
    public long date_modified;
    public long date_published;
    public String _display_name;
    public String ean;
    public String launcher_type;
    public String mainAuthorFirstName;
    public String mainAuthorLastName;
    public String mainAuthorMiddleName;
    public String mime_type;
    public String page_count;
    public long product_type;
    public String publisher;
    public long rating;
    public long _size;
    public long storage_location;
    public String cover_image;
    public String thumb_image;
    public String title;
    public long user_rating;
    public long valid;
    public long volume_id;

    public NookBook(final Cursor tmpCursor) {
        try {
            this._id = tmpCursor.getLong(tmpCursor.getColumnIndex(BaseColumns._ID));
        } catch (final Exception e) {
        }
        try {
            this.date_added = tmpCursor.getLong(tmpCursor.getColumnIndex(NookBook.COLUMN_NAME_date_added));
        } catch (final Exception e) {
        }
        try {
            this.date_last_accessed = tmpCursor.getLong(tmpCursor
                    .getColumnIndex(NookBook.COLUMN_NAME_date_last_accessed));
        } catch (final Exception e) {
        }
        try {
            this.date_modified = tmpCursor.getLong(tmpCursor.getColumnIndex(NookBook.COLUMN_NAME_date_modified));
        } catch (final Exception e) {
        }
        try {
            this.date_published = tmpCursor.getLong(tmpCursor.getColumnIndex(NookBook.COLUMN_NAME_date_published));
        } catch (final Exception e) {
        }
        try {
            this.product_type = tmpCursor.getLong(tmpCursor.getColumnIndex(NookBook.COLUMN_NAME_product_type));
        } catch (final Exception e) {
        }
        try {
            this.rating = tmpCursor.getLong(tmpCursor.getColumnIndex(NookBook.COLUMN_NAME_rating));
        } catch (final Exception e) {
        }
        try {
            this._size = tmpCursor.getLong(tmpCursor.getColumnIndex(NookBook.COLUMN_NAME_size));
        } catch (final Exception e) {
        }
        try {
            this.storage_location = tmpCursor.getLong(tmpCursor.getColumnIndex(NookBook.COLUMN_NAME_storage_location));
        } catch (final Exception e) {
        }
        try {
            this.user_rating = tmpCursor.getLong(tmpCursor.getColumnIndex(NookBook.COLUMN_NAME_user_rating));
        } catch (final Exception e) {
        }
        try {
            this.valid = tmpCursor.getLong(tmpCursor.getColumnIndex(NookBook.COLUMN_NAME_VALID));
        } catch (final Exception e) {
        }
        try {
            this.volume_id = tmpCursor.getLong(tmpCursor.getColumnIndex(NookBook.COLUMN_NAME_VOLUME_ID));
        } catch (final Exception e) {
        }
        this.authors = tmpCursor.getString(tmpCursor.getColumnIndex(NookBook.COLUMN_NAME_authors));
        this.category = tmpCursor.getString(tmpCursor.getColumnIndex(NookBook.COLUMN_NAME_category));
        this._data = tmpCursor.getString(tmpCursor.getColumnIndex(NookBook.COLUMN_NAME_data));
        this._display_name = tmpCursor.getString(tmpCursor.getColumnIndex(NookBook.COLUMN_NAME_display_name));
        this.ean = tmpCursor.getString(tmpCursor.getColumnIndex(NookBook.COLUMN_NAME_ean));
        this.launcher_type = tmpCursor.getString(tmpCursor.getColumnIndex(NookBook.COLUMN_NAME_launcher_type));
        this.mainAuthorFirstName = tmpCursor.getString(tmpCursor
                .getColumnIndex(NookBook.COLUMN_NAME_mainAuthorFirstName));
        this.mainAuthorLastName = tmpCursor
                .getString(tmpCursor.getColumnIndex(NookBook.COLUMN_NAME_mainAuthorLastName));
        this.mainAuthorMiddleName = tmpCursor.getString(tmpCursor
                .getColumnIndex(NookBook.COLUMN_NAME_mainAuthorMiddleName));
        this.mime_type = tmpCursor.getString(tmpCursor.getColumnIndex(NookBook.COLUMN_NAME_MIME_TYPE));
        this.page_count = tmpCursor.getString(tmpCursor.getColumnIndex(NookBook.COLUMN_NAME_page_count));
        this.publisher = tmpCursor.getString(tmpCursor.getColumnIndex(NookBook.COLUMN_NAME_publisher));
        this.cover_image = tmpCursor.getString(tmpCursor.getColumnIndex(NookBook.COLUMN_NAME_cover_image));
        this.thumb_image = tmpCursor.getString(tmpCursor.getColumnIndex(NookBook.COLUMN_NAME_thumb_image));
        this.title = tmpCursor.getString(tmpCursor.getColumnIndex(NookBook.COLUMN_NAME_title));
    }

    public ContentValues getContentValues() {
        final ContentValues map = new ContentValues();
        if (this._id > -1) {
            map.put(BaseColumns._ID, this._id);
        }
        map.put(NookBook.COLUMN_NAME_authors, this.authors);
        map.put(NookBook.COLUMN_NAME_category, this.category);
        map.put(NookBook.COLUMN_NAME_data, this._data);
        map.put(NookBook.COLUMN_NAME_date_added, this.date_added);
        map.put(NookBook.COLUMN_NAME_date_last_accessed, this.date_last_accessed);
        map.put(NookBook.COLUMN_NAME_date_modified, this.date_modified);
        map.put(NookBook.COLUMN_NAME_date_published, this.date_published);
        map.put(NookBook.COLUMN_NAME_display_name, this._display_name);
        map.put(NookBook.COLUMN_NAME_ean, this.ean);
        map.put(NookBook.COLUMN_NAME_launcher_type, this.launcher_type);
        map.put(NookBook.COLUMN_NAME_mainAuthorFirstName, this.mainAuthorFirstName);
        map.put(NookBook.COLUMN_NAME_mainAuthorLastName, this.mainAuthorLastName);
        map.put(NookBook.COLUMN_NAME_mainAuthorMiddleName, this.mainAuthorMiddleName);
        map.put(NookBook.COLUMN_NAME_MIME_TYPE, this.mime_type);
        map.put(NookBook.COLUMN_NAME_page_count, this.page_count);
        map.put(NookBook.COLUMN_NAME_product_type, this.product_type);
        map.put(NookBook.COLUMN_NAME_publisher, this.publisher);
        map.put(NookBook.COLUMN_NAME_rating, this.rating);
        map.put(NookBook.COLUMN_NAME_size, this._size);
        map.put(NookBook.COLUMN_NAME_storage_location, this.storage_location);
        map.put(NookBook.COLUMN_NAME_cover_image, this.cover_image);
        map.put(NookBook.COLUMN_NAME_thumb_image, this.thumb_image);
        map.put(NookBook.COLUMN_NAME_title, this.title);
        map.put(NookBook.COLUMN_NAME_user_rating, this.user_rating);
        map.put(NookBook.COLUMN_NAME_VALID, this.valid);
        map.put(NookBook.COLUMN_NAME_VOLUME_ID, this.volume_id);

        return map;
    }

}
