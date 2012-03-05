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
package uk.co.droidinactu.ebooklauncher.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.co.droidinactu.common.view.HorizontialListView;
import uk.co.droidinactu.ebooklauncher.EBookLauncherActivity;
import uk.co.droidinactu.ebooklauncher.EBookLauncherApplication;
import uk.co.droidinactu.ebooklauncher.EditPreferences;
import uk.co.droidinactu.ebooklauncher.R;
import uk.co.droidinactu.ebooklauncher.contenttable.Contents;
import uk.co.droidinactu.ebooklauncher.data.Application;
import uk.co.droidinactu.ebooklauncher.data.DeviceFactory;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public final class Page2Activity extends AbstractActivity implements OnClickListener, OnItemSelectedListener {

    private static final String LOG_TAG = "Page2Activity::";

    private static final String PREF_SELECTED_APP = "SELECTED_APP";
    private Cursor appsCursor = null;
    private int appsStart;
    private Button btnAllNotes;
    private Button btnBrowser;
    private Button btnDictionary;
    private Button btnGoogleBooks;
    private Button btnHandwriting;
    private ImageView btnNextApps;
    private ImageButton btnNextPage;
    private ImageView btnPrevApps;
    private ImageButton btnPrevPage;
    private Button btnPubliclibrary;
    private Button btnPurchasedContent;
    private Button btnTextMemo;
    private TextView lblApplications;
    private TextView lblClock;
    private SimpleCursorAdapter listAdapterApplications;
    private HorizontialListView listviewApplications;
    private EBookLauncherApplication myApp;

    private final List<String> selectedAppNames = new ArrayList<String>();

    @Override
    public void onClick(final View v) {
        if (v.getTag().equals("public library")) {
            final String url = "http://sonysearch.overdrive.com/";
            final Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            this.startActivity(i);

        } else if (v.getTag().equals("google books")) {
            final String url = "http://books.google.com/";
            final Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            this.startActivity(i);

        } else if (v.getTag().equals("browser")) {
            final String url = "http://www.google.co.uk/";
            final Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            this.startActivity(i);

        } else if (v.getTag().equals("purchased content")) {
            final Intent launchIntent = this.getPackageManager().getLaunchIntentForPackage(
                    "com.sony.drbd.ebook.storebrowser");
            this.startActivity(launchIntent);
            // final Intent launchIntent = new Intent(Intent.ACTION_MAIN);
            // launchIntent.setComponent(new
            // ComponentName("com.sony.drbd.ebook.storebrowser",
            // "com.sony.drbd.ebook.storebrowser.EbookStoreBrowserActivity"));
            // this.startActivity(launchIntent);

        } else if (v.getTag().equals("all notes")) {
            final Intent launchIntent = new Intent(Intent.ACTION_MAIN);
            launchIntent.setComponent(new ComponentName("com.sony.drbd.ebook.reader",
                    "com.sony.drbd.ebook.reader.activities.AllNotesListActivity"));
            this.startActivity(launchIntent);

        } else if (v.getTag().equals("dictionary")) {
            Intent launchIntent = new Intent(Intent.ACTION_MAIN);
            launchIntent = this.getPackageManager().getLaunchIntentForPackage("com.sony.drbd.ebook.Dictionary");
            this.startActivity(launchIntent);

        } else if (v.getTag().equals("handwriting")) {
            final Intent launchIntnt = new Intent(this.getApplication(), TextMemoActivity.class);
            launchIntnt.putExtra(TextMemoActivity.HANDWRITING_OR_TEXT, TextMemoActivity.HANDWRITING);
            this.startActivity(launchIntnt);

        } else if (v.getTag().equals("text memo")) {
            final Intent launchIntnt = new Intent(this.getApplication(), TextMemoActivity.class);
            launchIntnt.putExtra(TextMemoActivity.HANDWRITING_OR_TEXT, TextMemoActivity.TEXT);
            this.startActivity(launchIntnt);

        } else if (v.getTag().equals("next_apps")) {
            this.appsStart = this.appsStart + EBookLauncherActivity.NUMBER_BOOKS_ON_SHELF;
            this.updateAppsList();
        } else if (v.getTag().equals("prev_apps")) {
            this.appsStart = this.appsStart - EBookLauncherActivity.NUMBER_BOOKS_ON_SHELF;
            if (this.appsStart < 0) {
                this.appsStart = 0;
            }
            this.updateAppsList();

        } else if (v.getTag().equals("prevpage")) {
            this.finish();

        } else if (v.getTag().equals("nextpage")) {
            this.startActivity(new Intent(this.getApplication(), RecentBooksActivity.class));
        }
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(DeviceFactory.getPage2Layout());

        this.myApp = (EBookLauncherApplication) this.getApplication();

        this.lblClock = (TextView) this.findViewById(R.id.page2_text_clock);

        this.btnNextPage = (ImageButton) this.findViewById(R.id.page2_btn_nextpage);
        this.btnNextPage.setOnClickListener(this);

        this.btnPrevPage = (ImageButton) this.findViewById(R.id.page2_btn_prevpage);
        this.btnPrevPage.setOnClickListener(this);

        this.btnPubliclibrary = (Button) this.findViewById(R.id.page2_btn_public_library);
        this.btnPubliclibrary.setOnClickListener(this);
        this.btnPubliclibrary.setSelected(false);

        this.btnGoogleBooks = (Button) this.findViewById(R.id.page2_btn_google_books);
        this.btnGoogleBooks.setOnClickListener(this);

        this.btnBrowser = (Button) this.findViewById(R.id.page2_btn_browser);
        this.btnBrowser.setOnClickListener(this);

        this.btnPurchasedContent = (Button) this.findViewById(R.id.page2_btn_purchased_content);
        this.btnPurchasedContent.setOnClickListener(this);

        this.btnAllNotes = (Button) this.findViewById(R.id.page2_btn_all_notes);
        this.btnAllNotes.setOnClickListener(this);

        this.btnDictionary = (Button) this.findViewById(R.id.page2_btn_dictionary);
        this.btnDictionary.setOnClickListener(this);

        this.btnHandwriting = (Button) this.findViewById(R.id.page2_btn_handwriting);
        this.btnHandwriting.setOnClickListener(this);

        this.btnTextMemo = (Button) this.findViewById(R.id.page2_btn_text_memo);
        this.btnTextMemo.setOnClickListener(this);

        this.btnNextApps = (ImageView) this.findViewById(R.id.page2_btn_next_apps);
        this.btnNextApps.setOnClickListener(this);

        this.btnPrevApps = (ImageView) this.findViewById(R.id.page2_btn_prev_apps);
        this.btnPrevApps.setOnClickListener(this);

        this.lblApplications = (TextView) this.findViewById(R.id.page2_text_apps);
        this.lblApplications.setOnClickListener(this);

        this.listviewApplications = (HorizontialListView) this.findViewById(R.id.page2_apps);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(0, EBookLauncherActivity.MENU_SETTINGS, 0, R.string.menu_settings).setIcon(
                android.R.drawable.ic_menu_preferences);
        menu.add(0, EBookLauncherActivity.MENU_PREFERENCES, 0, R.string.menu_preferences).setIcon(
                android.R.drawable.ic_menu_preferences);
        menu.add(0, EBookLauncherActivity.MENU_CLEAR_DEFAULTS, 0, R.string.menu_clear_default_settings).setIcon(
                android.R.drawable.ic_menu_close_clear_cancel);
        menu.add(0, EBookLauncherActivity.MENU_SELECT_APPS, 0, R.string.menu_select_apps).setIcon(
                R.drawable.ic_menu_select_apps);
        menu.add(0, EBookLauncherActivity.MENU_ABOUT, 0, R.string.menu_about).setIcon(R.drawable.ic_menu_about);

        return true;
    }

    @Override
    public void onItemSelected(final AdapterView<?> arg0, final View view, final int position, final long id) {
        final Application app = new Application(this.listAdapterApplications.getCursor());
        final ArrayList<Application> apps = this.myApp.dataMdl.getApplications(this, this.selectedAppNames);
        for (final Application a : apps) {
            if (a.title.equals(app.title)) {
                app.intent = a.intent;
                break;
            }
        }
        this.startActivity(app.intent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        Log.d(EBookLauncherApplication.LOG_TAG, Page2Activity.LOG_TAG + "onKeyDown() called ");
        boolean retValue = false;
        switch (keyCode) {
        case 19: {
            // left arrow pressed
            this.finish();
            retValue = true;
            break;
        }
        case 20: {
            // right arrow pressed
            break;
        }
        default:
            retValue = super.onKeyDown(keyCode, event);
        }
        return retValue;
    }

    @Override
    public void onNothingSelected(final AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        boolean retvalue = false;
        switch (item.getItemId()) {
        case EBookLauncherActivity.MENU_SELECT_APPS:
            this.showSelectAppsDialog();
            break;

        case EBookLauncherActivity.MENU_PREFERENCES:
            this.startActivityForResult(new Intent(this.getApplication(), EditPreferences.class),
                    EBookLauncherActivity.ACTIVITY_EDIT_PREFERENCES);
            retvalue = true;
            break;

        case EBookLauncherActivity.MENU_SETTINGS:
            this.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
            retvalue = true;
            break;

        case EBookLauncherActivity.MENU_CLEAR_DEFAULTS:
            final PackageManager pm = this.getPackageManager();
            pm.clearPackagePreferredActivities("uk.co.droidinactu.ebooklauncher");
            pm.clearPackagePreferredActivities("com.android.launcher");
            retvalue = true;
            break;

        case EBookLauncherActivity.MENU_ABOUT:
            EBookLauncherActivity.showAboutWithExtras(this, this.myApp);
            retvalue = true;
            break;

        default:
            return super.onOptionsItemSelected(item);
        }
        return retvalue;
    }

    @Override
    protected void onPause() {
        super.onPause();
        final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor ed = mPrefs.edit();
        for (int x = 0; x < this.selectedAppNames.size(); x++) {
            ed.putString(Page2Activity.PREF_SELECTED_APP + x, this.selectedAppNames.get(x));
        }
        ed.commit();
        this.btnPubliclibrary.setSelected(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        int x = 0;
        String filterStr = mPrefs.getString(Page2Activity.PREF_SELECTED_APP + x, "");
        while (filterStr.length() > 0) {
            this.selectedAppNames.add(filterStr);
            x++;
            filterStr = mPrefs.getString(Page2Activity.PREF_SELECTED_APP + x, "");
        }

        this.updateAppsList();

        final ContentResolver cr = this.getContentResolver();
        Uri getIncdntsUri = Uri.withAppendedPath(Contents.Notepad.TextMemo.CONTENT_URI, "");
        final Cursor txtMemosCursor = cr.query(getIncdntsUri, null, null, null, null);
        txtMemosCursor.moveToFirst();
        final int nbrTextMemos = txtMemosCursor.getCount();

        getIncdntsUri = Uri.withAppendedPath(Contents.Notepad.Handwriting.CONTENT_URI, "");
        final Cursor hwMemosCursor = cr.query(getIncdntsUri, null, null, null, null);
        hwMemosCursor.moveToFirst();
        final int nbrHandwrittenMemos = hwMemosCursor.getCount();

        getIncdntsUri = Uri.withAppendedPath(Contents.Markups.Annotation.CONTENT_URI, "");
        final Cursor anMemosCursor = cr.query(getIncdntsUri, null, null, null, null);
        anMemosCursor.moveToFirst();
        final int nbrAllNotes = anMemosCursor.getCount();

        this.btnAllNotes.setText(String.format(this.getResources().getString(R.string.page2_All_Notes), nbrAllNotes));
        this.btnTextMemo.setText(String.format(this.getResources().getString(R.string.page2_Text_Memo), nbrTextMemos));
        this.btnHandwriting.setText(String.format(this.getResources().getString(R.string.page2_Handwriting),
                nbrHandwrittenMemos));

        this.updateCurrentTime();
    }

    private void showSelectAppsDialog() {

        final List<Application> allApps = this.myApp.dataMdl.getApplications(this, null);
        final int nbrClctns = allApps.size();
        final boolean[] states = new boolean[nbrClctns];
        final CharSequence[] items = new CharSequence[nbrClctns];
        for (int x = 0; x < nbrClctns; x++) {
            items[x] = allApps.get(x).title;
            if (this.selectedAppNames.contains(allApps.get(x))) {
                states[x] = true;
            } else {
                states[x] = false;
            }
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.page2_apps_selection_dialog_title);
        builder.setPositiveButton(R.string.page2_apps_selection_dialog_btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                Page2Activity.this.updateAppsList();
            }
        });
        builder.setMultiChoiceItems(items, states, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, final int item, final boolean state) {
                if (state) {
                    Page2Activity.this.selectedAppNames.add(items[item].toString());
                } else {
                    Page2Activity.this.selectedAppNames.remove(items[item].toString());
                }
            }
        });
        builder.show();
    }

    private void updateAppsList() {
        try {
            this.appsCursor = this.myApp.dataMdl.getApplicationsCursor(this.getApplication(), 0, 999,
                    this.selectedAppNames);
            final int nbrItems = this.appsCursor.getCount();
            if (this.appsStart >= nbrItems) {
                this.appsStart = nbrItems - 1;
            }
            final String tmpStr;
            if ((this.appsStart + 1) < nbrItems) {
                tmpStr = String.format(this.getResources().getString(R.string.page1_nbr_item_format_1),
                        this.appsStart + 1,
                        (this.appsStart + EBookLauncherActivity.NUMBER_BOOKS_ON_SHELF) > nbrItems ? nbrItems
                                : this.appsStart + EBookLauncherActivity.NUMBER_BOOKS_ON_SHELF, nbrItems);
            } else {
                tmpStr = String.format(this.getResources().getString(R.string.page1_nbr_item_format_2),
                        this.appsStart + 1, nbrItems);
            }

            this.lblApplications.setText(this.getResources().getString(R.string.page2_applications) + " " + tmpStr);
            this.appsCursor.close();
            this.appsCursor = this.myApp.dataMdl.getApplicationsCursor(this.getApplication(), this.appsStart,
                    EBookLauncherActivity.NUMBER_BOOKS_ON_SHELF, this.selectedAppNames);

            this.listAdapterApplications = new SimpleCursorAdapter(this, R.layout.app_list_item, this.appsCursor,
                    Application.applicationCursorCols, new int[] { R.id.app_list_title, R.id.app_list_item_img });

            if ((this.appsCursor == null) || ((this.appsCursor != null) && (this.appsCursor.getCount() == 0))) {
                Toast.makeText(this.getApplication(), "No apps available", Toast.LENGTH_LONG);
            } else {
                this.listviewApplications.setOnItemSelectedListener(this);
                this.listAdapterApplications.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

                    @Override
                    public boolean setViewValue(final View view, final Cursor cursor, final int columnIndex) {
                        boolean retval = false;
                        final int iconColIndex = cursor.getColumnIndexOrThrow(Application.applicationCursorCols[1]);
                        final int nameColIndex = cursor.getColumnIndexOrThrow(Application.applicationCursorCols[0]);

                        if (columnIndex == iconColIndex) {
                            try {
                                final ImageView coverImg = (ImageView) view;
                                final String appName = cursor.getString(nameColIndex);
                                final Application app = Page2Activity.this.myApp.dataMdl.getApplicationNamed(
                                        Page2Activity.this.getApplication(), appName);
                                if (app != null) {
                                    coverImg.setImageDrawable(app.icon);
                                    retval = true;
                                }
                            } catch (final Exception e) {
                                Log.e(EBookLauncherApplication.LOG_TAG,
                                        Page2Activity.LOG_TAG + "setViewValue()::Exception ", e);
                            }
                            retval = true;
                        }
                        return retval;
                    }
                });
                this.listviewApplications.setAdapter(this.listAdapterApplications);
                this.registerForContextMenu(this.listviewApplications);
                this.startManagingCursor(this.appsCursor);
            }
        } catch (final Exception e) {
            Log.e(EBookLauncherApplication.LOG_TAG, Page2Activity.LOG_TAG + "updateAppsList()::Exception ", e);
        }
    }

    private void updateCurrentTime() {
        this.lblClock.setText(DateFormat.format("kk:mm:ss", new Date()));
    }
}
