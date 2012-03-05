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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import uk.co.droidinactu.common.Base64;
import uk.co.droidinactu.common.model.AbstractDataObj;
import uk.co.droidinactu.ebooklauncher.EBookLauncherApplication;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * @author andy
 * 
 */
public class CalibreBook extends AbstractDataObj {

    private static final ArrayList<String> coverImgNames = new ArrayList<String>();

    public static final String DATABASE_TABLE_NAME = "ebook";
    public static final String FIELD_NAME_APPLICATION_ID = "application_id";
    public static final String FIELD_NAME_author_sort = "author_sort";
    public static final String FIELD_NAME_authors = "authors";
    public static final String FIELD_NAME_book_producer = "book_producer";
    public static final String FIELD_NAME_BOOK_TITLE = "title";
    public static final String FIELD_NAME_category = "category";
    public static final String FIELD_NAME_comments = "comments";
    public static final String FIELD_NAME_cover = "cover";
    public static final String FIELD_NAME_db_id = "db_id";
    public static final String FIELD_NAME_ddc = "ddc";
    public static final String FIELD_NAME_isbn = "isbn";
    public static final String FIELD_NAME_language = "language";
    public static final String FIELD_NAME_lcc = "lcc";
    public static final String FIELD_NAME_lccn = "lccn";
    public static final String FIELD_NAME_lpath = "lpath";
    public static final String FIELD_NAME_mime = "mime";
    public static final String FIELD_NAME_pubdate = "pubdate";
    public static final String FIELD_NAME_publication_type = "publication_type";
    public static final String FIELD_NAME_publisher = "publisher";
    public static final String FIELD_NAME_rating = "rating";
    public static final String FIELD_NAME_rights = "rights";
    public static final String FIELD_NAME_series = "series";
    public static final String FIELD_NAME_series_index = "series_index";
    public static final String FIELD_NAME_size = "size";
    public static final String FIELD_NAME_tags = "tags";
    public static final String FIELD_NAME_thumbnail_data = "thumbnail_data";
    public static final String FIELD_NAME_thumbnail_height = "thumbnail_height";
    public static final String FIELD_NAME_thumbnail_width = "thumbnail_width";
    public static final String FIELD_NAME_timestamp = "timestamp";
    public static final String FIELD_NAME_title_sort = "title_sort";
    public static final String FIELD_NAME_uuid = "uuid";
    private static String LOG_TAG = "EBook:";

    /**
     * this number denotes the version of this class. so that we can decide if
     * the structure of this class is the same as the one being deserialised
     * into it
     */
    private static final long serialVersionUID = 1L;

    static {
        CalibreBook.coverImgNames.add("cover.jpeg");
        CalibreBook.coverImgNames.add("cover1.jpeg");
        CalibreBook.coverImgNames.add("cover.jpg");
        CalibreBook.coverImgNames.add("cover1.jpg");
        CalibreBook.coverImgNames.add("cover_image.jpg");
    }

    public static String[] getFieldNames() {
        return new String[] { BaseColumns._ID,
                AbstractDataObj.FIELD_NAME_LAST_UPDATED,
                CalibreBook.FIELD_NAME_BOOK_TITLE,
                CalibreBook.FIELD_NAME_APPLICATION_ID,
                CalibreBook.FIELD_NAME_author_sort,
                CalibreBook.FIELD_NAME_book_producer,
                CalibreBook.FIELD_NAME_category,
                CalibreBook.FIELD_NAME_comments,
                CalibreBook.FIELD_NAME_cover,
                CalibreBook.FIELD_NAME_db_id,
                CalibreBook.FIELD_NAME_ddc,
                CalibreBook.FIELD_NAME_isbn,
                CalibreBook.FIELD_NAME_language,
                CalibreBook.FIELD_NAME_lcc,
                CalibreBook.FIELD_NAME_lccn,
                CalibreBook.FIELD_NAME_lpath,
                CalibreBook.FIELD_NAME_mime,
                CalibreBook.FIELD_NAME_publication_type,
                CalibreBook.FIELD_NAME_pubdate,
                CalibreBook.FIELD_NAME_publisher,
                CalibreBook.FIELD_NAME_rating,
                CalibreBook.FIELD_NAME_rights,
                CalibreBook.FIELD_NAME_series,
                CalibreBook.FIELD_NAME_series_index,
                CalibreBook.FIELD_NAME_size,
                CalibreBook.FIELD_NAME_timestamp,
                CalibreBook.FIELD_NAME_title_sort,
                CalibreBook.FIELD_NAME_uuid,
                CalibreBook.FIELD_NAME_thumbnail_height,
                CalibreBook.FIELD_NAME_thumbnail_width,
                CalibreBook.FIELD_NAME_thumbnail_data,
                CalibreBook.FIELD_NAME_authors,
                CalibreBook.FIELD_NAME_tags };
    }

