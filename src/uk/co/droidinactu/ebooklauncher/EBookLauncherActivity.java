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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;

import uk.co.droidinactu.common.view.AboutDialog;
import uk.co.droidinactu.common.view.HorizontialListView;
import uk.co.droidinactu.ebooklauncher.data.Book;
import uk.co.droidinactu.ebooklauncher.data.DeviceFactory;
import uk.co.droidinactu.ebooklauncher.view.ApplicationsActivity;
import uk.co.droidinactu.ebooklauncher.view.BookDetailsActivity;
import uk.co.droidinactu.ebooklauncher.view.BookSortBy;
import uk.co.droidinactu.ebooklauncher.view.BooksActivity;
import uk.co.droidinactu.ebooklauncher.view.CollectionsActivity;
import uk.co.droidinactu.ebooklauncher.view.CoverFlowActivity;
import uk.co.droidinactu.ebooklauncher.view.Page2Activity;
import uk.co.droidinactu.ebooklauncher.view.PeriodicalsActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.eink.norefresh.A2Service;

/**
 * this is the main screen of the app. 
 * @author aspela
 */
public final class EBookLauncherActivity extends Activity implements OnClickListener {

    public static final int ACTIVITY_EDIT_PREFERENCES = 8192;
    public static final int CONTEXTMENU_DELETE_BOOK = 2;
    public static final int CONTEXTMENU_OPEN_BOOK = 1;
    public static final int CONTEXTMENU_VIEW_DETAILS = 0;

    private final static String DOWNLOAD_APK_URL = "http://code.google.com/p/sonyprst1home/downloads/list";

    /** tag used for logging */
    public static final String LOG_TAG = "EBookLauncherActivity :";

    public static final int MENU_ABOUT = 10;
    public static final int MENU_CHECK_NEW_VERSION = 20;
    public static final int MENU_CHOOSE_COLLECTION = 40;
    public static final int MENU_CLEAR_DEFAULTS = 50;
    public static final int MENU_CLEAR_FILTERS = 30;
    public static final int MENU_PREFERENCES = 60;
    public static final int MENU_SELECT_APPS = 70;
    public static final int MENU_SEND_LOG = 80;
    public static final int MENU_SETTINGS = 90;
    public static final int NEW_VER_NOTIFICATION_ID = 3124;
    public static final int NUMBER_BOOKS_ON_SHELF = 4;
    private final static String VERSION_TXT_URL = "http://sonyprst1home.googlecode.com/hg/version.txt";

    /**
     * Show an about dialog for this application.
     */
    public static void showAboutWithExtras(final Activity cntxt, final EBookLauncherApplication app) {
        AboutDialog about = new AboutDialog(cntxt, app);
        about.setTitle(app.getApplicationName());
        about.show();
    }

    private Button btnApps;
    private Button btnBooks;
    private long btnBooksCount = 0;

    private Button btnCollections;
    private long btnCollectionsCount = 0;
    private Button btnCoverflow;
    private ImageButton btnNextPage;
    private Button btnPeriodicals;
    private long btnPeriodicalsCount = 0;
    private final AsyncTask<Integer, Integer, String> checkForNewVersion = new AsyncTask<Integer, Integer, String>() {
        private int currentVersion = -1;

        @Override
        protected String doInBackground(final Integer... params) {
            Thread.currentThread().setName("checkForNewVersion()");

            this.currentVersion = params[0];

            String version = "";
            BufferedReader in = null;

            try {
                // Create a URL for the desired page
                final URL url = new URL(EBookLauncherActivity.VERSION_TXT_URL);

                // Read all the text returned by the server
                in = new BufferedReader(new InputStreamReader(url.openStream()));
                String str;
                while ((str = in.readLine()) != null) {
                    version += str;
                }
            } catch (final MalformedURLException e) {
            } catch (final IOException e) {
            } finally {
                try {
                    in.close();
                } catch (final Exception e) {
                    // if we have a problem closing the database we ignore it
                }
                in = null;
            }

            return version;
        }

        /*
         * (non-Javadoc)
         * 
         * @see android.os .AsyncTask #onPostExecute ( java.lang.Object )
         */
        @Override
        protected void onPostExecute(final String result) {
            if ((result != null) && (result.length() > 0)) {
                final String verCode = result.substring(0, result.indexOf(','));
                final String verName = result.substring(verCode.length());

                if (this.currentVersion < Integer.parseInt(verCode)) {
                    // a new version is available, so post a notification

                    // configure the notification
                    final Notification notification = new Notification(R.drawable.ic_launcher, String.format(
                            EBookLauncherActivity.this.getResources().getString(R.string.new_version_available),
                            verName), System.currentTimeMillis());
                    notification.flags = notification.flags | Notification.FLAG_AUTO_CANCEL;

                    final Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
                    notificationIntent.setData(Uri.parse(EBookLauncherActivity.DOWNLOAD_APK_URL));

                    final Context context = EBookLauncherActivity.this.getApplicationContext();
                    final CharSequence contentTitle = "My notification";
                    final CharSequence contentText = "Hello World!";
                    final PendingIntent contentIntent = PendingIntent.getActivity(EBookLauncherActivity.this, 0,
                            notificationIntent, 0);

                    notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

                    // then add it to the NotificationManager
                    final NotificationManager notificationManager = (NotificationManager) EBookLauncherActivity.this
                            .getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(EBookLauncherActivity.NEW_VER_NOTIFICATION_ID, notification);
                }
            }
        }

    };
    private int collection1Start = 0;
    private int collection2Start = 0;
    private int currentlyReadingStart = 0;
    private Cursor cursrCollcnts1;
    private Cursor cursrCollcnts2;
    private Cursor cursrCurrentRead;
    private ImageView imgBtnCollection1Next;
    private ImageView imgBtnCollection1Prev;
    private ImageView imgBtnCollection2Next;
    private ImageView imgBtnCollection2Prev;
    private ImageView imgBtnCurrentReadNext;
    private ImageView imgBtnCurrentReadPrev;
    private TextView lblClock;
    private TextView lblCollection1;
    private TextView lblCollection2;
    private TextView lblCurrentRead;
    private SimpleCursorAdapter listAdapterCollectns1;
    private SimpleCursorAdapter listAdapterCollectns2;
    private SimpleCursorAdapter listAdapterCurrentRead;

