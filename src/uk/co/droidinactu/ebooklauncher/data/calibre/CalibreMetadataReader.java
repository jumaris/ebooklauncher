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
package uk.co.droidinactu.ebooklauncher.data.calibre;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import uk.co.droidinactu.common.file.AsciiFileReader;
import uk.co.droidinactu.ebooklauncher.EBookLauncherApplication;
import android.util.Log;

/**
 * @author andy
 */
public class CalibreMetadataReader extends AsciiFileReader {

    public static final String lineEndings = "[{,}]\"";

    private static String LOG_TAG = "CalibreMetsDataRdr:";
    private ArrayList<String> bookAuthorList = new ArrayList<String>();

    private ArrayList<String> bookTagList = new ArrayList<String>();

    private final ImportProgressObject currProgressObj = new ImportProgressObject();

    private final CalibreDataModel dataMdl;
    private final int nbrBooksBetweenUiUpdates = 4;

    private final long sleepBetweenBookImports = 150;
    private CalibreBook tmpBk;

    public CalibreMetadataReader(final CalibreDataModel dataModel) {
        this.dataMdl = dataModel;
        this.tmpBk = new CalibreBook();
    }

    private String getBase64String(final String base64Strd) {
        String temp = base64Strd;

        if (temp.endsWith("=")) {
            temp = temp.substring(0, temp.length() - 1);
        }
        if (temp.endsWith("=")) {
            temp = temp.substring(0, temp.length() - 1);
        }

        int asciiChars = temp.length();// - temp.Count(c =>
        // Char.IsWhiteSpace(c));
        switch (asciiChars % 4) {
        case 1:
            // This would always produce an exception!!
            // Regardless what (or what not) you attach to your string!
            // Better would be some kind of throw new Exception()
            // return new byte[0];
        case 0:
            asciiChars = 0;
            break;
        case 2:
            asciiChars = 2;
            temp += "==";
            break;
        case 3:
            asciiChars = 1;
            temp += "=";
            break;
        }

        return temp;
    }

    private HashMap<String, String> getKeyValuePair(final String fileLine) {
        final HashMap<String, String> recordFields = new HashMap<String, String>();
        final StringTokenizer tk = new StringTokenizer(fileLine, ":");
        final String key = tk.nextToken();
        String value = tk.nextToken();
        while (tk.hasMoreTokens()) {
            value += ":" + tk.nextToken();
        }
        recordFields.put(key, value);
        return recordFields;
    }

    @Override
    public String nextLineFromFile() {
        String tmpStr = super.nextLineFromFile();
        if (!tmpStr.endsWith("null")) {
            while (!CalibreMetadataReader.lineEndings.contains(tmpStr.substring(tmpStr.length() - 1))) {
                tmpStr += "\\n" + super.nextLineFromFile();
            }
        }
        this.currProgressObj.currentBooksRead += tmpStr.length();
        return tmpStr;
    }