    public int application_id = -1;
    public String author_sort = "";

    public HashMap<String, String> author_sort_map = new HashMap<String, String>();
    public ArrayList<Author> authors = new ArrayList<Author>();

    public String authorString = "";
    public String book_producer = "";
    public String category = "";
    public HashMap<String, String> classifiers = new HashMap<String, String>();
    public String comments = "";
    public String cover = "";
    public List<String> cover_data = new ArrayList<String>();
    private Bitmap coverImg = null;
    public String db_id = "";
    public String ddc = "";
    protected final DecimalFormat decFmt = new DecimalFormat("#0.0");
    public int id = -1;
    public String isbn = "";
    public String language = "";
    public List<String> languages = new ArrayList<String>();
    public String lcc = "";
    public String lccn = "";
    public String lpath = "";
    public String mime = "";
    public String pubdate = "";
    public String publication_type = "";
    public String publisher = "";
    public int rating = -1;
    public String rights = "";
    private String series = "";
    private String series_index = "";
    private int size = -1;
    public ArrayList<Tag> tags = new ArrayList<Tag>();
    public String tagString = "";
    public CalibreThumbnail thumbnail = new CalibreThumbnail();
    public String timestamp = "";
    public String title = "";
    public String title_sort = "";

    public List<String> user_metadata = new ArrayList<String>();

    public String uuid = "";

    public CalibreBook() {
    }

    public CalibreBook(final Cursor aRow) {
        this.title = aRow.getString(aRow.getColumnIndex(CalibreBook.FIELD_NAME_BOOK_TITLE));
        this.application_id = aRow.getInt(aRow.getColumnIndex(CalibreBook.FIELD_NAME_APPLICATION_ID));
        this.author_sort = aRow.getString(aRow.getColumnIndex(CalibreBook.FIELD_NAME_author_sort));
        this.book_producer = aRow.getString(aRow.getColumnIndex(CalibreBook.FIELD_NAME_book_producer));
        this.category = aRow.getString(aRow.getColumnIndex(CalibreBook.FIELD_NAME_category));
        this.comments = aRow.getString(aRow.getColumnIndex(CalibreBook.FIELD_NAME_comments));
        this.cover = aRow.getString(aRow.getColumnIndex(CalibreBook.FIELD_NAME_cover));
        this.db_id = aRow.getString(aRow.getColumnIndex(CalibreBook.FIELD_NAME_db_id));
        this.ddc = aRow.getString(aRow.getColumnIndex(CalibreBook.FIELD_NAME_ddc));
        this.isbn = aRow.getString(aRow.getColumnIndex(CalibreBook.FIELD_NAME_isbn));
        this.language = aRow.getString(aRow.getColumnIndex(CalibreBook.FIELD_NAME_language));
        this.lcc = aRow.getString(aRow.getColumnIndex(CalibreBook.FIELD_NAME_lcc));
        this.lccn = aRow.getString(aRow.getColumnIndex(CalibreBook.FIELD_NAME_lccn));
        this.lpath = aRow.getString(aRow.getColumnIndex(CalibreBook.FIELD_NAME_lpath));
        this.mime = aRow.getString(aRow.getColumnIndex(CalibreBook.FIELD_NAME_mime));
        this.pubdate = aRow.getString(aRow.getColumnIndex(CalibreBook.FIELD_NAME_pubdate));
        this.publication_type = aRow.getString(aRow.getColumnIndex(CalibreBook.FIELD_NAME_publication_type));
        this.publisher = aRow.getString(aRow.getColumnIndex(CalibreBook.FIELD_NAME_publisher));
        this.rating = aRow.getInt(aRow.getColumnIndex(CalibreBook.FIELD_NAME_rating));
        this.rights = aRow.getString(aRow.getColumnIndex(CalibreBook.FIELD_NAME_rights));
        this.setSeries(aRow.getString(aRow.getColumnIndex(CalibreBook.FIELD_NAME_series)));
        this.setSeriesIndex(aRow.getString(aRow.getColumnIndex(CalibreBook.FIELD_NAME_series_index)));
        this.setSize(aRow.getInt(aRow.getColumnIndex(CalibreBook.FIELD_NAME_size)));
        this.title_sort = aRow.getString(aRow.getColumnIndex(CalibreBook.FIELD_NAME_title_sort));
        this.uuid = aRow.getString(aRow.getColumnIndex(CalibreBook.FIELD_NAME_uuid));
        this.authorString = aRow.getString(aRow.getColumnIndex(CalibreBook.FIELD_NAME_authors));
        this.tagString = aRow.getString(aRow.getColumnIndex(CalibreBook.FIELD_NAME_tags));

        this.thumbnail = new CalibreThumbnail();
        this.thumbnail.data = aRow.getString(aRow.getColumnIndex(CalibreBook.FIELD_NAME_thumbnail_data));
        this.thumbnail.height = aRow.getInt(aRow.getColumnIndex(CalibreBook.FIELD_NAME_thumbnail_height));
        this.thumbnail.width = aRow.getInt(aRow.getColumnIndex(CalibreBook.FIELD_NAME_thumbnail_width));

        this.setUniqueIdentifier(aRow.getString(aRow.getColumnIndex(BaseColumns._ID)));
        this.setLastUpdated(aRow.getString(aRow.getColumnIndex(AbstractDataObj.FIELD_NAME_LAST_UPDATED)));
    }

