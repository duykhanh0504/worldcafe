package com.aseanfan.worldcafe.UI;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.DBHelper;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.UserModel;
import com.aseanfan.worldcafe.Provider.Store;
import com.aseanfan.worldcafe.Service.MyFirebaseInstanceIDService;
import com.aseanfan.worldcafe.Service.SocketService;
import com.aseanfan.worldcafe.UI.Component.ViewDialog;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.Utils.Utils;
import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.util.ByteBufferUtil;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IntroActivity extends AppCompatActivity {

    @BindView(R.id.intro_view_pager)
    ViewPager viewPager;

    @BindView(R.id.bottom_pages)
    ViewGroup bottomPages;

    @BindView(R.id.nextpage)
    ImageButton nextpage;

    private CallbackManager mCallbackManager;

  //  @BindView(R.id.content_login)
   // LinearLayout content_login;


    private int lastPage = 0;
    private boolean justCreated = false;
    private boolean startPressed = false;
    private String[] images;
    private int[] listtitle;


    public void login(String email , final int type) {



        final ProgressDialog progressDialog = new ProgressDialog(IntroActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        // Gson gson = new Gson();
        // JsonObject dataJson = gson.toJsonTree(null).getAsJsonObject();
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("email",email);
        dataJson.addProperty("device","android");
        dataJson.addProperty("login_type",type);

        RestAPI.PostDataMaster(getApplicationContext(), dataJson, RestAPI.POST_LOGIN, new RestAPI.RestAPIListenner() {

            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);
                        progressDialog.dismiss();
                       // dialog.showDialogCancel( IntroActivity.this,getResources().getString(R.string.can_not_connect_server) );
                        // Toast.makeText(getBaseContext(),"Failed to connect to server, please try again", Toast.LENGTH_LONG).show();
                        return;
                    }
                    JsonObject jsons = (new JsonParser()).parse(s).getAsJsonObject();
                    int statuscode = jsons.get("status").getAsInt();
                    if(statuscode ==2)
                    {
                     //   dialog.showDialogCancel( IntroActivity.this,jsons.get("message").getAsString() );
                        return;
                    }
                    if (statuscode == RestAPI.STATUS_SUCCESS) {
                        DBHelper.getInstance(getApplicationContext()).CreateMessageTable();
                        DBHelper.getInstance(getApplicationContext()).CreateNotifyTable();
                        // DBHelper.getInstance(getApplicationContext()).CreateMessageTable();
                        JsonObject jsonObject = jsons.getAsJsonArray("result").get(0).getAsJsonObject();
                        Gson gson = new Gson();
                        final UserModel u = gson.fromJson(jsonObject, UserModel.class);
                        Store.putStringData(IntroActivity.this, Store.ACCESSTOKEN, jsons.get("access_token").getAsString());
                        AccountController.getInstance().SetAccount(u);

                        DBHelper.getInstance(getApplicationContext()).insertPerson(u);
                        startService(new Intent(getApplicationContext(), SocketService.class));
                        startService(new Intent(getApplicationContext(), MyFirebaseInstanceIDService.class));

                        Store.putBooleanData(IntroActivity.this, Store.LOGGED, true);

                        {
                            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                            intent.putExtra("fromlogin", 1);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }


                    }
                    else
                    {
                        progressDialog.dismiss();

                    }
                } catch(Exception ex){
                    progressDialog.dismiss();
                    ex.printStackTrace();
                }
                finally {
                    progressDialog.dismiss();
                }


            }
        });
        // TODO: Implement your own authentication logic here.

    }

    public void register(String email) {

        final ProgressDialog progressDialog = new ProgressDialog(IntroActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Create account...");
        progressDialog.show();
        JsonObject dataJson = new JsonObject();
        String url;
        //  _signupButton.setEnabled(false);

            Gson gson = new Gson();
            JsonParser jsonParser = new JsonParser();
            dataJson = (JsonObject) jsonParser.parse(gson.toJson(AccountController.getInstance().getAccount()));
            dataJson.remove("v_followed");
            dataJson.remove("v_follower");
            dataJson.remove("v_total_like");
            url = RestAPI.POST_SIGNUPBYFACEBOOK;


        dataJson.addProperty("email",email);
        dataJson.addProperty("username",AccountController.getInstance().getAccount().getUsername());



        RestAPI.PostDataMaster(getApplicationContext(), dataJson, url, new RestAPI.RestAPIListenner() {

            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);
                     //   dialog.showDialogCancel( LoginActivity.this,getResources().getString(R.string.can_not_connect_server) );
                        return;
                    }
                    JsonObject jsons = (new JsonParser()).parse(s).getAsJsonObject();
                    int statuscode = jsons.get("status").getAsInt();
                    progressDialog.dismiss();
                    if (statuscode == RestAPI.STATUS_SUCCESS) {


                        AccountController.getInstance().getAccount().setId(jsons.get("account_id").getAsLong());
                        login(  AccountController.getInstance().getAccount().getEmail(), 1);


                    } else if (statuscode == RestAPI.STATUS_ACCOUNTESIXT) {
                           /* if (USING_FACEBOOK == true) {
                                login(email, password, LOGIN_FACEBOOK);
                            } else*/ {
                         //   dialog.showDialogCancel( LoginActivity.this,"Account exist!!!" );
                            //Toast.makeText(LoginActivity.this, "Account exist!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception ex) {
                    progressDialog.dismiss();
                    ViewDialog dialog = new ViewDialog();
                 //   dialog.showDialogCancel(LoginActivity.this,ex.getMessage());
                    //Toast.makeText(LoginActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    ex.printStackTrace();
                }

            }
        });
    }

    public void facebookManagerCallback()
    {
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Store.putStringData(IntroActivity.this, Store.TOKENFACEBOOK, loginResult.getAccessToken().getToken());
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v("LoginActivity", response.toString());

                                        // Application code
                                        try {
                                            UserModel u =  new UserModel();
                                            u.setEmail(object.getString("email"));
                                            u.setUsername(object.getString("name"));
                                            u.setFacebookid(object.getString("id"));
                                            if(object.getString("gender").equals("male")) {
                                                u.setSex(Constants.MALE);
                                            }
                                            else
                                            {
                                                u.setSex(Constants.FEMALE);
                                            }

                                            u.setAvarta(String.format(getString(R.string.facebookAvatarUrl),object.getString("id")));
                                            AccountController.getInstance().SetAccount(u);
                                            register(u.getEmail());

                                        } catch (JSONException e) {
                                           // dialog.showDialogCancel( LoginActivity.this,e.getMessage() );
                                            e.printStackTrace();

                                        }

                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender, picture");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException exception) {

                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

     /*   try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.aseanfan.worldcafe.worldcafe",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = null;
                try {
                    md = MessageDigest.getInstance("SHA");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        }*/

        if(Store.getBooleanData(IntroActivity.this , Store.LOGGED) == true)
        {

            Intent intent = new Intent(IntroActivity.this , MainActivity.class);
            startActivity(intent);
            finish();
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);

        UiInit();

        mCallbackManager = CallbackManager.Factory.create();
      /*  _loginFacebookButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday"));*/


        facebookManagerCallback();

        justCreated = true;


    }

    public class FadePageTransformer implements ViewPager.PageTransformer {
        public void transformPage(View view, float position) {

            if (position <= -1.0F || position >= 1.0F) {        // [-Infinity,-1) OR (1,+Infinity]
                view.setAlpha(0.0F);
                view.setVisibility(View.GONE);
            } else if( position == 0.0F ) {     // [0]
                view.setAlpha(1.0F);
                view.setVisibility(View.VISIBLE);
            } else {

                // Position is between [-1,1]
                view.setAlpha(1.0F - Math.abs(position));
              //  view.setTranslationX(-position * (view.getWidth() / 2));
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    void UiInit()
    {
        images = new String[]{
               "intro1",
                "intro2",
                "intro3",
                "intro4"
        };
        listtitle = new int[]{
                R.string.intro1,
                R.string.intro2,
                R.string.intro3
        };

        viewPager.setAdapter(new IntroAdapter());
        viewPager.setPageMargin(0);
        viewPager.setOffscreenPageLimit(1);

        viewPager.setPageTransformer(true,null);
    }

    //@OnClick(R.id.Login)
    public void Login() {
        Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
        intent.putExtra("type", Constants.PAGE_LOGIN);
        startActivity(intent);
        finish();
    }

    //@OnClick(R.id.Signup)
    public void Signup() {
        Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
        intent.putExtra("type",Constants.PAGE_REGISTER);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.nextpage)
    public void nextpage() {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
       /* Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
        intent.putExtra("type",Constants.PAGE_REGISTER);
        startActivity(intent);
        finish();*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (justCreated) {

            viewPager.setCurrentItem(0);
            lastPage = 0;
            justCreated = false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if (mCallbackManager.onActivityResult(requestCode, resultCode, data)) {
                return;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private class IntroAdapter extends PagerAdapter {

        private  int count1 = 4;
        @Override
        public int getCount() {
            return count1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view;
            if(position ==3)
            {
                 view = View.inflate(container.getContext(), R.layout.intro_login_view, null);
                ImageView headerimage = (ImageView) view.findViewById(R.id.header_image);
                Button signupFB = (Button) view.findViewById(R.id.Signupwithfb);
                Button signup = (Button) view.findViewById(R.id.Signup);
                Button login = (Button) view.findViewById(R.id.Login);

                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Login();
                    }
                });

                signup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Signup();
                    }
                });

                signupFB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LoginManager.getInstance().logInWithReadPermissions(IntroActivity.this, Arrays.asList("public_profile", "email", "user_birthday"));
                    }
                });

                //  ImageView icon = (ImageView) view.findViewById(R.id.icon_image);

                container.addView(view, 0);

               // headerimage.setBackgroundResource(images[position]);
                try {
                    Glide.with(IntroActivity.this).load(Utils.getAssetImage(IntroActivity.this,images[position])).into(headerimage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //   title.setVisibility(View.GONE);
              //  icon.setVisibility(View.GONE);
            }
            else
            {
                 view = View.inflate(container.getContext(), R.layout.intro_view_layout, null);
                ImageView headerimage = (ImageView) view.findViewById(R.id.header_image);
                TextView title = (TextView) view.findViewById(R.id.txttitle);
                //  ImageView icon = (ImageView) view.findViewById(R.id.icon_image);

                container.addView(view, 0);

             //   headerimage.setBackgroundResource(images[position]);
                try {
                    Glide.with(IntroActivity.this).load(Utils.getAssetImage(IntroActivity.this,images[position])).into(headerimage);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                title.setVisibility(View.VISIBLE);
             //   icon.setVisibility(View.VISIBLE);
                title.setText(getResources().getString(listtitle[position]));
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            int count = bottomPages.getChildCount();
            if(position==3) {
             //   count1 = 1;
               // notifyDataSetChanged();
                bottomPages.setVisibility(View.INVISIBLE);
                nextpage.setVisibility(View.INVISIBLE);
            //    content_login.setVisibility(View.VISIBLE);
            }
            else {
                bottomPages.setVisibility(View.VISIBLE);
                nextpage.setVisibility(View.VISIBLE);
             //   content_login.setVisibility(View.INVISIBLE);
                for (int a = 0; a < count; a++) {
                    View child = bottomPages.getChildAt(a);
                    if (a == position) {
                        child.setBackgroundResource(R.drawable.circle_select);
                    } else {
                        child.setBackgroundResource(R.drawable.circle);
                    }
                }
            }

        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            if (observer != null) {
                super.unregisterDataSetObserver(observer);
            }
        }
    }
}
