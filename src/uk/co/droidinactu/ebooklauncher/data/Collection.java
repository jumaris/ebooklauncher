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
package uk.co.droidinactu.ebooklauncher.data;

import java.util.ArrayList;
import java.util.HashMap;

import uk.co.droidinactu.common.model.AbstractDataObj;
import android.content.ContentValues;
import android.database.Cursor;

/**
 * this object defines all aspects of a collection as defined in the Sony PRS-T1
 * database.
 * 
 * @author root
 * 
 */
public final class Collection extends AbstractDataObj {

    public static String COLUMN_NAME_KANA_TITLE = "kana_title";
    public static String COLUMN_NAME_SOURCE_ID = "source_id";
    public static String COLUMN_NAME_TITLE = "title";
    public static String COLUMN_NAME_UUID = "uuid";

    public static String DATABASE_TABLE_NAME = "collection";

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public String kana_title;
    public long source_id;
    public String title;
    public int nbrItemsInCollections = 0;
    public String uuid;

    public Collection() {
        // TODO Auto-generated constructor stub
    }

    public Collection(final Cursor tmpCursor) {
        super(tmpCursor);
        try {
            this.title = tmpCursor.getString(tmpCursor.getColumnIndex(Collection.COLUMN_NAME_TITLE));
        } catch (final Exception e) {
        }
        try {
            this.kana_title = tmpCursor.getString(tmpCursor.getColumnIndex(Collection.COLUMN_NAME_KANA_TITLE));
        } catch (final Exception e) {
        }
        try {
            this.source_id = tmpCursor.getLong(tmpCursor.getColumnIndex(Collection.COLUMN_NAME_SOURCE_ID));
        } catch (final Exception e) {
        }
        try {
            this.uuid = tmpCursor.getString(tmpCursor.getColumnIndex(Collection.COLUMN_NAME_UUID));
        } catch (final Exception e) {
        }
    }

    @Override
    public String[] getColumnNames() {
        final String[] sprCols = super.getColumnNames();
        final String[] colNames = new String[sprCols.length + 3];
        for (int y = 0; y < sprCols.length; y++) {
            colNames[y] = sprCols[y];
        }
        int x = sprCols.length - 1;
        colNames[x++] = Collection.COLUMN_NAME_TITLE;
        colNames[x++] = Collection.COLUMN_NAME_UUID;
        colNames[x++] = Collection.COLUMN_NAME_KANA_TITLE;
        colNames[x++] = Collection.COLUMN_NAME_SOURCE_ID;
        return colNames;
    }

    @Override
    public ContentValues getContentValues() {
        final ContentValues map = super.getContentValues();
        map.put(Collection.COLUMN_NAME_TITLE, this.title);
        map.put(Collection.COLUMN_NAME_KANA_TITLE, this.kana_title);
        map.put(Collection.COLUMN_NAME_SOURCE_ID, this.source_id);
        map.put(Collection.COLUMN_NAME_UUID, this.uuid);
        return map;
    }

    @Override
    public HashMap<Integer, ArrayList<String>> getFields() {
        final HashMap<Integer, ArrayList<String>> fields = super.getFields();

        int x = fields.size();
        fields.put(x++, this.getArrayList(Collection.COLUMN_NAME_TITLE, "VARCHAR(255) NOT NULL"));
        fields.put(x++, this.getArrayList(Collection.COLUMN_NAME_UUID, "VARCHAR(255) NOT NULL"));
        fields.put(x++, this.getArrayList(Collection.COLUMN_NAME_KANA_TITLE, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(Collection.COLUMN_NAME_SOURCE_ID, "VARCHAR(255)"));
        return fields;
    }

    @Override
    public String[] getIndexSql() {
        final String idxs[] = new String[] { "create index author_index on " + Collection.DATABASE_TABLE_NAME
                + " ("
                + Collection.COLUMN_NAME_TITLE
                + " collate nocase);" };
        return idxs;
    }

    @Override
    public String getTableName() {
        return Collection.DATABASE_TABLE_NAME;
    }

    @Override
    public String toString() {
        return "collection: " + this.title;
    }

}