    private boolean doCompare(final FilterEntry fe, final String strToCheck) {
        if (strToCheck.toLowerCase().contains(fe.token.toLowerCase())) { return true; }
        return false;
    }

    public String getAuthors() {
        String authorLst = "";
        for (final Author auth : this.authors) {
            authorLst += auth.getAuthorName() + ", ";
        }
        try {
            authorLst = authorLst.substring(0, authorLst.length() - 2);
        } catch (final StringIndexOutOfBoundsException e) {
        }
        return authorLst;
    }

    @Override
    public ContentValues getContentValues() {
        final ContentValues map = super.getContentValues();
        map.put(CalibreBook.FIELD_NAME_BOOK_TITLE, this.title);
        map.put(CalibreBook.FIELD_NAME_APPLICATION_ID, this.application_id);
        map.put(CalibreBook.FIELD_NAME_author_sort, this.author_sort);
        map.put(CalibreBook.FIELD_NAME_book_producer, this.book_producer);
        map.put(CalibreBook.FIELD_NAME_category, this.category);
        map.put(CalibreBook.FIELD_NAME_comments, this.comments);
        map.put(CalibreBook.FIELD_NAME_cover, this.cover);
        map.put(CalibreBook.FIELD_NAME_db_id, this.db_id);
        map.put(CalibreBook.FIELD_NAME_ddc, this.ddc);
        map.put(CalibreBook.FIELD_NAME_isbn, this.isbn);
        map.put(CalibreBook.FIELD_NAME_language, this.language);
        map.put(CalibreBook.FIELD_NAME_lcc, this.lcc);
        map.put(CalibreBook.FIELD_NAME_lccn, this.lccn);
        map.put(CalibreBook.FIELD_NAME_lpath, this.lpath);
        map.put(CalibreBook.FIELD_NAME_mime, this.mime);
        map.put(CalibreBook.FIELD_NAME_publication_type, this.publication_type);
        map.put(CalibreBook.FIELD_NAME_pubdate, this.pubdate);
        map.put(CalibreBook.FIELD_NAME_publisher, this.publisher);
        map.put(CalibreBook.FIELD_NAME_rating, this.rating);
        map.put(CalibreBook.FIELD_NAME_rights, this.rights);
        map.put(CalibreBook.FIELD_NAME_series, this.series);
        map.put(CalibreBook.FIELD_NAME_series_index, this.series_index);
        map.put(CalibreBook.FIELD_NAME_size, this.size);
        map.put(CalibreBook.FIELD_NAME_timestamp, this.timestamp);
        map.put(CalibreBook.FIELD_NAME_title_sort, this.title_sort);
        map.put(CalibreBook.FIELD_NAME_uuid, this.uuid);
        map.put(CalibreBook.FIELD_NAME_thumbnail_height, this.thumbnail.height);
        map.put(CalibreBook.FIELD_NAME_thumbnail_width, this.thumbnail.width);
        map.put(CalibreBook.FIELD_NAME_thumbnail_data, this.thumbnail.data);
        map.put(CalibreBook.FIELD_NAME_authors, this.authorString);
        map.put(CalibreBook.FIELD_NAME_tags, this.tagString);
        return map;
    }

