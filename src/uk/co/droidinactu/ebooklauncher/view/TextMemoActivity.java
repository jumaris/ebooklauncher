/*
 * Copyright 2012 Andy Aspell-Clark
 * 
 * This file is part of eBookLauncher.
 * 
 * eBookLauncher is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * eBookLauncher is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with eBookLauncher. If
 * not, see http://www.gnu.org/licenses/.
 */
package uk.co.droidinactu.ebooklauncher.view;

import java.io.File;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import uk.co.droidinactu.common.file.AsciiFileReader;
import uk.co.droidinactu.common.view.EInkListView;
import uk.co.droidinactu.ebooklauncher.EBookLauncherApplication;
import uk.co.droidinactu.ebooklauncher.R;
import uk.co.droidinactu.ebooklauncher.contenttable.ContentColumns;
import uk.co.droidinactu.ebooklauncher.contenttable.Contents;
import uk.co.droidinactu.ebooklauncher.contenttable.NotepadColumns;
import uk.co.droidinactu.ebooklauncher.data.DrawingNote;
import uk.co.droidinactu.ebooklauncher.data.Notepad;
import uk.co.droidinactu.ebooklauncher.data.TextNote;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
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

public final class TextMemoActivity extends AbstractListActivity implements OnItemClickListener, OnClickListener {

	private static final int[] colsMapTo = new int[] { R.id.text_note_item_img, R.id.text_note_item_title,
	        R.id.text_note_item_date };
	private static final String[] columns = new String[] { BaseColumns._ID, NotepadColumns.TITLE,
	        NotepadColumns.CREATED_DATE };

	public static final String LOG_TAG = "TextMemoActivity";
	public static final String HANDWRITING = "handwriting";
	public static final String HANDWRITING_OR_TEXT = "handwriting_or_text";
	public static final String TEXT = "text";

	private ImageButton btnSortOrdr;
	private String filterChar = "";
	private LinearLayout indexBtns;
	private EInkListView listView;
	private Cursor memosCursor;
	private EBookLauncherApplication myApp;

	private final String PREF_FILTER_PREFIX = "PREF_FILTER_";
	private final String PREF_SORT_BY = "PREF_SORT_BY";

	private TextMemoSortBy sortBy = TextMemoSortBy.TITLE;

	private TextView txtSortOrdr;
	private TextView txtViewTitle;
	private String handwritingOrText = TEXT;

	@Override
	public void onClick(final View v) {

		if (v.getTag() == null) {
			final String btnPressed = ((Button) v).getText().toString();
			this.filterChar = btnPressed;

		} else if (v.getTag().equals("periodical_grid_btn_sort")) {
			if (this.sortBy == TextMemoSortBy.TITLE) {
				this.sortBy = TextMemoSortBy.AUTHOR;
			} else {
				this.sortBy = TextMemoSortBy.TITLE;
			}

		}
		this.update();
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.text_note_list);

		this.myApp = (EBookLauncherApplication) this.getApplication();

		final Intent intent = this.getIntent();
		final Bundle extras = intent.getExtras();
		if (extras != null) {
			this.handwritingOrText = extras.getString(HANDWRITING_OR_TEXT);
		}

		this.txtViewTitle = (TextView) this.findViewById(R.id.text_note_grid_txt_title);
		this.txtSortOrdr = (TextView) this.findViewById(R.id.text_note_grid_txt_sort_order);
		this.indexBtns = (LinearLayout) this.findViewById(R.id.text_note_grid_btn_index);

		this.btnSortOrdr = (ImageButton) this.findViewById(R.id.text_note_grid_btn_sort);
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

		this.listView = (EInkListView) this.findViewById(R.id.text_note_list);
		this.listView.requestFocus();
		this.listView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {

		this.showNotImplementedDialog();

		this.memosCursor.moveToPosition(position);
		if (this.handwritingOrText.equals(TEXT)) {
			final TextNote tn = new TextNote(this.memosCursor);
			final AsciiFileReader afr = new AsciiFileReader();
			afr.setFilename("/mnt/sdcard/" + tn.fullFilename);
			afr.openFile();
			String line = afr.firstLineFromFile();
			while (line != null && line.length() > 0) {
				line = afr.nextLineFromFile();
			}

			new Intent();
		} else if (this.handwritingOrText.equals(HANDWRITING)) {
			new DrawingNote(this.memosCursor);

		}
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
		if (tmpSortOrdr == TextMemoSortBy.AUTHOR.ordinal()) {
			this.sortBy = TextMemoSortBy.AUTHOR;
		} else {
			this.sortBy = TextMemoSortBy.TITLE;
		}

		this.update();
	}

	private void update() {
		Uri getIncdntsUri = Uri.withAppendedPath(Contents.Notepad.TextMemo.CONTENT_URI, "");
		if (this.handwritingOrText.equals(HANDWRITING)) {
			getIncdntsUri = Uri.withAppendedPath(Contents.Notepad.Handwriting.CONTENT_URI, "");
		}

		final ContentResolver cr = this.getContentResolver();
		this.memosCursor = cr.query(getIncdntsUri, null, null, null, null);
		this.memosCursor.moveToFirst();

		final SimpleCursorAdapter tmpAdaptor = new SimpleCursorAdapter(this, R.layout.text_note_list_item,
		        this.memosCursor, TextMemoActivity.columns, TextMemoActivity.colsMapTo);
		this.listView.setAdapter(tmpAdaptor);
		tmpAdaptor.notifyDataSetChanged();

		tmpAdaptor.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
			@Override
			public boolean setViewValue(final View view, final Cursor cursor, final int columnIndex) {
				boolean retval = false;
				final int idColIndex = cursor.getColumnIndexOrThrow(BaseColumns._ID);
				final int filepathColIndex = cursor.getColumnIndexOrThrow(ContentColumns.FILE_PATH);
				cursor.getColumnIndexOrThrow(ContentColumns.FILE_NAME);

				if (columnIndex == idColIndex) {
					try {
						final Serializer serializer = new Persister();
						final File source = new File("/mnt/sdcard/" + cursor.getString(filepathColIndex));
						Notepad note = serializer.read(Notepad.class, source);

						final ImageView coverImg = (ImageView) view;
						if (note.type.equals("drawing")) {
							note = serializer.read(DrawingNote.class, source);
							// coverImg.setImageDrawable(new BitmapDrawable(bitmap));
							coverImg.setVisibility(View.VISIBLE);
						} else {
							note = serializer.read(TextNote.class, source);
							coverImg.setVisibility(View.INVISIBLE);
						}
						retval = true;
					} catch (final Exception e) {
						Log.e(BooksActivity.LOG_TAG, "exception populating grid", e);
					}
					retval = true;
				}
				return retval;
			}
		});
	}
}
