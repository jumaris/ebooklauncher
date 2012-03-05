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

import uk.co.droidinactu.common.model.AbstractDataObj;
import android.content.ContentValues;
import android.database.Cursor;

public final class Picture extends AbstractDataObj {

    public static String COLUMN_NAME_KANA_TITLE = "kana_title";
    public static String COLUMN_NAME_SOURCE_ID = "source_id";
    public static String COLUMN_NAME_TITLE = "title";
    public static String COLUMN_NAME_UUID = "uuid";

    String kana_title;
    long source_id;
    String title;
    String uuid;

    public Picture() {
        // TODO Auto-generated constructor stub
    }

    public Picture(final Cursor tmpCursor) {
        super(tmpCursor);
        try {
            this.title = tmpCursor.getString(tmpCursor.getColumnIndex(COLUMN_NAME_TITLE));
        } catch (final Exception e) {
        }
        try {
            this.kana_title = tmpCursor.getString(tmpCursor.getColumnIndex(COLUMN_NAME_KANA_TITLE));
        } catch (final Exception e) {
        }
        try {
            this.source_id = tmpCursor.getLong(tmpCursor.getColumnIndex(COLUMN_NAME_SOURCE_ID));
        } catch (final Exception e) {
        }
        try {
            this.uuid = tmpCursor.getString(tmpCursor.getColumnIndex(COLUMN_NAME_UUID));
        } catch (final Exception e) {
        }
    }

    @Override
    public ContentValues getContentValues() {
        final ContentValues map = super.getContentValues();
        map.put(COLUMN_NAME_TITLE, this.title);
        map.put(COLUMN_NAME_KANA_TITLE, this.kana_title);
        map.put(COLUMN_NAME_SOURCE_ID, this.source_id);
        map.put(COLUMN_NAME_UUID, this.uuid);
        return map;
    }

    @Override
    public String[] getIndexSql() {
        final String idxs[] = new String[] {};
        return idxs;
    }

    @Override
    public String getTableName() {
        return "picture";
    }

    @Override
    public String toString() {
        return "picture: " + this.title;
    }

}
