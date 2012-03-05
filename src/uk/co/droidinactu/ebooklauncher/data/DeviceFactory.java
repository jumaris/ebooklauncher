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

import java.io.File;

import uk.co.droidinactu.ebooklauncher.EBookLauncherApplication;
import uk.co.droidinactu.ebooklauncher.R;
import uk.co.droidinactu.ebooklauncher.data.calibre.CalibreDataModel;
import uk.co.droidinactu.ebooklauncher.data.nook.NookBook;
import uk.co.droidinactu.ebooklauncher.data.nook.NookDataModel;
import uk.co.droidinactu.ebooklauncher.data.nook.NookShelf;
import uk.co.droidinactu.ebooklauncher.data.sony.SonyDataModel;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * <p>This class is used for any data manipulation that needs to be tied to the device that the app is running on.
 * </p><p>
 * it currently recognises these devices:
 * <ul><li>Sony PRS-T1</li>
 *     <li>Barnes & Noble Nook Simple Touch</li>
 * </ul>
 * 
 */
public class DeviceFactory {

    public static final String DEVICE_MANUFACTURER_BARNES_AND_NOBLE = "BarnesAndNoble";
    public static final String DEVICE_MANUFACTURER_SONY = "sony";

    public static final String DEVICE_MODEL_BARNES_AND_NOBLE = "NOOK";
    public static final String DEVICE_MODEL_SONY = "PRS-T1";

    /** tag used for logging */
    private static final String LOG_TAG = "DataModelFactory:";

    public static void deleteOldDatabaseFile() {
        new Thread() {
            @Override
            public void run() {
                this.setName("deleting old db file");
                final File f = new File("/data/data/uk.co.droidinactu.ebooklauncher/databases/*");
                if (f.exists()) {
                    f.delete();
                    f.deleteOnExit();
                }
            }
        }.start();
    }

    public static int getAppsGridItemLayout() {
        if (android.os.Build.MANUFACTURER.equalsIgnoreCase(DeviceFactory.DEVICE_MANUFACTURER_BARNES_AND_NOBLE)) {
            return R.layout.application_nook;
        } else {
            return R.layout.application;
        }
    }

    public static int getAppsGridLayout() {
        if (android.os.Build.MANUFACTURER.equalsIgnoreCase(DeviceFactory.DEVICE_MANUFACTURER_BARNES_AND_NOBLE)) {
            return R.layout.apps_grid_nook;
        } else {
            return R.layout.apps_grid;
        }
    }

    /**
     * This function returns a Book object that is created using data 
     * from a cursor read in from a database on the Nook.
     * @param nookCursor the cursor read in from the nook database
     * @return a Book object
     */
    public static Book getBook(final Cursor nookCursor) {
        final Book b = new Book();
        try {
            b.setUniqueIdentifier(nookCursor.getLong(nookCursor.getColumnIndex(BaseColumns._ID)));
        } catch (final Exception e) {
        }
        try {
            b.added_date = nookCursor.getLong(nookCursor.getColumnIndex(NookBook.COLUMN_NAME_date_added));
        } catch (final Exception e) {
        }
        try {
            b.modified_date = nookCursor.getLong(nookCursor.getColumnIndex(NookBook.COLUMN_NAME_date_modified));
        } catch (final Exception e) {
        }
        try {
            b.author = nookCursor.getString(nookCursor.getColumnIndex(NookBook.COLUMN_NAME_authors));
        } catch (final Exception e) {
        }
        b.file_name = nookCursor.getString(nookCursor.getColumnIndex(NookBook.COLUMN_NAME_display_name));
        b.file_path = nookCursor.getString(nookCursor.getColumnIndex(NookBook.COLUMN_NAME_data));
        try {
            b.file_size = nookCursor.getLong(nookCursor.getColumnIndex(NookBook.COLUMN_NAME_size));
        } catch (final Exception e) {
        }
        b.mime_type = nookCursor.getString(nookCursor.getColumnIndex(NookBook.COLUMN_NAME_MIME_TYPE));
        try {
            b.reading_time = nookCursor.getInt(nookCursor.getColumnIndex(NookBook.COLUMN_NAME_date_last_accessed));
        } catch (final Exception e) {
        }
        b.thumbnail = nookCursor.getString(nookCursor.getColumnIndex(NookBook.COLUMN_NAME_thumb_image));
        b.title = nookCursor.getString(nookCursor.getColumnIndex(NookBook.COLUMN_NAME_title));
        return b;
    }

