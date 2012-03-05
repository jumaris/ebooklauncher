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
package uk.co.droidinactu.ebooklauncher.data.calibre;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.co.droidinactu.common.model.AbstractDataObj;
import uk.co.droidinactu.ebooklauncher.EBookLauncherApplication;
import uk.co.droidinactu.ebooklauncher.data.AbstractDataModel;
import uk.co.droidinactu.ebooklauncher.data.Book;
import uk.co.droidinactu.ebooklauncher.data.Collection;
import uk.co.droidinactu.ebooklauncher.view.BookSortBy;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.util.Log;

public class CalibreDataModel extends AbstractDataModel {

    /** tag used for logging */
    private static final String LOG_TAG = "CalibreDataModel:";
    private CalibreDatabaseController mDbCntlr;

    public CalibreDataModel(final Context ctx) {
        super(ctx);
        this.mDbCntlr = new CalibreDatabaseController(this.cntxt);
    }

    public long addAuthor(final String anAuthor) {
        Author tmpAuth = new Author(anAuthor);
        final Cursor results = this.mDbCntlr.query(tmpAuth.getSelectAuthor(), new String[] {});

        if (results.getCount() == 0) {
            try {
                final Date now = new Date();
                tmpAuth.setLastUpdated(now.getTime());

                this.mDbCntlr.insert(tmpAuth);

                tmpAuth = this.getAuthor(tmpAuth);
            } catch (final SQLException e) {
                Log.e(EBookLauncherApplication.LOG_TAG + CalibreDataModel.LOG_TAG,
                        "Error inserting new author : " + e.toString(), e);
            }
        }
        return tmpAuth.getUniqueIdentifier();
    }

    public Integer addAuthorBookLink(final Author anAuthor, final CalibreBook ebk) {
        final AuthorEbookLink tmpAuth = new AuthorEbookLink(anAuthor.getUniqueIdentifier(), ebk.getUniqueIdentifier());
        final Date now = new Date();
        tmpAuth.setLastUpdated(now.getTime());
        this.mDbCntlr.insert(new AuthorEbookLink().getTableName(), null, tmpAuth.getContentValues());
        return null;
    }

    public Integer addAuthorBookLink(final String authorName, final CalibreBook ebk) {
        Author anAuthor = this.getAuthor(authorName);
        if (anAuthor == null) {
            final Long authId = this.addAuthor(authorName);
            anAuthor = this.getAuthor(authId);
        }
        return this.addAuthorBookLink(anAuthor, ebk);
    }

    public Long addBook(CalibreBook eBk) {
        long id = -1;
        try {
            eBk.setLastUpdated(new Date().getTime());

            id = this.mDbCntlr.insert(eBk);

        } catch (final SQLException e) {
            Log.e(EBookLauncherApplication.LOG_TAG + CalibreDataModel.LOG_TAG,
                    "Error inserting new author : " + e.toString(), e);
        }
        return id;
    }

    public Long addTag(final String aTag) {
        final Tag tmpTag = new Tag(aTag);
        final Cursor results = this.mDbCntlr.selectData(tmpTag.getSelectTag());

        if (results.getCount() == 0) {
            try {
                final Date now = new Date();
                tmpTag.setLastUpdated(now.getTime());
                this.mDbCntlr.insert(tmpTag.getTableName(), null, tmpTag.getContentValues());
            } catch (final SQLException e) {
                Log.e(EBookLauncherApplication.LOG_TAG + CalibreDataModel.LOG_TAG,
                        "Error inserting new author : " + e.toString(), e);
            }
        }
        return null;
    }

    public Integer addTagBookLink(final String tagName, final CalibreBook ebk) {
        Tag aTag = this.getTag(tagName);
        if (aTag == null) {
            this.addTag(tagName);
            aTag = this.getTag(tagName);
        }
        return this.addTagBookLink(aTag, ebk);
    }

    public Integer addTagBookLink(final Tag aTag, final CalibreBook ebk) {
        final TagEbookLink tmpTgBk = new TagEbookLink(aTag.getUniqueIdentifier(), ebk.getUniqueIdentifier());
        final Date now = new Date();
        tmpTgBk.setLastUpdated(now.getTime());
        this.mDbCntlr.insert(tmpTgBk);
        return null;
    }

    public void clearDb() {
        this.openDb();
        this.mDbCntlr.executeSql(new CalibreBook().getSqlDeleteAll());
        this.mDbCntlr.executeSql(new Author().getSqlDeleteAll());
        this.mDbCntlr.executeSql(new AuthorEbookLink().getSqlDeleteAll());
        this.mDbCntlr.executeSql(new Tag().getSqlDeleteAll());
        this.mDbCntlr.executeSql(new TagEbookLink().getSqlDeleteAll());
        this.mDbCntlr.executeSql(new RecentEBook().getSqlDeleteAll());
    }

    @Override
    public void closeDb() {
        this.mDbCntlr.close();
    }

