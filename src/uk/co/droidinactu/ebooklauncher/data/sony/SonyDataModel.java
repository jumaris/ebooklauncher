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
package uk.co.droidinactu.ebooklauncher.data.sony;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import uk.co.droidinactu.ebooklauncher.EBookLauncherApplication;
import uk.co.droidinactu.ebooklauncher.R;
import uk.co.droidinactu.ebooklauncher.data.AbstractDataModel;
import uk.co.droidinactu.ebooklauncher.data.Book;
import uk.co.droidinactu.ebooklauncher.data.Collection;
import uk.co.droidinactu.ebooklauncher.data.Periodical;
import uk.co.droidinactu.ebooklauncher.data.TextNote;
import uk.co.droidinactu.ebooklauncher.view.BookSortBy;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

public final class SonyDataModel extends AbstractDataModel {

    /** tag used for logging */
    private static final String LOG_TAG = "SonyDataModel:";
    private final SonyDatabaseController mDbCntlr;

    public SonyDataModel(final Context ctx) {
        super(ctx);
        this.mDbCntlr = new SonyDatabaseController(this.cntxt);
    }

    private Cursor addVirtualCollections(final Cursor cInt) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void closeDb() {
        this.mDbCntlr.close();
    }

    @Override
    public Book getBook(final Context cntxt, final long id) {
        String sqlStr = "select * from " + Book.DATABASE_TABLE_NAME + " where _id=" + id;
        if (this.mDbCntlr.myDBExt) {
            sqlStr = "select * from " + Book.DATABASE_TABLE_NAME
                    + " where _id="
                    + id
                    + " union all select * from extDb."
                    + Book.DATABASE_TABLE_NAME
                    + " where _id="
                    + id;
        }
        final Cursor cInt = this.mDbCntlr.query(sqlStr, null);
        cInt.moveToFirst();
        final Book bk = new Book(cInt);
        cInt.close();
        return bk;
    }

