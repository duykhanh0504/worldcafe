package com.aseanfan.worldcafe.Helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.aseanfan.worldcafe.Provider.Store;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.builder.Builders;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class RestAPI {

    private static int TIME_OUT = 12000;

    public static String root_url = "http://10.0.2.2:8080";
    public static String root_url_dev = "";

    public static String POST_SIGNUP = root_url + "/api/signup";// identifier
    public static String POST_LOGIN = root_url + "/api/login";

    private static int POST_METHOD = 0;
    private static int DELETE_METHOD = 1;
    private static int GET_METHOD = 2;

    //public static int NUMBER_REQUEST_TOKEN =3;


   /* public static String loginUrl(String user, String pass) {
        return String.format(POST_SIGNUP, user, pass);
    }*/



    public static void loadImageUrl(Context context, ImageView imageView, String reference) {
        Ion.with(context).load("").withBitmap().intoImageView(imageView);
    }

    public static Future<Bitmap> getBitmapFromUrl(Context context, String reference) {
        return Ion.with(context).load(""+reference).withBitmap().asBitmap();
    }

    public interface RestAPIListenner {
        public void OnComplete(int httpCode, String error, String s);
        //public void OnTokenInvalid(String url);
    }


    public static JsonElement getData(JsonObject jsonObject) {
        return jsonObject.get("data");
    }

    public static boolean checkAuthenticationCode(int code) {
        return ((code == 401)) ? true : false;
    }

    public  static boolean checkExpiredtoken(String s)
    {
        JSONObject mainObject = null;
        try {
            mainObject = new JSONObject(s);
            String  errorstatus = mainObject.getString("error");
            if(errorstatus.equals("invalid_token")) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean checkHttpCode(int code) {
        return ((code == 200) || (code == 201)) ? true : false;
    }

    public static TrustManager[] trustAllCerts = new X509TrustManager[]{new X509TrustManager() {

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType) {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }};


    public static javax.net.ssl.SSLSocketFactory getSSLSocketFactory() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext inst = SSLContext.getInstance("TLS");
        inst.init(null, trustAllCerts, null);
        return inst.getSocketFactory();
    }

    public static SSLContext getSSLContextInst() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext inst = SSLContext.getInstance("TLS");
        inst.init(null, trustAllCerts, null);
        return inst;
    }

    public static void initIon(Context context) {
        try {
            Ion.getDefault(context).configure().createSSLContext("TLS");
            Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setSSLContext(getSSLContextInst());
            Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setTrustManagers(trustAllCerts);
            Ion.getDefault(context).getHttpClient().getSSLSocketMiddleware().setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
 /*   private static boolean checkOauth(String url) {
        String header = Store.getStringData(context, WpkToken.STORE_TOKEN_TYPE) + " " + Store.getStringData(context, WpkToken.STORE_ACCESS_TOKEN);
        if (url.contains("oauth/token?grant_type=password") || url.startsWith(POST_REGISTER)) {
            header = "Basic d3JhcHB5X2FwcDp3cmFwcHlfYXBw";
        }
        return header;
    }
*/

    /*private static String getHeaderHttps(Context context, String url) {
        String header = "Basic d3JhcHB5X2FwcDp3cmFwcHlfYXBw";//Store.getStringData(context, WpkToken.STORE_TOKEN_TYPE) + " " + Store.getStringData(context, WpkToken.STORE_ACCESS_TOKEN);
       /* if (url.contains("oauth/token?grant_type=password") || url.startsWith(POST_REGISTER)) {
            header = "Basic d3JhcHB5X2FwcDp3cmFwcHlfYXBw";
        }*/
       // return header;
    //}

    public static Builders.Any.B getIon(Context context, String url, String method) {
        return Ion.with(context).load(method,url).setTimeout(TIME_OUT);
    }

    public static Future<Response<String>> apiGET(Context context, String url) {
        return getIon(context,url,"GET").asString().withResponse();
    }

    public static Future<Response<String>> apiPOST(Context context, String url, JsonObject jsonObject) {
        return getIon(context,url,"POST").setJsonObjectBody((jsonObject==null)? new JsonObject() : jsonObject).asString().withResponse();
    }

    public static Future<Response<String>> apiPUT(Context context, String url, JsonObject jsonObject) {
        return getIon(context,url,"PUT").setJsonObjectBody((jsonObject==null)? new JsonObject() : jsonObject).asString().withResponse();
    }

    public static Future<Response<String>> apiDELETE(Context context, String url, JsonObject jsonObject) {
        return getIon(context,url,"DELETE").setJsonObjectBody((jsonObject==null)? new JsonObject() : jsonObject).asString().withResponse();
    }

    public static void PostDataMaster(final Context context, final JsonObject jsonObject, final String url, final RestAPIListenner listenner) {
        apiPOST(context,url,jsonObject).setCallback(new FutureCallback<Response<String>>() {
            @Override
            public void onCompleted(Exception e, Response<String> result) {
                try {

                    listenner.OnComplete((result != null) ? result.getHeaders().code() : 0, (e != null) ? e.getLocalizedMessage() : null, (result != null) ? result.getResult() : null);

                }catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
    }

    public static void DeleteDataMaster(final Context context, final JsonObject jsonObject, final String url, final RestAPIListenner listenner) {
        apiDELETE(context, url, jsonObject).setCallback(new FutureCallback<Response<String>>() {
            @Override
            public void onCompleted(Exception e, Response<String> result) {
                try {

                        listenner.OnComplete((result != null) ? result.getHeaders().code() : 0, (e != null) ? e.getLocalizedMessage() : null, (result != null) ? result.getResult() : null);

                }catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
    }

    public static void GetDataMaster(final Context context, final String url, final RestAPIListenner listenner) {
        apiGET(context,url).setCallback(new FutureCallback<Response<String>>() {
            @Override
            public void onCompleted(Exception e, Response<String> result) {

                    listenner.OnComplete((result != null && result.getHeaders() != null) ? result.getHeaders().code() : 0, (e != null) ? e.getLocalizedMessage() : null, (result != null) ? result.getResult() : null);

            }
        });
    }

//    public static void UploadFile(Context context, String url, String type, File file, final RestAPIListenner listenner) {
//        String header = getHeaderHttps(context,url);
//        Ion.with(context)
//                .load(url)
//                .addHeader("Authorization",header)
//                .setMultipartParameter("type", type)
//                .setMultipartFile("file","multipart/form-data",file)
//                .asString().withResponse()
//                .setCallback(new FutureCallback<Response<String>>() {
//                    @Override
//                    public void onCompleted(Exception e, Response<String> result) {
//                        listenner.OnComplete((result != null && result.getHeaders() != null) ? result.getHeaders().code() : 0, (e != null) ? e.getLocalizedMessage() : null, (result != null) ? result.getResult() : null);
//                    }
//                });
//    }

    public static Future<Response<String>> uploadFile(Context context,File file, String type) {
        return Ion.with(context)
                .load("")
               // .addHeader("Authorization",header)
                .setMultipartParameter("type", type)
                .setMultipartFile("file","multipart/form-data",file)
                .asString().withResponse();
    }


    public static HashMap<String, String> jsonToMap(JsonObject object) {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        return gson.fromJson(object, type);
    }



}