    public static String[] getBookColumnsToMap(final boolean displayFilename) {

        String[] colsToMap = null;

        if (DeviceFactory.getDeviceManufacturer().equalsIgnoreCase(DeviceFactory.DEVICE_MANUFACTURER_SONY) && !displayFilename) {
            // Using SonyDataModel
            colsToMap = new String[] { BaseColumns._ID, Book.COLUMN_NAME_TITLE, Book.COLUMN_NAME_AUTHOR };

        } else if (DeviceFactory.getDeviceManufacturer().equalsIgnoreCase(DeviceFactory.DEVICE_MANUFACTURER_SONY) && displayFilename) {
            // Using SonyDataModel
            colsToMap = new String[] { BaseColumns._ID, Book.COLUMN_NAME_FILE_NAME };

        } else if (DeviceFactory.getDeviceManufacturer().equalsIgnoreCase(
                DeviceFactory.DEVICE_MANUFACTURER_BARNES_AND_NOBLE) && !displayFilename) {
            // Using NookDataModel
            colsToMap = new String[] { BaseColumns._ID, NookBook.COLUMN_NAME_title, NookBook.COLUMN_NAME_authors };

        } else if (DeviceFactory.getDeviceManufacturer().equalsIgnoreCase(
                DeviceFactory.DEVICE_MANUFACTURER_BARNES_AND_NOBLE) && displayFilename) {
            // Using NookDataModel
            colsToMap = new String[] { BaseColumns._ID, NookBook.COLUMN_NAME_display_name };

        } else if (!displayFilename) {
            // Using CalibreDataModel

        } else if (displayFilename) {
            // Using CalibreDataModel
        }
        return colsToMap;
    }

    public static int getBookGridItemLayout() {
        if (android.os.Build.MANUFACTURER.equalsIgnoreCase(DeviceFactory.DEVICE_MANUFACTURER_BARNES_AND_NOBLE)) {
            return R.layout.book_grid_item_nook;
        } else {
            return R.layout.book_grid_item;
        }
    }

    public static int getBookListItemLayout() {
        if (android.os.Build.MANUFACTURER.equalsIgnoreCase(DeviceFactory.DEVICE_MANUFACTURER_BARNES_AND_NOBLE)) {
            return R.layout.book_list_item;
        } else {
            return R.layout.book_list_item;
        }
    }

    public static String[] getCalibreMetadataFilenames() {
        String[] filenames = null;

        if (DeviceFactory.getDeviceManufacturer().equalsIgnoreCase(DeviceFactory.DEVICE_MANUFACTURER_SONY)) {
            // Using SonyDataModel
            filenames = new String[] { "/mnt/extsd/metadata.calibre", "/mnt/sdcard/metadata.calibre" };

        } else if (DeviceFactory.getDeviceManufacturer().equalsIgnoreCase(
                DeviceFactory.DEVICE_MANUFACTURER_BARNES_AND_NOBLE)) {
            // Using NookDataModel
            filenames = new String[] { "/mnt/sdcard/metadata.calibre" };

        } else {

            filenames = new String[] { "/mnt/sdcard/metadata.calibre" };
        }
        return filenames;
    }

    public static String[] getCollectionColumnsToMap() {
        String[] colsToMap = null;

        if (DeviceFactory.getDeviceManufacturer().equalsIgnoreCase(DeviceFactory.DEVICE_MANUFACTURER_SONY)) {
            // Using SonyDataModel
            colsToMap = new String[] { Collection.COLUMN_NAME_TITLE, Collection.COLUMN_NAME_UUID };

        } else if (DeviceFactory.getDeviceManufacturer().equalsIgnoreCase(
                DeviceFactory.DEVICE_MANUFACTURER_BARNES_AND_NOBLE)) {
            // Using NookDataModel
            colsToMap = new String[] { NookShelf.COLUMN_NAME_shelf_name, NookShelf.COLUMN_NAME_total_library_items };
        }
        return colsToMap;
    }

