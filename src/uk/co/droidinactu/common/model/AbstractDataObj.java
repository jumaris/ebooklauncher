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
package uk.co.droidinactu.common.model;

/// Description of AbstractDataObj.

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Observable;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

/**
 * this is the base class of all objects that are stored in a database.
 * 
 * @author andy
 */
public abstract class AbstractDataObj extends Observable implements Serializable {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat(AbstractDataObj.dateFormatString);

    /**
     * Make a SimpleDateFormat for toString()'s output. This has short (text)
     * date, a space, short (text) month, a space, 2-digit date, a space, hour
     * (0-23), minute, second, a space, short timezone, a final space, and a
     * long year.
     */
    public static final String dateFormatString = "yyyy MM dd HH:mm:ss";
    public static String FIELD_NAME_LAST_UPDATED = "last_updated";

    /**
     * this number denotes the version of this class. so that we can decide if
     * the structure of this class is the same as the one being deserialized
     * into it
     */
    private static final long serialVersionUID = 1L;

    protected static final int UNIQUE_RECORD_ID_FIELD_IDX = 1;

    protected String lastUpdated = "";

    protected long uniqueIdentifier = -1;

    /**
     * Simple default constructor for data objects
     */
    public AbstractDataObj() {
        final Date now = new Date();
        this.lastUpdated = "" + now.getTime();
    }

    public AbstractDataObj(final Cursor tmpCursor) {
        try {
            this.uniqueIdentifier = tmpCursor.getLong(tmpCursor.getColumnIndex(BaseColumns._ID));
        } catch (final Exception e) {
        }
    }

    /**
     * Constructor that takes in a row read from the database and sets up the
     * unique id from the record read in.
     * 
     * @param aRow
     *            of data from the database
     */
    public AbstractDataObj(final HashMap<String, String> aRow) {
        try {
            this.setUniqueIdentifier(Long.parseLong(aRow.get(BaseColumns._ID)));
        } catch (final Exception e) {
            ;
        }
        this.setLastUpdated(aRow.get(AbstractDataObj.FIELD_NAME_LAST_UPDATED));
    }

    public final String dateToString(final Date aDate) {
        final String dateStr = AbstractDataObj.dateFormat.format(aDate);
        return dateStr;
    }

    public final ArrayList<String> getArrayList(final String val1, final String val2) {
        final ArrayList<String> ary = new ArrayList<String>();
        ary.add(val1);
        ary.add(val2);
        return ary;
    }

    public String[] getColumnNames() {
        final String[] colNames = new String[2];
        int x = 0;
        colNames[x++] = BaseColumns._ID;
        colNames[x++] = FIELD_NAME_LAST_UPDATED;
        return colNames;
    }

    public ContentValues getContentValues() {
        final ContentValues map = new ContentValues();
        if (this.uniqueIdentifier > -1) {
            map.put(BaseColumns._ID, this.uniqueIdentifier);
        }
        map.put(AbstractDataObj.FIELD_NAME_LAST_UPDATED, this.lastUpdated);
        return map;
    }

    public HashMap<Integer, ArrayList<String>> getFields() {
        final HashMap<Integer, ArrayList<String>> fields = new HashMap<Integer, ArrayList<String>>();
        fields.put(0, this.getArrayList(BaseColumns._ID, "INTEGER PRIMARY KEY AUTOINCREMENT"));
        fields.put(1, this.getArrayList(AbstractDataObj.FIELD_NAME_LAST_UPDATED, "VARCHAR(255) NOT NULL"));
        return fields;
    }

    public String[] getIndexSql() {
        return new String[] { "" };
    }

    /**
     * @return the lastUpdated
     */
    public String getLastUpdated() {
        return this.lastUpdated;
    }

    public String getSelectAllSql() {
        return "select * from " + this.getTableName() + "";
    }

    public String getSelectByIdSql(final Long objId) {
        return "select * from " + this.getTableName() + " where " + BaseColumns._ID + "=" + objId;
    }

    public String getSelectByLastUpdate() {
        return "select * from " + this.getTableName() + " where " + AbstractDataObj.FIELD_NAME_LAST_UPDATED + "='"
                + this.lastUpdated + "'";
    }

    /**
     * this returns the sql statements which will allow creation of the sqlite
     * table
     */
    public String getSqlCreate() {
        final HashMap<Integer, ArrayList<String>> fields = this.getFields();

        final StringBuilder sb = new StringBuilder("CREATE TABLE ").append(this.getTableName()).append(" ( ");
        for (final ArrayList<String> fieldInfo : fields.values()) {
            // if
            // (!fieldInfo.get(0).equals(AbstractDataObj.FIELD_NAME_UNIQUE_IDENTIFIER))
            // {
            sb.append(fieldInfo.get(0)).append(" ");
            sb.append(fieldInfo.get(1));
            sb.append(",");
            // }
        }
        final String sqlStr = sb.substring(0, sb.length() - 1) + ");";
        return sqlStr;
    }

    public String getSqlDeleteAll() {
        return "delete from " + this.getTableName();
    }

    public abstract String getTableName();

    /**
     * This property allows access to the object's unique identifier
     */
    public final Long getUniqueIdentifier() {
        return this.uniqueIdentifier;
    }

    public String makeSafeSQL(final String str) {
        String tmpStr = str.replace('"', '`');
        tmpStr = tmpStr.replace('\'', '`');
        tmpStr = tmpStr.replace('\\', '_');

        return tmpStr;
    }

    protected String removeQuotes(final String string) {
        String rtStr = string;
        if (rtStr.startsWith("\"")) {
            rtStr = rtStr.substring(1);
        }
        if (rtStr.endsWith("\"")) {
            rtStr = rtStr.substring(0, rtStr.length() - 1);
        }
        return rtStr;
    }

    public void setLastUpdated(final long time) {
        this.setLastUpdated("" + time);
    }

    /**
     * @param lastUpdated
     *            the lastUpdated to set
     */
    public void setLastUpdated(final String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public final void setUniqueIdentifier(final Long value) {
        this.uniqueIdentifier = value;
    }

    public final void setUniqueIdentifier(final String value) {
        this.uniqueIdentifier = Long.parseLong(value);
    }

    public final Date stringToDate(final String aDateStr) {
        Date date = new Date();
        try {
            date = AbstractDataObj.dateFormat.parse(aDateStr);
        } catch (final ParseException e) {
            // e.printStackTrace();
        }
        return date;
    }

} // class()