    public Author getAuthor(final Author anAuthor) {
        final Cursor results = this.mDbCntlr.selectData(anAuthor.getSelectByLastUpdate());

        Author auth = null;
        if (results.getCount() > 0) {
            results.moveToFirst();
            auth = new Author(results);
        }
        results.close();
        return auth;
    }

    public Author getAuthor(final Long objId) {
        final Cursor results = this.mDbCntlr.selectData(new Author().getSelectByIdSql(objId));

        Author auth = null;
        if (results.getCount() > 0) {
            results.moveToFirst();
            auth = new Author(results);
        }
        results.close();
        return auth;
    }

    public Author getAuthor(final String authorName) {
        final Cursor results = this.mDbCntlr.selectData(new Author(authorName).getSelectAuthor());

        Author auth = null;
        if (results.getCount() > 0) {
            results.moveToFirst();
            auth = new Author(results);
        }
        results.close();
        return auth;
    }

    public ArrayList<String> getAuthors() {
        // if (authorsCursor == null) { final Uri getIncdntsUri =
        // Uri.withAppendedPath(InLibrisLibertasData.EBooks.CONTENT_URI, "");
        // final ContentResolver cr = contxt.getContentResolver();
        // authorsCursor = cr.query(getIncdntsUri, null, null, null, null); }
        // return authorsCursor;
        final ArrayList<String> tmpList = new ArrayList<String>();
        final Cursor results = this.mDbCntlr.selectData(new Author().getSelectAllSql());

        results.moveToFirst();
        for (int x = 0; x < results.getCount(); x++) {
            final Author auth = new Author(results);
            tmpList.add(auth.getAuthorName());
            results.moveToNext();
        }
        results.close();
        return tmpList;
    }

    public ArrayList<Author> getAuthorsForBook(final CalibreBook eBk) {
        final ArrayList<Author> tmpList = new ArrayList<Author>();

        final Cursor results = this.mDbCntlr.selectData(new Author().getSelectAllForBook(eBk.getUniqueIdentifier()));

        results.moveToFirst();
        for (int x = 0; x < results.getCount(); x++) {
            final Author auth = new Author(results);
            tmpList.add(auth);
            results.moveToNext();
        }
        results.close();
        return tmpList;
    }

    /**
     * get a book from the calibre database that matches the title and author of the Sony Book 
     * @param eBk
     * @return
     */
    public CalibreBook getBook(Book eBk) {
        CalibreBook tmpBk = new CalibreBook();
        tmpBk.authorString = eBk.author;
        tmpBk.title = eBk.title;
        return this.getBook(tmpBk);
    }

    public CalibreBook getBook(CalibreBook eBk) {
        final Cursor results = this.mDbCntlr.query(eBk.getSqlSelectBookFromTitleAndAuthor());

        results.moveToFirst();
        CalibreBook ebk = null;
        if (results.getCount() > 0) {
            results.moveToFirst();
            ebk = new CalibreBook(results);
            ebk.authors = this.getAuthorsForBook(ebk);
            ebk.tags = this.getTagsForBook(ebk);
        }
        results.close();
        return ebk;
    }

    @Override
    public Book getBook(Context cntxt, long id) {
        CalibreBook ebk = this.getBook(id);
        Book bk = new Book(ebk);
        return bk;
    }

    public CalibreBook getBook(final long bkId) {
        final Cursor results = this.mDbCntlr.query(new Book().getSelectByIdSql(bkId));

        results.moveToFirst();
        CalibreBook ebk = null;
        if (results.getCount() > 0) {
            results.moveToFirst();
            ebk = new CalibreBook(results);
            ebk.authors = this.getAuthorsForBook(ebk);
            ebk.tags = this.getTagsForBook(ebk);
        }
        results.close();
        return ebk;
    }

    @Override
    public Bitmap getBookCoverImg(final Context cntxt, final String thumbnailFilename) {
        // TODO Auto-generated method stub
        return null;
    }

    public Cursor getBooks() {
        Cursor results = this.mDbCntlr.query(new Book().getSelectAllSql());
        return results;
    }

    @Override
    public Cursor getBooks(final Context cntxt, final String filterStr, final BookSortBy sortBy,
            final String searchString) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<CalibreBook> getBooksForAuthor(final String aStr) {
        // throw new NotImplementedException();
        return null;
    }

    @Override
    public Cursor getBooksInCollection(final String collectionName, final int start, final int end,
            final BookSortBy sortBy) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<CalibreBook> getBooksInSeries(final String tag) {
        // throw new NotImplementedException();
        return null;
    }

    public List<CalibreBook> getBooksWithTag(final String aStr) {
        // throw new NotImplementedException();
        return null;
    }