    public synchronized Bitmap getCoverImg(final Context ctx, final int noImgId, final int noImgIdEpub,
            final int noImgIdPdf) {
        if (this.coverImg != null) { return this.coverImg; }
        Bitmap tmpBmp = null;
        try {
            tmpBmp = this.getCoverImgFromThumbnail(ctx);
            if (tmpBmp != null) {
                this.coverImg = tmpBmp;
                return this.coverImg;
            } else if (this.lpath.endsWith("epub")) {
                tmpBmp = this.getCoverImgFromEpub(ctx);
                if (tmpBmp != null) {
                    this.coverImg = tmpBmp;
                    return this.coverImg;
                }
            } else if (this.lpath.endsWith("pdf")) {
                tmpBmp = this.getCoverImgFromPdf(ctx);
                if (tmpBmp != null) {
                    this.coverImg = tmpBmp;
                    return this.coverImg;
                }
            }
        } catch (final Exception e) {
        }
        return BitmapFactory.decodeResource(ctx.getResources(), noImgId);
    }

    private Bitmap getCoverImgFromEpub(final Context ctx) {
        try {
            // create a buffer to improve copy performance later.
            final byte[] buffer = new byte[2048];

            ZipInputStream zipinputstream = null;
            ZipEntry zipentry;
            zipinputstream = new ZipInputStream(new FileInputStream("/sdcard/" + this.lpath));

            while ((zipentry = zipinputstream.getNextEntry()) != null) {
                // for each entry to be extracted
                final String entryName = zipentry.getName();
                // System.out.println("File ::" + entryName);

                if (CalibreBook.coverImgNames.contains(entryName)) {
                    // Once we get the entry from the stream, the stream is
                    // positioned read to read the raw data, and we keep
                    // reading until read returns 0 or less.
                    final String outpath = "/sdcard/CoverImg.jpg";
                    FileOutputStream output = null;
                    try {
                        output = new FileOutputStream(outpath);
                        int len = 0;
                        while ((len = zipinputstream.read(buffer)) > 0) {
                            output.write(buffer, 0, len);
                        }
                    } finally {
                        // we must always close the output file
                        if (output != null) {
                            output.close();
                        }
                    }
                    zipinputstream.closeEntry();

                    final Bitmap bmp = BitmapFactory.decodeFile(outpath);
                    final File f = new File(outpath);
                    f.delete();
                    return bmp;
                }
            }// while

            zipinputstream.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap getCoverImgFromPdf(final Context ctx) {
        final Bitmap mBitmap = null;
        try {
            final File file = new File("/sdcard/" + this.lpath);

        } catch (final Exception e) {
        }
        return null;
    }

    private Bitmap getCoverImgFromThumbnail(final Context ctx) {
        Bitmap bmp = null;
        if (this.thumbnail.data.length() > 0) {
            byte[] imgBytes = null;
            try {
                // this.thumbnail.data.length()
                imgBytes = Base64.toBytes(this.thumbnail.data);
                if (imgBytes != null) {
                    bmp = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
                }
                if (bmp == null) {
                    final String outpath = "/sdcard/tmpCoverImg.jpg";
                    this.writeImageBytesToFile(imgBytes, outpath);
                    bmp = BitmapFactory.decodeFile(outpath);
                    final File f = new File(outpath);
                    f.delete();
                }
            } catch (final Exception e) {
                final String msg = e.getMessage();
                Log.e(EBookLauncherApplication.LOG_TAG + CalibreBook.LOG_TAG, msg, e);
            }
        }
        return bmp;
    }

    @Override
    public HashMap<Integer, ArrayList<String>> getFields() {
        final HashMap<Integer, ArrayList<String>> fields = super.getFields();

        int x = fields.size();
        fields.put(x++, this.getArrayList(CalibreBook.FIELD_NAME_BOOK_TITLE, "VARCHAR(255) NOT NULL"));
        fields.put(x++, this.getArrayList(CalibreBook.FIELD_NAME_APPLICATION_ID, "VARCHAR(255) NOT NULL"));
        fields.put(x++, this.getArrayList(CalibreBook.FIELD_NAME_authors, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(CalibreBook.FIELD_NAME_author_sort, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(CalibreBook.FIELD_NAME_book_producer, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(CalibreBook.FIELD_NAME_category, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(CalibreBook.FIELD_NAME_comments, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(CalibreBook.FIELD_NAME_cover, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(CalibreBook.FIELD_NAME_db_id, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(CalibreBook.FIELD_NAME_ddc, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(CalibreBook.FIELD_NAME_isbn, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(CalibreBook.FIELD_NAME_language, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(CalibreBook.FIELD_NAME_lcc, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(CalibreBook.FIELD_NAME_lccn, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(CalibreBook.FIELD_NAME_lpath, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(CalibreBook.FIELD_NAME_mime, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(CalibreBook.FIELD_NAME_publication_type, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(CalibreBook.FIELD_NAME_pubdate, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(CalibreBook.FIELD_NAME_publisher, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(CalibreBook.FIELD_NAME_rating, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(CalibreBook.FIELD_NAME_rights, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(CalibreBook.FIELD_NAME_series, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(CalibreBook.FIELD_NAME_series_index, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(CalibreBook.FIELD_NAME_size, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(CalibreBook.FIELD_NAME_timestamp, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(CalibreBook.FIELD_NAME_title_sort, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(CalibreBook.FIELD_NAME_uuid, "VARCHAR(255)"));
        fields.put(x++, this.getArrayList(CalibreBook.FIELD_NAME_tags, "VARCHAR(1024)"));
        fields.put(x++, this.getArrayList(CalibreBook.FIELD_NAME_thumbnail_data, "VARCHAR(1024)"));
        fields.put(x++, this.getArrayList(CalibreBook.FIELD_NAME_thumbnail_width, "INTEGER"));
        fields.put(x++, this.getArrayList(CalibreBook.FIELD_NAME_thumbnail_height, "INTEGER"));
        return fields;
    }

    public String getSelectAllSeries() {
        return "select DISTINCT (" + CalibreBook.FIELD_NAME_series + ") from " + this.getTableName();
    }

    public String getSelectSql(final SortOrder sOrdr) {
        String orderby = CalibreBook.FIELD_NAME_BOOK_TITLE;
        if (sOrdr.equals(SortOrder.Author)) {
            orderby = CalibreBook.FIELD_NAME_author_sort;
        } else if (sOrdr.equals(SortOrder.Author)) {
            orderby = CalibreBook.FIELD_NAME_series;
        }

        return "select * from " + this.getTableName() + " order by " + orderby;
    }

    public String getSelectSqlFiltered() {
        return "select * from " + this.getTableName(); // + " where " +
        // AbstractDataObj.FIELD_NAME_UNIQUE_IDENTIFIER
        // +
        // "=" + objId;
    }

    public String getSeries() {
        return this.series;
    }

    public String getSeriesIndex() {
        return this.series_index;
    }

    public String getSeriesString() {
        if (!this.series.equals("null") && (this.series.length() > 0)) { return "book " + this.series_index
                + " of "
                + this.series; }
        return " ";
    }

    public String getSizeString() {
        String sizeStr = "" + this.size + " Bytes";
        if (this.size > 1000000) {
            sizeStr = this.decFmt.format(this.size / 1000000) + " Mb";
        } else if (this.size > 1000) {
            sizeStr = this.decFmt.format(this.size / 1000) + " Kb";
        }
        return sizeStr;
    }

    public String getSqlSelectBookFromTitleAndAuthor() {
        return "select * from " + this.getTableName()
                + " where "
                + CalibreBook.FIELD_NAME_BOOK_TITLE
                + "=\'"
                + this.title
                + "\' and "
                + CalibreBook.FIELD_NAME_author_sort
                + "=\'"
                + this.author_sort
                + "\'";
    }

    public String getSqlUpdateFromV001() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getSqlUpdateFromV002() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getTableName() {
        return CalibreBook.DATABASE_TABLE_NAME;
    }

    public CharSequence getTagList() {
        String txtTags = "";
        for (final Tag tg : this.tags) {
            txtTags += tg.name + " & ";
        }
        try {
            txtTags = txtTags.substring(0, txtTags.length() - 2);
        } catch (final StringIndexOutOfBoundsException e) {
        }
        return txtTags;
    }

    public synchronized boolean matchesFilter(final FilterEntry fe) {
        boolean titleMatch = false;
        boolean publisherMatch = false;
        boolean seriesMatch = false;
        final boolean authorMatch = false;
        final boolean tagMatch = false;

        if (this.doCompare(fe, this.title)) {
            titleMatch = true;
            return true;
        } else if (this.doCompare(fe, this.publisher)) {
            publisherMatch = true;
            return true;

        } else if (this.doCompare(fe, this.series)) {
            seriesMatch = true;
            return true;
        }
        if (!(titleMatch || publisherMatch)) {
            // for (final Author author : this.authors) {
            // if (this.doCompare(fe, author.name)) {
            // authorMatch = true;
            // return true;
            // }
            // }
            // for (final Tag tag : this.tags) {
            // if (this.doCompare(fe, tag.name)) {
            // tagMatch = true;
            // return true;
            // }
            // }
        }
        return seriesMatch || titleMatch || publisherMatch || authorMatch || tagMatch;
    }

    public void readMetadata() {
        if (this.lpath.endsWith("epub")) {
            this.readMetaDataFromEpub();
        }
    }

    private void readMetaDataFromEpub() {
        // try {
        //
        // // create a buffer to improve copy performance later.
        // final byte[] buffer = new byte[2048];
        //
        // ZipInputStream zipinputstream = null;
        // final ZipEntry zipentry;
        // zipinputstream = new ZipInputStream(new FileInputStream("/sdcard/" +
        // this.lpath));
        //
        // final EpubReader epbrdr = new EpubReader();
        // final Book bk = epbrdr.readEpub(zipinputstream);
        // zipinputstream.close();
        //
        // this.convertMetadata(bk);
        // } catch (final Exception e) {
        // e.printStackTrace();
        // }
    }

    public void setAuthors(final ArrayList<String> bookAuthorList) {
        this.authorString = "";
        for (final String s : bookAuthorList) {
            this.authorString += "||" + s;
            this.authors.add(new Author(s));
        }
    }

    public void setSeries(final String value) {
        this.series = value;
    }

    public void setSeriesIndex(final String value) {
        this.series_index = value;
    }

    public void setSize(final int size) {
        this.size = size;
    }

    public void setTags(final ArrayList<String> bookTagList) {
        this.tagString = "";
        for (final String s : bookTagList) {
            this.tagString += "||" + s;
            this.tags.add(new Tag(s));
        }
        this.tagString = this.tagString.substring(2);
    }

    @Override
    public String toString() {
        return this.title + " [book " + this.series_index + " of " + this.series + "]\nby " + this.getAuthors();
    }

    private void writeImageBytesToFile(final byte[] imgBytes, final String strFilePath) {
        try {
            FileOutputStream fos = new FileOutputStream(strFilePath);
            fos.write(imgBytes);
            fos.flush();
            fos.close();
            fos = null;
            System.gc();
        } catch (final FileNotFoundException ex) {
            System.out.println("FileNotFoundException : " + ex);
        } catch (final IOException ioe) {
            System.out.println("IOException : " + ioe);
        }
    }

}
