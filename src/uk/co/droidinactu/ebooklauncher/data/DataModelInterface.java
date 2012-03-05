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
package uk.co.droidinactu.ebooklauncher.data;

import java.util.ArrayList;
import java.util.List;

import uk.co.droidinactu.ebooklauncher.view.BookSortBy;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;

/**
 * This class defines the methods that a data model must publish.
 * 
 * @author andy
 */
public interface DataModelInterface {
	
	void closeDb();

	Application getApplicationNamed(final Context cntxt, final String appName);

	ArrayList<Application> getApplications(final Context cntxt,
			final int start, final int numberAppsOnShelf,
			final List<String> selectedAppNames);

	ArrayList<Application> getApplications(final Context cntxt,
			final List<String> selectedAppNames);

	Cursor getApplicationsCursor(final Context cntxt, final int start,
			final int numberAppsOnShelf, final List<String> selectedAppNames);

	Book getBook(final Context cntxt, final long id);

	Bitmap getBookCoverImg(final Context cntxt, final String thumbnailFilename);

	Cursor getBooks(final Context cntxt, final String filterStr,
			final BookSortBy sortBy, final String searchString);

	Cursor getBooksInCollection(final Collection clctn, final BookSortBy sortBy);

	Cursor getBooksInCollection(final String collectionName,
			final BookSortBy sortBy);

	Cursor getBooksInCollection(final String collectionName, final int start,
			final int end, final BookSortBy sortBy);

	Collection getCollection(final long id);

	Cursor getCollection(final String collectionName);

	List<String> getCollectionNames(final Context cntxt);

	Cursor getCollections(final int start, final int nbrItems,
			final String filterChar);

	Cursor getCurrentlyReading(final Context cntxt);

	Cursor getCurrentlyReading(final Context cntxt, final int start,
			final int nbrItems, final BookSortBy sortBy);

	String getDbFilename();

	Cursor getPeriodicals(final Context cntxt);

	Cursor getPeriodicals(final Context cntxt, final int start,
			final int nbrItems);

	Cursor getPictures(final Context cntxt);

	Cursor getRecentlyAdded(final Context cntxt);

	Cursor getRecentlyAdded(final Context cntxt, final int start,
			final int nbrItems);

	boolean isOpened();

	void launchBook(final Context cntxt, final long id);

	void openDb();
}
