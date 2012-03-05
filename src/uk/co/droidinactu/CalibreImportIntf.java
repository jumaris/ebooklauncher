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
package uk.co.droidinactu;

/**
 * CLASS DOCUMENTATION _MUST_ BE GIVEN HERE. ENSURE RELEVENT @see javadoc
 * REFERENCES ARE USED IF REQUIRED.
 * 
 * @author aspela
 */
public class CalibreImportIntf {

    static {
        System.loadLibrary("ndk1");
    }

    public native String getString(int value1, int value2);

    public native void helloLog(String logThis);

    /**
     * <p>
     * this function opens the named calibre metadata file and imports the contents into the named
     * sqlite database.
     * </p><p>
     * it will :
     * <ul>
     * <li>add new books         (lpaths not equal)</li>
     * <li>update existing books (lpaths equal)</li>
     * </ul>
     * 
     * @param metadataFilename the name of the calibre metadata file
     * @param dbFilename the name of the sqlite database
     */
    public native void importFile(String metadataFilename, String dbFilename);

    public native String invokeNativeFunction();
}
