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
package uk.co.droidinactu.ebooklauncher.view;

import java.util.Date;

import uk.co.droidinactu.ebooklauncher.EBookLauncherActivity;
import uk.co.droidinactu.ebooklauncher.EBookLauncherApplication;
import uk.co.droidinactu.ebooklauncher.EditPreferences;
import uk.co.droidinactu.ebooklauncher.R;
import uk.co.droidinactu.ebooklauncher.data.Book;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class CoverFlowActivity extends Activity implements OnClickListener {

    private static final String LOG_TAG = "CoverFlowActivity::";
    private static final int NUMBER_BOOKS_TO_SHOW = 3;
    private static final String PREF_SELECTED_COLLECTION = "PREF_SELECTED_COLLECTION";
    private Cursor booksCursor = null;
    private int bookStart = 0;
    private ImageView btnNextBook;
    private ImageButton btnNextPage;
    private ImageView btnPrevBook;
    private TextView lblClock;

    private EBookLauncherApplication myApp;
    private final String selectedCollection = "";

    @Override
    public void onClick(final View v) {
        if (v.getTag().equals("next_book")) {
            this.bookStart = this.bookStart + 1;
            this.updateBookList();
        } else if (v.getTag().equals("prev_book")) {
            this.bookStart = this.bookStart - 1;
            if (this.bookStart < -1) {
                this.bookStart = -1;
            }
            this.updateBookList();

        } else if (v.getTag().equals("nextpage")) {
            this.finish();
        }
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.coverflow_layout);

        this.myApp = (EBookLauncherApplication) this.getApplication();

        this.btnNextPage = (ImageButton) this.findViewById(R.id.cvrflw_btn_nextpage);
        this.btnNextPage.setOnClickListener(this);

        this.btnNextBook = (ImageView) this.findViewById(R.id.cvrflw_btn_next_book);
        this.btnNextBook.setOnClickListener(this);

        this.btnPrevBook = (ImageView) this.findViewById(R.id.cvrflw_btn_prev_book);
        this.btnPrevBook.setOnClickListener(this);

        this.lblClock = (TextView) this.findViewById(R.id.cvrflw_text_clock);

        this.myApp = (EBookLauncherApplication) this.getApplication();
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
        menu.add(0, EBookLauncherActivity.MENU_CHOOSE_COLLECTION, 0, R.string.menu_choose_collection).setIcon(
                R.drawable.ic_menu_choose_collection);

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        Log.d(EBookLauncherApplication.LOG_TAG, CoverFlowActivity.LOG_TAG + "onKeyDown() called ");
        boolean retValue = false;
        switch (keyCode) {
        case 19: {
            // left arrow pressed
            break;
        }
        case 20: {
            // right arrow pressed
            this.finish();
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

        case EBookLauncherActivity.MENU_CHOOSE_COLLECTION:

            retvalue = true;
            break;

        default:
            return super.onOptionsItemSelected(item);
        }
        return retvalue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();
        final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor ed = mPrefs.edit();
        ed.putString(CoverFlowActivity.PREF_SELECTED_COLLECTION, this.selectedCollection);
        ed.commit();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();

        this.updateBookList();
        this.updateCurrentTime();
    }

    private void updateBookList() {

        final int tmpStartIdx = this.bookStart == -1 ? 0 : this.bookStart;
        final int tmpNbrBooks = this.bookStart == -1 ? CoverFlowActivity.NUMBER_BOOKS_TO_SHOW - 1
                : CoverFlowActivity.NUMBER_BOOKS_TO_SHOW;

        if ((this.selectedCollection == null) || (this.selectedCollection.length() == 0)) {
            this.booksCursor = this.myApp.dataMdl.getCurrentlyReading(this, tmpStartIdx, tmpNbrBooks, null);
        } else {
            this.booksCursor = this.myApp.dataMdl.getBooksInCollection(this.selectedCollection, tmpStartIdx,
                    tmpNbrBooks, null);
        }

        if (this.booksCursor == null) { return; }

        Book leftBook = null;
        Book middleBook = null;
        Book rightBook = null;

        try {
            this.booksCursor.moveToFirst();
            leftBook = new Book(this.booksCursor);
        } catch (final Exception e) {
            leftBook = null;
        }

        try {
            this.booksCursor.moveToNext();
            middleBook = new Book(this.booksCursor);
        } catch (final Exception e) {
            middleBook = null;
        }

        try {
            this.booksCursor.moveToNext();
            rightBook = new Book(this.booksCursor);
        } catch (final Exception e) {
            rightBook = null;
        }

        if ((leftBook != null) && (middleBook == null) && (rightBook == null)) {
            if (this.bookStart == -1) {
                middleBook = leftBook;
                leftBook = null;
            }
        }
        if ((leftBook != null) && (middleBook != null) && (rightBook == null)) {
            if (this.bookStart < 0) {
                rightBook = middleBook;
                middleBook = leftBook;
                leftBook = null;
            }
        }

        final Bitmap defaultCoverImg = BitmapFactory.decodeResource(this.getResources(), R.drawable.blank_book_cover);
        Bitmap bitmap = null;

        final ImageView leftBookCover = (ImageView) this.findViewById(R.id.cvrflw_book_grid_item_left_img);
        final TextView leftBookTitle = (TextView) this.findViewById(R.id.cvrflw_book_grid_item_left_title);
        if (leftBook != null) {
            bitmap = this.myApp.dataMdl.getBookCoverImg(this, leftBook.thumbnail);
            leftBookCover.setImageDrawable(new BitmapDrawable(bitmap));
            leftBookCover.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View paramView) {
                    CoverFlowActivity.this.bookStart = CoverFlowActivity.this.bookStart - 1;
                    CoverFlowActivity.this.updateBookList();
                }
            });
            leftBookTitle.setText(leftBook.title);
        } else {
            leftBookTitle.setText("");
            leftBookCover.setImageBitmap(defaultCoverImg);
        }

        final ImageView rightBookCover = (ImageView) this.findViewById(R.id.cvrflw_book_grid_item_right_img);
        final TextView rightBookTitle = (TextView) this.findViewById(R.id.cvrflw_book_grid_item_right_title);
        if (rightBook != null) {
            bitmap = this.myApp.dataMdl.getBookCoverImg(this, rightBook.thumbnail);
            rightBookCover.setImageDrawable(new BitmapDrawable(bitmap));
            rightBookCover.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View paramView) {
                    CoverFlowActivity.this.bookStart = CoverFlowActivity.this.bookStart + 1;
                    CoverFlowActivity.this.updateBookList();
                }
            });
            rightBookTitle.setText(rightBook.title);
        } else {
            rightBookTitle.setText("");
            rightBookCover.setImageBitmap(defaultCoverImg);
        }

        final Book selectedBook = middleBook;
        final ImageView middleBookCover = (ImageView) this.findViewById(R.id.cvrflw_book_grid_item_middle_img);
        bitmap = this.myApp.dataMdl.getBookCoverImg(this, middleBook.thumbnail);
        middleBookCover.setImageDrawable(new BitmapDrawable(bitmap));
        middleBookCover.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View paramView) {
                CoverFlowActivity.this.myApp.dataMdl.launchBook(CoverFlowActivity.this,
                        selectedBook.getUniqueIdentifier());
            }
        });

        final TextView middleBookTitle = (TextView) this.findViewById(R.id.cvrflw_BookDetailsTitle);
        middleBookTitle.setText(middleBook.title);
        final TextView middleBookSeries = (TextView) this.findViewById(R.id.cvrflw_BookDetailsSeries);
        middleBookSeries.setText("");
        final TextView middleBookAuthor = (TextView) this.findViewById(R.id.cvrflw_BookDetailsAuthor);
        middleBookAuthor.setText(middleBook.author);
        final TextView middleBookFilename = (TextView) this.findViewById(R.id.cvrflw_BookDetailsPublisher);
        middleBookFilename.setText(middleBook.file_name);
        final TextView middleBookTags = (TextView) this.findViewById(R.id.cvrflw_BookDetailsTags);
        middleBookTags.setText("");
        final TextView middleBookIsbn = (TextView) this.findViewById(R.id.cvrflw_BookDetailsISBN);
        middleBookIsbn.setText("");
        final TextView middleBookDescription = (TextView) this.findViewById(R.id.cvrflw_BookDetailsSummary);
        middleBookDescription.setText(middleBook.description);
    }

    private void updateCurrentTime() {
        this.lblClock.setText(DateFormat.format("kk:mm:ss", new Date()));
    }
}
