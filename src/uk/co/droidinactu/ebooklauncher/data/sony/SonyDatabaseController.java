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
package uk.co.droidinactu.ebooklauncher.data.sony;

import java.io.File;

import uk.co.droidinactu.ebooklauncher.EBookLauncherApplication;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 
 * @author andy
 */
public final class SonyDatabaseController {
    public static final String DATABASE_NAME = "books.db";
    public static final String DATABASE_PATH_EXT = "/mnt/extsd/Sony_Reader/database/";
    public static final String DATABASE_PATH_INT = "/mnt/sdcard/Sony_Reader/database/";

    /** tag used for logging */
    private static final String LOG_TAG = "SonyDatabaseController";

    public boolean isOpened = false;
    public boolean myDBExt = false;
    public SQLiteDatabase myDBInt = null;

    public SonyDatabaseController(final Context context) {
        super();
    }

    public void close() {
        if (this.myDBInt != null) {
            this.myDBInt.close();
            this.myDBInt = null;
        }
    }

    public void execSql(final String sql) {
        try {
            this.myDBInt.execSQL(sql);
        } catch (final Exception e) {
            Log.e(EBookLauncherApplication.LOG_TAG,
                    SonyDatabaseController.LOG_TAG + "Exception executing sqlite.execSQL [" + sql + "]", e);
        }
    }

    public void open() {
        Log.i(EBookLauncherApplication.LOG_TAG, SonyDatabaseController.LOG_TAG + "open()");
        this.myDBExt = false;
        final File dbFile = new File(SonyDatabaseController.DATABASE_PATH_INT + SonyDatabaseController.DATABASE_NAME);
        if (dbFile.exists() && (this.myDBInt == null)) {
            this.myDBInt = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
            final File dbFile2 = new File(
                    SonyDatabaseController.DATABASE_PATH_EXT + SonyDatabaseController.DATABASE_NAME);
            if (dbFile2.exists()) {
                this.myDBInt.execSQL("attach database '" + dbFile2.getAbsolutePath() + "' as extDb");
                this.myDBExt = true;
            }
            this.isOpened = true;
        }
    }

    /**
     * 
     * @param sql
     * @param selectionArgs
     * @return
     */
    public Cursor query(final String sql, final String[] selectionArgs) {
        Cursor retCrsr = null;
        try {
            retCrsr = this.myDBInt.rawQuery(sql, selectionArgs);
        } catch (final Exception e) {
            Log.e(EBookLauncherApplication.LOG_TAG,
                    SonyDatabaseController.LOG_TAG + "Exception executing sqlite.rawQuery [" + sql + "]", e);
        }
        return retCrsr;
    }
}
