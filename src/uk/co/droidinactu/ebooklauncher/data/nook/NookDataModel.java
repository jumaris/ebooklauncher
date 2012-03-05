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

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.co.droidinactu.ebooklauncher.EBookLauncherApplication;
import uk.co.droidinactu.ebooklauncher.R;
import uk.co.droidinactu.ebooklauncher.data.AbstractDataModel;
import uk.co.droidinactu.ebooklauncher.data.Book;
import uk.co.droidinactu.ebooklauncher.data.Collection;
import uk.co.droidinactu.ebooklauncher.data.DeviceFactory;
import uk.co.droidinactu.ebooklauncher.view.BookSortBy;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * this is the data model for the Barnes and Nobel Nook Simple Touch.
 * 
 * @author aspela
 */
public class NookDataModel extends AbstractDataModel {

    /** URI used for reading items from the product external db table */
    private static final String Uri_external_products = "content://media/external/products/";

    /** URI used for reading items from the shelf_item external db table */
    private static final String Uri_external_shelf_item = "content://media/external/shelf_item/";

    /** URI used for reading items from the shelf db table */
    private static final String Uri_external_shelf = "content://media/external/shelf/";
    private static final String Uri_internal_docs = "content://media/internal/docs/";
    private static final String Uri_internal_products = "content://media/internal/products/";

    /** URI used for reading items from the shelf internal db table */
    private static final String Uri_internal_shelf_item = "content://media/internal/shelf_item/";

    /** URI used for reading items from the shelf internal db table */
    private static final String Uri_internal_shelf = "content://media/internal/shelf/";

    /** URI used for reading items from the library_items db view */
    private static final String Uri_library_items = "content://media/internal/library_items/";

    /** tag used for logging */
    private static final String LOG_TAG = "NookDataModel:";

    public NookDataModel(final Context ctx) {
        super(ctx);
        Log.i(EBookLauncherApplication.LOG_TAG, NookDataModel.LOG_TAG + "NookDataModel()");
    }

    /**
     * close the database. does nothing on a nook as we use a ContentProvider to read the data.
     */
    @Override
    public void closeDb() {
        Log.i(EBookLauncherApplication.LOG_TAG, NookDataModel.LOG_TAG + "closeDb()");
    }

    @Override
    public Book getBook(final Context cntxt, final long id) {
        final Uri getIncdntsUri = Uri.withAppendedPath(Uri.parse("content://media/internal/docs/"), "" + id);
        final ContentResolver cr = cntxt.getContentResolver();
        final Cursor results = cr.query(getIncdntsUri, null, null, null, null);

        results.moveToFirst();
        final Book bk = DeviceFactory.getBook(results);
        results.close();
        return bk;
    }

