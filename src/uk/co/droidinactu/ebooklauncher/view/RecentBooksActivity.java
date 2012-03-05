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

import uk.co.droidinactu.common.view.EInkGridView;
import uk.co.droidinactu.ebooklauncher.EBookLauncherApplication;
import uk.co.droidinactu.ebooklauncher.EditPreferences;
import uk.co.droidinactu.ebooklauncher.R;
import uk.co.droidinactu.ebooklauncher.data.Book;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public final class RecentBooksActivity extends AbstractListActivity implements OnItemClickListener, OnClickListener {

    public static final int[] bookColsMapTo = new int[] { R.id.book_grid_item_img,
            R.id.book_grid_item_title,
            R.id.book_grid_item_author };

    public static final int[] bookColsMapToFilename = new int[] { R.id.book_grid_item_img, R.id.book_grid_item_filename };

    public static final String[] bookColumns = new String[] { BaseColumns._ID,
            Book.COLUMN_NAME_TITLE,
            Book.COLUMN_NAME_AUTHOR };

    public static final String[] bookColumnsFilename = new String[] { BaseColumns._ID, Book.COLUMN_NAME_FILE_NAME };

    public static final String LOG_TAG = "RecentBooksActivity";

    private EInkGridView bkGridView;
    private ImageButton btnSortOrdr;
    private Cursor cursr = null;
    private TextView lblClock;
    private EBookLauncherApplication myApp;
    private final String PREF_SORT_BY = "PREF_SORT_BY";
    private BookSortBy sortBy = BookSortBy.TITLE;
    private TextView txtSortOrdr;
    private TextView txtViewTitle;
    private ImageButton btnPrevPage;

    private String[] getBookColumns() {
        String[] columns = null;
        columns = RecentBooksActivity.bookColumns;
        if (this.sortBy == BookSortBy.FILENAME) {
            columns[1] = Book.COLUMN_NAME_FILE_NAME;
        } else {
            columns[1] = Book.COLUMN_NAME_TITLE;
        }
        return columns;
    }

    private int[] getBookColumnsMapTo() {
        int[] mapTo = null;
        mapTo = RecentBooksActivity.bookColsMapTo;
        return mapTo;
    }

    @Override
    public void onClick(final View v) {

        if (v.getTag().equals("recent_book_grid_btn_sort")) {
            if (this.sortBy == BookSortBy.TITLE) {
                this.sortBy = BookSortBy.AUTHOR;
            } else if (this.sortBy == BookSortBy.AUTHOR) {
                this.sortBy = BookSortBy.FILENAME;
            } else {
                this.sortBy = BookSortBy.TITLE;
            }
        } else if (v.getTag().equals("prevpage")) {
            this.finish();

        } else if (v.getTag().equals("recent_book_grid_btn_close")) {
            this.finish();
        }
        this.update();
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.recent_book_grid);

        this.myApp = (EBookLauncherApplication) this.getApplication();

        this.lblClock = (TextView) this.findViewById(R.id.recent_book_text_clock);

        this.txtViewTitle = (TextView) this.findViewById(R.id.recent_book_grid_txt_books);
        this.txtViewTitle.setOnClickListener(this);

        this.txtSortOrdr = (TextView) this.findViewById(R.id.recent_book_grid_txt_sort_order);

        this.btnSortOrdr = (ImageButton) this.findViewById(R.id.recent_book_grid_btn_sort);
        this.btnSortOrdr.setOnClickListener(this);

        this.bkGridView = (EInkGridView) this.findViewById(R.id.recent_book_grid_books);
        this.bkGridView.requestFocus();
        this.bkGridView.setOnItemClickListener(this);

        this.btnPrevPage = (ImageButton) this.findViewById(R.id.recent_book_grid_btn_prevpage);
        this.btnPrevPage.setOnClickListener(this);
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        this.myApp.dataMdl.launchBook(this, id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        Log.d(EBookLauncherApplication.LOG_TAG, RecentBooksActivity.LOG_TAG + "onKeyDown() called ");
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
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        final int tmpSortOrdr = mPrefs.getInt(this.PREF_SORT_BY, BookSortBy.TITLE.ordinal());
        if (tmpSortOrdr == BookSortBy.FILENAME.ordinal()) {
            this.sortBy = BookSortBy.FILENAME;
        } else if (tmpSortOrdr == BookSortBy.AUTHOR.ordinal()) {
            this.sortBy = BookSortBy.AUTHOR;
        } else {
            this.sortBy = BookSortBy.TITLE;
        }

        this.update();
        this.updateCurrentTime();
    }

    private void update() {
        try {
            String sortOrderLblText;
            switch (this.sortBy) {
            case FILENAME:
                sortOrderLblText = this.getResources().getString(R.string.book_grid_sorted_filename);
                break;
            case AUTHOR:
                sortOrderLblText = this.getResources().getString(R.string.book_grid_sorted_author);
                break;
            default:
                sortOrderLblText = this.getResources().getString(R.string.book_grid_sorted_title);
                break;
            }
            this.txtSortOrdr.setText(sortOrderLblText);

            if (this.cursr != null) {
                try {
                    this.cursr.close();
                } catch (final Exception e) {
                }
            }

            this.cursr = this.myApp.dataMdl.getCurrentlyReading(this, 0, 9999, this.sortBy);
            if (this.cursr == null) {
                this.txtViewTitle.setText(String.format(this.getResources().getString(R.string.recent_book_grid_title),
                        0));
                return;
            } else {
                this.txtViewTitle.setText(String.format(this.getResources().getString(R.string.recent_book_grid_title),
                        this.cursr.getCount()));
            }

            this.startManagingCursor(this.cursr);
            SimpleCursorAdapter tmpAdaptor = null;

            if (EditPreferences.useFilenames(this)) {
                tmpAdaptor = new SimpleCursorAdapter(this, R.layout.book_grid_item_filename, this.cursr,
                        RecentBooksActivity.bookColumnsFilename, RecentBooksActivity.bookColsMapToFilename);
            } else {
                tmpAdaptor = new SimpleCursorAdapter(this, R.layout.book_grid_item, this.cursr, this.getBookColumns(),
                        this.getBookColumnsMapTo());
            }

            tmpAdaptor.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(final View view, final Cursor cursor, final int columnIndex) {
                    boolean retval = false;
                    final int idColIndex = cursor.getColumnIndexOrThrow(BaseColumns._ID);
                    final int filenameColIndex = cursor.getColumnIndexOrThrow(Book.COLUMN_NAME_FILE_NAME);
                    final int thumbColIndex = cursor.getColumnIndexOrThrow(Book.COLUMN_NAME_THUMBNAIL);

                    if (columnIndex == idColIndex) {
                        try {
                            final ImageView coverImg = (ImageView) view;
                            final String thumbnailFilename = cursor.getString(thumbColIndex);
                            final Bitmap bitmap = RecentBooksActivity.this.myApp.dataMdl.getBookCoverImg(
                                    RecentBooksActivity.this, thumbnailFilename);
                            coverImg.setImageDrawable(new BitmapDrawable(bitmap));
                            retval = true;
                        } catch (final Exception e) {
                            Log.e(RecentBooksActivity.LOG_TAG, "exception populating list", e);
                        }
                        retval = true;
                    } else if ((columnIndex == filenameColIndex) && (RecentBooksActivity.this.sortBy == BookSortBy.FILENAME)) {
                        String filename = cursor.getString(filenameColIndex);
                        final TextView filenameView = (TextView) view;
                        if (filename.endsWith(".epub") || filename.endsWith(".mobi")) {
                            filename = filename.substring(0, filename.length() - 5);
                        } else if (filename.endsWith(".pdf")) {
                            filename = filename.substring(0, filename.length() - 4);
                        }
                        filenameView.setText(filename);
                        retval = true;
                    }
                    return retval;
                }
            });
            this.bkGridView.setAdapter(tmpAdaptor);
            tmpAdaptor.notifyDataSetChanged();
        } catch (final Exception e) {
            Log.e(RecentBooksActivity.LOG_TAG, "exception populating list", e);
            Toast.makeText(this, "Exception Caught creating book grid", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCurrentTime() {
        this.lblClock.setText(DateFormat.format("kk:mm:ss", new Date()));
    }
}
