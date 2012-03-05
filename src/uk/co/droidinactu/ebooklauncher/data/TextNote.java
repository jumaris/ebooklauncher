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

import org.simpleframework.xml.Element;

import android.database.Cursor;

/*

 <?xml version='1.0' encoding='UTF-8' standalone='yes' ?>
 <n0:notepad type="text" createDate="1322166022815" xmlns:n0="http://www.sony.com/notepad">
 <text>test  note</text>
 </n0:notepad>

 */
public final class TextNote extends Notepad {

	@Element(name = "text")
	public String text;

	public TextNote() {
	}

	public TextNote(final Cursor memosCursor) {
		super(memosCursor);
	}

}
