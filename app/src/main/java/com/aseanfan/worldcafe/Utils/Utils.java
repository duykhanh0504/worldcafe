package com.aseanfan.worldcafe.Utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;


import com.aseanfan.worldcafe.App.App;
import com.aseanfan.worldcafe.Model.AreaModel;
import com.aseanfan.worldcafe.Model.CityModel;
import com.aseanfan.worldcafe.UI.Component.MySpannable;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formattable;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class Utils {


    public static String prefix = " VND ";
    public static final int MAX_LENGTH = 20;
    public static final int MAX_DECIMAL = 3;

    public static Bitmap createImage(int width, int height, int color, String name , Context context) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint2 = new Paint();
        paint2.setColor(color);
        canvas.drawCircle(width / 2, height / 2, 100, paint2);
       // canvas.drawRect(0F, 0F, (float) width, (float) height, paint2);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        paint.setTextScaleX(1);
        canvas.drawText("+"+name, width - convertDpToPixel(30,context), height - convertDpToPixel(15,context), paint);
        return bitmap;
    }

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

    public static List<CityModel> initDefaultCity()
    {
        List<CityModel> listcity = new ArrayList();
        listcity.add(new CityModel(1,"HCM"));
        listcity.add(new CityModel(2,"HA NOI"));
        listcity.add(new CityModel(3,"DA NANG"));
        listcity.add(new CityModel(4,"Tokyo"));
        listcity.add(new CityModel(5,"Osaka"));
        return listcity;
    }

    public static String ConvertDate(String s)
    {
        if(s==null)
            return "";
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
    public static String ConvertCurrency(String number) {
        String prezzo;
        try {

            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            DecimalFormat decimalFormat = new DecimalFormat("Rls ###,###,###,###", symbols);
             prezzo = decimalFormat.format(Integer.parseInt(number));
        }
        catch (Exception e)
        {
            prezzo = "0";
        }
        return prezzo ;
    }

    public static String formatInteger(String str) {

        try {


             BigDecimal parsed = new BigDecimal(str);
             DecimalFormat formatter =
                    new DecimalFormat("#,###", new DecimalFormatSymbols(Locale.US));

            return formatter.format(parsed);
        }catch (Exception e)
        {

        }
        return "0";
    }

    public static String formatDecimal(String str) {
        if (str.equals(".")) {
            return   ".";
        }
        BigDecimal parsed = new BigDecimal(str);
        // example pattern VND #,###.00
        DecimalFormat formatter = new DecimalFormat(  "#,###." + getDecimalPattern(str),
                new DecimalFormatSymbols(Locale.US));
        formatter.setRoundingMode(RoundingMode.DOWN);
        return formatter.format(parsed);
    }

    /**
     * It will return suitable pattern for format decimal
     * For example: 10.2 -> return 0 | 10.23 -> return 00, | 10.235 -> return 000
     */
    public static String getDecimalPattern(String str) {
        int decimalCount = str.length() - str.indexOf(".") - 1;
        StringBuilder decimalPattern = new StringBuilder();
        for (int i = 0; i < decimalCount && i < MAX_DECIMAL; i++) {
            decimalPattern.append("0");
        }
        return decimalPattern.toString();
    }

    public static boolean isStringNullOrWhiteSpace(String value) {
        final Pattern pattern = Pattern.compile("^\\w*$");    //pattern for 0 or more whitespace characters
        if (value == null || pattern.matcher(value).find()) {
            return true;
        }
        return false;
    }

    public static String ConvertDiffTime(String diff)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date1;
        try {
             date1 = simpleDateFormat.parse(diff);
             long difference = date1.getTime();
             int days = (int) (difference / (1000*60*60*24));
             int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
             int mins = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
             if(days > 0)
             {
                 return days + " days ago";
             }
            else if(hours > 0)
            {
                return hours + " hours ago";
            }
            else if(mins > 0)
            {
                return mins + " minutes ago";
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "a moment ago";

    }
/*
    public String CaculaterTime(String starttime , String endtime)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm",Locale.US);
        Date startDate = simpleDateFormat.parse(starttime);
        Date endDate = simpleDateFormat.parse(endtime);

        long difference = endDate.getTime() - startDate.getTime();
        if(difference<0)
        {
            Date dateMax = simpleDateFormat.parse("24:00");
            Date dateMin = simpleDateFormat.parse("00:00");
            difference=(dateMax.getTime() -startDate.getTime() )+(endDate.getTime()-dateMin.getTime());
        }
        int days = (int) (difference / (1000*60*60*24));
        int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
        int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
        Log.i("log_tag","Hours: "+hours+", Mins: "+min);
    }
*/
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
    public static BigDecimal parse(final String amount, final Locale locale) throws ParseException {
        final NumberFormat format = NumberFormat.getNumberInstance(locale);
        if (format instanceof DecimalFormat) {
            ((DecimalFormat) format).setParseBigDecimal(true);
        }
        return (BigDecimal) format.parse(amount.replaceAll("[^\\d.,]",""));
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

    public static Drawable getAssetImage(Context context, String filename) throws IOException {
        AssetManager assets = context.getResources().getAssets();
        InputStream buffer = new BufferedInputStream((assets.open("drawable/" + filename + ".jpg")));
        Bitmap bitmap = BitmapFactory.decodeStream(buffer);
        return new BitmapDrawable(context.getResources(), bitmap);
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
