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

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public final class EditPreferences extends PreferenceActivity {

    private static final String LOG_TAG = "EditPreferences::";

    public static final String PREF_COLLECTION1_NAME = "Collection1Name";
    public static final String PREF_COLLECTION2_NAME = "Collection2Name";

    public static final String PREF_CURRENT_READ_NUMBER = "NumberRecentReadBooks";
    public static final String PREF_CURRENT_READ_NUMBER_DEF = "10";

    public static final String PREF_FIRST_EXECUTION = "FirstExecution";
    public static final String PREF_FIRST_EXECUTION_DEF = "true";

    public static final String PREF_RECENTLY_ADDED_NUMBER = "NumberRecentlyAddedBooks";
    public static final String PREF_RECENTLY_ADDED_NUMBER_DEF = "10";

    public static final String PREF_USE_FILENAMES = "UseFilenames";
    public static final Boolean PREF_USE_FILENAMES_DEF = false;

    public static final String PREF_USER_EMAIL_ADDR = "EmailAddress";
    public static final String PREF_USER_EMAIL_ADDR_DEF = "";

    public static String getCollectionName1(final Context cntxt) {
        final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(cntxt);
        final String prefVal = mPrefs.getString(EditPreferences.PREF_COLLECTION1_NAME,
                cntxt.getResources().getString(R.string.pref_collection_periodicals));
        return prefVal;
    }

    public static String getCollectionName2(final Context cntxt) {
        final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(cntxt);
        final String prefVal = mPrefs.getString(EditPreferences.PREF_COLLECTION2_NAME,
                cntxt.getResources().getString(R.string.pref_collection_recently_added));
        return prefVal;
    }

    public static int getCurrentReadDuration(final Context cntxt) {
        final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(cntxt);
        final String prefVal = mPrefs.getString(EditPreferences.PREF_CURRENT_READ_NUMBER,
                EditPreferences.PREF_CURRENT_READ_NUMBER_DEF);
        return Integer.parseInt(prefVal);
    }

    public static int getRecentlyAddedDuration(final Context cntxt) {
        final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(cntxt);
        final String prefVal = mPrefs.getString(EditPreferences.PREF_RECENTLY_ADDED_NUMBER,
                EditPreferences.PREF_RECENTLY_ADDED_NUMBER_DEF);
        return Integer.parseInt(prefVal);
    }

    public static String getUserEmailAddr(final Context cntxt) {
        final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(cntxt);
        final String prefVal = mPrefs.getString(EditPreferences.PREF_USER_EMAIL_ADDR,
                EditPreferences.PREF_USER_EMAIL_ADDR_DEF);
        return prefVal;
    }

    public static boolean isFirstExecution(final Context cntxt) {
        final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(cntxt);
        final String prefVal = mPrefs.getString(EditPreferences.PREF_FIRST_EXECUTION,
                EditPreferences.PREF_FIRST_EXECUTION_DEF);
        return prefVal.equals(EditPreferences.PREF_FIRST_EXECUTION_DEF);
    }

    public static void setCollectionName1(final Context cntxt, final String val) {
        final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(cntxt);
        final SharedPreferences.Editor ed = mPrefs.edit();
        ed.putString(EditPreferences.PREF_COLLECTION1_NAME, val);
        ed.commit();
    }

    public static void setCollectionName2(final Context cntxt, final String val) {
        final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(cntxt);
        final SharedPreferences.Editor ed = mPrefs.edit();
        ed.putString(EditPreferences.PREF_COLLECTION2_NAME, val);
        ed.commit();
    }

    public static void setCurrentReadDuration(final Context cntxt, final String val) {
        final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(cntxt);
        final SharedPreferences.Editor ed = mPrefs.edit();
        ed.putString(EditPreferences.PREF_CURRENT_READ_NUMBER, val);
        ed.commit();
    }

    public static void setFirstExecution(final Context cntxt, final boolean val) {
        final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(cntxt);
        final SharedPreferences.Editor ed = mPrefs.edit();
        ed.putString(EditPreferences.PREF_FIRST_EXECUTION, val ? EditPreferences.PREF_FIRST_EXECUTION_DEF : "false");
        ed.commit();
    }

    public static void setRecentlyAddedDuration(final Context cntxt, final String val) {
        final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(cntxt);
        final SharedPreferences.Editor ed = mPrefs.edit();
        ed.putString(EditPreferences.PREF_RECENTLY_ADDED_NUMBER, val);
        ed.commit();
    }

    public static void setUserEmailAddr(final Context cntxt, final String val) {
        final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(cntxt);
        final SharedPreferences.Editor ed = mPrefs.edit();
        ed.putString(EditPreferences.PREF_USER_EMAIL_ADDR, val);
        ed.commit();
    }

    public static boolean useFilenames(final Context cntxt) {
        final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(cntxt);
        final boolean prefVal = mPrefs.getBoolean(EditPreferences.PREF_USE_FILENAMES,
                EditPreferences.PREF_USE_FILENAMES_DEF);
        return prefVal;
    }

    private ListPreference collection1Name;
    private ListPreference collection2Name;
    private EditTextPreference currentlyReading;
    private EBookLauncherApplication myApp;
    private EditTextPreference recentlyAdded;

    private final Preference.OnPreferenceChangeListener syncToggle = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(final Preference preference, final Object newValue) {
            boolean retValue = false;
            if (preference.getKey().equals(EditPreferences.PREF_RECENTLY_ADDED_NUMBER)) {
                try {
                    EditPreferences.setRecentlyAddedDuration(EditPreferences.this, newValue.toString());
                    currentlyReading.setSummary(String.format(
                            EditPreferences.this.getResources().getString(
                                    R.string.pref_number_of_recently_added_summary), newValue));
                    retValue = true;
                } catch (final Exception e) {
                    Log.e(EBookLauncherApplication.LOG_TAG, EditPreferences.LOG_TAG
                            + "onPreferenceChange()::Exception ", e);
                }
            } else if (preference.getKey().equals(EditPreferences.PREF_CURRENT_READ_NUMBER)) {
                try {
                    EditPreferences.setCurrentReadDuration(EditPreferences.this, newValue.toString());
                    currentlyReading.setSummary(String.format(
                            EditPreferences.this.getResources()
                                    .getString(R.string.pref_number_of_recently_read_summary), newValue));
                    retValue = true;
                } catch (final Exception e) {
                    Log.e(EBookLauncherApplication.LOG_TAG, EditPreferences.LOG_TAG
                            + "onPreferenceChange()::Exception ", e);
                }
            } else if (preference.getKey().equals(EditPreferences.PREF_COLLECTION1_NAME)) {
                try {
                    EditPreferences.setCollectionName1(EditPreferences.this, newValue.toString());
                    collection1Name.setSummary(String.format(
                            EditPreferences.this.getResources().getString(R.string.pref_collection1_name_summary),
                            newValue));
                    retValue = true;
                } catch (final Exception e) {
                    Log.e(EBookLauncherApplication.LOG_TAG, EditPreferences.LOG_TAG
                            + "onPreferenceChange()::Exception ", e);
                }
            } else if (preference.getKey().equals(EditPreferences.PREF_COLLECTION2_NAME)) {
                try {
                    EditPreferences.setCollectionName2(EditPreferences.this, newValue.toString());
                    collection2Name.setSummary(String.format(
                            EditPreferences.this.getResources().getString(R.string.pref_collection2_name_summary),
                            newValue));
                    retValue = true;
                } catch (final Exception e) {
                    Log.e(EBookLauncherApplication.LOG_TAG, EditPreferences.LOG_TAG
                            + "onPreferenceChange()::Exception ", e);
                }
            }
            return retValue;
        }
    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            addPreferencesFromResource(R.xml.preferences);
            myApp = (EBookLauncherApplication) getApplication();
        } catch (final Exception e) {
            Log.e(EBookLauncherApplication.LOG_TAG, EditPreferences.LOG_TAG + "onCreate()::Exception ", e);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            final List<String> clctns = myApp.dataMdl.getCollectionNames(this);
            final String[] clcntNameArry = clctns.toArray(new String[clctns.size()]);

            collection1Name = (ListPreference) findPreference(EditPreferences.PREF_COLLECTION1_NAME);
            collection1Name.setOnPreferenceChangeListener(syncToggle);
            collection1Name.setEntries(clcntNameArry);
            collection1Name.setEntryValues(clcntNameArry);
            String tmpValStr = EditPreferences.getCollectionName1(this);
            collection1Name.setSummary(String.format(getResources().getString(R.string.pref_collection1_name_summary),
                    tmpValStr));

            collection2Name = (ListPreference) findPreference(EditPreferences.PREF_COLLECTION2_NAME);
            collection2Name.setOnPreferenceChangeListener(syncToggle);
            collection2Name.setEntries(clcntNameArry);
            collection2Name.setEntryValues(clcntNameArry);
            tmpValStr = EditPreferences.getCollectionName2(this);
            collection2Name.setSummary(String.format(getResources().getString(R.string.pref_collection2_name_summary),
                    tmpValStr));

            currentlyReading = (EditTextPreference) findPreference(EditPreferences.PREF_CURRENT_READ_NUMBER);
            currentlyReading.setOnPreferenceChangeListener(syncToggle);
            int tmpValInt = EditPreferences.getCurrentReadDuration(this);
            currentlyReading.setSummary(String.format(
                    getResources().getString(R.string.pref_number_of_recently_read_summary), tmpValInt));

            recentlyAdded = (EditTextPreference) findPreference(EditPreferences.PREF_RECENTLY_ADDED_NUMBER);
            recentlyAdded.setOnPreferenceChangeListener(syncToggle);
            tmpValInt = EditPreferences.getRecentlyAddedDuration(this);
            recentlyAdded.setSummary(String.format(
                    getResources().getString(R.string.pref_number_of_recently_added_summary), tmpValInt));
        } catch (final Exception e) {
            Log.e(EBookLauncherApplication.LOG_TAG, EditPreferences.LOG_TAG + "onResume()::Exception ", e);
        }
    }

}
