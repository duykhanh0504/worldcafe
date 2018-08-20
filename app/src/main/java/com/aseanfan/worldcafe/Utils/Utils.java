package com.aseanfan.worldcafe.Utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Display;


import com.aseanfan.worldcafe.App.App;
import com.aseanfan.worldcafe.Model.AreaModel;
import com.aseanfan.worldcafe.Model.CityModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class Utils {


    public static List<AreaModel> initDefaultCountry()
    {

        List<AreaModel> listarea = new ArrayList<>();

        listarea.clear();
        List<CityModel> listcity = new ArrayList();
        listcity.add(new CityModel(1,"HCM"));
        listcity.add(new CityModel(2,"HA NOI"));
        listcity.add(new CityModel(3,"DA NANG"));

        listarea.add(new AreaModel(1,"Viet Nam",listcity));
        List<CityModel> listcity1 = new ArrayList();
        listcity1.add(new CityModel(4,"Tokyo"));
        listcity1.add(new CityModel(5,"Osaka"));
        listarea.add(new AreaModel(2,"Japan",listcity1));
        return listarea;

    }

    public static String ConvertDate(String s)
    {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm",Locale.US);
        DateFormat targetFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
        String formattedDate = null;
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(s);
            formattedDate = targetFormat.format(convertedDate);
        } catch (ParseException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        return formattedDate;
    }

    public static String currencyFormat(Long amount) {
        NumberFormat formatter = new DecimalFormat("#,###");
        Long myNumber = amount;
        String formattedNumber = formatter.format(myNumber);
        return formattedNumber;
    }

    public static String encodeStringUrl(String url) {
        String encodedUrl =null;
        try {
            encodedUrl = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return encodedUrl;
        }
        return encodedUrl;
    }

    public static String decodeStringUrl(String encodedUrl) {
        String decodedUrl =null;
        try {
            decodedUrl = URLDecoder.decode(encodedUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return decodedUrl;
        }
        return decodedUrl;
    }


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

    public static int convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int)(dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static int getwidthScreen(Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return metrics.widthPixels;
    }

    public static int getheightScreen(Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return metrics.heightPixels;
    }



    public static int convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int dp = (int)(px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public static String stringConvertDateString(String date) {
        String outputText = "";
        if (date.contains("T")) {
            DateFormat inputFormat = null;
            inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);

            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

            Date parsed = null;

            try {
                parsed = inputFormat.parse(date);
                outputText = outputFormat.format(parsed);
            } catch (ParseException e) {

            }

        } else {
            SimpleDateFormat input = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
            SimpleDateFormat output = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            try {
                Date day = null;
                String str = null;
                day = input.parse(date);
                outputText = output.format(day);

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        return outputText;
    }

    public static String convertStringToLocalTime(String time) {

        DateFormat readFormat = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss Z", Locale.ENGLISH);

        DateFormat writeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");// ("HH:mm:ss
        // aaa
        // dd/MM/yyy");
        TimeZone utcZone = TimeZone.getDefault();
        writeFormat.setTimeZone(utcZone);
        Date date = null;
        try {
            date = readFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = "";
        if (date != null) {
            formattedDate = writeFormat.format(date);
        }
        return formattedDate;

    }


}
