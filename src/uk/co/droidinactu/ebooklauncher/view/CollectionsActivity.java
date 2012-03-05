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

import uk.co.droidinactu.common.view.EInkListView;
import uk.co.droidinactu.ebooklauncher.EBookLauncherApplication;
import uk.co.droidinactu.ebooklauncher.R;
import uk.co.droidinactu.ebooklauncher.data.Collection;
import uk.co.droidinactu.ebooklauncher.data.DeviceFactory;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public final class CollectionsActivity extends AbstractListActivity implements OnItemClickListener, OnClickListener {

	private static final int[] collctnColsMapTo = new int[] { R.id.collection_list_item_title,
			R.id.collection_list_item_count };

	public static final String LOG_TAG = "CollectionsActivity";
	private static final int NUMBER_COLLECTIONS_SHOWN = 10;
	private int collectionsStart = 0;
	private Cursor cursr;
	private String filterChar = null;
	private LinearLayout indexBtns;
	private EInkListView listView;
	private EBookLauncherApplication myApp;
	private final String PREF_SORT_BY = "PREF_SORT_BY";
	private CollectionSortBy sortBy = CollectionSortBy.TITLE;
	private TextView txtSortOrdr;
	private TextView txtViewTitle;
	private ImageButton btnClose;

	@Override
	public void onClick(final View v) {

		if (v.getTag() == null) {
			final String btnPressed = ((Button) v).getText().toString();
			filterChar = btnPressed;

		} else if (v.getTag().equals("collection_list_txt_books")) {
			filterChar = null;

		} else if (v.getTag().equals("next_collection_page")) {
			collectionsStart = collectionsStart + CollectionsActivity.NUMBER_COLLECTIONS_SHOWN;

		} else if (v.getTag().equals("prev_collection_page")) {
			collectionsStart = collectionsStart - CollectionsActivity.NUMBER_COLLECTIONS_SHOWN;
			if (collectionsStart < 0) {
				collectionsStart = 0;
			}

		} else if (v.getTag().equals("collection_list_btn_sort")) {
			if (sortBy == CollectionSortBy.TITLE) {
				sortBy = CollectionSortBy.NBR_BOOKS;
			} else {
				sortBy = CollectionSortBy.TITLE;
			}
			// this.updateList();
			showNotImplementedDialog();

		} else if (v.getTag().equals("collection_list_btn_search")) {
			showNotImplementedDialog();

		} else if (v.getTag().equals("collection_list_btn_close")) {
			finish();

		}
		update();
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.collection_list);

		myApp = (EBookLauncherApplication) getApplication();

		listView = (EInkListView) findViewById(R.id.collection_list);
		listView.setOnItemClickListener(this);

		txtViewTitle = (TextView) findViewById(R.id.collection_list_txt_books);
		txtViewTitle.setOnClickListener(this);

		txtSortOrdr = (TextView) findViewById(R.id.collection_list_txt_sort_order);
		indexBtns = (LinearLayout) findViewById(R.id.collection_list_btn_index);

		btnClose = (ImageButton) findViewById(R.id.collection_list_btn_close);
		if (DeviceFactory.isNook()) {
			btnClose.setOnClickListener(this);
		} else {
			btnClose.setVisibility(View.INVISIBLE);
		}

		indexBtns.removeAllViews();
		Button btn = new Button(this);
		btn.setOnClickListener(this);
		btn.setText("0");
		indexBtns.addView(btn);

		final int idxBtnWidth = 50;
		final int idxBtnHeight = 63;
		final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) btn.getLayoutParams();
		params.width = idxBtnWidth;
		params.height = idxBtnHeight;
		params.setMargins(0, 0, 0, 0); // substitute parameters for left, top,
										// right, bottom
		btn.setLayoutParams(params);

		for (char ch = 'A'; ch <= 'Z'; ch++) {
			btn = new Button(this);
			btn.setOnClickListener(this);
			btn.setText("" + ch);
			btn.setLayoutParams(params);
			indexBtns.addView(btn);
		}

	}

	@Override
	public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
		final Intent incdntDetailsIntnt = new Intent(this, BooksActivity.class);
		incdntDetailsIntnt.putExtra(BooksActivity.SELECTED_COLLECTION_ID, id);
		startActivity(incdntDetailsIntnt);
	}

	@Override
	protected void onPause() {
		super.onPause();
		final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		final SharedPreferences.Editor ed = mPrefs.edit();
		ed.putInt(PREF_SORT_BY, sortBy.ordinal());
		ed.commit();
	}

	@Override
	protected void onResume() {
		super.onResume();
		final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		final int tmpSortOrdr = mPrefs.getInt(PREF_SORT_BY, CollectionSortBy.TITLE.ordinal());
		if (tmpSortOrdr == CollectionSortBy.NBR_BOOKS.ordinal()) {
			sortBy = CollectionSortBy.NBR_BOOKS;
		} else {
			sortBy = CollectionSortBy.TITLE;
		}
		update();
	}

	private void update() {
		try {
			cursr = myApp.dataMdl.getCollections(collectionsStart, CollectionsActivity.NUMBER_COLLECTIONS_SHOWN,
					filterChar);
			if (filterChar != null && filterChar.length() > 0) {
				txtViewTitle.setText(String.format(getResources().getString(R.string.collection_list_title_filtered),
						cursr.getCount(), filterChar));
			} else {
				txtViewTitle.setText(String.format(getResources().getString(R.string.collection_list_title),
						cursr.getCount()));
			}

			startManagingCursor(cursr);

			final SimpleCursorAdapter tmpAdaptor = new SimpleCursorAdapter(this, R.layout.collection_list_item, cursr,
					DeviceFactory.getCollectionColumnsToMap(), CollectionsActivity.collctnColsMapTo);

			if (!DeviceFactory.isNook()) {
				tmpAdaptor.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
					@Override
					public boolean setViewValue(final View view, final Cursor cursor, final int columnIndex) {
						boolean retval = false;
						if (columnIndex == cursor.getColumnIndexOrThrow(Collection.COLUMN_NAME_UUID)) {
							try {
								final int titleColIndex = cursor.getColumnIndexOrThrow(Collection.COLUMN_NAME_TITLE);
								final TextView txtbookCount = (TextView) view;
								final String collcnName = cursor.getString(titleColIndex);
								final Cursor crsr = myApp.dataMdl.getBooksInCollection(collcnName, BookSortBy.TITLE);
								final int bookCount = crsr.getCount();
								crsr.close();
								txtbookCount.setText("" + bookCount);
								retval = true;
							} catch (final Exception e) {
							}
							retval = true;
						}
						return retval;
					}
				});
			}
			listView.setAdapter(tmpAdaptor);
			tmpAdaptor.notifyDataSetChanged();
		} catch (final Exception e) {
			Log.e(CollectionsActivity.LOG_TAG, "exception populating list", e);
		}
	}
}
