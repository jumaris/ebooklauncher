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
import android.provider.BaseColumns;

/**
 * @author andy
 * 
 */
public class Tag extends AbstractDataObj {

    public static final String DATABASE_TABLE_NAME = "tag";

    public static final String FIELD_NAME_TAG_NAME = "tag_name";
    public static final String FIELD_NAME_TAG_SORT = "tag_sort";
    /**
     * this number denotes the version of this class. so that we can decide if
     * the structure of this class is the same as the one being deserialized
     * into it
     */
    private static final long serialVersionUID = 1L;

    public String name = "";

    public Tag() {
    }

    public Tag(final HashMap<String, String> aRow) {
        super(aRow);
        this.name = aRow.get(Tag.FIELD_NAME_TAG_NAME);
    }

    public Tag(final String aName) {
        this.name = aName;
    }

    @Override
    public ContentValues getContentValues() {
        final ContentValues map = super.getContentValues();
        map.put(Tag.FIELD_NAME_TAG_NAME, this.makeSafeSQL(this.name));
        return map;
    }

    @Override
    public HashMap<Integer, ArrayList<String>> getFields() {
        final HashMap<Integer, ArrayList<String>> fields = super.getFields();

        int x = fields.size();
        fields.put(x++, this.getArrayList(Tag.FIELD_NAME_TAG_NAME, "VARCHAR(255) NOT NULL"));
        return fields;
    }

    public String getSelectAllForBook(final Long uniqueIdentifier) {
        final String ebkTagLnkTblName = new TagEbookLink().getTableName();
        final String tagTblName = new Tag().getTableName();
        final String ebkTblName = new CalibreBook().getTableName();

        final String tmpStr = "SELECT " + tagTblName + "." + Tag.FIELD_NAME_TAG_NAME + " " + " FROM "
                + ebkTagLnkTblName + " JOIN " + ebkTblName + " ON " + ebkTagLnkTblName + "."
                + TagEbookLink.FIELD_NAME_EBOOK_ID + " = " + ebkTblName + "." + BaseColumns._ID + " JOIN " + tagTblName
                + " ON " + ebkTagLnkTblName + "." + TagEbookLink.FIELD_NAME_TAG_ID + " = " + tagTblName + "."
                + BaseColumns._ID + " where " + ebkTagLnkTblName + "." + TagEbookLink.FIELD_NAME_EBOOK_ID + "="
                + uniqueIdentifier;
        return tmpStr;
    }

    public String getSelectTag() {
        return "select * from " + this.getTableName() + " where " + Tag.FIELD_NAME_TAG_NAME + "=\'"
                + this.makeSafeSQL(this.name) + "\'";
    }

    public String getSelectTag(final String tagName) {
        return "select * from " + this.getTableName() + " where " + Tag.FIELD_NAME_TAG_NAME + "=\'"
                + this.makeSafeSQL(tagName) + "\'";
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

    @Override
    public String toString() {
        return this.name;
    }

}