    @Override
    public Bitmap getBookCoverImg(final Context cntxt, final String thumbnailFilename) {
        Bitmap bitmap = BitmapFactory.decodeFile("/mnt/sdcard/" + thumbnailFilename);
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeFile("/mnt/extsd/" + thumbnailFilename);
        }
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.ic_missing_thumbnail_picture);
        }
        return bitmap;
    }

    @Override
    public Cursor getBooks(final Context cntxt, final String filterStr, final BookSortBy sortBy,
            final String searchString) {
        String whereClause = "";
        if ((filterStr.length() > 0) && (searchString.length() == 0)) {
            whereClause += " where " + this.getSortOrderField(sortBy) + " like '" + filterStr + "%' ";
        } else if ((filterStr.length() > 0) && (searchString.length() > 0)) {
            whereClause += " where " + this.getSortOrderField(sortBy)
                    + " like '"
                    + filterStr
                    + "%"
                    + searchString
                    + "%' ";
        } else if ((filterStr.length() == 0) && (searchString.length() > 0)) {
            whereClause += " where " + this.getSortOrderField(sortBy) + " like '%" + searchString + "%' ";
        }

        String sqlStr = "select * from " + Book.DATABASE_TABLE_NAME
                + whereClause
                + " order by "
                + this.getSortOrderField(sortBy)
                + " asc";

        if (this.mDbCntlr.myDBExt) {
            sqlStr = "select * from " + Book.DATABASE_TABLE_NAME
                    + whereClause
                    + " UNION ALL SELECT * FROM extDb."
                    + Book.DATABASE_TABLE_NAME
                    + whereClause
                    + " order by "
                    + this.getSortOrderField(sortBy)
                    + " asc";
        }

        if (filterStr.equals("0")) {
            final StringBuilder likeStr = new StringBuilder(" title like");
            for (int x = 0; x <= 9; x++) {
                likeStr.append(" '" + x + "%' or title like ");
            }
            likeStr.replace(likeStr.lastIndexOf("or"), likeStr.length(), "");
            sqlStr = "select * from " + Book.DATABASE_TABLE_NAME
                    + " where "
                    + likeStr
                    + " order by "
                    + this.getSortOrderField(sortBy)
                    + " asc";
            if (this.mDbCntlr.myDBExt) {
                sqlStr = "select * from " + Book.DATABASE_TABLE_NAME
                        + " where "
                        + likeStr
                        + " UNION ALL SELECT * FROM extDb."
                        + Book.DATABASE_TABLE_NAME
                        + " where "
                        + likeStr
                        + " order by "
                        + this.getSortOrderField(sortBy)
                        + " asc";
            }
        }

        return this.mDbCntlr.query(sqlStr, null);
    }

    @Override
    public Cursor getBooksInCollection(final String collectionName, final int start, final int end,
            final BookSortBy sortBy) {

        String sqlStr = "select books.* from books,collection,collections where books._id=collections.content_id and " + " collections.collection_id=collection._id and collection.title=? order by "
                + this.getSortOrderField(sortBy)
                + " asc limit "
                + start
                + ","
                + end;
        String[] selectionArgs = new String[] { collectionName };
        if (this.mDbCntlr.myDBExt) {
            sqlStr = "select books.* from books,collection,collections where (books._id=collections.content_id and " + " collections.collection_id=collection._id and collection.title=?)"
                    + " union all select books.* from extdb.books,extdb.collection,extdb.collections where (extdb.books._id=extdb.collections.content_id and "
                    + " extdb.collections.collection_id=extdb.collection._id and extdb.collection.title=?)"
                    + " order by "
                    + this.getSortOrderField(sortBy)
                    + " asc limit "
                    + start
                    + ","
                    + end;
            selectionArgs = new String[] { collectionName, collectionName };
        }
        return this.mDbCntlr.query(sqlStr, selectionArgs);
    }

    @Override
    public Collection getCollection(final long id) {
        String sqlStr = "select * from " + Collection.DATABASE_TABLE_NAME
                + " where _id='"
                + id
                + "'  order by title asc";
        if (this.mDbCntlr.myDBExt) {
            sqlStr = "select * from " + Collection.DATABASE_TABLE_NAME
                    + " where _id='"
                    + id
                    + "' UNION ALL SELECT * FROM extDb."
                    + Collection.DATABASE_TABLE_NAME
                    + " final where _id='"
                    + id
                    + "' order by title asc";
        }
        final Cursor cInt = this.mDbCntlr.query(sqlStr, null);
        cInt.moveToFirst();
        final Collection b = new Collection(cInt);
        cInt.close();
        return b;
    }

    @Override
    public Cursor getCollection(final String collectionName) {
        String sqlStr = "select distinct title from " + Collection.DATABASE_TABLE_NAME
                + " where title='"
                + collectionName
                + "' order by title asc";
        if (this.mDbCntlr.myDBExt) {
            sqlStr = "select distinct title from " + Collection.DATABASE_TABLE_NAME
                    + " where title='"
                    + collectionName
                    + "' UNION ALL SELECT * FROM extDb."
                    + Collection.DATABASE_TABLE_NAME
                    + " final where title='"
                    + collectionName
                    + "' order by title asc";
        }
        return this.mDbCntlr.query(sqlStr, null);
    }

    @Override
    public List<String> getCollectionNames(final Context cntxt) {
        final Cursor cInt = this.getCollections(0, 999, null);

        final List<String> retVal = new ArrayList<String>();
        cInt.moveToFirst();
        while (cInt.isAfterLast() == false) {
            final Object tmpObj = cInt.getString(1);
            if (!retVal.contains(tmpObj.toString())) {
                retVal.add(tmpObj.toString());
            }
            cInt.moveToNext();
        }
        cInt.close();

        retVal.add(0, cntxt.getResources().getString(R.string.pref_collection_periodicals));
        retVal.add(0, cntxt.getResources().getString(R.string.pref_collection_recently_added));

        Collections.sort(retVal);
        return retVal;
    }

    @Override
    public Cursor getCollections(final int start, final int nbrItems, final String filterChar) {

        String sqlStr = "select * from Collection group by title order by title asc";
        if (this.mDbCntlr.myDBExt) {
            sqlStr = "select * from (select * from Collection union all select * from extdb.Collection) " + " where title in (select distinct title from (select * from Collection union all select * from extdb.Collection) ) "
                    + " group by title order by title asc";
        }

        if ((filterChar != null) && filterChar.equals("0")) {
            final StringBuilder likeStr = new StringBuilder(" title like");
            for (int x = 0; x <= 9; x++) {
                likeStr.append(" '" + x + "%' or title like ");
            }
            likeStr.replace(likeStr.lastIndexOf("or"), likeStr.length(), "");
            sqlStr = "select * from " + Collection.DATABASE_TABLE_NAME
                    + " where "
                    + likeStr
                    + " order by "
                    + Collection.COLUMN_NAME_TITLE
                    + " asc";
            if (this.mDbCntlr.myDBExt) {
                sqlStr = "select * from (select * from Collection union all select * from extdb.Collection) " + " where title in (select distinct title from (select * from Collection union all select * from extdb.Collection) ) "
                        + " and ("
                        + likeStr
                        + ") group by title order by title asc";
            }
        } else if (filterChar != null) {
            sqlStr = "select * from " + Collection.DATABASE_TABLE_NAME
                    + " where title like '"
                    + filterChar
                    + "%' order by title asc";
            if (this.mDbCntlr.myDBExt) {
                sqlStr = "select * from (select * from Collection union all select * from extdb.Collection) " + " where title in (select distinct title from (select * from Collection union all select * from extdb.Collection) ) "
                        + " and title like '"
                        + filterChar
                        + "%' group by title order by title asc";
            }
        }

        final Cursor cInt = this.mDbCntlr.query(sqlStr, null);
        // Add periodicals & recent add to cursor
        return cInt;
    }

    @Override
    public Cursor getCurrentlyReading(final Context cntxt, final int start, final int nbrItems, final BookSortBy sortBy) {
        String orderby = Book.COLUMN_NAME_READING_TIME + " desc";
        if (sortBy != null) {
            switch (sortBy) {
            case TITLE:
                orderby = Book.COLUMN_NAME_TITLE + " asc";
                break;
            case AUTHOR:
                orderby = Book.COLUMN_NAME_AUTHOR + " asc";
                break;
            case FILENAME:
                orderby = Book.COLUMN_NAME_FILE_NAME + " asc";
                break;
            default:
                orderby = Book.COLUMN_NAME_READING_TIME + " desc";
                break;
            }
        }
        String sqlStr = "select * from " + Book.DATABASE_TABLE_NAME
                + " where reading_time not null order by "
                + orderby
                + " limit "
                + start
                + ","
                + nbrItems;
        if (this.mDbCntlr.myDBExt) {
            sqlStr = "select * from " + Book.DATABASE_TABLE_NAME
                    + " where reading_time not null UNION ALL SELECT * FROM extDb."
                    + Book.DATABASE_TABLE_NAME
                    + " where reading_time not null order by "
                    + orderby
                    + " limit "
                    + start
                    + ","
                    + nbrItems;
        }
        return this.mDbCntlr.query(sqlStr, null);
    }

    @Override
    public String getDbFilename() {
        return SonyDatabaseController.DATABASE_PATH_INT + SonyDatabaseController.DATABASE_NAME;
    }

    @Override
    public Cursor getPeriodicals(final Context cntxt, final int start, final int nbrItems) {
        String sqlStr = "select * from (select * from " + Periodical.DATABASE_TABLE_NAME
                + ") as p,books where books._id=p._id  order by "
                + Periodical.COLUMN_NAME_PERIODICAL_NAME
                + " asc limit "
                + start
                + ","
                + nbrItems;
        if (this.mDbCntlr.myDBExt) {
            sqlStr = "select * from (select * from " + Periodical.DATABASE_TABLE_NAME
                    + " UNION ALL SELECT * FROM extDb."
                    + Periodical.DATABASE_TABLE_NAME
                    + ") as p,books where books._id=p._id  order by "
                    + Periodical.COLUMN_NAME_PERIODICAL_NAME
                    + " asc limit "
                    + start
                    + ","
                    + nbrItems;
        }
        return this.mDbCntlr.query(sqlStr, null);
    }

    @Override
    public Cursor getPictures(final Context cntxt) {
        return null;
    }

    @Override
    public Cursor getRecentlyAdded(final Context cntxt, final int start, final int nbrItems) {
        String sqlStr = "select * from " + Book.DATABASE_TABLE_NAME
                + " where added_date not null order by added_date desc limit "
                + start
                + ","
                + nbrItems;
        if (this.mDbCntlr.myDBExt) {
            sqlStr = "select * from " + Book.DATABASE_TABLE_NAME
                    + " where added_date not null UNION ALL SELECT * FROM extDb."
                    + Book.DATABASE_TABLE_NAME
                    + " where added_date not null order by added_date desc limit "
                    + start
                    + ","
                    + nbrItems;
        }
        return this.mDbCntlr.query(sqlStr, null);
    }

    @Override
    public boolean isOpened() {
        return this.mDbCntlr.isOpened;
    }

    @Override
    public void launchBook(final Context cntxt, final long id) {
        super.launchBook(cntxt, id);

        // update reading_time in database
        final long reading_time = new Date().getTime();
        final String sql = "update books set reading_time=" + reading_time + " where _id=" + id;
        try {
            this.mDbCntlr.execSql(sql);
        } catch (final Exception e) {
            Log.e(EBookLauncherApplication.LOG_TAG, SonyDataModel.LOG_TAG + "Exception executing sqlite.execSQL ["
                    + sql
                    + "]", e);
        }
    }

    public void launchTextNote(final Context cntxt, final TextNote tn) {
        final Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        final File file = new File("/mnt/sdcard/" + tn.fullFilename);
        intent.setDataAndType(Uri.fromFile(file), tn.mimetype);
        cntxt.startActivity(intent);
    }

    @Override
    public void openDb() {
        this.mDbCntlr.open();
    }

}
