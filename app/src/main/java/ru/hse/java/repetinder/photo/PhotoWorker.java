package ru.hse.java.repetinder.photo;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * PhotoWorker is class that work with images stored from phone
 * and standardizes images
 *
 * Standard: 512 x 512 pix
 */
public class PhotoWorker {

    public final static int WIDTH = 512;
    public final static int HEIGHT = 512;

    /**
     * crop bitmap to square
     * @param originalBitmap
     * @return squared bitmap
     */
    public static Bitmap cropBitmap(Bitmap originalBitmap) {
        Log.v("Bitmap", "Cropping bitmap");

        int originalHeight = originalBitmap.getHeight();
        int originalWidth = originalBitmap.getWidth();

        int squaredSize = Math.min(originalHeight, originalWidth);

        int fromHeight = (originalHeight == squaredSize
                ? 0 : getFromValue(originalHeight, squaredSize));
        int fromWidth = (originalWidth == squaredSize
                ? 0 : getFromValue(originalWidth, squaredSize));
        return Bitmap.createBitmap(originalBitmap, fromWidth, fromHeight, squaredSize, squaredSize);
    }

    private static int getFromValue(int fullSize, int cropSize) {
        return (fullSize - cropSize) / 2;
    }

    /**
     *
     * @param originalBitmap
     * @return scaled bitmap to standard bitmap
     */
    public static Bitmap createResizedBitmap(Bitmap originalBitmap) {
        Log.v("Bitmap", "Resizing bitmap");
        return Bitmap.createScaledBitmap(originalBitmap, WIDTH, HEIGHT, true);
    }
}