    public static String getCoverImgColumnName() {
        String retStr = "";

        if (DeviceFactory.getDeviceManufacturer().equalsIgnoreCase(DeviceFactory.DEVICE_MANUFACTURER_SONY)) {
            // Using SonyDataModel
            retStr = Book.COLUMN_NAME_THUMBNAIL;

        } else if (DeviceFactory.getDeviceManufacturer().equalsIgnoreCase(
                DeviceFactory.DEVICE_MANUFACTURER_BARNES_AND_NOBLE)) {
            // Using NookDataModel
            retStr = NookBook.COLUMN_NAME_cover_image;

        } else {
            // Using CalibreDataModel
        }

        return retStr;
    }

    /**
     * This method returns a device specific data model class.
     * 
     * @param ctx
     * @return the datamodel class for the device that the app is running on
     */
    public static DataModelInterface getDataModel(final Context ctx) {
        DataModelInterface dataModel = null;

        // then we decide which DataModel class to return
        if (DeviceFactory.getDeviceManufacturer().equalsIgnoreCase(DeviceFactory.DEVICE_MANUFACTURER_SONY)) {
            // Using SonyDataModel
            Log.i(EBookLauncherApplication.LOG_TAG, DeviceFactory.LOG_TAG + "returning a SonyDataModel");
            dataModel = new SonyDataModel(ctx);
            dataModel.openDb();

        } else if (DeviceFactory.getDeviceManufacturer().equalsIgnoreCase(
                DeviceFactory.DEVICE_MANUFACTURER_BARNES_AND_NOBLE)) {
            // Using NookDataModel
            Log.i(EBookLauncherApplication.LOG_TAG, DeviceFactory.LOG_TAG + "returning a NookDataModel");
            dataModel = new NookDataModel(ctx);
            dataModel.openDb();

        } else {
            // Using CalibreDataModel
            Log.i(EBookLauncherApplication.LOG_TAG, DeviceFactory.LOG_TAG + "returning a CalibreDataModel");
            dataModel = new CalibreDataModel(ctx);
            dataModel.openDb();
        }

        return dataModel;
    }

    public static String getDeviceManufacturer() {
        final String tmpStr = android.os.Build.MANUFACTURER;
        // tmpStr = DataModelFactory.DEVICE_MANUFACTURER_BARNES_AND_NOBLE;
        return tmpStr;
    }

    public static String getDeviceModel() {
        final String tmpStr = android.os.Build.MODEL;
        // tmpStr = DataModelFactory.DEVICE_MODEL_BARNES_AND_NOBLE;
        return tmpStr;
    }

    public static String getFilenameColumnName() {
        String retStr = "";

        if (DeviceFactory.getDeviceManufacturer().equalsIgnoreCase(DeviceFactory.DEVICE_MANUFACTURER_SONY)) {
            // Using SonyDataModel
            retStr = Book.COLUMN_NAME_FILE_NAME;

        } else if (DeviceFactory.getDeviceManufacturer().equalsIgnoreCase(
                DeviceFactory.DEVICE_MANUFACTURER_BARNES_AND_NOBLE)) {
            // Using NookDataModel
            retStr = NookBook.COLUMN_NAME_data;

        } else {
            // Using CalibreDataModel
        }

        return retStr;
    }

    public static int getPage1Layout() {
        if (android.os.Build.MANUFACTURER.equalsIgnoreCase(DeviceFactory.DEVICE_MANUFACTURER_BARNES_AND_NOBLE)) {
            return R.layout.page1_nook;
        } else {
            return R.layout.page1;
        }
    }

    public static int getPage2Layout() {
        if (android.os.Build.MANUFACTURER.equalsIgnoreCase(DeviceFactory.DEVICE_MANUFACTURER_BARNES_AND_NOBLE)) {
            return R.layout.page2;
        } else {
            return R.layout.page2;
        }
    }

    public static String[] getPeriodicalColumnsToMap() {
        String[] colsToMap = null;

        if (DeviceFactory.getDeviceManufacturer().equalsIgnoreCase(DeviceFactory.DEVICE_MANUFACTURER_SONY)) {
            colsToMap = new String[] { BaseColumns._ID, Periodical.COLUMN_NAME_PERIODICAL_NAME, };
        } else {
            colsToMap = new String[] { BaseColumns._ID, NookBook.COLUMN_NAME_title };
        }
        return colsToMap;
    }

    public static boolean isNook() {
        return android.os.Build.MANUFACTURER == DeviceFactory.DEVICE_MANUFACTURER_BARNES_AND_NOBLE;
    }

}
