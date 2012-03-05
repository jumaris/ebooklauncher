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

import uk.co.droidinactu.common.view.EInkGridView;
import uk.co.droidinactu.ebooklauncher.EBookLauncherApplication;
import uk.co.droidinactu.ebooklauncher.R;
import uk.co.droidinactu.ebooklauncher.data.DeviceFactory;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public final class PeriodicalsActivity extends AbstractListActivity implements OnItemClickListener, OnClickListener {

    private static final int[] bookColsMapTo = new int[] { R.id.periodical_grid_item_img,
            R.id.periodical_grid_item_title, };

    public static final String LOG_TAG = "PeriodicalsActivity";

    private static final int NUMBER_PERIODICALS_SHOWN = 16;

    private EInkGridView bkGridView;
    private int booksStart = 0;
    private ImageButton btnSearch;
    private ImageButton btnSortOrdr;
    private Cursor cursr = null;
    private String filterChar = "";

    private LinearLayout indexBtns;
    private EBookLauncherApplication myApp;

    private final String PREF_FILTER_PREFIX = "PREF_FILTER_";
    private final String PREF_SORT_BY = "PREF_SORT_BY";
    private PeriodicalSortBy sortBy = PeriodicalSortBy.TITLE;
    private TextView txtSortOrdr;

    private TextView txtViewTitle;

    @Override
    public void onClick(final View v) {

        if (v.getTag() == null) {
            final String btnPressed = ((Button) v).getText().toString();
            this.filterChar = btnPressed;

        } else if (v.getTag().equals("next_collection_page")) {
            this.booksStart = this.booksStart + PeriodicalsActivity.NUMBER_PERIODICALS_SHOWN;
            this.update();
        } else if (v.getTag().equals("prev_collection_page")) {
            this.booksStart = this.booksStart - PeriodicalsActivity.NUMBER_PERIODICALS_SHOWN;
            if (this.booksStart < 0) {
                this.booksStart = 0;
            }

        } else if (v.getTag().equals("periodical_grid_btn_grid")) {
            this.showNotImplementedDialog();

        } else if (v.getTag().equals("periodical_grid_btn_sort")) {
            if (this.sortBy == PeriodicalSortBy.TITLE) {
                this.sortBy = PeriodicalSortBy.AUTHOR;
            } else {
                this.sortBy = PeriodicalSortBy.TITLE;
            }

        } else if (v.getTag().equals("periodical_grid_btn_search")) {
            this.showNotImplementedDialog();

        } else if (v.getTag().equals("periodical_grid_btn_close")) {
            this.finish();

        }
        this.update();
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.periodical_grid);

        this.myApp = (EBookLauncherApplication) this.getApplication();

        this.txtViewTitle = (TextView) this.findViewById(R.id.periodical_grid_txt_books);
        this.txtSortOrdr = (TextView) this.findViewById(R.id.periodical_grid_txt_sort_order);
        this.indexBtns = (LinearLayout) this.findViewById(R.id.periodical_grid_btn_index);

        this.btnSearch = (ImageButton) this.findViewById(R.id.periodical_grid_btn_search);
        this.btnSearch.setOnClickListener(this);

        this.btnSortOrdr = (ImageButton) this.findViewById(R.id.periodical_grid_btn_sort);
        this.btnSortOrdr.setOnClickListener(this);

        ImageButton btnClose = (ImageButton) this.findViewById(R.id.periodical_grid_btn_close);
        btnClose.setOnClickListener(this);

        this.indexBtns.removeAllViews();
        Button btn = new Button(this);
        btn.setOnClickListener(this);
        btn.setText("0");
        this.indexBtns.addView(btn);

        for (char ch = 'A'; ch <= 'Z'; ch++) {
            btn = new Button(this);
            btn.setOnClickListener(this);
            btn.setText("" + ch);
            this.indexBtns.addView(btn);
        }

        this.bkGridView = (EInkGridView) this.findViewById(R.id.periodical_grid_books);
        this.bkGridView.requestFocus();
        this.bkGridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        this.myApp.dataMdl.launchBook(this, id);
    }

    @Override
    protected void onPause() {
        super.onPause();
        final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor ed = mPrefs.edit();
        ed.putInt(this.PREF_SORT_BY, this.sortBy.ordinal());
        ed.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        final int tmpSortOrdr = mPrefs.getInt(this.PREF_SORT_BY, BookSortBy.TITLE.ordinal());
        if (tmpSortOrdr == PeriodicalSortBy.AUTHOR.ordinal()) {
            this.sortBy = PeriodicalSortBy.AUTHOR;
        } else {
            this.sortBy = PeriodicalSortBy.TITLE;
        }

        this.update();
    }

    private void update() {
        try {
            if (this.cursr != null) {
                try {
                    this.cursr.close();
                } catch (final Exception e) {
                }
            }

            this.cursr = this.myApp.dataMdl.getPeriodicals(this);
            this.txtViewTitle.setText(this.getResources().getString(R.string.periodical_grid_title));

            if (this.cursr == null) { return; }
            this.startManagingCursor(this.cursr);
            final SimpleCursorAdapter tmpAdaptor = new SimpleCursorAdapter(this, R.layout.periodical_grid_item,
                    this.cursr, DeviceFactory.getPeriodicalColumnsToMap(), PeriodicalsActivity.bookColsMapTo);

            tmpAdaptor.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(final View view, final Cursor cursor, final int columnIndex) {
                    boolean retval = false;
                    final int idColIndex = cursor.getColumnIndexOrThrow(BaseColumns._ID);
                    final int thumbColIndex = cursor.getColumnIndexOrThrow(DeviceFactory.getCoverImgColumnName());

                    if (columnIndex == idColIndex) {
                        try {
                            final ImageView coverImg = (ImageView) view;
                            final String thumbnailFilename = cursor.getString(thumbColIndex);
                            final Bitmap bitmap = PeriodicalsActivity.this.myApp.dataMdl.getBookCoverImg(
                                    PeriodicalsActivity.this, thumbnailFilename);
                            coverImg.setImageDrawable(new BitmapDrawable(bitmap));
                            retval = true;
                        } catch (final Exception e) {
                            Log.e(PeriodicalsActivity.LOG_TAG, "exception populating list", e);
                        }
                        retval = true;
                    }
                    return retval;
                }
            });
            this.bkGridView.setAdapter(tmpAdaptor);
            tmpAdaptor.notifyDataSetChanged();
        } catch (final Exception e) {
            Log.e(PeriodicalsActivity.LOG_TAG, "exception populating list", e);
            Toast.makeText(this, "Exception Caught creating book grid", Toast.LENGTH_SHORT).show();
        }
    }
}
