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

import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;

/**
 * Represents a launchable application. An application is made of a name (or
 * title), an intent and an icon.
 */
public final class Application {
    public static final String[] applicationCursorCols = new String[] { "title", "icon", "intent", "_id" };

    public long _id = -1;

    /**
     * When set to true, indicates that the icon has been resized.
     */
    public boolean filtered;

    /**
     * The application icon.
     */
    public Drawable icon;

    /**
     * The intent used to start the application.
     */
    public Intent intent;

    /**
     * The application name.
     */
    public CharSequence title;

    public Application() {
        // TODO Auto-generated constructor stub
    }

    public Application(final Cursor tmpCursor) {
        this.title = tmpCursor.getString(tmpCursor.getColumnIndex(applicationCursorCols[0]));
        // this.icon =
        // tmpCursor.getString(tmpCursor.getColumnIndex(applicationCursorCols[1]));
        // this.intent =
        // tmpCursor.getString(tmpCursor.getColumnIndex(applicationCursorCols[2]));
        this._id = tmpCursor.getLong(tmpCursor.getColumnIndex(applicationCursorCols[3]));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Application)) {
            return false;
        }

        final Application that = (Application) o;
        return this.title.equals(that.title)
                && this.intent.getComponent().getClassName().equals(that.intent.getComponent().getClassName());
    }

    @Override
    public int hashCode() {
        int result;
        result = this.title != null ? this.title.hashCode() : 0;
        final String name = this.intent.getComponent().getClassName();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    /**
     * Creates the application intent based on a component name and various
     * launch flags.
     * 
     * @param className
     *            the class name of the component representing the intent
     * @param launchFlags
     *            the launch flags
     */
    public void setActivity(final ComponentName className, final int launchFlags) {
        this.intent = new Intent(Intent.ACTION_MAIN);
        this.intent.addCategory(Intent.CATEGORY_LAUNCHER);
        this.intent.setComponent(className);
        this.intent.setFlags(launchFlags);
    }
}
