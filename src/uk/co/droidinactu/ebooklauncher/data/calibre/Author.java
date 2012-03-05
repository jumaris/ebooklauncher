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
import java.util.HashMap;

import uk.co.droidinactu.common.model.AbstractDataObj;
import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

/**
 * @author andy
 * 
 */
public class Author extends AbstractDataObj {

    public static final String DATABASE_TABLE_NAME = "author";

    public static final String FIELD_NAME_AUTHOR_NAME = "author_name";
    public static final String FIELD_NAME_AUTHOR_SORT = "author_sort";
    /**
     * this number denotes the version of this class. so that we can decide if
     * the structure of this class is the same as the one being deserialized
     * into it
     */
    private static final long serialVersionUID = 1L;

    public String authorName = "";
    public String authorSort = "";

    public Author() {
    }

    public Author(final Cursor results) {
        this.setAuthorName(results.getString(results.getColumnIndex(FIELD_NAME_AUTHOR_NAME)));
        this.setAuthorSort(results.getString(results.getColumnIndex(FIELD_NAME_AUTHOR_SORT)));
        this.setLastUpdated(results.getString(results.getColumnIndex(BaseColumns._ID)));
        this.setUniqueIdentifier(results.getString(results.getColumnIndex(AbstractDataObj.FIELD_NAME_LAST_UPDATED)));
    }

    public Author(final HashMap<String, String> aRow) {
        super(aRow);
        this.authorName = aRow.get(Author.FIELD_NAME_AUTHOR_NAME);
        this.authorSort = aRow.get(Author.FIELD_NAME_AUTHOR_SORT);
    }

    public Author(final String anAuthor) {
        this.authorName = anAuthor;
    }

    public String getAuthorName() {
        return this.authorName;
    }

    public String getAuthorSort() {
        return this.authorSort;
    }

    @Override
    public ContentValues getContentValues() {
        final ContentValues map = super.getContentValues();
        map.put(Author.FIELD_NAME_AUTHOR_NAME, this.makeSafeSQL(this.authorName));
        map.put(Author.FIELD_NAME_AUTHOR_SORT, this.makeSafeSQL(this.authorSort));
        return map;
    }

    @Override
    public HashMap<Integer, ArrayList<String>> getFields() {
        final HashMap<Integer, ArrayList<String>> fields = super.getFields();

        int x = fields.size();
        fields.put(x++, this.getArrayList(Author.FIELD_NAME_AUTHOR_NAME, "VARCHAR(255) NOT NULL"));
        fields.put(x++, this.getArrayList(Author.FIELD_NAME_AUTHOR_SORT, "VARCHAR(255) NOT NULL"));
        return fields;
    }

    public String getSelectAllForBook(final Long uniqueIdentifier) {
        final String ebkAuthLnkTblName = new AuthorEbookLink().getTableName();
        final String authTblName = new Author().getTableName();
        final String ebkTblName = new CalibreBook().getTableName();

        final String tmpStr = "SELECT " + authTblName + "." + Author.FIELD_NAME_AUTHOR_NAME + " " + " FROM "
                + ebkAuthLnkTblName + " JOIN " + ebkTblName + " ON " + ebkAuthLnkTblName + "."
                + AuthorEbookLink.FIELD_NAME_EBOOK_ID + " = " + ebkTblName + "." + BaseColumns._ID + " JOIN "
                + authTblName + " ON " + ebkAuthLnkTblName + "." + AuthorEbookLink.FIELD_NAME_AUTHOR_ID + " = "
                + authTblName + "." + BaseColumns._ID + " where " + ebkAuthLnkTblName + "."
                + AuthorEbookLink.FIELD_NAME_EBOOK_ID + "=" + uniqueIdentifier;
        return tmpStr;
    }

    public String getSelectAuthor() {
        return "select * from " + this.getTableName() + " where " + Author.FIELD_NAME_AUTHOR_NAME + "=\'"
                + this.makeSafeSQL(this.authorName) + "\'";
    }

    public String getSqlUpdateFromV001() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getSqlUpdateFromV002() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getTableName() {
        return Author.DATABASE_TABLE_NAME;
    }

    public void setAuthorName(final String authorName) {
        this.authorName = authorName;
    }

    public void setAuthorSort(final String authorSort) {
        this.authorSort = authorSort;
    }

    @Override
    public String toString() {
        return this.authorName;
    }
}
