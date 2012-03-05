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
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import uk.co.droidinactu.ebooklauncher.EBookLauncherApplication;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class EBookImporter {

    private static final String LOG_TAG = "InLibris_EBookImporter";

    private final CalibreDataModel dataMdl;

    private DbMetadata dbMetaData;
    private final List<String> ebookDirList = new ArrayList<String>();

    public EBookImporter(final CalibreDataModel dataModel, final DbMetadata dbMetaDta) {
        this.dataMdl = dataModel;
        this.dbMetaData = dbMetaDta;
        this.ebookDirList.add("/sdcard/ebooks");
        this.ebookDirList.add("/sdcard/EBooks");
        this.ebookDirList.add("/sdcard/eBooks");
    }

    public void addEbookDir(final String dirName) {
        this.ebookDirList.add(dirName);
    }

    /**
     * This method checks for the existence of the calibre metadata file.
     * 
     * @param calibreFilename
     * @return 3 if an unknown error occurred while we looked for the file; 2 if
     *         the file does not exist; 1 if the file exists, but we cannot read
     *         it; 0 if the file exists and we can read it (okay to use)
     */
    private int checkCalibreMetadataFile(final String calibreFilename) {
        int retValue = 0;
        final File f = new File(calibreFilename);
        try {
            if (!f.exists()) {
                retValue = 2;
            } else {
                if (!f.canRead()) {
                    retValue = 1;
                }
            }
        } catch (final Exception e) {
            retValue = 3;
        }
        return retValue;
    }

    /*
     * Maybe consider the Google search syntax? All terms being ANDed for top matches and ORed for
     * lower ranking matches, with text in quotes being exact matches, and a dash ("-") before a
     * term meaning "NOT". Maybe not. Maybe Or/And/AndNot/OrNot would be simpler and clearer...
     * 
     * My personal usage of multi-tag searching I think would normally be all ANDs, e.g.
     * "Sci-Fi AND ToRead".
     */
    public List<CalibreBook> filterBookListGoogle(final List<FilterEntry> filterTokns) {
        final List<CalibreBook> bkListTmp = new ArrayList<CalibreBook>();

        // FIXME:

        return bkListTmp;
    }

    private void findBooksInDir(final String dirname) {
        final int nextIndexNbr = 0;
        final List<File> files = this.getFilesInDir(dirname, false);
        for (final File f : files) {
            if (f.getName().endsWith(".epub")) {
                this.dataMdl.addBook(this.importEpubFile(dirname + File.separator + f.getName(), nextIndexNbr));
            } else if (f.getName().endsWith(".pdf")) {
                // FIXME bkList.add(importPDFFile(dirname+File.separator+s,
                // nextIndexNbr)) ;
            }
        }

        final List<File> subdirs = this.getFilesInDir(dirname, true);
        for (final File f : subdirs) {
            this.findBooksInDir(dirname + File.separator + f.getName());
        }
    }

    private void getCoverImages(final Context ctx, final int noImgId, final int noImgIdEpub, final int noImgIdPdf) {
        final Thread importMetadataThrd = new Thread() {
            @Override
            public void run() {
                final Cursor results = EBookImporter.this.dataMdl.getBooks(ctx, null, null, null);
                results.moveToFirst();
                for (int x = 0; x < results.getCount(); x++) {
                    final CalibreBook ebk = new CalibreBook(results);
                    ebk.getCoverImg(ctx, noImgId, noImgIdEpub, noImgIdPdf);
                    results.moveToNext();
                }
                results.close();
            }
        };
        importMetadataThrd.setName("Caching Book Covers");
        importMetadataThrd.start();
    }

    private List<File> getFilesInDir(final String dirname, final boolean isDir) {
        final File dir = new File(dirname);

        // String[] children = dir.list();
        // if (children == null) { // Either dir does not exist or is not a
        // directory
        // }
        // else {
        // for (int i=0; i<children.length; i++) { // Get filename of file or
        // directory
        // String filename = children[i];
        // }
        // } // It is also possible to filter the list of returned files.
        //
        // // This example does not return any files that start with `.'.
        // FilenameFilter filter = new FilenameFilter() {
        // public boolean accept(File dir, String name) {
        // return !name.startsWith(".");
        // }
        // };
        // children = dir.list(filter); // The list of files can also be
        // retrieved as File objects
        // File[] files = dir.listFiles();

        // This filter only returns directories
        final FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(final File file) {
                return isDir && file.isDirectory();
            }
        };
        // files = dir.listFiles(fileFilter);

        final List<File> fileList = new ArrayList<File>();
        for (final File f : dir.listFiles(fileFilter)) {
            fileList.add(f);
        }
        return fileList;
    }

    private String[] getToken(String str) {
        boolean foundquotes = false;
        final String[] retValues = new String[2];
        String retStr = "";
        try {
            if (str.startsWith("\"")) {
                foundquotes = true;
                // we are looking for the next quote symbol
                final int quoteLoc = str.indexOf('\"', 1);
                retStr = str.substring(1, quoteLoc);
            } else {
                final int spaceLoc = str.indexOf(' ');
                if (spaceLoc < 0) {
                    retStr = str;
                } else {
                    retStr = str.substring(0, spaceLoc);
                }
            }
        } catch (final Exception e) {
            retStr = str;
        }

        if (foundquotes) {
            str = str.substring(retStr.length() + 2);
        } else {
            str = str.substring(retStr.length());
        }

        retValues[0] = retStr.trim();
        retValues[1] = str.trim();

        return retValues;
    }

    public void importBooks(final String metadataFilename) {
        final CalibreMetadataReader cbkRdr = new CalibreMetadataReader(this.dataMdl);

        final File f = new File(metadataFilename);
        if (f.exists()) {
            // if (f.lastModified() == this.dbMetaData.timestamp) { return; }
            if (this.dbMetaData == null) {
                this.dbMetaData = new DbMetadata();
            }

            this.dbMetaData.timestamp = f.lastModified();
            this.dataMdl.updateDbMetaData(this.dbMetaData);

            cbkRdr.setFilename(metadataFilename);
            try {
                cbkRdr.openFile();
            } catch (final Exception e) {
                Log.e(EBookLauncherApplication.LOG_TAG + EBookImporter.LOG_TAG,
                        "Exception opening metadata file : " + e.getMessage());
            }
            try {
                cbkRdr.readBooks();
            } catch (final Exception e) {
                Log.e(EBookLauncherApplication.LOG_TAG + EBookImporter.LOG_TAG,
                        "Exception reading books : " + e.getMessage());
            }
            try {
                cbkRdr.closeFile();
            } catch (final Exception e) {
                Log.e(EBookLauncherApplication.LOG_TAG + EBookImporter.LOG_TAG,
                        "Exception closing metadata file : " + e.getMessage());
            }
        } else {
            // Search for books in sub dir's
            for (final String dir : this.ebookDirList) {
                this.findBooksInDir(dir);
            }
        }
    }

    private CalibreBook importEpubFile(final String filename, final int nextIndexNbr) {
        final CalibreBook cb = new CalibreBook();
        cb.lpath = filename;
        cb.readMetadata();

        final File f = new File(filename);
        // read in file metadata
        // use tika??

        return cb;
    }

    // private ArrayList<FilterEntry> parseFilter(String filter) {
    // final ArrayList<FilterEntry> filterTokns = new ArrayList<FilterEntry>();
    // String boolOp = "Or";
    //
    // // get first 'token' (whether a word or a quote contained string)
    // String[] tkn = getToken(filter.trim());
    // filter = tkn[1];
    // try {
    // if (BooleanOperatorFactory.isBooleanOperator(tkn[0])) {
    // boolOp = tkn[0];
    // tkn = getToken(filter.trim());
    // filter = tkn[1];
    // }
    // } catch (final StringIndexOutOfBoundsException e) {
    // System.out.println("StringIndexOutOfBoundsException parsing filter : " +
    // e.getMessage());
    // filter = "";
    // } catch (final Exception e) {
    // System.out.println("General Exception parsing filter : " +
    // e.getMessage());
    // filter = "";
    // }
    //
    // filterTokns.add(new
    // FilterEntry(BooleanOperatorFactory.getBooleanOperator(boolOp), tkn[0]));
    //
    // while (filter.length() > 0) {
    // tkn = getToken(filter.trim());
    // filter = tkn[1];
    // if (BooleanOperatorFactory.isBooleanOperator(tkn[0])) {
    // boolOp = tkn[0];
    // tkn = getToken(filter.trim());
    // filter = tkn[1];
    // } else {
    // boolOp = "Or";
    // }
    // filterTokns.add(new
    // FilterEntry(BooleanOperatorFactory.getBooleanOperator(boolOp), tkn[0]));
    // }
    // return filterTokns;
    // }

    private void removeEbookDir(final String dirName) {
        this.ebookDirList.remove(dirName);
    }

    // private void setFilter(final String filter) {
    // final ArrayList<FilterEntry> fltrTkns = parseFilter(filter);
    // // FIXME : How do we now filter the book list?
    // }

    public void sortBooks(final int sortOrderIndex) {
        // switch (sortOrderIndex) {
        // case 1:
        // Collections.sort(this.bkList, new EBookComparatorAuthorSortTitle());
        // Collections.sort(this.bkListFiltered, new
        // EBookComparatorAuthorSortTitle());
        // break;
        // case 2:
        // Collections.sort(this.bkList, new EBookComparatorSeries());
        // Collections.sort(this.bkListFiltered, new EBookComparatorSeries());
        // break;
        // default:
        // Collections.sort(this.bkList, new EBookComparatorTitle());
        // Collections.sort(this.bkListFiltered, new EBookComparatorTitle());
        // }
    }

}
