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

public class RecentEBook extends AbstractDataObj {

    public static final String DATABASE_TABLE_NAME = "recentEbooks";

    public static final String FIELD_NAME_EBOOK_ID = "ebookId";
    /**
     * this number denotes the version of this class. so that we can decide if
     * the structure of this class is the same as the one being deserialized
     * into it
     */
    private static final long serialVersionUID = 1L;

    public Long ebookId;

    public RecentEBook() {
    }

    public RecentEBook(final HashMap<String, String> aRow) {
        super(aRow);
        try {
            this.ebookId = Long.parseLong(aRow.get(RecentEBook.FIELD_NAME_EBOOK_ID));
        } catch (final Exception e) {
            ;
        }
    }

    @Override
    public ContentValues getContentValues() {
        final ContentValues map = super.getContentValues();
        map.put(RecentEBook.FIELD_NAME_EBOOK_ID, this.ebookId);
        return map;
    }

    @Override
    public HashMap<Integer, ArrayList<String>> getFields() {
        final HashMap<Integer, ArrayList<String>> fields = super.getFields();

        int x = fields.size();
        fields.put(x++, this.getArrayList(RecentEBook.FIELD_NAME_EBOOK_ID, "INTEGER"));
        return fields;
    }

    @Override
    public String getSelectAllSql() {
        final String ebkTblName = new CalibreBook().getTableName();

        final String tmpStr = "SELECT * FROM " + this.getTableName() + " JOIN " + ebkTblName + " ON "
                + this.getTableName() + "." + FIELD_NAME_EBOOK_ID + " = " + ebkTblName + "." + BaseColumns._ID
                + " order by " + AbstractDataObj.FIELD_NAME_LAST_UPDATED + " DESC";
        return tmpStr;
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
        return RecentEBook.DATABASE_TABLE_NAME;
    }

}
