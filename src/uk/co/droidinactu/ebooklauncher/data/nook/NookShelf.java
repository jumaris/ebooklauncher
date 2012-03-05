/*
 * Title: uk.co.droidinactu.ebooklauncher.data.nook.NookShelf
 * Copyright (c) 2008
 * Company: EADS-DS UK
 * Author: aspela
 * $Id$
 *
 * Package: uk.co.droidinactu.ebooklauncher.data.nook
 *
 */
package uk.co.droidinactu.ebooklauncher.data.nook;

/**
 * CREATE TABLE shelf (
 * _id INTEGER PRIMARY KEY,
 * shelf_name TEXT NOT NULL UNIQUE,
 * shelf_position INTEGER UNIQUE,
 * total_library_items INTEGER DEFAULT 0,
 * shelf_date_added INTEGER NOT NULL,
 * shelf_date_modified INTEGER)
 *
 * @author aspela
 */
public class NookShelf {
    public static final String COLUMN_NAME_shelf_name = "shelf_name";
    public static final String COLUMN_NAME_shelf_position = "shelf_position";
    public static final String COLUMN_NAME_total_library_items = "total_library_items";
    public static final String COLUMN_NAME_shelf_date_added = "shelf_date_added";
    public static final String COLUMN_NAME_shelf_date_modified = "shelf_date_modified";
}