    @Override
    public Bitmap getBookCoverImg(final Context cntxt, final String thumbnailFilename) {
        Bitmap bitmap = BitmapFactory.decodeFile(thumbnailFilename);
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.ic_missing_thumbnail_picture);
        }
        return bitmap;
    }

    @Override
    public Cursor getBooks(final Context cntxt, final String filterStr, final BookSortBy sortBy,
            final String searchString) {

        final Uri queryUri = Uri.withAppendedPath(Uri.parse(NookDataModel.Uri_internal_docs), "");
        final ContentResolver cr = cntxt.getContentResolver();

        final String selection = NookBook.COLUMN_NAME_product_type + "=?";
        final String[] selectionArgs = new String[] { "1" };

        final Cursor results = cr.query(queryUri, null, selection, selectionArgs, null);

        return results;
    }

    @Override
    public Cursor getBooksInCollection(final String collectionName, final int start, final int end,
            final BookSortBy sortBy) {
        Collection c = this.getCollectionFromName(collectionName);

        Uri queryUri = Uri.withAppendedPath(Uri.parse(NookDataModel.Uri_internal_shelf_item), "");
        final ContentResolver cr = this.cntxt.getContentResolver();
        String selection = "shelf_id=?";
        String[] selectionArgs = new String[] { "" + c.getUniqueIdentifier() };
        final Cursor shelfItems = cr.query(queryUri, null, selection, selectionArgs, null);
        shelfItems.moveToFirst();

        queryUri = Uri.withAppendedPath(Uri.parse(NookDataModel.Uri_internal_docs), "");
        selection = "_id IN (SELECT replace(library_item_id,'content://media/internal/docs/','') FROM shelf_item WHERE shelf_id=?)";
        selectionArgs = new String[] { "" + c.getUniqueIdentifier() };
        final Cursor books = cr.query(queryUri, null, selection, selectionArgs, null);

        books.moveToFirst();
        return books;
    }

    @Override
    public Collection getCollection(final long id) {
        final Uri queryUri = Uri.withAppendedPath(Uri.parse(NookDataModel.Uri_internal_shelf), "" + id);
        final ContentResolver cr = this.cntxt.getContentResolver();
        final Cursor results = cr.query(queryUri, null, null, null, null);
        results.moveToFirst();
        Collection c = new Collection();
        c.title = results.getString(1);
        c.nbrItemsInCollections = results.getInt(3);
        return c;
    }

    @Override
    public Cursor getCollection(final String collectionName) {
        final Uri queryUri = Uri.withAppendedPath(Uri.parse(NookDataModel.Uri_internal_shelf), "");
        final ContentResolver cr = this.cntxt.getContentResolver();
        String selection = "shelf_name=?";
        String[] selectionArgs = new String[] { collectionName };
        final Cursor results = cr.query(queryUri, null, selection, selectionArgs, null);
        results.moveToFirst();
        return results;
    }

    public Collection getCollectionFromName(final String collectionName) {
        final Uri queryUri = Uri.withAppendedPath(Uri.parse(NookDataModel.Uri_internal_shelf), "");
        final ContentResolver cr = this.cntxt.getContentResolver();
        String selection = "shelf_name=?";
        String[] selectionArgs = new String[] { collectionName };
        final Cursor results = cr.query(queryUri, null, selection, selectionArgs, null);
        results.moveToFirst();
        Collection c = new Collection();
        c.setUniqueIdentifier(results.getLong(0));
        c.title = results.getString(1);
        c.nbrItemsInCollections = results.getInt(3);
        return c;
    }

    @Override
    public List<String> getCollectionNames(final Context cntxt) {
        final Cursor results = this.getCollections(0, 999, null);
        final List<String> retVal = new ArrayList<String>();
        results.moveToFirst();
        while (results.isAfterLast() == false) {
            // final Object tmpObjId = results.getString(0);
            final Object tmpObjName = results.getString(1);
            // final Object tmpObjPosition = results.getString(2);
            // final Object tmpObjNbrItems = results.getString(3);
            if (!retVal.contains(tmpObjName.toString())) {
                retVal.add(tmpObjName.toString());
            }
            results.moveToNext();
        }
        results.close();
        return retVal;
    }

    @Override
    public Cursor getCollections(final int start, final int nbrItems, final String filterChar) {
        final Uri queryUri = Uri.withAppendedPath(Uri.parse(NookDataModel.Uri_internal_shelf), "");
        final ContentResolver cr = this.cntxt.getContentResolver();
        final Cursor results = cr.query(queryUri, null, null, null, null);
        return results;
    }

    @Override
    public Cursor getCurrentlyReading(final Context cntxt, final int start, final int nbrItems, final BookSortBy sortBy) {
        String orderby = NookBook.COLUMN_NAME_date_last_accessed + " desc";
        if (sortBy != null) {
            switch (sortBy) {
            case TITLE:
                orderby = NookBook.COLUMN_NAME_title + " asc";
                break;
            case AUTHOR:
                orderby = NookBook.COLUMN_NAME_authors + " asc";
                break;
            case FILENAME:
                orderby = NookBook.COLUMN_NAME_display_name + " asc";
                break;
            default:
                orderby = NookBook.COLUMN_NAME_date_last_accessed + " desc";
                break;
            }
        }

        final String selection = NookBook.COLUMN_NAME_date_last_accessed + " not null";
        final String[] selectionArgs = null;
        orderby += " limit " + start + "," + nbrItems;

        final Uri queryUri = Uri.withAppendedPath(Uri.parse(NookDataModel.Uri_internal_docs), "");
        final ContentResolver cr = this.cntxt.getContentResolver();
        final Cursor results = cr.query(queryUri, null, selection, selectionArgs, orderby);
        results.moveToFirst();
        return results;
    }

    @Override
    public String getDbFilename() {
        return "";
    }

    public NookBook getNookBook(final Context cntxt, final long id) {
        final Uri getIncdntsUri = Uri.withAppendedPath(Uri.parse("content://media/internal/docs/"), "" + id);
        final ContentResolver cr = cntxt.getContentResolver();
        final Cursor results = cr.query(getIncdntsUri, null, null, null, null);

        results.moveToFirst();
        final NookBook bk = new NookBook(results);
        results.close();
        return bk;
    }

    @Override
    public Cursor getPeriodicals(final Context cntxt, final int start, final int nbrItems) {

        final Uri queryUri = Uri.withAppendedPath(Uri.parse(NookDataModel.Uri_internal_docs), "");
        final ContentResolver cr = cntxt.getContentResolver();

        final String selection = NookBook.COLUMN_NAME_product_type + "=?";
        final String[] selectionArgs = new String[] { "2" };

        final Cursor results = cr.query(queryUri, null, selection, selectionArgs, null);

        return results;
    }

    @Override
    public Cursor getPictures(final Context cntxt) {
        return null;
    }

    @Override
    public Cursor getRecentlyAdded(final Context cntxt, final int start, final int nbrItems) {
        final String orderby = NookBook.COLUMN_NAME_date_added + " desc limit " + start + "," + nbrItems;
        final String selection = NookBook.COLUMN_NAME_date_added + " not null";
        final String[] selectionArgs = null;

        final Uri queryUri = Uri.withAppendedPath(Uri.parse(NookDataModel.Uri_internal_docs), "");
        final ContentResolver cr = this.cntxt.getContentResolver();
        final Cursor results = cr.query(queryUri, null, selection, selectionArgs, orderby);
        results.moveToFirst();
        return results;
    }

    @Override
    public boolean isOpened() {
        return true;
    }

    /**
     * this function reads the book data from the database, then launches 
     * an intent to open the book.
     */
    @Override
    public void launchBook(final Context cntxt, final long id) {
        Log.i(EBookLauncherApplication.LOG_TAG, NookDataModel.LOG_TAG + "launchBook()");
        final NookBook bk = this.getNookBook(cntxt, id);
        final Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        final File file = new File(bk._data);
        String filetype = bk.mime_type;
        if ((filetype == null) && bk._data.endsWith("epub")) {
            filetype = "application/epub+zip";
        } else if ((filetype == null) && bk._data.endsWith("pdf")) {
            filetype = "application/pdf";
        }
        intent.setDataAndType(Uri.fromFile(file), filetype);
        cntxt.startActivity(intent);

        // update database date_last_accessed column

        final String selection = BaseColumns._ID + "=?";
        final String[] selectionArgs = new String[] { "" + id };
        final Uri queryUri = Uri.withAppendedPath(Uri.parse(NookDataModel.Uri_internal_docs), "");
        final ContentResolver cr = this.cntxt.getContentResolver();
        bk.date_last_accessed = new Date().getTime();
        int retVal = cr.update(queryUri, bk.getContentValues(), selection, selectionArgs);
        Log.i(EBookLauncherApplication.LOG_TAG, NookDataModel.LOG_TAG + "launchBook() update db returned ["
                + retVal
                + "]");
    }

    /**
     * open the database. does nothing on a nook as we use a ContentProvider to read the data.
     */
    @Override
    public void openDb() {
        Log.i(EBookLauncherApplication.LOG_TAG, NookDataModel.LOG_TAG + "openDb()");
    }

}
