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
package uk.co.droidinactu.common.model;

import java.util.ArrayList;
import java.util.HashMap;

import android.provider.BaseColumns;

/**
 * this object is used to define any data that is made up of key/value pairs.
 * 
 * @author andy
 */
public abstract class AbstractTypeObject extends AbstractDataObj {

	protected static String FIELD_NAME_TYPE_NAME = "typename";
	protected static String FIELD_NAME_TYPE_VALUE = "typevalue";
	protected String description = "";
	protected String name = "";

	/**
	 * Default Constructor.
	 */
	public AbstractTypeObject() {
	}

	/**
	 * Constructor used for creating an Animal from a Database Row.
	 * 
	 * @param arow
	 */
	public AbstractTypeObject(final HashMap<String, String> arow) {
		super(arow);
		this.name = arow.get(AbstractTypeObject.FIELD_NAME_TYPE_NAME);
		this.description = arow.get(AbstractTypeObject.FIELD_NAME_TYPE_VALUE);
	}

	/**
	 * gets the description for this type.
	 * 
	 * @return a string describing this app.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * return the field descriptions that make up a row in this table.
	 */
	@Override
	public HashMap<Integer, ArrayList<String>> getFields() {
		final HashMap<Integer, ArrayList<String>> fields = new HashMap<Integer, ArrayList<String>>();

		int x = 1;
		fields.put(x++,
				this.getArrayList(BaseColumns._ID, "INTEGER PRIMARY KEY"));
		fields.put(x++, this
				.getArrayList(AbstractTypeObject.FIELD_NAME_TYPE_NAME,
						"VARCHAR(50) not null"));
		fields.put(x++, this.getArrayList(
				AbstractTypeObject.FIELD_NAME_TYPE_VALUE, "VARCHAR(255)"));

		// this.dumpFields(fields);
		return fields;
	}

	/**
	 * gets the name of this type.
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	// /**
	// * return the sql statement used to insert a new type object into the
	// database.
	// */
	// @Override
	// public String getSqlInsert() {
	// StringBuilder sqlStmt = new StringBuilder(super.getSqlInsert());
	// sqlStmt.append("\"").append(this.getName()).append("\",");
	// sqlStmt.append("\"").append(this.getDescription()).append("\"");
	// sqlStmt.append(");");
	// return sqlStmt.toString();
	// }
	//
	// /**
	// * return the sql statement used to update a type object in the database.
	// */
	// @Override
	// public String getSqlUpdate() {
	// StringBuilder sqlStmt = new StringBuilder(super.getSqlUpdate());
	// sqlStmt.append(" ").append(FIELD_NAME_TYPE_NAME).append("=\"").append(this.name).append("\"");
	// sqlStmt.append(",").append(FIELD_NAME_TYPE_VALUE).append("=\"").append(this.description).append("\"");
	//
	// sqlStmt.append(" where ").append(FIELD_NAME_UNIQUE_IDENTIFIER).append("=").append(this.uniqueIdentifier);
	// return sqlStmt.toString();
	// }

	/**
	 * sets the description for this type.
	 */
	public void setDescription(final String desc) {
		this.description = desc;
	}

	/**
	 * sets the name of this type.
	 */
	public void setName(final String nme) {
		this.name = nme;
	}

	/**
	 * return a string representation of this type.
	 */
	@Override
	public String toString() {
		final String meStr = this.name
				+ (this.description.length() > 0 ? ":" + this.description : "");
		return meStr;
	}
}
