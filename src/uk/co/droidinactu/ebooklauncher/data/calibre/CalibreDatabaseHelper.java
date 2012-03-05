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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * @author aspela
 * 
 */
public class CalibreDatabaseHelper extends SQLiteOpenHelper {

    private boolean createSampleDataOnStartup = false;
    private boolean firstTimeThrough = true;
    private final CalibreDbConstants dbConstants;

    public CalibreDatabaseHelper(final Context context, final CalibreDbConstants dbCnstants, final boolean createSamples) {
        super(context, dbCnstants.getDbName(), null, dbCnstants.getDbVersion());
        this.dbConstants = dbCnstants;
    }

    private void createTables(final SQLiteDatabase db) {
        db.execSQL(new CalibreBook().getSqlCreate());
        db.execSQL(new Author().getSqlCreate());
        db.execSQL(new AuthorEbookLink().getSqlCreate());
        db.execSQL(new Tag().getSqlCreate());
        db.execSQL(new TagEbookLink().getSqlCreate());
        db.execSQL(new DbMetadata().getSqlCreate());
        db.execSQL(new EBookMetaData().getSqlCreate());
        db.execSQL(new RecentEBook().getSqlCreate());
    }

    private void doUpgradeFrom001(final SQLiteDatabase db) {
        db.execSQL(new CalibreBook().getSqlUpdateFromV001());
        db.execSQL(new Author().getSqlUpdateFromV001());
        db.execSQL(new AuthorEbookLink().getSqlUpdateFromV001());
        db.execSQL(new Tag().getSqlUpdateFromV001());
        db.execSQL(new TagEbookLink().getSqlUpdateFromV001());
        db.execSQL(new DbMetadata().getSqlUpdateFromV001());
        db.execSQL(new EBookMetaData().getSqlUpdateFromV001());
        db.execSQL(new RecentEBook().getSqlUpdateFromV001());
    }

    private void doUpgradeFrom002(final SQLiteDatabase db) {
        db.execSQL(new CalibreBook().getSqlUpdateFromV002());
        db.execSQL(new Author().getSqlUpdateFromV002());
        db.execSQL(new AuthorEbookLink().getSqlUpdateFromV002());
        db.execSQL(new Tag().getSqlUpdateFromV002());
        db.execSQL(new TagEbookLink().getSqlUpdateFromV002());
        db.execSQL(new DbMetadata().getSqlUpdateFromV002());
        db.execSQL(new EBookMetaData().getSqlUpdateFromV002());
        db.execSQL(new RecentEBook().getSqlUpdateFromV002());
    }

    private void dropTables(final SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + new CalibreBook().getTableName());
        db.execSQL("DROP TABLE IF EXISTS " + new Author().getTableName());
        db.execSQL("DROP TABLE IF EXISTS " + new AuthorEbookLink().getTableName());
        db.execSQL("DROP TABLE IF EXISTS " + new Tag().getTableName());
        db.execSQL("DROP TABLE IF EXISTS " + new TagEbookLink().getTableName());
        db.execSQL("DROP TABLE IF EXISTS " + new DbMetadata().getTableName());
        db.execSQL("DROP TABLE IF EXISTS " + new EBookMetaData().getTableName());
        db.execSQL("DROP TABLE IF EXISTS " + new RecentEBook().getTableName());
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        this.createTables(db);
    }

    @Override
    public void onOpen(final SQLiteDatabase db) {
        super.onOpen(db);
        if (this.createSampleDataOnStartup && this.firstTimeThrough) {
            this.dropTables(db);
            this.createTables(db);
            this.firstTimeThrough = false;
        }
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        switch (oldVersion) {
        case (1):
            this.doUpgradeFrom001(db);
        case (2):
            this.doUpgradeFrom002(db);
        }
        this.dropTables(db);
        this.onCreate(db);
    }

}
