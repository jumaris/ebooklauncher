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

import uk.co.droidinactu.common.ImageFunctions;
import uk.co.droidinactu.ebooklauncher.EBookLauncherApplication;
import uk.co.droidinactu.ebooklauncher.R;
import uk.co.droidinactu.ebooklauncher.data.Book;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class BookDetailsActivity extends Activity {

	private final static int newHeight = 120;
	private final static int newWidth = 80;
	public static final String SELECTED_BOOK_ID = "SELECTED_BOOK_ID";

	private TextView bookAuthor;
	private TextView bookISBN;
	private TextView bookPublisher;
	private TextView bookSeries;
	private TextView bookTitle;
	private TextView bookFilename;
	private TextView bookTags;
	private TextView bookSummary;
	private ImageView coverImg;
	private EBookLauncherApplication myApp;
	private long bookId;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.book_details);

		final Intent intent = this.getIntent();
		final Bundle extras = intent.getExtras();
		if (extras != null) {
			this.bookId = extras.getLong(BookDetailsActivity.SELECTED_BOOK_ID);
		}

		this.coverImg = (ImageView) this.findViewById(R.id.BookDetailsCoverImg);
		this.bookTitle = (TextView) this.findViewById(R.id.BookDetailsTitle);
		this.bookSeries = (TextView) this.findViewById(R.id.BookDetailsSeries);
		this.bookAuthor = (TextView) this.findViewById(R.id.BookDetailsAuthor);
		this.bookPublisher = (TextView) this.findViewById(R.id.BookDetailsPublisher);
		this.bookISBN = (TextView) this.findViewById(R.id.BookDetailsISBN);
		this.bookTags = (TextView) this.findViewById(R.id.BookDetailsTags);
		this.bookSummary = (TextView) this.findViewById(R.id.BookDetailsSummary);
		this.bookFilename = (TextView) this.findViewById(R.id.BookDetailsFilename);

		this.myApp = (EBookLauncherApplication) this.getApplication();
		final Book bk = this.myApp.dataMdl.getBook(this, this.bookId);

		final Bitmap bmp = this.myApp.dataMdl.getBookCoverImg(this, bk.file_name);
		this.coverImg.setImageDrawable(new BitmapDrawable(ImageFunctions.scaleImage(bmp, BookDetailsActivity.newWidth,
		        BookDetailsActivity.newHeight)));

		this.bookTitle.setText(bk.title, TextView.BufferType.SPANNABLE);
		this.bookFilename.setText(bk.file_path, TextView.BufferType.SPANNABLE);
		this.bookAuthor.setText(bk.author.replace("||", " & "), TextView.BufferType.SPANNABLE);
		this.bookSummary.setText(bk.description);

	}

}