    @Override
    public Collection getCollection(final long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Cursor getCollection(final String collectionName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> getCollectionNames(final Context cntxt) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Cursor getCollections(final int start, final int nbrItems, final String filterChar) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Cursor getCurrentlyReading(final Context cntxt, final int start, final int nbrItems, final BookSortBy sortBy) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDbFilename() {
        // TODO Auto-generated method stub
        return null;
    }

    private DbMetadata getDbMetadata() {
        final Cursor results = this.mDbCntlr.selectData(new DbMetadata().getSelectAllSql());

        DbMetadata dbMetaDta = null;
        if (results.getCount() > 0) {
            results.moveToFirst();
            dbMetaDta = new DbMetadata(results);
        }
        results.close();
        return dbMetaDta;
    }

    @Override
    public Cursor getPeriodicals(final Context cntxt, final int start, final int nbrItems) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Cursor getPictures(final Context cntxt) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Cursor getRecentlyAdded(final Context cntxt, final int start, final int nbrItems) {
        // TODO Auto-generated method stub
        return null;
    }

    public ArrayList<String> getSeries() {
        final ArrayList<String> tmpList = new ArrayList<String>();
        final Cursor results = this.mDbCntlr.selectData(new CalibreBook().getSelectAllSeries());

        results.moveToFirst();
        if (results.getCount() > 0) {
            tmpList.add(results.getString(results.getColumnIndex(CalibreBook.FIELD_NAME_series)));
            results.moveToNext();
        }
        results.close();
        return tmpList;
    }

    public Tag getTag(final String tagName) {
        final Cursor results = this.mDbCntlr.selectData(new Tag().getSelectTag(tagName));

        Tag tg = null;
        if (results.getCount() > 0) {
            results.moveToFirst();
            tg = new Tag();
            tg.setLastUpdated(results.getString(results.getColumnIndex(BaseColumns._ID)));
            tg.setUniqueIdentifier(results.getString(results.getColumnIndex(AbstractDataObj.FIELD_NAME_LAST_UPDATED)));
            tg.name = results.getString(results.getColumnIndex(Tag.FIELD_NAME_TAG_NAME));
        }
        results.close();
        return tg;
    }

    public Tag getTag(final Tag tmpBk) {
        final Cursor results = this.mDbCntlr.selectData(tmpBk.getSelectByLastUpdate());

        Tag tg = null;
        if (results.getCount() > 0) {
            results.moveToFirst();
            tg = new Tag();
            tg.setLastUpdated(results.getString(results.getColumnIndex(BaseColumns._ID)));
            tg.setUniqueIdentifier(results.getString(results.getColumnIndex(AbstractDataObj.FIELD_NAME_LAST_UPDATED)));
            tg.name = results.getString(results.getColumnIndex(Tag.FIELD_NAME_TAG_NAME));
        }
        results.close();
        return tg;
    }

    public ArrayList<String> getTags() {
        final ArrayList<String> tmpList = new ArrayList<String>();
        final Cursor results = this.mDbCntlr.selectData(new Tag().getSelectAllSql());

        results.moveToFirst();
        for (int x = 0; x < results.getCount(); x++) {
            tmpList.add(results.getString(results.getColumnIndex(Tag.FIELD_NAME_TAG_NAME)));
            results.moveToNext();
        }
        results.close();
        return tmpList;
    }

    public ArrayList<Tag> getTagsForBook(final CalibreBook eBk) {
        final ArrayList<Tag> tmpList = new ArrayList<Tag>();

        final Cursor results = this.mDbCntlr.selectData(new Tag().getSelectAllForBook(eBk.getUniqueIdentifier()));

        results.moveToFirst();
        for (int x = 0; x < results.getCount(); x++) {
            final Tag tg = new Tag();
            tg.setLastUpdated(results.getString(results.getColumnIndex(BaseColumns._ID)));
            tg.setUniqueIdentifier(results.getString(results.getColumnIndex(AbstractDataObj.FIELD_NAME_LAST_UPDATED)));
            tg.name = results.getString(results.getColumnIndex(Tag.FIELD_NAME_TAG_NAME));
            tmpList.add(tg);
            results.moveToNext();
        }
        results.close();
        return tmpList;
    }

    public void importCalibreMetadata(final String metadataFilename) {
        final EBookImporter imprtr = new EBookImporter(this, this.getDbMetadata());
        imprtr.importBooks(metadataFilename);
    }

    public void importCalibreMetadata(final String[] filenames) {
        this.clearDb();
        AsyncTask<String[], Integer, Integer> importThread = new AsyncTask<String[], Integer, Integer>() {

            @Override
            protected Integer doInBackground(String[]... params) {
                Thread.currentThread().setName("Calibre Importer");
                String[] filenames = params[0];
                for (String s : filenames) {
                    CalibreDataModel.this.importCalibreMetadata(s);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
            }
        };
        importThread.execute(filenames);
    }

    @Override
    public boolean isOpened() {
        return this.mDbCntlr.isOpened;
    }

    @Override
    public void openDb() {
        this.mDbCntlr.open();
    }

    public void updateDbMetaData(final DbMetadata dbMetaData) {
        if (dbMetaData.getUniqueIdentifier() > -1) {
            this.mDbCntlr.update(dbMetaData.getTableName(), dbMetaData.getContentValues(),
                    dbMetaData.getUniqueIdentifier());
        } else {
            this.mDbCntlr.insert(dbMetaData.getTableName(), null, dbMetaData.getContentValues());
        }
    }

}
