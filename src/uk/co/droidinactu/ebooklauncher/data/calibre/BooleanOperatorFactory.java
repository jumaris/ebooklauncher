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

public final class BooleanOperatorFactory {

    public static BooleanOperator getBooleanOperator(final String str) {
        if (str.equalsIgnoreCase("AND")) {
            return BooleanOperator.AND;
        }
        if (str.equalsIgnoreCase("ANDNOT")) {
            return BooleanOperator.ANDNOT;
        }
        if (str.equalsIgnoreCase("ORNOT")) {
            return BooleanOperator.ORNOT;
        }
        return BooleanOperator.OR;
    }

    public static boolean isBooleanOperator(final String str) {
        if (str.equalsIgnoreCase("AND")) {
            return true;
        }
        if (str.equalsIgnoreCase("ANDNOT")) {
            return true;
        }
        if (str.equalsIgnoreCase("ORNOT")) {
            return true;
        }
        if (str.equalsIgnoreCase("OR")) {
            return true;
        }
        return false;
    }

}