    private HorizontialListView listviewCollectns1;

    private HorizontialListView listviewCollectns2;
    private HorizontialListView listviewCurrentRead;
    private EBookLauncherApplication myApp;

    private Button btnSettings;
    private TextView lblAppVerNbr;

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
        case ACTIVITY_EDIT_PREFERENCES:
            this.updateCurrentlyReading();
            this.updateCollectionlist1();
            this.updateCollectionlist2();
            break;
        default:
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(final View v) {
        if (v.getTag().equals("apps")) {
            this.startActivity(new Intent(this.getApplication(), ApplicationsActivity.class));

        } else if (v.getTag().equals("settings")) {
            this.startActivity(new Intent(this.getApplication(), EditPreferences.class));

        } else if (v.getTag().equals("coverflow")) {
            this.startActivity(new Intent(this.getApplication(), CoverFlowActivity.class));

        } else if (v.getTag().equals("collections")) {
            this.startActivity(new Intent(this.getApplication(), CollectionsActivity.class));

        } else if (v.getTag().equals("books")) {
            this.startActivity(new Intent(this.getApplication(), BooksActivity.class));

        } else if (v.getTag().equals("periodicals")) {
            this.startActivity(new Intent(this.getApplication(), PeriodicalsActivity.class));

        } else if (v.getTag().equals("nextpage")) {
            this.startActivity(new Intent(this.getApplication(), Page2Activity.class));

        } else if (v.getTag().equals("next_currnt_read")) {
            this.currentlyReadingStart = this.currentlyReadingStart + EBookLauncherActivity.NUMBER_BOOKS_ON_SHELF;
            this.updateCurrentlyReading();

        } else if (v.getTag().equals("prev_currnt_read")) {
            this.currentlyReadingStart = this.currentlyReadingStart - EBookLauncherActivity.NUMBER_BOOKS_ON_SHELF;
            if (this.currentlyReadingStart < 0) {
                this.currentlyReadingStart = 0;
            }
            this.updateCurrentlyReading();

        } else if (v.getTag().equals("next_collection1")) {
            this.collection1Start = this.collection1Start + EBookLauncherActivity.NUMBER_BOOKS_ON_SHELF;
            this.updateCollectionlist1();

        } else if (v.getTag().equals("prev_collection1")) {
            this.collection1Start = this.collection1Start - EBookLauncherActivity.NUMBER_BOOKS_ON_SHELF;
            if (this.collection1Start < 0) {
                this.collection1Start = 0;
            }
            this.updateCollectionlist1();

        } else if (v.getTag().equals("next_collection2")) {
            this.collection2Start = this.collection2Start + EBookLauncherActivity.NUMBER_BOOKS_ON_SHELF;
            this.updateCollectionlist2();

        } else if (v.getTag().equals("prev_collection2")) {
            this.collection2Start = this.collection2Start - EBookLauncherActivity.NUMBER_BOOKS_ON_SHELF;
            if (this.collection2Start < 0) {
                this.collection2Start = 0;
            }
            this.updateCollectionlist2();
        }
        this.updateCurrentTime();
    }

    @Override
    public boolean onContextItemSelected(final MenuItem aItem) {

        final AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) aItem.getMenuInfo();

        // To get the id of the clicked item in the list use menuInfo.id
        Log.e(EBookLauncherApplication.LOG_TAG, EBookLauncherActivity.LOG_TAG + "list pos:"
                + menuInfo.position
                + " id:"
                + menuInfo.id);
        final Book bk = this.myApp.dataMdl.getBook(this, menuInfo.id);

        switch (aItem.getItemId()) {

        case CONTEXTMENU_VIEW_DETAILS:
            Log.d(EBookLauncherApplication.LOG_TAG + EBookLauncherActivity.LOG_TAG,
                    "Show details of book: " + bk.toString());
            this.startActivity(new Intent(this, BookDetailsActivity.class));
            return true;

        case CONTEXTMENU_OPEN_BOOK:
            return true;

        case CONTEXTMENU_DELETE_BOOK:
            Log.d(EBookLauncherApplication.LOG_TAG + EBookLauncherActivity.LOG_TAG, "Delete book: " + bk.toString());
            // this.myApp.getCalibreController().deleteBook(this.myApp.contextMenuSelectedBook);
            return true;

        default:
            return super.onContextItemSelected(aItem);
        }
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(DeviceFactory.getPage1Layout());

        // Start the NoRefresh Service
        Intent svc = new Intent(this, A2Service.class);
        this.startService(svc);

