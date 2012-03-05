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
package uk.co.droidinactu.common;

import java.io.File;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;

public final class ImageFunctions {

    private static HashMap<String, Bitmap> imageCache = new HashMap<String, Bitmap>();

    public static Bitmap getImage(final String filename) {
        Bitmap bmp = null;
        try {
            if (ImageFunctions.imageCache.containsKey(filename)) {
                bmp = ImageFunctions.imageCache.get(filename);
            } else {
                final File tmpf = new File(filename);
                if (tmpf.exists()) {
                    bmp = new BitmapDrawable(filename).getBitmap();
                    ImageFunctions.imageCache.put(filename, bmp);
                }
            }
        } catch (final Throwable e) {
            final String tmpMsg = e.getMessage();
        }
        return bmp;
    }

    /**
     * Scale a bitmap to the new size passed in.
     * 
     * @param bitmapOrg
     *            original bitmap (before scaling)
     * @param newWidth
     *            the width to which the image will be scaled
     * @param newHeight
     *            the height to which the image will be scaled
     * @return
     */
    public static Bitmap scaleImage(final Bitmap bitmapOrg, final int newWidth, final int newHeight) {

        final int width = bitmapOrg.getWidth();
        final int height = bitmapOrg.getHeight();

        // calculate the scale - in this case = 0.4f
        final float scaleWidth = (float) newWidth / width;
        final float scaleHeight = (float) newHeight / height;

        // create a matrix for the manipulation
        final Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // rotate the Bitmap
        // matrix.postRotate(45);

        // recreate the new Bitmap
        final Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width, height, matrix, true);

        // make a Drawable from Bitmap to allow to set the BitMap
        // to the ImageView, ImageButton or what ever
        // final BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);

        return resizedBitmap;
    }

}
