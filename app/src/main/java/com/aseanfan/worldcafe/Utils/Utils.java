package com.aseanfan.worldcafe.Utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Base64;
import android.view.Display;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class Utils {

    public static String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        return encodedImage;
    }

    public static String[] compressFormat(String pathName, Activity context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        Bitmap myBitmap = BitmapFactory.decodeFile(pathName, options);
        Display display = context.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Bitmap scaled = null;

        if (myBitmap.getWidth() > width || myBitmap.getHeight() > height) {

            if (myBitmap.getWidth() > myBitmap.getHeight()) {
                float scaleHt = (float) width / myBitmap.getWidth();
                scaled = Bitmap.createScaledBitmap(myBitmap, width, (int) (myBitmap.getHeight() * scaleHt), true);
            } else {
                float scaleHt = (float) height / myBitmap.getHeight();
                scaled = Bitmap.createScaledBitmap(myBitmap, (int) (myBitmap.getWidth() * scaleHt), height, true);
            }
        } else {
            scaled = myBitmap;
        }
        String extenfile = getExtentfile(pathName);

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        switch (extenfile) {
            case "jpg":
            case "jpeg":
            case "gif":
                scaled.compress(Bitmap.CompressFormat.JPEG, 90, bao);
                extenfile = "jpg";
                break;

            case "png":
                scaled.compress(Bitmap.CompressFormat.PNG, 90, bao);
                extenfile = "png";
                break;
        }
        byte[] ba = bao.toByteArray();
        String ba1 = Base64.encodeToString(ba, Base64.NO_WRAP);

        return new String[] { ba1, extenfile };
    }

    private static final String[] okFileExtensions = new String[] { "jpg", "png", "gif", "jpeg" };

    public static String getExtentfile(String path) {
        File f = new File(path);

        for (String extension : okFileExtensions) {
            if (f.getName().toLowerCase().endsWith(extension)) {
                return extension;
            }
        }
        return "jpg";
    }

}
