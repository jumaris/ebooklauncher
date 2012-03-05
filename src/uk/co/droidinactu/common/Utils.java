/*<p>
 * Copyright 2012 Andy Aspell-Clark
 *</p><p>
 * This file is part of eBookLauncher.
 * </p><p>
 * eBookLauncher is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * </p><p>
 * eBookLauncher is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 * </p><p>
 * You should have received a copy of the GNU General Public License along
 * with eBookLauncher. If not, see http://www.gnu.org/licenses/.
 *</p>
 */
package uk.co.droidinactu.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.co.droidinactu.ebooklauncher.EBookLauncherApplication;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Environment;
import android.util.Log;

/**
 * CLASS DOCUMENTATION _MUST_ BE GIVEN HERE. ENSURE RELEVENT @see javadoc
 * REFERENCES ARE USED IF REQUIRED.
 * 
 * @author aspela
 */
public final class Utils {

    /** tag used for logging */
    private static final String LOG_TAG = "Utils";

    public static boolean isSdMounted(final boolean requireWriteAccess) {
        // TODO: After fix the bug, add "if (VERBOSE)" before logging errors.
        final String state = Environment.getExternalStorageState();
        // Log.v(DataModel.LOG_TAG, "storage state is " + state);

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // if (requireWriteAccess) {
            // boolean writable = checkFsWritable();
            // Log.v(TAG, "storage writable is " + writable);
            // return writable;
            // } else {
            return true;
            // }
        } else if (!requireWriteAccess && Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) { return true; }
        return false;
    }

    public static boolean isSdPresent() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    public static void listInstalledApps(final Context ctx) {
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final PackageManager pm = ctx.getPackageManager();
        final List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);
        // String activityInfStr = "";
        final List<String> activities = new ArrayList<String>();
        for (final ResolveInfo ri : list) {
            final ActivityInfo actvyInf = ri.activityInfo;
            activities.add(actvyInf.name + " [" + actvyInf.packageName + "] [" + ri.loadLabel(pm) + "]");
            // activityInfStr += "\n" + actvyInf.name + " [" +
            // actvyInf.packageName + "]";
            // Log.d(SonyPrsT1HomeApplication.LOG_TAG, LOG_TAG + "Package " +
            // activityInfStr);
        }
        Collections.sort(activities);
        Log.d(EBookLauncherApplication.LOG_TAG, Utils.LOG_TAG + "List Apps Finished ");
    }

    public static void logDeviceBuildInfo() {
        try {
            Log.i(EBookLauncherApplication.LOG_TAG, Utils.LOG_TAG + "Device Manufacturer is ["
                    + android.os.Build.MANUFACTURER
                    + "]");
        } catch (final Exception e) {
        }
        try {
            Log.i(EBookLauncherApplication.LOG_TAG, Utils.LOG_TAG + "Device Model is [" + android.os.Build.MODEL + "]");
        } catch (final Exception e) {
        }
        try {
            Log.i(EBookLauncherApplication.LOG_TAG, Utils.LOG_TAG + "Device BOARD is [" + android.os.Build.BOARD + "]");
        } catch (final Exception e) {
        }
        try {
            Log.i(EBookLauncherApplication.LOG_TAG, Utils.LOG_TAG + "Device BOOTLOADER is ["
                    + android.os.Build.BOOTLOADER
                    + "]");
        } catch (final Exception e) {
        }
        try {
            Log.i(EBookLauncherApplication.LOG_TAG, Utils.LOG_TAG + "Device BRAND is [" + android.os.Build.BRAND + "]");
        } catch (final Exception e) {
        }
        try {
            Log.i(EBookLauncherApplication.LOG_TAG, Utils.LOG_TAG + "Device CPU_ABI is ["
                    + android.os.Build.CPU_ABI
                    + "]");
        } catch (final Exception e) {
        }
        try {
            Log.i(EBookLauncherApplication.LOG_TAG, Utils.LOG_TAG + "Device CPU_ABI2 is ["
                    + android.os.Build.CPU_ABI2
                    + "]");
        } catch (final Exception e) {
        }
        try {
            Log.i(EBookLauncherApplication.LOG_TAG, Utils.LOG_TAG + "Device DEVICE is ["
                    + android.os.Build.DEVICE
                    + "]");
        } catch (final Exception e) {
        }
        try {
            Log.i(EBookLauncherApplication.LOG_TAG, Utils.LOG_TAG + "Device DISPLAY is ["
                    + android.os.Build.DISPLAY
                    + "]");
        } catch (final Exception e) {
        }
        try {
            Log.i(EBookLauncherApplication.LOG_TAG, Utils.LOG_TAG + "Device FINGERPRINT is ["
                    + android.os.Build.FINGERPRINT
                    + "]");
        } catch (final Exception e) {
        }
        try {
            Log.i(EBookLauncherApplication.LOG_TAG, Utils.LOG_TAG + "Device HARDWARE is ["
                    + android.os.Build.HARDWARE
                    + "]");
        } catch (final Exception e) {
        }
        try {
            Log.i(EBookLauncherApplication.LOG_TAG, Utils.LOG_TAG + "Device HOST is [" + android.os.Build.HOST + "]");
        } catch (final Exception e) {
        }
        try {
            Log.i(EBookLauncherApplication.LOG_TAG, Utils.LOG_TAG + "Device ID is [" + android.os.Build.ID + "]");
        } catch (final Exception e) {
        }
        try {
            Log.i(EBookLauncherApplication.LOG_TAG, Utils.LOG_TAG + "Device MODEL is [" + android.os.Build.MODEL + "]");
        } catch (final Exception e) {
        }
        try {
            Log.i(EBookLauncherApplication.LOG_TAG, Utils.LOG_TAG + "Device PRODUCT is ["
                    + android.os.Build.PRODUCT
                    + "]");
        } catch (final Exception e) {
        }
        try {
            Log.i(EBookLauncherApplication.LOG_TAG, Utils.LOG_TAG + "Device RADIO is [" + android.os.Build.RADIO + "]");
        } catch (final Exception e) {
        }
        try {
            Log.i(EBookLauncherApplication.LOG_TAG, Utils.LOG_TAG + "Device TAGS is [" + android.os.Build.TAGS + "]");
        } catch (final Exception e) {
        }
        try {
            Log.i(EBookLauncherApplication.LOG_TAG, Utils.LOG_TAG + "Device TYPE is [" + android.os.Build.TYPE + "]");
        } catch (final Exception e) {
        }
        try {
            Log.i(EBookLauncherApplication.LOG_TAG, Utils.LOG_TAG + "Device UNKNOWN is ["
                    + android.os.Build.UNKNOWN
                    + "]");
        } catch (final Exception e) {
        }
        try {
            Log.i(EBookLauncherApplication.LOG_TAG, Utils.LOG_TAG + "Device USER is [" + android.os.Build.USER + "]");
        } catch (final Exception e) {
        }
        try {
            Log.i(EBookLauncherApplication.LOG_TAG, Utils.LOG_TAG + "Device TIME is [" + android.os.Build.TIME + "]");
        } catch (final Exception e) {
        }
        try {
            Log.i(EBookLauncherApplication.LOG_TAG, Utils.LOG_TAG + "Device VERSION.CODENAME is ["
                    + android.os.Build.VERSION.CODENAME
                    + "]");
        } catch (final Exception e) {
        }
        try {
            Log.i(EBookLauncherApplication.LOG_TAG, Utils.LOG_TAG + "Device VERSION.INCREMENTAL is ["
                    + android.os.Build.VERSION.INCREMENTAL
                    + "]");
        } catch (final Exception e) {
        }
        try {
            Log.i(EBookLauncherApplication.LOG_TAG, Utils.LOG_TAG + "Device VERSION.SDK is ["
                    + android.os.Build.VERSION.SDK
                    + "]");
        } catch (final Exception e) {
        }
        try {
            Log.i(EBookLauncherApplication.LOG_TAG, Utils.LOG_TAG + "Device VERSION.SDK_INT is ["
                    + android.os.Build.VERSION.SDK_INT
                    + "]");
        } catch (final Exception e) {
        }
        try {
            Log.i(EBookLauncherApplication.LOG_TAG, Utils.LOG_TAG + "Device VERSION.RELEASE is ["
                    + android.os.Build.VERSION.RELEASE
                    + "]");
        } catch (final Exception e) {
        }
    }

    /**
     * Reallocates an array with a new size, and copies the contents of the old
     * array to the new array.
     * 
     * @param oldArray
     *            the old array, to be reallocated.
     * @param newSize
     *            the new array size.
     * @return A new array with the same contents.
     */
    public static Object resizeArray(final Object oldArray, final int newSize) {
        final int oldSize = java.lang.reflect.Array.getLength(oldArray);
        final Class elementType = oldArray.getClass().getComponentType();
        final Object newArray = java.lang.reflect.Array.newInstance(elementType, newSize);
        final int preserveLength = Math.min(oldSize, newSize);
        if (preserveLength > 0) {
            System.arraycopy(oldArray, 0, newArray, 0, preserveLength);
        }
        return newArray;
    }
}
