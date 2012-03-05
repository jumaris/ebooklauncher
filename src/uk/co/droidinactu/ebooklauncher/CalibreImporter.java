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
package uk.co.droidinactu.ebooklauncher;

import uk.co.droidinactu.CalibreImportIntf;
import android.content.Context;
import android.os.Environment;

/**
 * CLASS DOCUMENTATION _MUST_ BE GIVEN HERE. ENSURE RELEVENT @see javadoc
 * REFERENCES ARE USED IF REQUIRED.
 * 
 * @author aspela
 */
public class CalibreImporter {

	public void importFile(final Context ctx) {

		final CalibreImportIntf nativeImprter = new CalibreImportIntf();

		String tmpdirName = ctx.getFilesDir().getAbsolutePath();
		// nativeImprter.importFile(tmpdirName,
		// "/data/data/uk.co.droidinactu.ebooklauncher/databases/SonyPrsT1HomeDb");

		tmpdirName = Environment.getExternalStorageDirectory().getAbsolutePath();
		nativeImprter.importFile(tmpdirName, "/data/data/uk.co.droidinactu.ebooklauncher/databases/SonyPrsT1Home.db");

	}
}
