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

public class AuthorEbookLink extends AbstractDataObj {

    public static final String DATABASE_TABLE_NAME = "authorEbookLink";

    public static final String FIELD_NAME_AUTHOR_ID = "author_id";
    public static final String FIELD_NAME_EBOOK_ID = "ebook_id";
    /**
     * this number denotes the version of this class. so that we can decide if
     * the structure of this class is the same as the one being deserialized
     * into it
     */
    private static final long serialVersionUID = 1L;

    private Long authorId = -1L;
    private Long ebookId = -1L;

    public AuthorEbookLink() {
    }

    public AuthorEbookLink(final HashMap<String, String> aRow) {
        super(aRow);
        this.authorId = Long.parseLong(aRow.get(AuthorEbookLink.FIELD_NAME_AUTHOR_ID));
        this.ebookId = Long.parseLong(aRow.get(AuthorEbookLink.FIELD_NAME_EBOOK_ID));
    }

    public AuthorEbookLink(final Long authId, final Long ebkId) {
        this.authorId = authId;
        this.ebookId = ebkId;
    }

    @Override
    public ContentValues getContentValues() {
        final ContentValues map = super.getContentValues();
        map.put(AuthorEbookLink.FIELD_NAME_AUTHOR_ID, this.authorId);
        map.put(AuthorEbookLink.FIELD_NAME_EBOOK_ID, this.ebookId);
        return map;
    }

    @Override
    public HashMap<Integer, ArrayList<String>> getFields() {
        final HashMap<Integer, ArrayList<String>> fields = super.getFields();

        int x = fields.size();
        fields.put(x++, this.getArrayList(AuthorEbookLink.FIELD_NAME_AUTHOR_ID, "Long NOT NULL"));
        fields.put(x++, this.getArrayList(AuthorEbookLink.FIELD_NAME_EBOOK_ID, "Long NOT NULL"));
        return fields;
    }

    public String getSqlAuthorsForBook(final int bkId) {
        return "select * from " + this.getTableName() + " where " + AuthorEbookLink.FIELD_NAME_EBOOK_ID + "=" + bkId
                + "";
    }

    public String getSqlBooksForAuthor(final int authId) {
        return "select * from " + this.getTableName() + " where " + AuthorEbookLink.FIELD_NAME_AUTHOR_ID + "=" + authId
                + "";
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
        return AuthorEbookLink.DATABASE_TABLE_NAME;
    }

}
