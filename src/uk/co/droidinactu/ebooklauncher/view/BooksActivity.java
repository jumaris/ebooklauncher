/*<p>
 * Copyright 2012 Andy Aspell-Clark
 *</p><p>
 * This file is part of eBookLauncher.
 * </p><p>
 * eBookLauncher is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * </p><p>
 * eBookLauncher is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 * </p><p>
 * You should have received a copy of the GNU General Public License along
 * with eBookLauncher. If not, see http://www.gnu.org/licenses/.
 *</p>
 */
package uk.co.droidinactu.ebooklauncher.view;

import java.util.ArrayList;
import java.util.List;

import uk.co.droidinactu.common.view.EInkGridView;
import uk.co.droidinactu.common.view.EInkListView;
import uk.co.droidinactu.ebooklauncher.EBookLauncherActivity;
import uk.co.droidinactu.ebooklauncher.EBookLauncherApplication;
import uk.co.droidinactu.ebooklauncher.EditPreferences;
import uk.co.droidinactu.ebooklauncher.R;
import uk.co.droidinactu.ebooklauncher.data.Book;
import uk.co.droidinactu.ebooklauncher.data.DeviceFactory;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public final class BooksActivity extends AbstractListActivity implements OnItemClickListener, OnClickListener {

    public static final int[] bookGridColsMapTo = new int[] { R.id.book_grid_item_img,
            R.id.book_grid_item_title,
            R.id.book_grid_item_author };

    public static final int[] bookGridColsMapToFilename = new int[] { R.id.book_grid_item_img,
            R.id.book_grid_item_filename };

    public static final int[] bookListColsMapTo = new int[] { R.id.book_list_item_img,
            R.id.book_list_item_title,
            R.id.book_list_item_author };

    public static final int[] bookListColsMapToFilename = new int[] { R.id.book_grid_item_img,
            R.id.book_list_item_filename };

    public static final String LOG_TAG = "BooksActivity";

    public static String SELECTED_COLLECTION_ID = "SELECTED_COLLECTION_ID";

    private static final String WILDCARD_STRING = "*";
    private EInkGridView bkGridView;
    private EInkListView bkListView;
    private SimpleCursorAdapter booksGridCursorAdaptor = null;
    private SimpleCursorAdapter booksListCursorAdaptor = null;
    private ImageButton btnFilter;
    private ImageButton btnGridList;
    private ImageButton btnSearch;
    private ImageButton btnSortOrdr;
    private final List<String> clctnFilter = new ArrayList<String>();
    private long colctnId = -1;
    private Cursor mBooksCursr = null;
    private String mColctnName = "";
    private String mFilterChar = "";
    private LinearLayout mIndexBtns;

    private String mSearchString = "";
    private BookSortBy mSortBy = BookSortBy.TITLE;
    private String mSortOrderLblText = "";
    private EBookLauncherApplication myApp;

    private final String PREF_COLLECTION_NAME = "PREF_COLLECTION_NAME";
    private final String PREF_FILTER_PREFIX = "PREF_FILTER_";
    private final String PREF_SEARCH_STRING = "PREF_SEARCH_STRING";
    private final String PREF_SORT_BY = "PREF_SORT_BY";
    private boolean showList = false;// show grid by default
    private TextView txtSortOrdr;

    private TextView txtViewTitle;

    private ImageButton btnClose;

    @Override
    public void onClick(final View v) {

        if (v.getTag() == null) {
            final String btnPressed = ((Button) v).getText().toString();
            this.mFilterChar = btnPressed;

        } else if (v.getTag().equals("book_grid_txt_books")) {
            this.mFilterChar = null;

        } else if (v.getTag().equals("book_grid_btn_filter")) {
            this.showFilterDialog();

        } else if (v.getTag().equals("book_grid_btn_grid")) {
            this.showList = !this.showList;
            if (this.showList) {
                this.btnGridList.setImageDrawable(this.getResources().getDrawable(R.drawable.btn_grid));
            } else {
                this.btnGridList.setImageDrawable(this.getResources().getDrawable(R.drawable.btn_list));
            }

        } else if (v.getTag().equals("book_grid_btn_sort")) {
            if (this.mSortBy == BookSortBy.TITLE) {
                this.mSortBy = BookSortBy.AUTHOR;
            } else if (this.mSortBy == BookSortBy.AUTHOR) {
                this.mSortBy = BookSortBy.FILENAME;
            } else {
                this.mSortBy = BookSortBy.TITLE;
            }

        } else if (v.getTag().equals("book_grid_btn_search")) {
            this.showSearchDialog();

        } else if (v.getTag().equals("book_grid_btn_close")) {
            this.finish();

        }
        this.update();
    }

    @Override
    public boolean onContextItemSelected(final MenuItem aItem) {

        final AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) aItem.getMenuInfo();

        // To get the id of the clicked item in the list use menuInfo.id
        Log.e(EBookLauncherApplication.LOG_TAG, BooksActivity.LOG_TAG + "list pos:"
                + menuInfo.position
                + " id:"
                + menuInfo.id);
        final Book bk = this.myApp.dataMdl.getBook(this, menuInfo.id);

        switch (aItem.getItemId()) {

        case EBookLauncherActivity.CONTEXTMENU_VIEW_DETAILS:
            Log.d(EBookLauncherApplication.LOG_TAG + BooksActivity.LOG_TAG, "Show details of book: " + bk.toString());
            final Intent bkDetlsIntnt = new Intent(this, BookDetailsActivity.class);
            bkDetlsIntnt.putExtra(BookDetailsActivity.SELECTED_BOOK_ID, menuInfo.id);
            this.startActivity(bkDetlsIntnt);
            return true;

        case EBookLauncherActivity.CONTEXTMENU_OPEN_BOOK:
            this.myApp.dataMdl.launchBook(this, menuInfo.id);
            return true;

        case EBookLauncherActivity.CONTEXTMENU_DELETE_BOOK:
            Log.d(EBookLauncherApplication.LOG_TAG + BooksActivity.LOG_TAG, "Delete book: " + bk.toString());
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
        this.setContentView(R.layout.book_grid);

        this.myApp = (EBookLauncherApplication) this.getApplication();

        final Intent intent = this.getIntent();
        final Bundle extras = intent.getExtras();
        if (extras != null) {
            this.colctnId = extras.getLong(BooksActivity.SELECTED_COLLECTION_ID);
        }

        this.txtViewTitle = (TextView) this.findViewById(R.id.book_grid_txt_books);
        this.txtViewTitle.setOnClickListener(this);

        this.txtSortOrdr = (TextView) this.findViewById(R.id.book_grid_txt_sort_order);
        this.mIndexBtns = (LinearLayout) this.findViewById(R.id.book_grid_btn_index);

        this.btnGridList = (ImageButton) this.findViewById(R.id.book_grid_btn_grid);
        this.btnGridList.setOnClickListener(this);

        this.btnSearch = (ImageButton) this.findViewById(R.id.book_grid_btn_search);
        this.btnSearch.setOnClickListener(this);

        this.btnSortOrdr = (ImageButton) this.findViewById(R.id.book_grid_btn_sort);
        this.btnSortOrdr.setOnClickListener(this);

        this.btnFilter = (ImageButton) this.findViewById(R.id.book_grid_btn_filter);
        this.btnFilter.setOnClickListener(this);

        this.btnClose = (ImageButton) this.findViewById(R.id.book_grid_btn_close);
        if (DeviceFactory.isNook()) {
            this.btnClose.setOnClickListener(this);
        } else {
            this.btnClose.setVisibility(View.INVISIBLE);
        }

        this.mIndexBtns.removeAllViews();
        Button btn = new Button(this);
        btn.setOnClickListener(this);
        btn.setText("0");
        this.mIndexBtns.addView(btn);

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
            this.mIndexBtns.addView(btn);
        }

        this.bkGridView = (EInkGridView) this.findViewById(R.id.book_grid_books);
        this.bkGridView.setOnItemClickListener(this);
        this.bkGridView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(final ContextMenu conMenu, final View v, final ContextMenuInfo menuInfo) {
                final LinearLayout ll = (LinearLayout) ((EInkGridView) v)
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

        this.bkListView = (EInkListView) this.findViewById(R.id.book_grid_books_list);
        this.bkListView.setOnItemClickListener(this);
        this.bkListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(final ContextMenu conMenu, final View v, final ContextMenuInfo menuInfo) {
                final LinearLayout ll = (LinearLayout) ((EInkListView) v)
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
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(0, EBookLauncherActivity.MENU_CLEAR_FILTERS, 0, R.string.menu_clear_filters).setIcon(
                R.drawable.ic_menu_clear_filters);

        return true;
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        this.myApp.dataMdl.launchBook(this, id);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final boolean retvalue = false;
        switch (item.getItemId()) {
        case EBookLauncherActivity.MENU_CLEAR_FILTERS:
            this.mColctnName = "";
            this.mFilterChar = "";
            this.mSearchString = "";
            this.update();
            break;

        default:
            return super.onOptionsItemSelected(item);
        }
        return retvalue;
    }

    @Override
    protected void onPause() {
        super.onPause();
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor ed = prefs.edit();
        ed.putInt(this.PREF_SORT_BY, this.mSortBy.ordinal());
        ed.putString(this.PREF_COLLECTION_NAME, this.mColctnName);
        ed.putString(this.PREF_SEARCH_STRING, this.mSearchString);
        for (int x = 0; x < this.clctnFilter.size(); x++) {
            ed.putString(this.PREF_FILTER_PREFIX + x, this.clctnFilter.get(x));
        }
        ed.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.mSearchString = prefs.getString(this.PREF_SEARCH_STRING, "");

        int x = 0;
        String filterStr = prefs.getString(this.PREF_FILTER_PREFIX + x, "");
        while (filterStr.length() > 0) {
            this.clctnFilter.add(filterStr);
            x++;
            filterStr = prefs.getString(this.PREF_FILTER_PREFIX + x, "");
        }

        final int tmpSortOrdr = prefs.getInt(this.PREF_SORT_BY, BookSortBy.TITLE.ordinal());
        if (tmpSortOrdr == BookSortBy.FILENAME.ordinal()) {
            this.mSortBy = BookSortBy.FILENAME;
        } else if (tmpSortOrdr == BookSortBy.AUTHOR.ordinal()) {
            this.mSortBy = BookSortBy.AUTHOR;
        } else {
            this.mSortBy = BookSortBy.TITLE;
        }

        if (this.colctnId > -1) {
            this.mColctnName = this.myApp.dataMdl.getCollection(this.colctnId).title;
        }
        this.update();
    }

    private void showFilterDialog() {

        final List<String> clctnNames = this.myApp.dataMdl.getCollectionNames(this);
        final int nbrClctns = clctnNames.size();
        final boolean[] states = new boolean[nbrClctns];
        final CharSequence[] items = new CharSequence[nbrClctns];
        for (int x = 0; x < nbrClctns; x++) {
            items[x] = clctnNames.get(x);
            if (this.clctnFilter.contains(clctnNames.get(x))) {
                states[x] = true;
            } else {
                states[x] = false;
            }
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.book_grid_filter_dialog_title);
        builder.setPositiveButton(R.string.book_grid_filter_dialog_btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                // BooksActivity.this.update();
                BooksActivity.this.showNotImplementedDialog();
            }
        });
        builder.setMultiChoiceItems(items, states, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, final int item, final boolean state) {
                if (state) {
                    BooksActivity.this.clctnFilter.add(items[item].toString());
                } else {
                    BooksActivity.this.clctnFilter.remove(items[item].toString());
                }
            }
        });
        builder.show();
    }

    private void showSearchDialog() {
        final EditText textEntryView = new EditText(this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.book_grid_filter_dialog_title).setView(textEntryView)
                .setPositiveButton(R.string.book_grid_search_dialog_btn_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int whichButton) {
                        BooksActivity.this.mSearchString = textEntryView.getText().toString();
                        BooksActivity.this.update();
                    }
                })
                .setNegativeButton(R.string.book_grid_search_dialog_btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        // Do nothing.
                    }
                });
        builder.show();
    }

    private void update() {
        try {
            switch (this.mSortBy) {
            case FILENAME:
                this.mSortOrderLblText = this.getResources().getString(R.string.book_grid_sorted_filename);
                break;
            case AUTHOR:
                this.mSortOrderLblText = this.getResources().getString(R.string.book_grid_sorted_author);
                break;
            default:
                this.mSortOrderLblText = this.getResources().getString(R.string.book_grid_sorted_title);
                break;
            }

            if (this.mBooksCursr != null) {
                try {
                    this.mBooksCursr.close();
                } catch (final Exception e) {
                }
            }

            if (this.mFilterChar.length() > 0) {
                this.mBooksCursr = this.myApp.dataMdl
                        .getBooks(this, this.mFilterChar, this.mSortBy, this.mSearchString);
                if (this.mSearchString.length() == 0) {
                    this.txtViewTitle.setText(String.format(
                            this.getResources().getString(R.string.book_grid_title_filtered),
                            this.mBooksCursr.getCount(), this.mFilterChar + BooksActivity.WILDCARD_STRING));
                } else {
                    this.txtViewTitle.setText(String.format(
                            this.getResources().getString(R.string.book_grid_title_filtered),
                            this.mBooksCursr.getCount(), this.mFilterChar + BooksActivity.WILDCARD_STRING
                                    + this.mSearchString
                                    + BooksActivity.WILDCARD_STRING));
                }

            } else if (this.mColctnName.length() > 0) {
                this.mBooksCursr = this.myApp.dataMdl.getBooksInCollection(this.mColctnName, this.mSortBy);
                this.txtViewTitle.setText(String.format(
                        this.getResources().getString(R.string.book_grid_title_collection),
                        this.mBooksCursr.getCount(), this.mColctnName));

            } else {
                this.mBooksCursr = this.myApp.dataMdl
                        .getBooks(this, this.mFilterChar, this.mSortBy, this.mSearchString);
                if (this.mSearchString.length() == 0) {
                    int bookCount = 0;
                    try {
                        this.mBooksCursr.moveToFirst();
                        bookCount = this.mBooksCursr.getCount();
                    } catch (NullPointerException npe) {
                    }
                    this.txtViewTitle.setText(String.format(this.getResources().getString(R.string.book_grid_title),
                            bookCount));
                } else {
                    this.txtViewTitle.setText(String.format(
                            this.getResources().getString(R.string.book_grid_title_filtered),
                            this.mBooksCursr.getCount(), BooksActivity.WILDCARD_STRING + this.mSearchString
                                    + BooksActivity.WILDCARD_STRING));
                }
            }
            if (this.mBooksCursr == null) { return; }
            this.startManagingCursor(this.mBooksCursr);
            this.txtSortOrdr.setText(this.mSortOrderLblText);

            if (EditPreferences.useFilenames(this)) {
                this.booksListCursorAdaptor = new SimpleCursorAdapter(this, R.layout.book_list_item_filename,
                        this.mBooksCursr, DeviceFactory.getBookColumnsToMap(true),
                        BooksActivity.bookListColsMapToFilename);

                this.booksGridCursorAdaptor = new SimpleCursorAdapter(this, R.layout.book_grid_item_filename,
                        this.mBooksCursr, DeviceFactory.getBookColumnsToMap(true),
                        BooksActivity.bookGridColsMapToFilename);
            } else {
                this.booksListCursorAdaptor = new SimpleCursorAdapter(this, R.layout.book_list_item, this.mBooksCursr,
                        DeviceFactory.getBookColumnsToMap(false), BooksActivity.bookListColsMapTo);

                this.booksGridCursorAdaptor = new SimpleCursorAdapter(this, DeviceFactory.getBookGridItemLayout(),
                        this.mBooksCursr, DeviceFactory.getBookColumnsToMap(false), BooksActivity.bookGridColsMapTo);

            }

            this.booksListCursorAdaptor.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(final View view, final Cursor cursor, final int columnIndex) {
                    boolean retval = false;
                    final int idColIndex = cursor.getColumnIndexOrThrow(BaseColumns._ID);
                    final int thumbColIndex = cursor.getColumnIndexOrThrow(DeviceFactory.getCoverImgColumnName());
                    final int filenameColIndex = cursor.getColumnIndexOrThrow(DeviceFactory.getFilenameColumnName());

                    if (columnIndex == idColIndex) {
                        try {
                            final ImageView coverImg = (ImageView) view;
                            final String thumbnailFilename = cursor.getString(thumbColIndex);
                            final Bitmap bitmap = BooksActivity.this.myApp.dataMdl.getBookCoverImg(BooksActivity.this,
                                    thumbnailFilename);
                            coverImg.setImageDrawable(new BitmapDrawable(bitmap));
                            retval = true;
                        } catch (final Exception e) {
                            Log.e(BooksActivity.LOG_TAG, "exception populating list", e);
                        }
                        retval = true;
                    } else if ((columnIndex == filenameColIndex) && (BooksActivity.this.mSortBy == BookSortBy.FILENAME)) {
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

            this.booksGridCursorAdaptor.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(final View view, final Cursor cursor, final int columnIndex) {
                    boolean retval = false;
                    try {
                        final int idColIndex = cursor.getColumnIndexOrThrow(BaseColumns._ID);
                        final int thumbColIndex = cursor.getColumnIndexOrThrow(DeviceFactory.getCoverImgColumnName());
                        final int filenameColIndex = cursor.getColumnIndexOrThrow(DeviceFactory.getFilenameColumnName());

                        if (columnIndex == idColIndex) {
                            try {
                                final ImageView coverImg = (ImageView) view;
                                final String thumbnailFilename = cursor.getString(thumbColIndex);
                                final Bitmap bitmap = BooksActivity.this.myApp.dataMdl.getBookCoverImg(
                                        BooksActivity.this, thumbnailFilename);
                                coverImg.setImageDrawable(new BitmapDrawable(bitmap));
                                retval = true;
                            } catch (final Exception e) {
                                Log.e(BooksActivity.LOG_TAG, "exception populating grid", e);
                            }
                            retval = true;
                        } else if ((columnIndex == filenameColIndex) && (BooksActivity.this.mSortBy == BookSortBy.FILENAME)) {
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
                    } catch (final Exception e) {
                    }
                    return retval;
                }
            });
            if (this.showList) {
                this.bkGridView.setVisibility(View.INVISIBLE);
                this.bkListView.setVisibility(View.VISIBLE);
                this.bkListView.setAdapter(this.booksListCursorAdaptor);
                this.booksListCursorAdaptor.notifyDataSetChanged();
            } else {
                this.bkGridView.setVisibility(View.VISIBLE);
                this.bkListView.setVisibility(View.INVISIBLE);
                this.bkGridView.setAdapter(this.booksGridCursorAdaptor);
                this.booksGridCursorAdaptor.notifyDataSetChanged();
            }

        } catch (final Exception e) {
            Log.e(BooksActivity.LOG_TAG, "exception populating list", e);
            Toast.makeText(this, "Exception Caught creating book grid", Toast.LENGTH_SHORT).show();
        }
    }
}
