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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.co.droidinactu.ebooklauncher.EditPreferences;
import uk.co.droidinactu.ebooklauncher.view.BookSortBy;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

/**
 * 
 * @author andy aspell-clark
 * 
 */
public abstract class AbstractDataModel implements DataModelInterface {

	protected Context cntxt = null;
	public boolean displayFilename = false;

	public AbstractDataModel(final Context ctx) {
		this.cntxt = ctx;
	}

	@Override
	public void closeDb() {
	}

	@Override
	public void openDb() {
	}

	/**
	 * returns the Application data object for the named application.
	 */
	@Override
	public final Application getApplicationNamed(final Context cntxt,
			final String appName) {
		final ArrayList<Application> appsListAll = this.getApplications(cntxt,
				null);

		for (int x = 0; x < (appsListAll.size() - 1); x++) {
			if (appsListAll.get(x).title.equals(appName)) {
				return appsListAll.get(x);
			}
		}
		return null;
	}

	@Override
	public final ArrayList<Application> getApplications(final Context cntxt,
			final int start, final int numberAppsOnShelf,
			final List<String> selectedAppNames) {
		final ArrayList<Application> appsListAll = this.getApplications(cntxt,
				selectedAppNames);
		final ArrayList<Application> appsList = new ArrayList<Application>();

		final int nbr = Math.min(numberAppsOnShelf, appsListAll.size());
		for (int x = start; x < (start + nbr); x++) {
			try {
				appsList.add(appsListAll.get(x));
			} catch (final Exception e) {
				break;
			}
		}
		return appsList;
	}

	@Override
	public final ArrayList<Application> getApplications(final Context cntxt,
			final List<String> selectedAppNames) {

		final ArrayList<Application> appsList = new ArrayList<Application>();
		final PackageManager manager = cntxt.getPackageManager();
		final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		final List<ResolveInfo> apps = manager.queryIntentActivities(
				mainIntent, 0);
		Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));

		if (apps != null) {
			final int count = apps.size();

			for (int i = 0; i < count; i++) {
				final Application application = new Application();
				final ResolveInfo info = apps.get(i);

				application.title = info.loadLabel(manager);
				application.setActivity(new ComponentName(
						info.activityInfo.applicationInfo.packageName,
						info.activityInfo.name), Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				application.icon = info.activityInfo.loadIcon(manager);

				if ((selectedAppNames == null)
						|| (selectedAppNames.size() == 0)) {
					appsList.add(application);
				} else if (selectedAppNames.contains(application.title)) {
					appsList.add(application);
				}
			}
		}
		return appsList;
	}

	@Override
	public final Cursor getApplicationsCursor(final Context cntxt,
			final int start, final int numberAppsOnShelf,
			final List<String> selectedAppNames) {
		final ArrayList<Application> apps = this.getApplications(cntxt, start,
				numberAppsOnShelf, selectedAppNames);
		final MatrixCursor mc = new MatrixCursor(
				Application.applicationCursorCols);

		for (int x = 0; x < apps.size(); x++) {
			final Application a = apps.get(x);
			mc.addRow(new Object[] { a.title, a.icon, a.intent, x });
		}
		return mc;
	}

	@Override
	public Cursor getBooksInCollection(final Collection clctn,
			final BookSortBy sortBy) {
		return this.getBooksInCollection(clctn.title, sortBy);
	}

	@Override
	public Cursor getBooksInCollection(final String collectionName,
			final BookSortBy sortBy) {
		return this.getBooksInCollection(collectionName, 0, 999, sortBy);
	}

	@Override
	public final Cursor getCurrentlyReading(final Context cntxt) {
		return this.getCurrentlyReading(cntxt, 0,
				EditPreferences.getCurrentReadDuration(cntxt), null);
	}

	@Override
	public final Cursor getPeriodicals(final Context cntxt) {
		return this.getPeriodicals(cntxt, 0, 999);
	}

	@Override
	public Cursor getRecentlyAdded(final Context cntxt) {
		return this.getRecentlyAdded(cntxt, 0,
				EditPreferences.getRecentlyAddedDuration(cntxt));
	}

	protected String getSortOrderField(final BookSortBy sortBy) {
		String sortOrder = Book.COLUMN_NAME_TITLE;
		switch (sortBy) {
		case FILENAME:
			sortOrder = Book.COLUMN_NAME_FILE_NAME;
			break;
		case AUTHOR:
			sortOrder = Book.COLUMN_NAME_AUTHOR;
			break;
		default:
			sortOrder = Book.COLUMN_NAME_TITLE;
			break;
		}
		return sortOrder;
	}

	@Override
	public void launchBook(final Context cntxt, final long id) {
		final Book bk = this.getBook(cntxt, id);
		final Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		File file = new File("/mnt/sdcard/" + bk.file_path);
		if (!file.exists()) {
			file = new File("/mnt/extsd/" + bk.file_path);
		}
		String filetype = bk.mime_type;
		if (filetype == null) {
			filetype = "application/epub+zip";
		}
		intent.setDataAndType(Uri.fromFile(file), filetype);
		cntxt.startActivity(intent);
	}
}