        this.myApp = (EBookLauncherApplication) this.getApplication();
        DeviceFactory.deleteOldDatabaseFile();

        // this.checkForNewVersion.execute(((SonyPrsT1HomeApplication)
        // this.getApplication()).getAppVersionNbr());
        // this.listInstalledApps();
        this.preferencesRestore();

        // final CalibreDataModel cdm = new CalibreDataModel(this);
        // cdm.importCalibreMetadata(DataModelFactory.getCalibreMetadataFilenames());

        this.btnPeriodicals = (Button) this.findViewById(R.id.page1_btn_periodicals);
        this.btnCollections = (Button) this.findViewById(R.id.page1_btn_collections);
        this.btnApps = (Button) this.findViewById(R.id.page1_btn_apps);
        this.btnBooks = (Button) this.findViewById(R.id.page1_btn_books);
        this.btnSettings = (Button) this.findViewById(R.id.page1_btn_settings);

        this.btnApps.setOnClickListener(this);
        this.btnBooks.setOnClickListener(this);
        this.btnCollections.setOnClickListener(this);
        this.btnPeriodicals.setOnClickListener(this);
        this.btnSettings.setOnClickListener(this);

        this.imgBtnCurrentReadNext = (ImageView) this.findViewById(R.id.page1_btn_next_currnt_read);
        this.imgBtnCurrentReadPrev = (ImageView) this.findViewById(R.id.page1_btn_prev_currnt_read);

        this.imgBtnCollection1Next = (ImageView) this.findViewById(R.id.page1_btn_next_collection1);
        this.imgBtnCollection1Prev = (ImageView) this.findViewById(R.id.page1_btn_prev_collection1);

        this.imgBtnCollection2Next = (ImageView) this.findViewById(R.id.page1_btn_next_collection2);
        this.imgBtnCollection2Prev = (ImageView) this.findViewById(R.id.page1_btn_prev_collection2);

        this.imgBtnCurrentReadNext.setOnClickListener(this);
        this.imgBtnCurrentReadPrev.setOnClickListener(this);

        this.imgBtnCollection1Next.setOnClickListener(this);
        this.imgBtnCollection1Prev.setOnClickListener(this);

        this.imgBtnCollection2Next.setOnClickListener(this);
        this.imgBtnCollection2Prev.setOnClickListener(this);

        this.lblClock = (TextView) this.findViewById(R.id.page1_text_clock);
        this.lblAppVerNbr = (TextView) this.findViewById(R.id.page1_text_app_version_nbr);
        this.lblAppVerNbr.setText("Ver: " + this.myApp.getAppVersion());

        this.lblCurrentRead = (TextView) this.findViewById(R.id.page1_text_currently_reading_lbl);
        this.lblCollection1 = (TextView) this.findViewById(R.id.page1_text_collection1_lbl);
        this.lblCollection2 = (TextView) this.findViewById(R.id.page1_text_collection2_lbl);

        this.listviewCurrentRead = (HorizontialListView) this.findViewById(R.id.page1_current_reading);
        this.listviewCollectns1 = (HorizontialListView) this.findViewById(R.id.page1_collections_list1);
        this.listviewCollectns2 = (HorizontialListView) this.findViewById(R.id.page1_collections_list2);

        if (android.os.Build.MANUFACTURER.equalsIgnoreCase(DeviceFactory.DEVICE_MANUFACTURER_BARNES_AND_NOBLE)) {

        } else {
            this.btnNextPage = (ImageButton) this.findViewById(R.id.page1_btn_nextpage);
            this.btnNextPage.setVisibility(View.VISIBLE);
            this.btnNextPage.setOnClickListener(this);
            // Coverflow only on Sony (for now)
            this.btnCoverflow = (Button) this.findViewById(R.id.page1_btn_coverflow);
            this.btnCoverflow.setOnClickListener(this);
        }
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
        menu.add(0, EBookLauncherActivity.MENU_ABOUT, 0, R.string.menu_about).setIcon(R.drawable.ic_menu_about);
        menu.add(0, EBookLauncherActivity.MENU_CHECK_NEW_VERSION, 0, R.string.check_for_new_version).setIcon(
                R.drawable.ic_menu_check_updates);

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        Log.d(EBookLauncherApplication.LOG_TAG, EBookLauncherActivity.LOG_TAG + "onKeyDown() called ");
        boolean retValue = false;
        switch (keyCode) {
        case 19: {
            // left arrow pressed
            this.startActivity(new Intent(this.getApplication(), CoverFlowActivity.class));
            retValue = true;
            break;
        }
        case 20: {
            // right arrow pressed
            this.startActivity(new Intent(this.getApplication(), Page2Activity.class));
            retValue = true;
            break;
        }
        default:
            retValue = super.onKeyDown(keyCode, event);
        }
        return retValue;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        boolean retvalue = false;
        switch (item.getItemId()) {
        case MENU_PREFERENCES:
            this.startActivityForResult(new Intent(this.getApplication(), EditPreferences.class),
                    EBookLauncherActivity.ACTIVITY_EDIT_PREFERENCES);
            retvalue = true;
            break;

        case MENU_SETTINGS:
            this.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
            retvalue = true;
            break;

        case MENU_CHECK_NEW_VERSION:
            final Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(EBookLauncherActivity.DOWNLOAD_APK_URL));
            this.startActivity(i);
            break;

