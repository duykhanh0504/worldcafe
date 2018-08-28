package com.aseanfan.worldcafe.UI;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.App.App;
import com.aseanfan.worldcafe.Helper.DBHelper;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.AreaModel;
import com.aseanfan.worldcafe.Model.CityModel;
import com.aseanfan.worldcafe.Model.EventModel;
import com.aseanfan.worldcafe.Model.UserModel;
import com.aseanfan.worldcafe.Provider.Store;
import com.aseanfan.worldcafe.Service.MyFirebaseInstanceIDService;
import com.aseanfan.worldcafe.Service.SocketService;
import com.aseanfan.worldcafe.UI.Adapter.SpinnerAreaAdapter;
import com.aseanfan.worldcafe.UI.Adapter.SpinnerCityAdapter;
import com.aseanfan.worldcafe.UI.Component.ViewDialog;
import com.aseanfan.worldcafe.Utils.Constants;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.aseanfan.worldcafe.Helper.cropper.CropImage;
import com.aseanfan.worldcafe.Helper.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

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
    private EditText _usernameupdate;
    private EditText _birthdayupdate;
    private RadioGroup radgroup;
    private Spinner country;
    private Spinner city;
    private Button _update;

    private EditText _inputactivecode;
    private Button _submit;

    private EditText _emailForgetPass;
    private Button _resetpassButton;

    private EditText _passcode;
    private EditText _newpass;
    private EditText _renewpass;
    private Button _changepassButton;

    private ViewDialog dialog = new ViewDialog();

    CallbackManager callbackManager;

    Uri selectedAvatar = null;

    private final int FACEBOOK_LOGIN = 64206;

    private boolean USING_FACEBOOK = false;

    private int LOGIN_FACEBOOK = 1;
    private int LOGIN_NORMAL = 0;

    private String email;
    private String password;

    LocationManager locationManager;
    Location mCurrentLocation = null;
    double latitude; // latitude
    double longitude; // longitude
    private int mYear, mMonth, mDay;

    private static  List<AreaModel> listarea = new ArrayList<>();
    private static  int countryid = 1;

    private SpinnerCityAdapter  adaptercity;
    private SpinnerAreaAdapter adaptercountry;

    public void getlocation() {
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
            else
            {
                locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                if (locationManager != null) {
                    mCurrentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (mCurrentLocation != null) {
                        latitude = mCurrentLocation.getLatitude();
                        longitude = mCurrentLocation.getLongitude();
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }


    }

   /* public void initDefaultCountry()
    {
        if(listarea ==null) {
            listarea = new ArrayList<>();
        }
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

    }*/

    void getlistcountry()
    {
       RestAPI.GetDataMaster(this, RestAPI.GET_LISTCOUNTRYANDCITY, new RestAPI.RestAPIListenner() {
           @Override
           public void OnComplete(int httpCode, String error, String s) {
               try {
                   if (!RestAPI.checkHttpCode(httpCode)) {
                       //AppFuncs.alert(getApplicationContext(),s,true);
                       dialog.showDialogCancel( LoginActivity.this,getResources().getString(R.string.can_not_connect_server) );
                       return;
                   }
                   JsonArray jsonArray = (new JsonParser()).parse(s).getAsJsonObject().getAsJsonArray("list");
                   Gson gson = new Gson();
                   java.lang.reflect.Type type = new TypeToken<List<AreaModel>>(){}.getType();
                   listarea = gson.fromJson(jsonArray, type);
            //       adaptercountry.setdata(listcountry);
                 //  getlistcity(listcountry.get(0).getid());

               }
               catch (Exception ex) {
                   dialog.showDialogCancel( LoginActivity.this,ex.getMessage());
                   ex.printStackTrace();
               }
           }
       });
    }

    void requestForgetPass( String email)
    {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Waiting...");
        progressDialog.show();
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("email",email);


        RestAPI.PostDataMaster(getApplicationContext(), dataJson, RestAPI.POST_FORGETPASS, new RestAPI.RestAPIListenner() {

            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        dialog.showDialogCancel( LoginActivity.this,getResources().getString(R.string.can_not_connect_server) );
                        return;
                    }
                    JsonObject jsons = (new JsonParser()).parse(s).getAsJsonObject();
                    int statuscode = jsons.get("status").getAsInt();
                    if(statuscode == RestAPI.STATUS_SUCCESS)
                    {
                        showPage(Constants.PAGE_CHANGEPASS);
                    }
                    else if(statuscode == 2)
                    {
                        dialog.showDialogCancel( LoginActivity.this,"Invalid Activation Code" );
                        //Toast.makeText(LoginActivity.this, "Invalid Activation Code", Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception ex) {
                    dialog.showDialogCancel( LoginActivity.this,ex.getMessage());
                  //  Toast.makeText(LoginActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    ex.printStackTrace();
                }
                finally {
                    progressDialog.dismiss();
                }

            }
        });
    }

    void changePass( String passcode , String newpass)
    {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Waiting...");
        progressDialog.show();
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("email",email);
        dataJson.addProperty("forgot_pass_code",passcode);
        dataJson.addProperty("password",newpass);


        RestAPI.PostDataMaster(getApplicationContext(), dataJson, RestAPI.POST_CHANGEFORGETPASS, new RestAPI.RestAPIListenner() {

            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        dialog.showDialogCancel( LoginActivity.this,getResources().getString(R.string.can_not_connect_server) );
                        return;
                    }
                    JsonObject jsons = (new JsonParser()).parse(s).getAsJsonObject();
                    int statuscode = jsons.get("status").getAsInt();
                    if(statuscode == RestAPI.STATUS_SUCCESS)
                    {
                        showPage(Constants.PAGE_LOGIN);
                    }
                    else if(statuscode == 2)
                    {
                        dialog.showDialogCancel( LoginActivity.this,"Invalid Activation Code" );
                        //Toast.makeText(LoginActivity.this, "Invalid Activation Code", Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception ex) {

                    dialog.showDialogCancel( LoginActivity.this,ex.getMessage() );
                    Toast.makeText(LoginActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    ex.printStackTrace();
                }
                finally {
                    progressDialog.dismiss();
                }

            }
        });
    }

    public List<CityModel> getlistcity(int countryid)
    {
        for (AreaModel temp: listarea)
        {
            if(temp.getid() == countryid)
            {
                return temp.getListCity();
            }

        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        getlocation();

       // initDefaultCountry();
        listarea = new ArrayList<>();
        listarea = Utils.initDefaultCountry();

        getlistcountry();

        LoginManager.getInstance().logOut();


        InitView();


        _forgotLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();*/
                showPage(Constants.PAGE_FORGETPASS);

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

        _submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activation(AccountController.getInstance().getAccount().getEmail(),_inputactivecode.getText().toString());
            }
        });

        radgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                changeSex(group, checkedId);
            }
        });

        _birthdayupdate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(LoginActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                _birthdayupdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                AccountController.getInstance().getAccount().setBirthday(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

         adaptercountry = new SpinnerAreaAdapter(LoginActivity.this,
                android.R.layout.simple_spinner_item,listarea);

        adaptercountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country.setAdapter(adaptercountry);
        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                countryid = listarea.get(i).getid();
                getlistcity(countryid);
                adaptercity.setdata( getlistcity(countryid));
                AccountController.getInstance().getAccount().setCountry(countryid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

         adaptercity = new SpinnerCityAdapter(LoginActivity.this,
                android.R.layout.simple_spinner_item,getlistcity(countryid));

        adaptercity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city.setAdapter(adaptercity);
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                AccountController.getInstance().getAccount().setCity(getlistcity(countryid).get(i).getid());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        _resetpassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_emailForgetPass.getText().toString().isEmpty())
                {
                    Toast.makeText(LoginActivity.this, "Email can not empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    requestForgetPass(_emailForgetPass.getText().toString());
                    email = _emailForgetPass.getText().toString();
                }
            }
        });

        _changepassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_newpass.getText().toString().isEmpty())
                {
                    dialog.showDialogCancel( LoginActivity.this,"Passcode can not empty" );
                //    Toast.makeText(LoginActivity.this, "Passcode can not empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(_newpass.getText().toString().equals(_renewpass.getText().toString()))
                    {
                        changePass(_passcode.getText().toString(),_renewpass.getText().toString());
                    }
                else
                    {
                        dialog.showDialogCancel( LoginActivity.this,"Password does not match the confirm password" );
                        //Toast.makeText(LoginActivity.this, "Password does not match the confirm password", Toast.LENGTH_SHORT).show();

                    }

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

    private boolean checkrequire(UserModel u)
    {
        if(u.getPhonenumber()!=null &&
                u.getCity()!= 0 &&
                u.getCountry()!=0 &&
                u.getUsername()!=null &&
                u.getBirthday()!=null)
            return true;
        return false;
    }

    private void changeSex(RadioGroup group, int checkedId) {
        int checkedRadioId = group.getCheckedRadioButtonId();

        if(checkedRadioId== R.id.rad_male) {
            AccountController.getInstance().getAccount().setSex(Constants.MALE);
        } else if(checkedRadioId== R.id.rad_female ) {
            AccountController.getInstance().getAccount().setSex(Constants.FEMALE);
        }
    }

    public void facebookManagerCallback()
    {
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Store.putStringData(LoginActivity.this, Store.TOKENFACEBOOK, loginResult.getAccessToken().getToken());
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
                                            u.setFacebookid(object.getString("id"));
                                            if(object.getString("gender").equals("male")) {
                                                u.setSex(Constants.MALE);
                                            }
                                            else
                                            {
                                                u.setSex(Constants.FEMALE);
                                            }
                                          //  u.setAvarta( object.getJSONObject("picture").getJSONObject("data").getString("url"));
                                            u.setAvarta(String.format(getString(R.string.facebookAvatarUrl),object.getString("id")));
                                            AccountController.getInstance().SetAccount(u);
                                            register(u.getEmail(),null);

                                        } catch (JSONException e) {
                                            dialog.showDialogCancel( LoginActivity.this,e.getMessage() );
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
        View viewActivation =  this.findViewById(R.id.flipViewActivation);
        View viewForgetPass =  this.findViewById(R.id.flipViewForgetPass);
        View viewChangePass =  this.findViewById(R.id.flipViewChangePass);

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
        _usernameupdate = (EditText)viewLoginUpdate.findViewById(R.id.input_username_update);
        _birthdayupdate = (EditText)viewLoginUpdate.findViewById(R.id.input_birthday);
        radgroup = (RadioGroup) viewLoginUpdate.findViewById(R.id.rad_sex);
        country = (Spinner)viewLoginUpdate.findViewById(R.id.spinner_country);
        city = (Spinner)viewLoginUpdate.findViewById(R.id.spinner_city);
        _update = (Button) viewLoginUpdate.findViewById(R.id.btn_update);

        _emailForgetPass = (EditText)viewForgetPass.findViewById(R.id.input_ForgetEmail);
        _resetpassButton = (Button) viewForgetPass.findViewById(R.id.btn_resetpassword);

        _passcode = (EditText)viewChangePass.findViewById(R.id.input_passcode);
        _newpass = (EditText)viewChangePass.findViewById(R.id.input_newpass);;
        _renewpass = (EditText)viewChangePass.findViewById(R.id.input_repass);;
        _changepassButton = (Button) viewChangePass.findViewById(R.id.btn_changepass);;

        _inputactivecode = (EditText)viewActivation.findViewById(R.id.input_code);
        _submit = (Button) viewActivation.findViewById(R.id.btn_submit);

        _avatarimage = (ImageView) viewLoginUpdate.findViewById(R.id.imageAvatar);

    }

    private void showPage(int Type)
    {

        _viewfliper.setDisplayedChild(Type);
    }

    private void showActive()
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
            _usernameupdate.setEnabled(false);
            _usernameupdate.setText(u.getUsername());
            showLoginUpdate();
            return;
        }
        showPage(Constants.PAGE_ACTIVE);

    }

    private void showLoginUpdate()
    {
        showPage(Constants.PAGE_UPDATE);

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
         //   dialog.showDialogCancel( LoginActivity.this,"Camera Permission error" );
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    public void activation(final String email , final String code) {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Waiting...");
        progressDialog.show();
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("email",email);
        dataJson.addProperty("active_code",code);

        RestAPI.PostDataMaster(getApplicationContext(), dataJson, RestAPI.POST_ACTIVATION, new RestAPI.RestAPIListenner() {

            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        dialog.showDialogCancel( LoginActivity.this,getResources().getString(R.string.can_not_connect_server) );
                        return;
                    }
                    JsonObject jsons = (new JsonParser()).parse(s).getAsJsonObject();
                    int statuscode = jsons.get("status").getAsInt();
                    if(statuscode == RestAPI.STATUS_SUCCESS)
                    {
                        showPage(Constants.PAGE_UPDATE);
                    }
                    else if(statuscode == 2)
                    {
                        dialog.showDialogCancel( LoginActivity.this,"Invalid Activation Code" );
                       // Toast.makeText(LoginActivity.this, "Invalid Activation Code", Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception ex) {
                    dialog.showDialogCancel( LoginActivity.this,ex.getMessage() );

                    //Toast.makeText(LoginActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    ex.printStackTrace();
                }
                finally {
                    progressDialog.dismiss();
                }

            }
        });
    }

    public void register(final String email , final String password) {

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Create account...");
        progressDialog.show();
        JsonObject dataJson = new JsonObject();
        String url;
      //  _signupButton.setEnabled(false);
        if(USING_FACEBOOK ==true) {
            Gson gson = new Gson();
            JsonParser jsonParser = new JsonParser();
             dataJson = (JsonObject) jsonParser.parse(gson.toJson(AccountController.getInstance().getAccount()));
            dataJson.remove("v_followed");
            dataJson.remove("v_follower");
             url = RestAPI.POST_SIGNUPBYFACEBOOK;
        }
        else
        {
            if(validateEmail(email)==false)
            {
                return;
            }

            if(validatePassword(password)==false)
            {
                return;
            }
            url = RestAPI.POST_SIGNUP;
            dataJson.addProperty("password", password);
        }

        dataJson.addProperty("email",email);

        if(USING_FACEBOOK == true)
        {
            dataJson.addProperty("username",AccountController.getInstance().getAccount().getUsername());
           // dataJson.addProperty("facebook_id", AccountController.getInstance().getAccount().getFacebookid());
        }

        RestAPI.PostDataMaster(getApplicationContext(), dataJson, url, new RestAPI.RestAPIListenner() {

            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);
                        dialog.showDialogCancel( LoginActivity.this,getResources().getString(R.string.can_not_connect_server) );
                        return;
                    }
                    JsonObject jsons = (new JsonParser()).parse(s).getAsJsonObject();
                    int statuscode = jsons.get("status").getAsInt();
                        progressDialog.dismiss();
                        if (statuscode == RestAPI.STATUS_SUCCESS) {

                            if (USING_FACEBOOK == true) {
                                AccountController.getInstance().getAccount().setId(jsons.get("account_id").getAsLong());
                                login(  AccountController.getInstance().getAccount().getEmail(), null, LOGIN_FACEBOOK);
                            }
                            else {
                                AccountController.getInstance().getAccount().setId(jsons.get("result").getAsJsonObject().get("account_id").getAsLong());
                                AccountController.getInstance().getAccount().setEmail(jsons.get("result").getAsJsonObject().get("email").getAsString());
                                showActive();
                            }
                           /* if(checkrequire(AccountController.getInstance().getAccount()) == true)
                            {
                                login(email, password, LOGIN_FACEBOOK);
                            }
                            else
                                showActive();*/
                        } else if (statuscode == RestAPI.STATUS_ACCOUNTESIXT) {
                           /* if (USING_FACEBOOK == true) {
                                login(email, password, LOGIN_FACEBOOK);
                            } else*/ {
                                dialog.showDialogCancel( LoginActivity.this,"Account exist!!!" );
                                //Toast.makeText(LoginActivity.this, "Account exist!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                } catch (Exception ex) {
                    progressDialog.dismiss();
                    ViewDialog dialog = new ViewDialog();
                    dialog.showDialogCancel(LoginActivity.this,ex.getMessage());
                    //Toast.makeText(LoginActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
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
        u.setUsername(_usernameupdate.getText().toString());

        if(validatePhonenumber(mobilephone) == false)
        {
            return;
        }
        if(_usernameupdate.getText().toString().isEmpty())
        {
            dialog.showDialogCancel( LoginActivity.this,"User name can not empty " );
         //   Toast.makeText(LoginActivity.this, "User name can not empty ", Toast.LENGTH_SHORT).show();
            return;
        }
        if(_birthdayupdate.getText().toString().isEmpty())
        {

            dialog.showDialogCancel( LoginActivity.this,"Birthday can not empty " );
         //   Toast.makeText(LoginActivity.this, "Birthday can not empty ", Toast.LENGTH_SHORT).show();
            return;
        }
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("account_id",u.getId());
        dataJson.addProperty("phonenumber",mobilephone);
        dataJson.addProperty("sex",u.getSex());
        dataJson.addProperty("birthday",u.getBirthday());
        dataJson.addProperty("username",u.getUsername());
        dataJson.addProperty("city",u.getCity());
        dataJson.addProperty("country",u.getCountry());

        if(selectedAvatar!=null)
        {
            String[] bb = Utils.compressFormat(selectedAvatar.getPath(), this);
            String base64 = bb[0];
            String imagename = System.currentTimeMillis() + "." + bb[1];
            dataJson.addProperty("base64",base64);
            dataJson.addProperty("image",imagename);
            dataJson.addProperty("type","image/");
        }


        RestAPI.PostDataMasterWithToken(getApplicationContext(), dataJson, RestAPI.POST_UPDATEUSER, new RestAPI.RestAPIListenner() {

            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);
                        dialog.showDialogCancel( LoginActivity.this,getResources().getString(R.string.can_not_connect_server) );
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
                    dialog.showDialogCancel( LoginActivity.this,ex.getMessage());
                    ex.printStackTrace();
                }

            }
        });
        // TODO: Implement your own authentication logic here.

    }

    @Override
    public void onBackPressed() {
        //Execute your code here
        if( _viewfliper.getDisplayedChild () == Constants.PAGE_LOGIN ||
                _viewfliper.getDisplayedChild () == Constants.PAGE_REGISTER)
        {
             Intent intent = new Intent(this , IntroActivity.class);
             startActivity(intent);
        }
        finish();

    }

    public void login(String email , String password, final int type) {

        /*if (!validate()) {
            onLoginFailed();
            return;
        }*/
      if(type == LOGIN_NORMAL)
      {
          if (password==null || password.isEmpty())
          {
             // Toast.makeText(LoginActivity.this, "Password can not empty", Toast.LENGTH_SHORT).show();
              dialog.showDialogCancel( LoginActivity.this,"Password can not empty" );
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
        dataJson.addProperty("device","android");
        dataJson.addProperty("location_lag",latitude);
        dataJson.addProperty("location_lng",longitude);
        dataJson.addProperty("login_type",type);

        RestAPI.PostDataMaster(getApplicationContext(), dataJson, RestAPI.POST_LOGIN, new RestAPI.RestAPIListenner() {

            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);
                        progressDialog.dismiss();
                        dialog.showDialogCancel( LoginActivity.this,getResources().getString(R.string.can_not_connect_server) );
                       // Toast.makeText(getBaseContext(),"Failed to connect to server, please try again", Toast.LENGTH_LONG).show();
                        return;
                    }
                    JsonObject jsons = (new JsonParser()).parse(s).getAsJsonObject();
                    int statuscode = jsons.get("status").getAsInt();
                    if (statuscode == RestAPI.STATUS_SUCCESS) {
                        DBHelper.getInstance(getApplicationContext()).CreateMessageTable();
                        DBHelper.getInstance(getApplicationContext()).CreateNotifyTable();
                       // DBHelper.getInstance(getApplicationContext()).CreateMessageTable();
                        JsonObject jsonObject = jsons.getAsJsonArray("result").get(0).getAsJsonObject();
                        Gson gson = new Gson();
                        final UserModel u = gson.fromJson(jsonObject, UserModel.class);
                        Store.putStringData(LoginActivity.this, Store.ACCESSTOKEN, jsons.get("access_token").getAsString());
                        if(type == LOGIN_NORMAL) {
                            if(u.getStatus() == 0)
                            {
                                showPage(Constants.PAGE_ACTIVE);
                                return;
                            }
                            if(u.getUsername() == null ||u.getUsername().isEmpty() )
                            {
                                showPage(Constants.PAGE_UPDATE);
                                return;
                            }
                        }
                        AccountController.getInstance().SetAccount(u);
                        DBHelper.getInstance(getApplicationContext()).insertPerson(u);
                        startService(new Intent(getApplicationContext(), SocketService.class));
                        startService(new Intent(getApplicationContext(), MyFirebaseInstanceIDService.class));

                        Store.putBooleanData(LoginActivity.this, Store.LOGGED, true);

                        {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }

                     /*   JsonObject dataJson = new JsonObject();
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

                                {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                        });*/

                        //  UserModel u = gson.fromJson(jsonObject.getAsJsonArray("result").get(0).getAsJsonObject(), UserModel.class);
                        //  UserModel u  =  RealmManager.open().createObjectFromJson(UserModel.class, JSONObject);

                        // if(jsons.get("status").getAsInt() == 200)
                        // {

                        //}

                    }
                    else
                    {
                        progressDialog.dismiss();
                        dialog.showDialogCancel( LoginActivity.this,getString(RestAPI.checkStatusCode(statuscode)));
                        //Toast.makeText(getBaseContext(), getString(RestAPI.checkStatusCode(statuscode)), Toast.LENGTH_LONG).show();
                    }
                    } catch(Exception ex){
                        progressDialog.dismiss();
                        dialog.showDialogCancel( LoginActivity.this,ex.getMessage());
                        //Toast.makeText(getBaseContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                            ex.printStackTrace();
                    }
                    finally {
                    progressDialog.dismiss();
                }


            }
        });
        // TODO: Implement your own authentication logic here.

    }

    public boolean validateEmail(String email)
    {
        boolean valid = true;
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            //Toast.makeText(getBaseContext(), "enter a valid email address", Toast.LENGTH_LONG).show();
            valid = false;
        } else {
            _emailText.setError(null);
        }
        return valid;
    }

    public boolean validatePassword(String password)
    {
        boolean valid = true;
        if (password.isEmpty() || password.length() < 6 || password.length() > 20) {
           // Toast.makeText(getBaseContext(), "between 6 and 20 alphanumeric characters", Toast.LENGTH_LONG).show();
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        return valid;
    }
    public boolean validatePhonenumber(String phone)
    {
        if (phone.isEmpty()) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(phone).matches();
        }
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