    public void readBooks() {

        final long fileSize = new File(this.m_sFilepath + this.getFilename()).length();
        this.currProgressObj.totalNbrBooks = new Long(fileSize).intValue();
        this.currProgressObj.currentBooksRead = 0;

        Log.d(EBookLauncherApplication.LOG_TAG + CalibreMetadataReader.LOG_TAG,
                "reading calibre metadata from [" + this.getFilename() + "]");

        String prevLine = "";
        String currLine = this.firstLineFromFile().trim();
        this.currProgressObj.currentBooksRead += currLine.length();

        if ((currLine.length() != 1) && !currLine.equals("[")) {
            // FIXME : File Format Error
            return;
        }
        prevLine = currLine;
        currLine = this.nextLineFromFile();
        int currentBkNbr = 0;

        while (currLine.length() > 0) {

            if ((prevLine.equals("},") && currLine.equals("{")) || (prevLine.equals("}") && currLine.equals("]"))) {
                final File f = new File(this.m_sFilepath + this.tmpBk.lpath);
                if (f.exists()) {
                    try {
                        this.tmpBk.setAuthors(this.bookAuthorList);
                        this.tmpBk.setTags(this.bookTagList);
                        this.tmpBk.setUniqueIdentifier(this.dataMdl.addBook(this.tmpBk));
                        currentBkNbr++;

                    } catch (final Exception e) {
                        Log.e(EBookLauncherApplication.LOG_TAG + CalibreMetadataReader.LOG_TAG,
                                "exception adding book to database: " + e.getMessage(), e);
                    }

                    for (final String s : this.bookAuthorList) {
                        try {
                            this.dataMdl.addAuthorBookLink(s, this.tmpBk);
                            // Thread.sleep(20);
                        } catch (final Exception e) {
                            Log.e(EBookLauncherApplication.LOG_TAG + CalibreMetadataReader.LOG_TAG,
                                    "exception adding author [" + s + "] to database: " + e.getMessage(), e);
                        }
                    }

                    for (final String s : this.bookTagList) {
                        try {
                            this.dataMdl.addTagBookLink(s, this.tmpBk);
                            // Thread.sleep(20);
                        } catch (final Exception e) {
                            Log.e(EBookLauncherApplication.LOG_TAG + CalibreMetadataReader.LOG_TAG,
                                    "exception adding tag [" + s + "] to database: " + e.getMessage(), e);
                        }
                    }

                    this.bookAuthorList = new ArrayList<String>();
                    this.bookTagList = new ArrayList<String>();
                }
                this.tmpBk = new CalibreBook();
                prevLine = currLine;
                currLine = this.nextLineFromFile();
            } else {
                if (currLine.length() > 2) {
                    try {
                        this.readRecord(currLine);
                    } catch (final Exception e) {
                        Log.e(EBookLauncherApplication.LOG_TAG + CalibreMetadataReader.LOG_TAG + ":readRecord",
                                "error parsing line [" + currLine + "] " + e.getMessage(), e);
                    }
                }
            }
            prevLine = currLine;
            currLine = this.nextLineFromFile();
        }
    }

    private List<String> readList(String fileLine) {
        // Log.d(LibraryConstants.LOG_TAG + CalibreMetadataReader.LOG_TAG +
        // "readList()",
        // "started");
        final List<String> recordFields = new ArrayList<String>();

        if (fileLine.endsWith("[],") || fileLine.endsWith("{},")) { return recordFields; }

        while ((fileLine = this.nextLineFromFile()).length() > 2) {
            if (fileLine.startsWith("\"")) {
                fileLine = fileLine.substring(1, fileLine.length() - 1);
            }
            if (fileLine.endsWith("\"")) {
                fileLine = fileLine.substring(0, fileLine.length() - 1);
            }
            recordFields.add(fileLine);
        }

        return recordFields;
    }

    private HashMap<String, String> readListKeyValue(String fileLine) {
        // Log.d(LibraryConstants.LOG_TAG + CalibreMetadataReader.LOG_TAG +
        // "readListKeyValue()", "started");
        final HashMap<String, String> recordFields = new HashMap<String, String>();

        if (fileLine.endsWith("[],") || fileLine.endsWith("{},")) { return recordFields; }

        while ((fileLine = this.nextLineFromFile().trim()).length() > 2) {
            recordFields.putAll(this.getKeyValuePair(fileLine));
        }

        return recordFields;
    }