        case MENU_CLEAR_DEFAULTS:
            final PackageManager pm = this.getPackageManager();
            pm.clearPackagePreferredActivities("uk.co.droidinactu.ebooklauncher");
            pm.clearPackagePreferredActivities("com.android.launcher");
            retvalue = true;
            break;

        case MENU_ABOUT:
            this.showAboutWithExtras();
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

        this.preferencesBackup();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // this.logDeviceBuildInfo();

        if (this.myApp.dataMdl == null) {
            this.myApp.dataMdl = DeviceFactory.getDataModel(this.getApplication());
            if (this.myApp.dataMdl == null) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Device \n[" + android.os.Build.MANUFACTURER + "]\nnot recognized!")
                        .setCancelable(false).setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, final int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();

            } else if (!this.myApp.dataMdl.isOpened()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Database\n[" + this.myApp.dataMdl.getDbFilename() + "]\nfailed to open!")
                        .setCancelable(false).setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, final int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();
            }
        }

        this.updateButtonCounts();

        this.updateCurrentlyReading();
        this.updateCollectionlist1();
        this.updateCollectionlist2();

        this.updateCurrentTime();
    }

    /**
     * copy the applications preferences to the sd card as a backup.
     */
    private void preferencesBackup() {
        try {
            final File f1 = new File(
                    "/data/data/uk.co.droidinactu.ebooklauncher/shared_prefs/uk.co.droidinactu.sony.prst1.home_preferences.xml");
            final File f2 = new File("/mnt/sdcard/sonyprst1home_shared_prefs.xml");
            if (f1.exists()) {
                FileUtils.copyFile(f1, f2);
            }
        } catch (final IOException e) {
            Log.e(EBookLauncherApplication.LOG_TAG, EBookLauncherActivity.LOG_TAG + "Exception ", e);
        }
    }

    /**
     * if the application does not currently have any preferences set and a
     * backup preferences file exists on the sd card, restore the preferences.
     */
    private void preferencesRestore() {
        try {
            final File f1 = new File(
                    "/data/data/uk.co.droidinactu.ebooklauncher/shared_prefs/uk.co.droidinactu.sony.prst1.home_preferences.xml");
            final File f2 = new File("/mnt/sdcard/sonyprst1home_shared_prefs.xml");
            if (!f1.exists() && f2.exists()) {
                FileUtils.copyFile(f2, f1);
            }
        } catch (final IOException e) {
            Log.e(EBookLauncherApplication.LOG_TAG, EBookLauncherActivity.LOG_TAG + "Exception ", e);
        }
    }

    private void setupList(final HorizontialListView listview, final SimpleCursorAdapter listAdapter, final Cursor cursr) {
        if ((cursr == null) || ((cursr != null) && (cursr.getCount() == 0))) {
            Toast.makeText(this.getApplication(), "No books available", Toast.LENGTH_LONG);
        } else {
            listview.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(final ContextMenu conMenu, final View v, final ContextMenuInfo menuInfo) {
                    final LinearLayout ll = (LinearLayout) ((HorizontialListView) v)
                            .getChildAt(((AdapterContextMenuInfo) menuInfo).position);
                    final Drawable bookCover = ((ImageView) ll.getChildAt(0)).getDrawable();
                    final String bookTitle = ((TextView) ll.getChildAt(1)).getText().toString();
                    conMenu.setHeaderIcon(bookCover); // set to book cover
                    conMenu.setHeaderTitle(bookTitle); // set to book title
                    conMenu.add(0, EBookLauncherActivity.CONTEXTMENU_VIEW_DETAILS, 0,
                            R.string.book_grid_context_item_view_book_details);
                    conMenu.add(0, EBookLauncherActivity.CONTEXTMENU_OPEN_BOOK, 1,
                            R.string.book_grid_context_item_open_book);
                    conMenu.add(0, EBookLauncherActivity.CONTEXTMENU_DELETE_BOOK, 2,
                            R.string.book_grid_context_item_delete_book);
                }
            });
            listview.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(final AdapterView<?> av, final View v, final int pos, final long id) {
                    EBookLauncherActivity.this.myApp.dataMdl.launchBook(EBookLauncherActivity.this, id);
                }

                @Override
                public void onNothingSelected(final AdapterView<?> arg0) {
                }
            });
            listAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(final View view, final Cursor cursor, final int columnIndex) {
                    boolean retval = false;
                    final int idColIndex = cursor.getColumnIndexOrThrow(BaseColumns._ID);
                    final int thumbColIndex = cursor.getColumnIndexOrThrow(DeviceFactory.getCoverImgColumnName());

                    if (columnIndex == idColIndex) {
                        try {
                            final ImageView coverImg = (ImageView) view;
                            final String thumbnailFilename = cursor.getString(thumbColIndex);
                            final Bitmap bitmap = EBookLauncherActivity.this.myApp.dataMdl.getBookCoverImg(
                                    EBookLauncherActivity.this, thumbnailFilename);
                            coverImg.setImageDrawable(new BitmapDrawable(bitmap));
                            retval = true;
                        } catch (final Exception e) {
                            Log.e(EBookLauncherApplication.LOG_TAG, EBookLauncherActivity.LOG_TAG + "Exception ", e);
                        }
                        retval = true;
                    }
                    return retval;
                }
            });
            listview.setAdapter(listAdapter);
            this.registerForContextMenu(listview);
            this.startManagingCursor(cursr);
        }
    }

    /**
     * Show an about dialog for this application.
     */
    public void showAboutWithExtras() {
        EBookLauncherActivity.showAboutWithExtras(this, this.myApp);
    }

    private void updateButtonCounts() {
        new Thread() {
            @Override
            public void run() {
                this.setName("updateButtonCounts()");
                Cursor tmpCursor = EBookLauncherActivity.this.myApp.dataMdl.getBooks(
                        EBookLauncherActivity.this.getApplication(), "", BookSortBy.TITLE, "");
                if (tmpCursor != null) {
                    EBookLauncherActivity.this.btnBooksCount = tmpCursor.getCount();
                    tmpCursor.close();
                }
                tmpCursor = EBookLauncherActivity.this.myApp.dataMdl.getCollections(0, 999, null);
                if (tmpCursor != null) {
                    EBookLauncherActivity.this.btnCollectionsCount = tmpCursor.getCount();
                    tmpCursor.close();
                }
                tmpCursor = EBookLauncherActivity.this.myApp.dataMdl.getPeriodicals(EBookLauncherActivity.this
                        .getApplication());
                if (tmpCursor != null) {
                    EBookLauncherActivity.this.btnPeriodicalsCount = tmpCursor.getCount();
                    tmpCursor.close();
                }

                final PackageManager manager = EBookLauncherActivity.this.getPackageManager();
                final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                final List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);

                EBookLauncherActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        EBookLauncherActivity.this.btnApps.setText(String.format(EBookLauncherActivity.this
                                .getResources().getString(R.string.page1_Apps), apps.size()));
                        EBookLauncherActivity.this.btnBooks.setText(String.format(EBookLauncherActivity.this
                                .getResources().getString(R.string.page1_Books),
                                EBookLauncherActivity.this.btnBooksCount));
                        EBookLauncherActivity.this.btnCollections.setText(String.format(EBookLauncherActivity.this
                                .getResources().getString(R.string.page1_Collections),
                                EBookLauncherActivity.this.btnCollectionsCount));
                        EBookLauncherActivity.this.btnPeriodicals.setText(String.format(EBookLauncherActivity.this
                                .getResources().getString(R.string.page1_Periodicals),
                                EBookLauncherActivity.this.btnPeriodicalsCount));
                    }
                });
            }
        }.start();
    }

    private void updateCollectionlist1() {
        new Thread() {
            @Override
            public void run() {
                this.setName("updateCollectionlist1()");
                final String clctname = EditPreferences.getCollectionName1(EBookLauncherActivity.this.getApplication());

                try {

                    if (clctname.equals(EBookLauncherActivity.this.getResources().getString(
                            R.string.pref_collection_recently_added))) {
                        EBookLauncherActivity.this.cursrCollcnts1 = EBookLauncherActivity.this.myApp.dataMdl
                                .getRecentlyAdded(EBookLauncherActivity.this);
                        final int nbrItems = EBookLauncherActivity.this.cursrCollcnts1.getCount();
                        if (EBookLauncherActivity.this.collection1Start >= nbrItems) {
                            EBookLauncherActivity.this.collection1Start = nbrItems - 1;
                        }
                        final String tmpStr;
                        if ((EBookLauncherActivity.this.collection1Start + 1) < nbrItems) {
                            tmpStr = String
                                    .format(EBookLauncherActivity.this.getResources().getString(
                                            R.string.page1_nbr_item_format_1),
                                            EBookLauncherActivity.this.collection1Start + 1,
                                            (EBookLauncherActivity.this.collection1Start + EBookLauncherActivity.NUMBER_BOOKS_ON_SHELF) > nbrItems ? nbrItems
                                                    : EBookLauncherActivity.this.collection1Start + EBookLauncherActivity.NUMBER_BOOKS_ON_SHELF,
                                            nbrItems);
                        } else {
                            tmpStr = String.format(
                                    EBookLauncherActivity.this.getResources().getString(
                                            R.string.page1_nbr_item_format_2),
                                    EBookLauncherActivity.this.collection1Start + 1, nbrItems);
                        }
                        EBookLauncherActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                EBookLauncherActivity.this.lblCollection1.setText(clctname + " " + tmpStr);
                            }
                        });
                        EBookLauncherActivity.this.cursrCollcnts1.close();
                        EBookLauncherActivity.this.cursrCollcnts1 = EBookLauncherActivity.this.myApp.dataMdl
                                .getRecentlyAdded(EBookLauncherActivity.this,
                                        EBookLauncherActivity.this.collection1Start,
                                        EBookLauncherActivity.NUMBER_BOOKS_ON_SHELF);

                    } else if (clctname.equals(EBookLauncherActivity.this.getResources().getString(
                            R.string.pref_collection_periodicals))) {
                        EBookLauncherActivity.this.cursrCollcnts1 = EBookLauncherActivity.this.myApp.dataMdl
                                .getPeriodicals(EBookLauncherActivity.this);
                        final int nbrItems = EBookLauncherActivity.this.cursrCollcnts1.getCount();
                        if (EBookLauncherActivity.this.collection1Start >= nbrItems) {
                            EBookLauncherActivity.this.collection1Start = nbrItems - 1;
                        }
                        final String tmpStr;
                        if ((EBookLauncherActivity.this.collection1Start + 1) < nbrItems) {
                            tmpStr = String
                                    .format(EBookLauncherActivity.this.getResources().getString(
                                            R.string.page1_nbr_item_format_1),
                                            EBookLauncherActivity.this.collection1Start + 1,
                                            (EBookLauncherActivity.this.collection1Start + EBookLauncherActivity.NUMBER_BOOKS_ON_SHELF) > nbrItems ? nbrItems
                                                    : EBookLauncherActivity.this.collection1Start + EBookLauncherActivity.NUMBER_BOOKS_ON_SHELF,
                                            nbrItems);
                        } else {
                            tmpStr = String.format(
                                    EBookLauncherActivity.this.getResources().getString(
                                            R.string.page1_nbr_item_format_2),
                                    EBookLauncherActivity.this.collection1Start + 1, nbrItems);
                        }
                        EBookLauncherActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                EBookLauncherActivity.this.lblCollection1.setText(clctname + " " + tmpStr);
                            }
                        });
                        EBookLauncherActivity.this.cursrCollcnts1.close();
                        EBookLauncherActivity.this.cursrCollcnts1 = EBookLauncherActivity.this.myApp.dataMdl
                                .getPeriodicals(EBookLauncherActivity.this,
                                        EBookLauncherActivity.this.collection1Start,
                                        EBookLauncherActivity.NUMBER_BOOKS_ON_SHELF);

                    } else {
                        EBookLauncherActivity.this.cursrCollcnts1 = EBookLauncherActivity.this.myApp.dataMdl
                                .getBooksInCollection(clctname, BookSortBy.TITLE);
                        final int nbrItems = EBookLauncherActivity.this.cursrCollcnts1.getCount();
                        if (EBookLauncherActivity.this.collection1Start >= nbrItems) {
                            EBookLauncherActivity.this.collection1Start = nbrItems - 1;
                        }
                        final String tmpStr;
                        if ((EBookLauncherActivity.this.collection1Start + 1) < nbrItems) {
                            tmpStr = String
                                    .format(EBookLauncherActivity.this.getResources().getString(
                                            R.string.page1_nbr_item_format_1),
                                            EBookLauncherActivity.this.collection1Start + 1,
                                            (EBookLauncherActivity.this.collection1Start + EBookLauncherActivity.NUMBER_BOOKS_ON_SHELF) > nbrItems ? nbrItems
                                                    : EBookLauncherActivity.this.collection1Start + EBookLauncherActivity.NUMBER_BOOKS_ON_SHELF,
                                            nbrItems);
                        } else {
                            tmpStr = String.format(
                                    EBookLauncherActivity.this.getResources().getString(
                                            R.string.page1_nbr_item_format_2),
                                    EBookLauncherActivity.this.collection1Start + 1, nbrItems);
                        }
                        EBookLauncherActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                EBookLauncherActivity.this.lblCollection1.setText(clctname + " " + tmpStr);
                            }
                        });
                        EBookLauncherActivity.this.cursrCollcnts1.close();
                        EBookLauncherActivity.this.cursrCollcnts1 = EBookLauncherActivity.this.myApp.dataMdl
                                .getBooksInCollection(clctname, EBookLauncherActivity.this.collection1Start,
                                        EBookLauncherActivity.NUMBER_BOOKS_ON_SHELF, BookSortBy.TITLE);
                    }

                    EBookLauncherActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (EditPreferences.useFilenames(EBookLauncherActivity.this)) {
                                EBookLauncherActivity.this.listAdapterCollectns1 = new SimpleCursorAdapter(
                                        EBookLauncherActivity.this, R.layout.book_grid_item_filename,
                                        EBookLauncherActivity.this.cursrCollcnts1, DeviceFactory
                                                .getBookColumnsToMap(true), BooksActivity.bookGridColsMapToFilename);
                            } else {
                                int layoutId = R.layout.book_grid_item;
                                if (android.os.Build.MANUFACTURER
                                        .equalsIgnoreCase(DeviceFactory.DEVICE_MANUFACTURER_BARNES_AND_NOBLE)) {
                                    layoutId = R.layout.book_grid_item_nook;
                                }
                                EBookLauncherActivity.this.listAdapterCollectns1 = new SimpleCursorAdapter(
                                        EBookLauncherActivity.this, layoutId,
                                        EBookLauncherActivity.this.cursrCollcnts1, DeviceFactory
                                                .getBookColumnsToMap(false), BooksActivity.bookGridColsMapTo);
                            }
                            EBookLauncherActivity.this.setupList(EBookLauncherActivity.this.listviewCollectns1,
                                    EBookLauncherActivity.this.listAdapterCollectns1,
                                    EBookLauncherActivity.this.cursrCollcnts1);

                        }
                    });
                } catch (final Exception e) {
                    Log.e(EBookLauncherApplication.LOG_TAG + EBookLauncherActivity.LOG_TAG, "Error Pop List", e);
                }
            }
        }.start();
    }

    private void updateCollectionlist2() {
        new Thread() {
            @Override
            public void run() {
                this.setName("updateCollectionlist2()");
                try {
                    final String clctname = EditPreferences.getCollectionName2(EBookLauncherActivity.this
                            .getApplication());

                    if (clctname.equals(EBookLauncherActivity.this.getResources().getString(
                            R.string.pref_collection_recently_added))) {
                        EBookLauncherActivity.this.cursrCollcnts2 = EBookLauncherActivity.this.myApp.dataMdl
                                .getRecentlyAdded(EBookLauncherActivity.this);
                        final int nbrItems = EBookLauncherActivity.this.cursrCollcnts2.getCount();
                        if (EBookLauncherActivity.this.collection2Start >= nbrItems) {
                            EBookLauncherActivity.this.collection2Start = nbrItems - 1;
                        }
                        final String tmpStr;
                        if ((EBookLauncherActivity.this.collection2Start + 1) < nbrItems) {
                            tmpStr = String
                                    .format(EBookLauncherActivity.this.getResources().getString(
                                            R.string.page1_nbr_item_format_1),
                                            EBookLauncherActivity.this.collection2Start + 1,
                                            (EBookLauncherActivity.this.collection2Start + EBookLauncherActivity.NUMBER_BOOKS_ON_SHELF) > nbrItems ? nbrItems
                                                    : EBookLauncherActivity.this.collection2Start + EBookLauncherActivity.NUMBER_BOOKS_ON_SHELF,
                                            nbrItems);
                        } else {
                            tmpStr = String.format(
                                    EBookLauncherActivity.this.getResources().getString(
                                            R.string.page1_nbr_item_format_2),
                                    EBookLauncherActivity.this.collection1Start + 1, nbrItems);
                        }
                        EBookLauncherActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                EBookLauncherActivity.this.lblCollection2.setText(clctname + " " + tmpStr);
                            }
                        });
                        EBookLauncherActivity.this.cursrCollcnts2.close();
                        EBookLauncherActivity.this.cursrCollcnts2 = EBookLauncherActivity.this.myApp.dataMdl
                                .getRecentlyAdded(EBookLauncherActivity.this,
                                        EBookLauncherActivity.this.collection2Start,
                                        EBookLauncherActivity.NUMBER_BOOKS_ON_SHELF);

                    } else if (clctname.equals(EBookLauncherActivity.this.getResources().getString(
                            R.string.pref_collection_periodicals))) {
                        EBookLauncherActivity.this.cursrCollcnts2 = EBookLauncherActivity.this.myApp.dataMdl
                                .getPeriodicals(EBookLauncherActivity.this);
                        final int nbrItems = EBookLauncherActivity.this.cursrCollcnts2.getCount();
                        if (EBookLauncherActivity.this.collection2Start >= nbrItems) {
                            EBookLauncherActivity.this.collection2Start = nbrItems - 1;
                        }
                        final String tmpStr;
                        if ((EBookLauncherActivity.this.collection2Start + 1) < nbrItems) {
                            tmpStr = String
                                    .format(EBookLauncherActivity.this.getResources().getString(
                                            R.string.page1_nbr_item_format_1),
                                            EBookLauncherActivity.this.collection2Start + 1,
                                            (EBookLauncherActivity.this.collection2Start + EBookLauncherActivity.NUMBER_BOOKS_ON_SHELF) > nbrItems ? nbrItems
                                                    : EBookLauncherActivity.this.collection2Start + EBookLauncherActivity.NUMBER_BOOKS_ON_SHELF,
                                            nbrItems);
                        } else {
                            tmpStr = String.format(
                                    EBookLauncherActivity.this.getResources().getString(
                                            R.string.page1_nbr_item_format_2),
                                    EBookLauncherActivity.this.collection2Start + 1, nbrItems);
                        }
                        EBookLauncherActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                EBookLauncherActivity.this.lblCollection2.setText(clctname + " " + tmpStr);
                            }
                        });
                        EBookLauncherActivity.this.cursrCollcnts2.close();
                        EBookLauncherActivity.this.cursrCollcnts2 = EBookLauncherActivity.this.myApp.dataMdl
                                .getPeriodicals(EBookLauncherActivity.this,
                                        EBookLauncherActivity.this.collection2Start,
                                        EBookLauncherActivity.NUMBER_BOOKS_ON_SHELF);

                    } else {
                        EBookLauncherActivity.this.cursrCollcnts2 = EBookLauncherActivity.this.myApp.dataMdl
                                .getBooksInCollection(clctname, BookSortBy.TITLE);
                        final int nbrItems = EBookLauncherActivity.this.cursrCollcnts2.getCount();
                        if (EBookLauncherActivity.this.collection2Start >= nbrItems) {
                            EBookLauncherActivity.this.collection2Start = nbrItems - 1;
                        }
                        final String tmpStr;
                        if ((EBookLauncherActivity.this.collection2Start + 1) < nbrItems) {
                            tmpStr = String
                                    .format(EBookLauncherActivity.this.getResources().getString(
                                            R.string.page1_nbr_item_format_1),
                                            EBookLauncherActivity.this.collection2Start + 1,
                                            (EBookLauncherActivity.this.collection2Start + EBookLauncherActivity.NUMBER_BOOKS_ON_SHELF) > nbrItems ? nbrItems
                                                    : EBookLauncherActivity.this.collection2Start + EBookLauncherActivity.NUMBER_BOOKS_ON_SHELF,
                                            nbrItems);
                        } else {
                            tmpStr = String.format(
                                    EBookLauncherActivity.this.getResources().getString(
                                            R.string.page1_nbr_item_format_2),
                                    EBookLauncherActivity.this.collection2Start + 1, nbrItems);
                        }
                        EBookLauncherActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                EBookLauncherActivity.this.lblCollection2.setText(clctname + " " + tmpStr);
                            }
                        });
                        EBookLauncherActivity.this.cursrCollcnts2.close();
                        EBookLauncherActivity.this.cursrCollcnts2 = EBookLauncherActivity.this.myApp.dataMdl
                                .getBooksInCollection(clctname, EBookLauncherActivity.this.collection2Start,
                                        EBookLauncherActivity.NUMBER_BOOKS_ON_SHELF, BookSortBy.TITLE);
                    }

                    EBookLauncherActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (EditPreferences.useFilenames(EBookLauncherActivity.this)) {
                                EBookLauncherActivity.this.listAdapterCollectns2 = new SimpleCursorAdapter(
                                        EBookLauncherActivity.this, R.layout.book_grid_item_filename,
                                        EBookLauncherActivity.this.cursrCollcnts2, DeviceFactory
                                                .getBookColumnsToMap(true), BooksActivity.bookGridColsMapToFilename);
                            } else {
                                int layoutId = R.layout.book_grid_item;
                                if (android.os.Build.MANUFACTURER
                                        .equalsIgnoreCase(DeviceFactory.DEVICE_MANUFACTURER_BARNES_AND_NOBLE)) {
                                    layoutId = R.layout.book_grid_item_nook;
                                }
                                EBookLauncherActivity.this.listAdapterCollectns2 = new SimpleCursorAdapter(
                                        EBookLauncherActivity.this, layoutId,
                                        EBookLauncherActivity.this.cursrCollcnts2, DeviceFactory
                                                .getBookColumnsToMap(false), BooksActivity.bookGridColsMapTo);

                            }
                            EBookLauncherActivity.this.setupList(EBookLauncherActivity.this.listviewCollectns2,
                                    EBookLauncherActivity.this.listAdapterCollectns2,
                                    EBookLauncherActivity.this.cursrCollcnts2);
                        }
                    });
                } catch (final Exception e) {
                    Log.e(EBookLauncherApplication.LOG_TAG, EBookLauncherActivity.LOG_TAG + "Exception ", e);
                }
            }
        }.start();
    }

    /**
     * this method populates the "Currently Reading" list.
     */
    private void updateCurrentlyReading() {
        new Thread() {
            @Override
            public void run() {
                this.setName("updateCurrentlyReading()");
                try {
                    EBookLauncherActivity.this.cursrCurrentRead = EBookLauncherActivity.this.myApp.dataMdl
                            .getCurrentlyReading(EBookLauncherActivity.this.getApplication());
                    final int nbrItems = EBookLauncherActivity.this.cursrCurrentRead.getCount();
                    if (EBookLauncherActivity.this.currentlyReadingStart >= nbrItems) {
                        EBookLauncherActivity.this.currentlyReadingStart = nbrItems - 1;
                    }
                    final String tmpStr;
                    if ((EBookLauncherActivity.this.currentlyReadingStart + 1) < nbrItems) {
                        tmpStr = String
                                .format(EBookLauncherActivity.this.getResources().getString(
                                        R.string.page1_nbr_item_format_1),
                                        EBookLauncherActivity.this.currentlyReadingStart + 1,
                                        (EBookLauncherActivity.this.currentlyReadingStart + EBookLauncherActivity.NUMBER_BOOKS_ON_SHELF) > nbrItems ? nbrItems
                                                : EBookLauncherActivity.this.currentlyReadingStart + EBookLauncherActivity.NUMBER_BOOKS_ON_SHELF,
                                        nbrItems);
                    } else {
                        tmpStr = String.format(
                                EBookLauncherActivity.this.getResources().getString(R.string.page1_nbr_item_format_2),
                                EBookLauncherActivity.this.currentlyReadingStart + 1, nbrItems);
                    }
                    EBookLauncherActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            EBookLauncherActivity.this.lblCurrentRead.setText(EBookLauncherActivity.this.getResources()
                                    .getString(R.string.page1_currently_reading_lbl) + " " + tmpStr);
                        }
                    });
                    EBookLauncherActivity.this.cursrCurrentRead.close();
                    EBookLauncherActivity.this.cursrCurrentRead = EBookLauncherActivity.this.myApp.dataMdl
                            .getCurrentlyReading(EBookLauncherActivity.this.getApplication(),
                                    EBookLauncherActivity.this.currentlyReadingStart,
                                    EBookLauncherActivity.NUMBER_BOOKS_ON_SHELF, null);

                    EBookLauncherActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (EditPreferences.useFilenames(EBookLauncherActivity.this)) {
                                EBookLauncherActivity.this.listAdapterCurrentRead = new SimpleCursorAdapter(
                                        EBookLauncherActivity.this, R.layout.book_grid_item_filename,
                                        EBookLauncherActivity.this.cursrCurrentRead, DeviceFactory
                                                .getBookColumnsToMap(true), BooksActivity.bookGridColsMapToFilename);
                            } else {
                                EBookLauncherActivity.this.listAdapterCurrentRead = new SimpleCursorAdapter(
                                        EBookLauncherActivity.this, DeviceFactory.getBookGridItemLayout(),
                                        EBookLauncherActivity.this.cursrCurrentRead, DeviceFactory
                                                .getBookColumnsToMap(false), BooksActivity.bookGridColsMapTo);

                            }
                            EBookLauncherActivity.this.setupList(EBookLauncherActivity.this.listviewCurrentRead,
                                    EBookLauncherActivity.this.listAdapterCurrentRead,
                                    EBookLauncherActivity.this.cursrCurrentRead);
                        }
                    });
                } catch (final Exception e) {
                    Log.e(EBookLauncherApplication.LOG_TAG, EBookLauncherActivity.LOG_TAG + "Exception ", e);
                }
            }
        }.start();
    }

    private void updateCurrentTime() {
        this.lblClock.setText(DateFormat.format("kk:mm:ss", new Date()));
    }
}