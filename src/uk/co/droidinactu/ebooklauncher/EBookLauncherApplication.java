/*
 * <p> Copyright 2012 Andy Aspell-Clark</p><p> This file is part of eBookLauncher. </p><p>
 * eBookLauncher is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. </p><p> eBookLauncher is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. </p><p> You should have received a copy of the GNU General Public License along with
 * eBookLauncher. If not, see http://www.gnu.org/licenses/.</p>
 */
package uk.co.droidinactu.ebooklauncher;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import uk.co.droidinactu.ebooklauncher.data.DataModelInterface;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

/**
 * 
 * @author andy
 * 
 */
public final class EBookLauncherApplication extends Application {

    public final static String LINE_SEPARATOR = System.getProperty("line.separator");

    /** tag used for logging */
    public static String LOG_TAG = "eBookLauncher:";

    public DataModelInterface dataMdl;

    public final DecimalFormat decFmt = new DecimalFormat("#0.00");
    public final DecimalFormat gbp = new DecimalFormat("£#0.00");

    public int nbrGridCols = 4;
    public int nbrGridRows = 10;
    public final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /*
     * Create a temporary file to see whether a volume is really writeable. It's important not to put it in the root
     * directory which may have a limit on the number of files.
     */
    protected boolean checkFsWritable() {
        final String directoryName = Environment.getExternalStorageDirectory().toString() + "/DCIM";
        final File directory = new File(directoryName);
        if (!directory.isDirectory()) {
            if (!directory.mkdirs()) { return false; }
        }
        final File f = new File(directoryName, ".probe");
        try {
            // Remove stale file if any
            if (f.exists()) {
                f.delete();
            }
            if (!f.createNewFile()) { return false; }
            f.delete();
            return true;
        } catch (final IOException ex) {
            return false;
        }
    }

    protected Drawable getAppImage(final String packageName) {
        try {
            return getPackageManager().getApplicationIcon(packageName);
        } catch (final NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getApplicationAuthor() {
        return this.getString(R.string.about_author);
    }

    public String getApplicationAuthorEmail() {
        return this.getString(R.string.about_email);
    }

    public String getApplicationAuthorWebsite() {
        return this.getString(R.string.about_website_url);
    }

    public String getApplicationDescription() {
        return this.getString(R.string.about_summary);
    }

    public Bitmap getApplicationImage() {
        return BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
    }

    public String getApplicationName() {
        return this.getString(R.string.app_name);
    }

    public String getAppSdCardPathDir() {
        final File extDir = Environment.getExternalStorageDirectory();
        return extDir.getPath() + File.separator + getApplicationName() + File.separator;
    }

    public String getAppVersion() {
        return this.getAppVersion("uk.co.droidinactu.ebooklauncher");
    }

    protected String getAppVersion(final String packageName) {
        String verName = "unknown";
        try {
            final PackageInfo pInfo = getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
            verName = pInfo.versionName;
        } catch (final NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    public int getAppVersionNbr() {
        return this.getAppVersionNbr("uk.co.droidinactu.ebooklauncher");
    }

    protected int getAppVersionNbr(final String packageName) {
        int verName = -1;
        try {
            final PackageInfo pInfo = getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
            verName = pInfo.versionCode;
        } catch (final NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    public DataModelInterface getDataModel() {
        return dataMdl;
    }

    public List<String> getInstalledComponentList() {
        final Intent componentSearchIntent = new Intent();
        componentSearchIntent.addCategory(Constants.COMPONENTS_INTENT_CATEGORY);
        componentSearchIntent.setAction(Constants.COMPONENTS_INTENT_ACTION_DEFAULT);
        final List<ResolveInfo> ril = getPackageManager().queryIntentActivities(componentSearchIntent,
                PackageManager.MATCH_DEFAULT_ONLY);
        final List<String> componentList = new ArrayList<String>();
        Log.d(EBookLauncherApplication.LOG_TAG, "Search for installed components found " + ril.size() + " matches.");
        for (final ResolveInfo ri : ril) {
            if (ri.activityInfo != null) {
                componentList.add(ri.activityInfo.packageName);// + ri.activityInfo.name);
                Log.d(EBookLauncherApplication.LOG_TAG,
                        "Found installed: " + componentList.get(componentList.size() - 1));
            }
        }
        return componentList;
    }

    public String getLogTag() {
        return EBookLauncherApplication.LOG_TAG;
    }

    public int getNbrCpuCores() {
        return Runtime.getRuntime().availableProcessors();
    }

    public String getPayPalDonateName() {
        return "InLibrisLibertas";
    }

    public final String getSdCardDir() {
        final File extDir = Environment.getExternalStorageDirectory();
        return extDir.getPath() + File.separator;
    }

    public boolean hasStorage() {
        return this.hasStorage(true);
    }

    public boolean hasStorage(final boolean requireWriteAccess) {
        final String state = Environment.getExternalStorageState();
        Log.v(getLogTag(), "storage state is " + state);

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            if (requireWriteAccess) {
                final boolean writable = checkFsWritable();
                Log.v(getLogTag(), "storage writable is " + writable);
                return writable;
            } else {
                return true;
            }
        } else if (!requireWriteAccess && Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) { return true; }
        return false;
    }

    public boolean isIntentAvailable(final String action) {
        final PackageManager packageManager = getPackageManager();
        final Intent intent = new Intent(action);
        final List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo.size() > 0) { return true; }
        return false;
    }

    public Boolean isSdAvailable() {
        boolean mExternalStorageAvailable = false;
        final String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
        } else {
            // Something else is wrong. It may be one of many other states, but
            // all we need to know is we can neither read nor write
            mExternalStorageAvailable = false;
        }
        return mExternalStorageAvailable;
    }

    public void setDataModel(final DataModelInterface dataMdl) {
        this.dataMdl = dataMdl;
    }

}
