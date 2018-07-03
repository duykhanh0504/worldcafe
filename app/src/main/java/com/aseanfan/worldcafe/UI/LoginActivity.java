package com.aseanfan.worldcafe.UI;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
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
import com.aseanfan.worldcafe.Utils.Utils;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

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
    private TextView _forgotLink;

    private Button _signupButton;
    private LoginButton _signupFacebookButton;
    private EditText _passwordTextSignup;
    private EditText _emailTextSignup;

    private ImageView _avatarimage;

    private EditText _mobileupdate;
    private EditText _emailupdate;
    private EditText _usernameupdate;
    private Button _update;

    private Socket mSocket;
    CallbackManager callbackManager;

    Uri selectedAvatar = null;

    private final int FACEBOOK_LOGIN = 64206;

    private  boolean USING_FACEBOOK = false;

    private int LOGIN_FACEBOOK =1;
    private int LOGIN_NORMAL =2;

    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        App app = (App) getApplication();
        mSocket = app.getSocket();

        LoginManager.getInstance().logOut();


        InitView();


        _forgotLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();*/
            }
        });
        int typePage = getIntent().getIntExtra("type",0);
        showPage(typePage);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = _emailTextSignup.getText().toString();
                password = _passwordTextSignup.getText().toString();
                register(email,password);
            }
        });


        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                email = _emailText.getText().toString();
                password = _passwordText.getText().toString();
                login(email,password,LOGIN_NORMAL);
            }
        });

        _update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobilephone = _mobileupdate.getText().toString();
                update(mobilephone);
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

        facebookManagerCallback();

    }

    public void facebookManagerCallback()
    {
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
                                            USING_FACEBOOK = true;
                                            UserModel u =  new UserModel();
                                            u.setEmail(object.getString("email"));
                                            u.setUsername(object.getString("name"));
                                            u.setAvarta( object.getJSONObject("picture").getJSONObject("data").getString("url"));
                                            AccountController.getInstance().SetAccount(u);
                                            register(u.getEmail(),null);

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


    void InitView()
    {
        _viewfliper = (ViewFlipper)this.findViewById(R.id.viewFlipper);

        View viewLogin = this.findViewById(R.id.flipViewLogin);
        View viewLoginUpdate =  this.findViewById(R.id.flipViewUpdateLogin);
        View viewRegister =  this.findViewById(R.id.flipViewRegister);

        _loginButton = (Button)viewLogin.findViewById(R.id.btn_login);
        _loginFacebookButton = (LoginButton)viewLogin.findViewById(R.id.btn_facebook_login);
        _passwordText = (EditText)viewLogin.findViewById(R.id.input_password);
        _emailText = (EditText)viewLogin.findViewById(R.id.input_email);
        _forgotLink = (TextView)viewLogin.findViewById(R.id.link_forgotpass);

        _signupButton = (Button)viewRegister.findViewById(R.id.btn_Signup);
        _signupFacebookButton = (LoginButton)viewRegister.findViewById(R.id.btn_facebook_login);
        _passwordTextSignup = (EditText)viewRegister.findViewById(R.id.input_password);
        _emailTextSignup = (EditText)viewRegister.findViewById(R.id.input_email);

        _mobileupdate = (EditText)viewLoginUpdate.findViewById(R.id.input_mobile_update);
        _emailupdate = (EditText)viewLoginUpdate.findViewById(R.id.input_email_update);
        _usernameupdate = (EditText)viewLoginUpdate.findViewById(R.id.input_username_update);
        _update = (Button) viewLoginUpdate.findViewById(R.id.btn_update);

        _avatarimage = (ImageView) viewLoginUpdate.findViewById(R.id.imageAvatar);

    }

    private void showPage(int Type)
    {

        _viewfliper.setDisplayedChild(Type);
    }


    private void showLoginUpdate()
    {
        if(USING_FACEBOOK  ==true) {
            UserModel u = AccountController.getInstance().getAccount();
            if (u.getAvarta() != null) {
                RequestOptions requestOptions = new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .circleCropTransform();

                Glide.with(this)
                        .load(u.getAvarta()).apply(requestOptions)
                        .into(_avatarimage);
            }
            _emailupdate.setEnabled(false);
            _usernameupdate.setEnabled(false);
            _emailupdate.setText(u.getEmail());
            _usernameupdate.setText(u.getUsername());

        }
        _viewfliper.setDisplayedChild(1);

    }


    private void selectImage() {
        try {
            PackageManager pm = getPackageManager();

            if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, 0 );
            }
            Intent intent = CropImage.activity(null)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .getIntent(this);
            startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
              /*  final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
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
                builder.show();*/

        } catch (Exception e) {
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void register(final String email , final String password) {

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Create account...");
        progressDialog.show();
        JsonObject dataJson = new JsonObject();
      //  _signupButton.setEnabled(false);
        if(USING_FACEBOOK ==true) {
            Gson gson = new Gson();
            JsonParser jsonParser = new JsonParser();
             dataJson = (JsonObject) jsonParser.parse(gson.toJson(AccountController.getInstance().getAccount()));
        }
        else
        {
            dataJson.addProperty("email",email);
        }
        dataJson.addProperty("password", password);
        if(USING_FACEBOOK == true)
        {
            dataJson.addProperty("username",AccountController.getInstance().getAccount().getUsername());
        }

        RestAPI.PostDataMaster(getApplicationContext(), dataJson, RestAPI.POST_SIGNUP, new RestAPI.RestAPIListenner() {

            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }
                    JsonObject jsons = (new JsonParser()).parse(s).getAsJsonObject();
                    int statuscode = jsons.get("status").getAsInt();
                        progressDialog.dismiss();
                        if (statuscode == RestAPI.STATUS_SUCCESS) {
                            AccountController.getInstance().getAccount().setId(jsons.get("result").getAsLong());
                            showLoginUpdate();
                        } else if (statuscode == RestAPI.STATUS_ACCOUNTESIXT) {
                            if (USING_FACEBOOK == true) {
                                login(email, password, LOGIN_FACEBOOK);
                            } else {
                                Toast.makeText(LoginActivity.this, "Account exist!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                } catch (Exception ex) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    ex.printStackTrace();
                }

            }
        });
    }

    public void update(String mobilephone) {

       // _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Updating...");
        progressDialog.show();

        final UserModel u = AccountController.getInstance().getAccount();
        u.setPhonenumber(mobilephone);

        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("account_id",u.getId());
        dataJson.addProperty("phonenumber",mobilephone);
        if(selectedAvatar!=null)
        {
            String[] bb = Utils.compressFormat(selectedAvatar.getPath(), this);
            String base64 = bb[0];
            String imagename = System.currentTimeMillis() + "." + bb[1];
            dataJson.addProperty("base64",base64);
            dataJson.addProperty("image",imagename);
            dataJson.addProperty("type","image/");
        }


        RestAPI.PostDataMaster(getApplicationContext(), dataJson, RestAPI.POST_UPDATEUSER, new RestAPI.RestAPIListenner() {

            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }

                  //  String email = _emailText.getText().toString();
                 //   String password = _passwordText.getText().toString();
                    if(USING_FACEBOOK==true) {
                        login(AccountController.getInstance().getAccount().getEmail(), password,LOGIN_FACEBOOK);
                    }
                    else
                    {
                        login(email, password,LOGIN_NORMAL);
                    }
                    progressDialog.dismiss();

                } catch (Exception ex) {
                    progressDialog.dismiss();
                    ex.printStackTrace();
                }

            }
        });
        // TODO: Implement your own authentication logic here.

    }

    public void login( String email ,String password,int type) {

      /*  if (!validate()) {
            onLoginFailed();
            return;
        }*/
      if(type == LOGIN_NORMAL)
      {
          if (password==null || password.isEmpty())
          {
              Toast.makeText(LoginActivity.this, "Password can not empty", Toast.LENGTH_SHORT).show();
              return;
          }
      }

        /*user.setEmail(jsonObject.get("email").getAsString());
        user.setPhonenumber(jsonObject.get("phonenumber").getAsString());
        user.setUsername(jsonObject.get("username").getAsString());*/
       // _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

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
                        progressDialog.dismiss();
                        Toast.makeText(getBaseContext(),"Failed to connect to server, please try again", Toast.LENGTH_LONG).show();
                        return;
                    }
                    JsonObject jsons = (new JsonParser()).parse(s).getAsJsonObject();
                    int statuscode = jsons.get("status").getAsInt();
                    if (statuscode == RestAPI.STATUS_SUCCESS) {
                        JsonObject jsonObject = jsons.getAsJsonArray("result").get(0).getAsJsonObject();
                        Gson gson = new Gson();
                        final UserModel u = gson.fromJson(jsonObject, UserModel.class);
                        DBHelper.getInstance(getApplicationContext()).insertPerson(u.getId(), u.getUsername(), u.getEmail(), u.getPhonenumber(), u.getAvarta());
                        AccountController.getInstance().SetAccount(u);

                        JsonObject dataJson = new JsonObject();
                        dataJson.addProperty("account_id", u.getId());

                        RestAPI.PostDataMaster(getApplicationContext(), dataJson, RestAPI.POST_UPDATESOCKET, new RestAPI.RestAPIListenner() {
                            @Override
                            public void OnComplete(int httpCode, String error, String s) {
                                if (!RestAPI.checkHttpCode(httpCode)) {
                                    //AppFuncs.alert(getApplicationContext(),s,true);

                                    return;
                                }

                                Store.putBooleanData(LoginActivity.this, Store.LOGGED, true);
                                progressDialog.dismiss();
                          /*  if(u.getPhonenumber()==null || u.getPhonenumber().isEmpty()) {
                                if (LoginActivity.this.getCurrentFocus() != null) {6
                                    InputMethodManager imm = (InputMethodManager) LoginActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(LoginActivity.this.getCurrentFocus().getWindowToken(), 0);
                                }
                                showLoginUpdate();
                            }
                            else*/
                                {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                        });

                        //  UserModel u = gson.fromJson(jsonObject.getAsJsonArray("result").get(0).getAsJsonObject(), UserModel.class);
                        //  UserModel u  =  RealmManager.open().createObjectFromJson(UserModel.class, JSONObject);

                        // if(jsons.get("status").getAsInt() == 200)
                        // {

                        //}

                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(getBaseContext(), getString(RestAPI.checkStatusCode(statuscode)), Toast.LENGTH_LONG).show();
                    }
                    } catch(Exception ex){
                        progressDialog.dismiss();
                        Toast.makeText(getBaseContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
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
            case FACEBOOK_LOGIN:
                if(resultCode == RESULT_OK){
                    callbackManager.onActivityResult(requestCode, resultCode, data);
                }
                break;
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    selectedAvatar = result.getUri();
                    Glide.with(this).load( selectedAvatar).apply(RequestOptions.circleCropTransform()).into( _avatarimage);
                }
                break;
        }
    }
}
