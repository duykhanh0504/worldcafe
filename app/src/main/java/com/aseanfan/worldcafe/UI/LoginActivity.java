package com.aseanfan.worldcafe.UI;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.App.App;
import com.aseanfan.worldcafe.Helper.DBHelper;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.UserModel;
import com.aseanfan.worldcafe.Provider.Store;
import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.Arrays;

import io.socket.client.Socket;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_SIGNUP = 0;

    private ViewFlipper _viewfliper;
    private Button _loginButton;
    private LoginButton _loginFacebookButton;
    private EditText _passwordText;
    private EditText _emailText;
    private TextView _signupLink;

    private ImageView _avatarimage;

    private EditText _mobileupdate;
    private Button _update;

    private Socket mSocket;
    CallbackManager callbackManager;

    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private final int FACEBOOK_LOGIN = 64206;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        App app = (App) getApplication();
        mSocket = app.getSocket();

        _viewfliper = (ViewFlipper)this.findViewById(R.id.viewFlipper);

        View viewLogin = this.findViewById(R.id.flipViewLogin);
        View viewLoginUpdate =  this.findViewById(R.id.flipViewUpdateLogin);
        _loginButton = (Button)viewLogin.findViewById(R.id.btn_login);
        _loginFacebookButton = (LoginButton)viewLogin.findViewById(R.id.btn_facebook_login);
        _passwordText = (EditText)viewLogin.findViewById(R.id.input_password);
        _emailText = (EditText)viewLogin.findViewById(R.id.input_email);
        _signupLink = (TextView)viewLogin.findViewById(R.id.link_signup);

        _mobileupdate = (EditText)viewLoginUpdate.findViewById(R.id.input_mobile_update);
        _update = (Button) viewLoginUpdate.findViewById(R.id.btn_update);

        _avatarimage = (ImageView) viewLoginUpdate.findViewById(R.id.imageAvatar);

        showLogin();

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
            }
        });

        _update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });

        callbackManager = CallbackManager.Factory.create();
        _loginFacebookButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday"));

        _avatarimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        String  i = "";
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
                                            u.setAvarta( object.getJSONObject("picture").getJSONObject("data").getString("url"));
                                            AccountController.getInstance().SetAccount(u);
                                            showLoginUpdate();
                                        } catch (JSONException e) {
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

    private void showLogin()
    {

        _viewfliper.setDisplayedChild(0);
    }

    private void showLoginUpdate()
    {

        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL);


        Glide.with(this)
                .load(AccountController.getInstance().getAccount().getAvarta()).apply(requestOptions)
                .into(_avatarimage);

        _viewfliper.setDisplayedChild(1);

    }


    private void selectImage() {
        try {
            PackageManager pm = getPackageManager();

            if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, 0 );
            }

                final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

        } catch (Exception e) {
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void update() {

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Updating...");
        progressDialog.show();

        String mobilephone = _mobileupdate.getText().toString();

        final UserModel u = AccountController.getInstance().getAccount();
        u.setPhonenumber(mobilephone);

        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("account_id",u.getId());
        dataJson.addProperty("phonenumber",mobilephone);


        RestAPI.PostDataMaster(getApplicationContext(), dataJson, RestAPI.POST_UPDATEUSER, new RestAPI.RestAPIListenner() {

            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }

                    Store.putBooleanData(LoginActivity.this,Store.LOGGED,true);
                    progressDialog.dismiss();

                    Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();


                } catch (Exception ex) {

                    ex.printStackTrace();
                }

            }
        });
        // TODO: Implement your own authentication logic here.

    }


    public void login() {

      /*  if (!validate()) {
            onLoginFailed();
            return;
        }*/

        /*user.setEmail(jsonObject.get("email").getAsString());
        user.setPhonenumber(jsonObject.get("phonenumber").getAsString());
        user.setUsername(jsonObject.get("username").getAsString());*/
        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

       // Gson gson = new Gson();
       // JsonObject dataJson = gson.toJsonTree(null).getAsJsonObject();
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("password",password);
        dataJson.addProperty("email",email);

        RestAPI.PostDataMaster(getApplicationContext(), dataJson, RestAPI.POST_LOGIN, new RestAPI.RestAPIListenner() {

            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }
                    JsonObject jsonObject = (new JsonParser()).parse(s).getAsJsonObject().getAsJsonArray("result").get(0).getAsJsonObject();
                    Gson gson = new Gson();
                    final UserModel u =  gson.fromJson(jsonObject, UserModel.class);
                    DBHelper.getInstance(getApplicationContext()).insertPerson(u.getId(),u.getUsername(),u.getEmail(),u.getPhonenumber(),u.getAvarta());
                    AccountController.getInstance().SetAccount(u);

                    JsonObject dataJson = new JsonObject();
                    dataJson.addProperty("account_id",u.getId());

                    RestAPI.PostDataMaster(getApplicationContext(), dataJson, RestAPI.POST_UPDATESOCKET, new RestAPI.RestAPIListenner() {
                        @Override
                        public void OnComplete(int httpCode, String error, String s) {
                            if (!RestAPI.checkHttpCode(httpCode)) {
                                //AppFuncs.alert(getApplicationContext(),s,true);

                                return;
                            }

                            Store.putBooleanData(LoginActivity.this,Store.LOGGED,true);
                            progressDialog.dismiss();
                            if(u.getPhonenumber()==null || u.getPhonenumber().isEmpty()) {
                                if (LoginActivity.this.getCurrentFocus() != null) {
                                    InputMethodManager imm = (InputMethodManager) LoginActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(LoginActivity.this.getCurrentFocus().getWindowToken(), 0);
                                }
                                showLoginUpdate();
                            }
                            else
                            {
                                Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }

                        }
                    });

                    //  UserModel u = gson.fromJson(jsonObject.getAsJsonArray("result").get(0).getAsJsonObject(), UserModel.class);
                  //  UserModel u  =  RealmManager.open().createObjectFromJson(UserModel.class, JSONObject);

                  //  if(jsonObject.get("status").getAsInt() == 200)
                   // {

                    //}


                } catch (Exception ex) {

                    ex.printStackTrace();
                }

            }
        });
        // TODO: Implement your own authentication logic here.

    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }


    private void alertDisplayer(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                       // Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                      //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        //startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case PICK_IMAGE_CAMERA:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    Glide.with(this).load( selectedImage).into( _avatarimage);

                }

                break;
            case PICK_IMAGE_GALLERY:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    Glide.with(this).load( selectedImage).into( _avatarimage);
                }
                break;
            case FACEBOOK_LOGIN:
                if(resultCode == RESULT_OK){
                    callbackManager.onActivityResult(requestCode, resultCode, data);
                }
                break;
        }
    }
}
