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

/**
 * 
 * CREATE VIEW periodicals AS
 * 
 * SELECT DISTINCT * FROM (SELECT DISTINCT
 * periodical_name,kana_periodical_name,CASE
 * 
 * WHEN COUNT(reading_time)=COUNT(*) THEN MAX(reading_time) ELSE NULL END
 * 
 * AS reading_time,MAX(reading_time) AS reading_time,MAX(publication_date) AS
 * publication_date,COUNT(periodical_name) AS _count FROM books
 * 
 * GROUP BY periodical_name, kana_periodical_name HAVING periodical_name NOT
 * NULL OR kana_periodical_name NOT NULL) AS g LEFT JOIN (SELECT
 * periodical_name,kana_periodical_name,publication_date,_id,source_id,
 * thumbnail,periodical_name_key,logos FROM books GROUP BY periodical_name,
 * kana_periodical_name, publication_date ) AS b ON(((g.periodical_name NOT NULL
 * AND(g.periodical_name = b.periodical_name)) OR (g.kana_periodical_name NOT
 * NULL AND(g.kana_periodical_name = b.kana_periodical_name ))) AND
 * g.publication_date = b.publication_date )
 * 
 */
/**
 * this object defines all aspects of a periodical as defined in the Sony PRS-T1
 * database.
 * 
 * @author abdy aspell-clark
 * 
 */
public final class Periodical extends AbstractDataObj {

    public static String COLUMN_NAME_COUNT = "count";
    public static String COLUMN_NAME_PERIODICAL_NAME = "periodical_name";
    public static String COLUMN_NAME_PUBLICATION_DATE = "publication_date";
    public static String COLUMN_NAME_READING_TIME = "reading_time";
    public static String COLUMN_NAME_THUMBNAIL = "thumbnail";
    public static String DATABASE_TABLE_NAME = "periodicals";

    public long count;
    public String periodical_name;
    public long publication_date;
    public long reading_time;
    public String thumbnail;

    public Periodical() {
        // TODO Auto-generated constructor stub
    }

    public Periodical(final Cursor tmpCursor) {
        super(tmpCursor);
        try {
            this.periodical_name = tmpCursor.getString(tmpCursor.getColumnIndex(COLUMN_NAME_PERIODICAL_NAME));
        } catch (final Exception e) {
        }
        try {
            this.thumbnail = tmpCursor.getString(tmpCursor.getColumnIndex(COLUMN_NAME_THUMBNAIL));
        } catch (final Exception e) {
        }
        try {
            this.publication_date = tmpCursor.getLong(tmpCursor.getColumnIndex(COLUMN_NAME_PUBLICATION_DATE));
        } catch (final Exception e) {
        }
        try {
            this.count = tmpCursor.getLong(tmpCursor.getColumnIndex(COLUMN_NAME_COUNT));
        } catch (final Exception e) {
        }
        try {
            this.reading_time = tmpCursor.getLong(tmpCursor.getColumnIndex(COLUMN_NAME_READING_TIME));
        } catch (final Exception e) {
        }
    }

    @Override
    public ContentValues getContentValues() {
        final ContentValues map = super.getContentValues();
        map.put(COLUMN_NAME_PERIODICAL_NAME, this.periodical_name);
        map.put(COLUMN_NAME_THUMBNAIL, this.thumbnail);
        map.put(COLUMN_NAME_PUBLICATION_DATE, this.publication_date);
        map.put(COLUMN_NAME_COUNT, this.count);
        map.put(COLUMN_NAME_READING_TIME, this.reading_time);
        return map;
    }

    @Override
    public String[] getIndexSql() {
        final String idxs[] = new String[] {};
        return idxs;
    }

    @Override
    public String getTableName() {
        return DATABASE_TABLE_NAME;
    }

    @Override
    public String toString() {
        return "periodical: " + this.periodical_name;
    }

}
