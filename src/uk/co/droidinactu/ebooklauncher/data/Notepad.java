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

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Commit;

import uk.co.droidinactu.ebooklauncher.contenttable.ContentColumns;
import uk.co.droidinactu.ebooklauncher.contenttable.NotepadColumns;
import android.database.Cursor;
import android.provider.BaseColumns;

@Root(name = "notepad", strict = false)
public class Notepad {

	@Attribute(name = "createDate")
	public String createDate;

	public String filename;
	public String fullFilename;
	public String id;
	public String mimetype;
	public String title;
	public String sourceId;
	public String addedDate;
	public String modifiedDate;
	public String fileSize;
	public String preventDelete;
	public String thumbnail;

	@Attribute(name = "type")
	public String type;

	public Notepad() {
	}

	public Notepad(final Cursor memosCursor) {
		// [_id, title, source_id, created_date, added_date, modified_date, mime_type, file_path,
		// file_name, file_size, thumbnail, prevent_delete]
		this.id = memosCursor.getString(memosCursor.getColumnIndex(BaseColumns._ID));
		this.sourceId = memosCursor.getString(memosCursor.getColumnIndex(ContentColumns.SOURCE_ID));
		this.title = memosCursor.getString(memosCursor.getColumnIndex(NotepadColumns.TITLE));
		this.createDate = memosCursor.getString(memosCursor.getColumnIndex(NotepadColumns.CREATED_DATE));
		this.addedDate = memosCursor.getString(memosCursor.getColumnIndex(ContentColumns.ADDED_DATE));
		this.modifiedDate = memosCursor.getString(memosCursor.getColumnIndex(ContentColumns.MODIFIED_DATE));
		this.mimetype = memosCursor.getString(memosCursor.getColumnIndex(ContentColumns.MIME_TYPE));
		this.fullFilename = memosCursor.getString(memosCursor.getColumnIndex(ContentColumns.FILE_PATH));
		this.filename = memosCursor.getString(memosCursor.getColumnIndex(ContentColumns.FILE_NAME));
		this.fileSize = memosCursor.getString(memosCursor.getColumnIndex(ContentColumns.FILE_SIZE));
		this.thumbnail = memosCursor.getString(memosCursor.getColumnIndex(NotepadColumns.THUMBNAIL));
		this.preventDelete = memosCursor.getString(memosCursor.getColumnIndex(ContentColumns.PREVENT_DELETE));
	}

	/**
	 * called by the XML deserializer after deserialization has finished. <br/>
	 * when called this method takes the string date representations and
	 * converts them to long values.
	 */
	@Commit
	public void build() {
	}

}
