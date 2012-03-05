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

public class TagEbookLink extends AbstractDataObj {

    public static final String DATABASE_TABLE_NAME = "tagEbookLink";

    public static final String FIELD_NAME_EBOOK_ID = "ebook_id";
    public static final String FIELD_NAME_TAG_ID = "tag_id";
    /**
     * this number denotes the version of this class. so that we can decide if
     * the structure of this class is the same as the one being deserialized
     * into it
     */
    private static final long serialVersionUID = 1L;

    private Long ebookId = -1L;
    private Long tagId = -1L;

    public TagEbookLink() {
    }

    public TagEbookLink(final HashMap<String, String> aRow) {
        super(aRow);
        this.tagId = Long.parseLong(aRow.get(TagEbookLink.FIELD_NAME_TAG_ID));
        this.ebookId = Long.parseLong(aRow.get(TagEbookLink.FIELD_NAME_EBOOK_ID));
    }

    public TagEbookLink(final Long tgId, final Long ebkId) {
        this.tagId = tgId;
        this.ebookId = ebkId;
    }

    @Override
    public ContentValues getContentValues() {
        final ContentValues map = super.getContentValues();
        map.put(TagEbookLink.FIELD_NAME_TAG_ID, this.tagId);
        map.put(TagEbookLink.FIELD_NAME_EBOOK_ID, this.ebookId);
        return map;
    }

    @Override
    public HashMap<Integer, ArrayList<String>> getFields() {
        final HashMap<Integer, ArrayList<String>> fields = super.getFields();

        int x = fields.size();
        fields.put(x++, this.getArrayList(TagEbookLink.FIELD_NAME_TAG_ID, "Long NOT NULL"));
        fields.put(x++, this.getArrayList(TagEbookLink.FIELD_NAME_EBOOK_ID, "Long NOT NULL"));
        return fields;
    }

    public String getSqlBooksFrTag(final int tgId) {
        return "select * from " + this.getTableName() + " where " + TagEbookLink.FIELD_NAME_TAG_ID + "=" + tgId + "";
    }

    public String getSqlTagsForBook(final int bkId) {
        return "select * from " + this.getTableName() + " where " + TagEbookLink.FIELD_NAME_EBOOK_ID + "=" + bkId + "";
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
        return DATABASE_TABLE_NAME;
    }

}
