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

import uk.co.droidinactu.common.model.AbstractDataObj;
import uk.co.droidinactu.ebooklauncher.EBookLauncherApplication;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * 
 * @author andy
 */
public class CalibreDatabaseController {

    /** tag used for logging */
    private static final String LOG_TAG = "CalibreDatabaseController:";
    public boolean isOpened = false;

    private CalibreDbConstants dbConstants;
    private CalibreDatabaseHelper dbhelper;
    public SQLiteDatabase myDB = null;

    public CalibreDatabaseController(final Context context) {
        super();
        Log.i(EBookLauncherApplication.LOG_TAG, CalibreDatabaseController.LOG_TAG + "CalibreDatabaseController()");
        this.dbhelper = new CalibreDatabaseHelperFactory().getDatabaseHelper(context, new CalibreDbConstants());
    }

    public void close() {
        Log.i(EBookLauncherApplication.LOG_TAG, CalibreDatabaseController.LOG_TAG + "close()");
        if (this.myDB != null) {
            this.myDB.close();
            this.myDB = null;
        }
    }

    public long delete(final AbstractDataObj obj) {
        return this.myDB.delete(obj.getTableName(), BaseColumns._ID + "=?", new String[] { obj.getUniqueIdentifier()
                .toString() });
    }

    public long delete(final String tableName, final Long objId) {
        return this.myDB.delete(tableName, BaseColumns._ID + "=?", new String[] { objId.toString() });
    }

    public void execSql(final String sql) {
        try {
            this.myDB.execSQL(sql);
        } catch (final Exception e) {
            Log.e(EBookLauncherApplication.LOG_TAG,
                    CalibreDatabaseController.LOG_TAG + "Exception executing sqlite.execSQL [" + sql + "]", e);
        }
    }

    public void executeSql(final ArrayList<String> sqlStmts) {
        for (final String stmt : sqlStmts) {
            this.executeSql(stmt);
        }
    }

    public void executeSql(final String sqlStmt) {
        try {
            if (this.myDB == null) {
                this.open();
            }
            this.myDB.execSQL(sqlStmt);
        } catch (final Exception e) {
            Log.e(EBookLauncherApplication.LOG_TAG, "exception", e);
        }
    }

    public long insert(final AbstractDataObj obj) {
        return this.insert(obj.getTableName(), null, obj.getContentValues());
    }

    public long insert(final String tableName, final String nullObjHack, final ContentValues contentValues) {
        return this.myDB.insert(tableName, nullObjHack, contentValues);
    }

    public void open() {
        Log.i(EBookLauncherApplication.LOG_TAG, CalibreDatabaseController.LOG_TAG + "open()");
        if (this.myDB == null) {
            this.myDB = this.dbhelper.getWritableDatabase();
            this.isOpened = true;
        }
    }

    /**
     * 
     * @param sql
     * @param selectionArgs
     * @return
     */
    public Cursor query(final String sql) {
        return this.query(sql, new String[] {});
    }

    public Cursor query(final String sql, final String[] selectionArgs) {
        Cursor retCrsr = null;
        try {
            retCrsr = this.myDB.rawQuery(sql, selectionArgs);
        } catch (final Exception e) {
            Log.e(EBookLauncherApplication.LOG_TAG,
                    CalibreDatabaseController.LOG_TAG + "Exception executing sqlite.rawQuery [" + sql + "]", e);
        }
        return retCrsr;
    }

    /**
     * This method reads in data from the database and puts it into a cursor.
     * 
     * @param sqlStmt
     *            is the sql statment which will be executed to read the
     *            database.
     * @return a cursor containing the selected rows.
     */
    public Cursor selectData(final String sqlStmt) {
        if (this.myDB == null) {
            this.open();
        }
        final Cursor c = this.myDB.rawQuery(sqlStmt, null);
        return c;
    }

    public void update(final AbstractDataObj obj) {
        this.update(obj.getTableName(), obj.getContentValues(), obj.getUniqueIdentifier());
    }

    public long update(final String tableName, final ContentValues contentValues, final Long objId) {
        return this.myDB.update(tableName, contentValues, BaseColumns._ID + "=?", new String[] { objId.toString() });
    }
}
