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
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public final class TextMemoEditorActivity extends AbstractActivity implements OnClickListener {

    public static final String LOG_TAG = "TextMemoEditorActivity";
    private Button btnNew;
    private Button btnSave;
    private final String filterChar = "";
    private EditText memoContent;

    private EBookLauncherApplication myApp;
    private final String PREF_FILTER_PREFIX = "PREF_FILTER_";

    private final String PREF_SORT_BY = "PREF_SORT_BY";
    private TextView txtViewTitle;

    @Override
    public void onClick(final View v) {

        if (v.getTag().equals("periodical_grid_btn_sort")) {

        }
        this.update();
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.text_note_list);

        this.myApp = (EBookLauncherApplication) this.getApplication();

        this.txtViewTitle = (TextView) this.findViewById(R.id.text_note_grid_txt_title);

        this.btnSave = (Button) this.findViewById(R.id.text_note_editor_btn_save);
        this.btnSave.setOnClickListener(this);

        this.btnNew = (Button) this.findViewById(R.id.text_note_editor_btn_new);
        this.btnNew.setOnClickListener(this);

        this.memoContent = (EditText) this.findViewById(R.id.text_note_list);
    }

    @Override
    protected void onPause() {
        super.onPause();
        final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor ed = mPrefs.edit();
        // ed.putInt(this.PREF_SORT_BY, this.sortBy.ordinal());
        ed.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        this.update();
    }

    private void update() {
        final Uri getIncdntsUri = Uri.withAppendedPath(Contents.Notepad.TextMemo.CONTENT_URI, "");
        final ContentResolver cr = this.getContentResolver();

        final String s = "";
    }
}