    private void readRecord(final String fileLine) {
        // Log.d(LibraryConstants.LOG_TAG + CalibreMetadataReader.LOG_TAG +
        // "readRecord()", "started");
        String key;
        final StringTokenizer tk = new StringTokenizer(fileLine, ":");
        key = tk.nextToken().trim().substring(1).trim();
        key = key.substring(0, key.length() - 1).trim();
        String value = tk.nextToken().trim();
        while (tk.hasMoreTokens()) {
            value += ":" + tk.nextToken();
        }

        if (value.endsWith(",")) {
            value = value.substring(0, value.length() - 1).trim();
        }
        if (value.startsWith("\"")) {
            value = value.substring(1, value.length() - 1).trim();
        }

        if (key.equals("application_id")) {
            try {
                this.tmpBk.application_id = Integer.parseInt(value);
            } catch (final Exception e) {
            }
        } else if (key.equals("author_sort")) {
            this.tmpBk.author_sort = value;
        } else if (key.equals("author_sort_map")) {
            this.tmpBk.author_sort_map = this.readListKeyValue(fileLine);
        } else if (key.equals("languages")) {
            this.tmpBk.languages = this.readList(fileLine);
        } else if (key.equals("classifiers")) {
            this.tmpBk.classifiers = this.readListKeyValue(fileLine);
        } else if (key.equals("cover_data")) {
            this.tmpBk.cover_data = this.readList(fileLine);
        } else if (key.equals("rating")) {
            try {
                this.tmpBk.rating = Integer.parseInt(value);
            } catch (final Exception e) {
            }
        } else if (key.equals("isbn")) {
            this.tmpBk.isbn = value;
        } else if (key.equals("pubdate")) {
            this.tmpBk.pubdate = value;
        } else if (key.equals("series")) {
            if (!value.equals("null")) {
                this.tmpBk.setSeries(value);
            }
        } else if (key.equals("timestamp")) {
            this.tmpBk.timestamp = value;
        } else if (key.equals("publication_type")) {
            this.tmpBk.publication_type = value;
        } else if (key.equals("size")) {
            try {
                this.tmpBk.setSize(Integer.parseInt(value));
            } catch (final Exception e) {
            }
        } else if (key.equals("category")) {
            this.tmpBk.category = value;
        } else if (key.equals("uuid")) {
            this.tmpBk.uuid = value;
        } else if (key.equals("title")) {
            this.tmpBk.title = value;
        } else if (key.equals("comments")) {
            this.tmpBk.comments = value;
        } else if (key.equals("ddc")) {
            this.tmpBk.ddc = value;
        } else if (key.equals("tags")) {
            for (final String s : this.readList(fileLine)) {
                this.bookTagList.add(s);
            }
        } else if (key.equals("mime")) {
            this.tmpBk.mime = value;
            if (value.equalsIgnoreCase("null")) {
                if (this.tmpBk.lpath.endsWith("epub")) {
                    this.tmpBk.mime = MimeTypes.MIME_TYPE_EPUB;
                } else if (this.tmpBk.lpath.endsWith("pdf")) {
                    this.tmpBk.mime = MimeTypes.MIME_TYPE_PDF;
                }
            }
        } else if (key.equals("thumbnail")) {
            if (!value.equalsIgnoreCase("null")) {
                final List<String> tmpList = this.readList(fileLine);
                this.tmpBk.thumbnail.height = Integer
                        .parseInt(tmpList.get(0).substring(0, tmpList.get(0).length() - 1));
                this.tmpBk.thumbnail.width = Integer.parseInt(tmpList.get(1).substring(0, tmpList.get(0).length() - 1));
                this.tmpBk.thumbnail.data = this.getBase64String(tmpList.get(2).substring(0,
                        tmpList.get(2).length() - 1));
            }
        } else if (key.equals("db_id")) {
            this.tmpBk.db_id = value;
        } else if (key.equals("user_metadata")) {
            this.tmpBk.user_metadata = this.readList(fileLine);
        } else if (key.equals("lcc")) {
            this.tmpBk.lcc = value;
        } else if (key.equals("authors")) {
            for (final String s : this.readList(fileLine)) {
                this.bookAuthorList.add(s);
            }
        } else if (key.equals("publisher")) {
            this.tmpBk.publisher = value;
        } else if (key.equals("series_index")) {
            this.tmpBk.setSeriesIndex(value);
        } else if (key.equals("lpath")) {
            this.tmpBk.lpath = value;
        } else if (key.equals("cover")) {
            this.tmpBk.cover = value;
        } else if (key.equals("language")) {
            this.tmpBk.language = value;
        } else if (key.equals("rights")) {
            this.tmpBk.rights = value;
        } else if (key.equals("lccn")) {
            this.tmpBk.lccn = value;
        } else if (key.equals("title_sort")) {
            this.tmpBk.title_sort = value;
        } else if (key.equals("book_producer")) {
            this.tmpBk.book_producer = value;
        } else {
            // FIXME unknown tag!!
        }
    }

}
