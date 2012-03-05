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

import uk.co.droidinactu.ebooklauncher.EBookLauncherApplication;
import uk.co.droidinactu.ebooklauncher.R;
import uk.co.droidinactu.ebooklauncher.contenttable.Contents;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public final class HandwritingActivity extends AbstractListActivity implements OnItemClickListener, OnClickListener {

    public static final String LOG_TAG = "HandwritingActivity";

    private GridView bkGridView;
    private ImageButton btnSortOrdr;
    private String filterChar = "";

    private LinearLayout indexBtns;
    private EBookLauncherApplication myApp;

    private final String PREF_FILTER_PREFIX = "PREF_FILTER_";
    private final String PREF_SORT_BY = "PREF_SORT_BY";
    private HandwritingSortBy sortBy = HandwritingSortBy.TITLE;
    private TextView txtSortOrdr;

    private TextView txtViewTitle;

    @Override
    public void onClick(final View v) {

        if (v.getTag() == null) {
            final String btnPressed = ((Button) v).getText().toString();
            this.filterChar = btnPressed;

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

        this.btnSortOrdr = (ImageButton) this.findViewById(R.id.periodical_grid_btn_sort);
        this.btnSortOrdr.setOnClickListener(this);

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

        this.bkGridView = (GridView) this.findViewById(R.id.periodical_grid_books);
        this.bkGridView.requestFocus();
        this.bkGridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {

        this.showNotImplementedDialog();
        return;

        // final Intent localIntent = new Intent();
        // localIntent.setAction("android.intent.action.sony.notepad.svg.edit");
        // // localIntent.setDataAndType(Uri.parse("file://" +
        // // this.notes[id].filename), "application/x-sony-notepad-svg");
        // this.startActivity(localIntent);
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
        if (tmpSortOrdr == HandwritingSortBy.AUTHOR.ordinal()) {
            this.sortBy = HandwritingSortBy.AUTHOR;
        } else {
            this.sortBy = HandwritingSortBy.TITLE;
        }

        this.update();
    }

    private void update() {
        final Uri getIncdntsUri = Uri.withAppendedPath(Contents.Notepad.Handwriting.CONTENT_URI, "");
        final ContentResolver cr = this.getContentResolver();
        final Cursor memosCursor = cr.query(getIncdntsUri, null, null, null, null);
        memosCursor.moveToFirst();

        final int nbrMemos = memosCursor.getCount();

        final String s = "";
    }
}
