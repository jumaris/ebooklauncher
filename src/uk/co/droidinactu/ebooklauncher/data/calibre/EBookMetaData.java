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

/**
 * @author andy
 * 
 */
public class EBookMetaData extends AbstractDataObj {

    public static final String DATABASE_TABLE_NAME = "metadata";

    public static final String FIELD_NAME_METADATA_BOOK_ID = "metadata_bookId";
    public static final String FIELD_NAME_METADATA_KEY = "metadata_key";
    public static final String FIELD_NAME_METADATA_VALUE = "metadata_value";
    /**
     * this number denotes the version of this class. so that we can decide if
     * the structure of this class is the same as the one being deserialized
     * into it
     */
    private static final long serialVersionUID = 1L;

    public int bookId = -1;
    public String key = "";
    public String value = "";

    public EBookMetaData() {
    }

    public EBookMetaData(final HashMap<String, String> aRow) {
        super(aRow);
        this.bookId = Integer.parseInt(aRow.get(EBookMetaData.FIELD_NAME_METADATA_BOOK_ID));
        this.key = aRow.get(EBookMetaData.FIELD_NAME_METADATA_KEY);
        this.value = aRow.get(EBookMetaData.FIELD_NAME_METADATA_VALUE);
    }

    public EBookMetaData(final int abook, final String akey, final String avalue) {
        this.bookId = abook;
        this.key = akey;
        this.value = avalue;
    }

    @Override
    public ContentValues getContentValues() {
        final ContentValues map = super.getContentValues();
        map.put(EBookMetaData.FIELD_NAME_METADATA_BOOK_ID, this.bookId);
        map.put(EBookMetaData.FIELD_NAME_METADATA_KEY, this.makeSafeSQL(this.key));
        map.put(EBookMetaData.FIELD_NAME_METADATA_VALUE, this.makeSafeSQL(this.value));
        return map;
    }

    @Override
    public HashMap<Integer, ArrayList<String>> getFields() {
        final HashMap<Integer, ArrayList<String>> fields = super.getFields();

        int x = fields.size();
        fields.put(x++, this.getArrayList(EBookMetaData.FIELD_NAME_METADATA_BOOK_ID, "INTEGER NOT NULL"));
        fields.put(x++, this.getArrayList(EBookMetaData.FIELD_NAME_METADATA_KEY, "VARCHAR(255) NOT NULL"));
        fields.put(x++, this.getArrayList(EBookMetaData.FIELD_NAME_METADATA_VALUE, "VARCHAR(255) NOT NULL"));
        return fields;
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
        return EBookMetaData.DATABASE_TABLE_NAME;
    }

    @Override
    public String toString() {
        return "book " + this.bookId + " [" + this.key + ":" + this.value + "]";
    }

}
