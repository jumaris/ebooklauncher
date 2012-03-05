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
public class DbMetadata extends AbstractDataObj {

    public static final String DATABASE_TABLE_NAME = "dbmetadata";

    public static final String FIELD_NAME_DbMetadata_sortorder = "db_sortorder";
    public static final String FIELD_NAME_DbMetadata_timestamp = "db_timestamp";
    public static final String FIELD_NAME_DbMetadata_VERSION = "db_version";
    /**
     * this number denotes the version of this class. so that we can decide if
     * the structure of this class is the same as the one being deserialized
     * into it
     */
    private static final long serialVersionUID = 1L;

    public SortOrder currSortOrder = SortOrder.Title;
    public long timestamp = 0;
    public String version = "";

    public DbMetadata() {
    }

    public DbMetadata(final Cursor results) {
        this.setSortOrder(results.getString(results.getColumnIndex(DbMetadata.FIELD_NAME_DbMetadata_sortorder)));
        this.timestamp = results.getLong(results.getColumnIndex(DbMetadata.FIELD_NAME_DbMetadata_timestamp));
        this.version = results.getString(results.getColumnIndex(DbMetadata.FIELD_NAME_DbMetadata_VERSION));
        this.setLastUpdated(results.getString(results.getColumnIndex(AbstractDataObj.FIELD_NAME_LAST_UPDATED)));
        this.setUniqueIdentifier(results.getLong(results.getColumnIndex(BaseColumns._ID)));
    }

    public DbMetadata(final HashMap<String, String> aRow) {
        super(aRow);
        this.version = aRow.get(DbMetadata.FIELD_NAME_DbMetadata_VERSION);
        this.timestamp = Long.parseLong(aRow.get(DbMetadata.FIELD_NAME_DbMetadata_timestamp));
        this.setSortOrder(aRow.get(DbMetadata.FIELD_NAME_DbMetadata_sortorder));
    }

    @Override
    public ContentValues getContentValues() {
        final ContentValues map = super.getContentValues();
        map.put(DbMetadata.FIELD_NAME_DbMetadata_VERSION, this.version);
        map.put(DbMetadata.FIELD_NAME_DbMetadata_timestamp, this.timestamp);
        map.put(DbMetadata.FIELD_NAME_DbMetadata_sortorder, this.currSortOrder.toString());
        return map;
    }

    @Override
    public HashMap<Integer, ArrayList<String>> getFields() {
        final HashMap<Integer, ArrayList<String>> fields = super.getFields();

        int x = fields.size();
        fields.put(x++, this.getArrayList(DbMetadata.FIELD_NAME_DbMetadata_VERSION, "VARCHAR(255) NOT NULL"));
        fields.put(x++, this.getArrayList(DbMetadata.FIELD_NAME_DbMetadata_timestamp, "LONG"));
        fields.put(x++, this.getArrayList(DbMetadata.FIELD_NAME_DbMetadata_sortorder, "Integer"));
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
        return DATABASE_TABLE_NAME;
    }

    public void setSortOrder(final String so) {
        this.currSortOrder = SortOrder.Title;
        if (so.equals(SortOrder.Author)) {
            this.currSortOrder = SortOrder.Author;
        } else if (so.equals(SortOrder.Series)) {
            this.currSortOrder = SortOrder.Series;
        }
    }

}
