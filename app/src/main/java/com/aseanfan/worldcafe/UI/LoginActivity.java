package com.aseanfan.worldcafe.UI;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;


import butterknife.BindView;
import butterknife.ButterKnife;
import io.socket.client.Socket;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_SIGNUP = 0;

  /*  @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;*/

    private ViewFlipper _viewfliper;
    private Button _loginButton;
    private EditText _passwordText;
    private EditText _emailText;
    private TextView _signupLink;

    private EditText _mobileupdate;
    private Button _update;

    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       // ButterKnife.bind(this);

        App app = (App) getApplication();
        mSocket = app.getSocket();

        _viewfliper = (ViewFlipper)this.findViewById(R.id.viewFlipper);

        View viewLogin = this.findViewById(R.id.flipViewLogin);
        View viewLoginUpdate =  this.findViewById(R.id.flipViewUpdateLogin);
        _loginButton = (Button)viewLogin.findViewById(R.id.btn_login);
        _passwordText = (EditText)viewLogin.findViewById(R.id.input_password);
        _emailText = (EditText)viewLogin.findViewById(R.id.input_email);
        _signupLink = (TextView)viewLogin.findViewById(R.id.link_signup);

        _mobileupdate = (EditText)viewLoginUpdate.findViewById(R.id.input_mobile_update);
        _update = (Button) viewLoginUpdate.findViewById(R.id.btn_update);

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


      /*  fblogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog dlg = new ProgressDialog(LoginActivity.this);
                dlg.setTitle("Please, wait a moment.");
                dlg.setMessage("Logging in...");
                dlg.show();

                Collection<String> permissions = Arrays.asList("public_profile", "email");

                ParseFacebookUtils.logInWithReadPermissionsInBackground(LoginActivity.this, permissions, new LogInCallback() {

                    @Override
                    public void done(ParseUser user, ParseException err) {
                        if (err != null) {
                            dlg.dismiss();
                            ParseUser.logOut();
                            Log.e("err", "err", err);
                        }
                        if (user == null) {
                            dlg.dismiss();
                            ParseUser.logOut();
                            Toast.makeText(LoginActivity.this, "The user cancelled the Facebook login.", Toast.LENGTH_LONG).show();
                            Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                        } else if (user.isNew()) {
                            dlg.dismiss();
                            Toast.makeText(LoginActivity.this, "User signed up and logged in through Facebook.", Toast.LENGTH_LONG).show();
                            Log.d("MyApp", "User signed up and logged in through Facebook!");
                            getUserDetailFromFB();
                        } else {
                            dlg.dismiss();
                            Toast.makeText(LoginActivity.this, "User logged in through Facebook.", Toast.LENGTH_LONG).show();
                            Log.d("MyApp", "User logged in through Facebook!");
                            alertDisplayer("Oh, you!","Welcome back!");
                        }
                    }
                });
            }
        });*/
    }

    private void showLogin()
    {

        _viewfliper.setDisplayedChild(0);
    }

    private void showLoginUpdate()
    {

        _viewfliper.setDisplayedChild(1);

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
                    DBHelper.getInstance(getApplicationContext()).insertPerson(u.getId(),u.getUsername(),u.getEmail(),u.getPhonenumber());
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

    private void getUserDetailFromFB(){
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),new GraphRequest.GraphJSONObjectCallback(){
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                ParseUser user = ParseUser.getCurrentUser();
                try{
                    user.setUsername(object.getString("name"));
                }catch(JSONException e){
                    e.printStackTrace();
                }
                try{
                    user.setEmail(object.getString("email"));
                }catch(JSONException e){
                    e.printStackTrace();
                }
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        alertDisplayer("First Time Login!", "Welcome!");
                    }
                });
            }

        });

        Bundle parameters = new Bundle();
        parameters.putString("fields","name,email");
        request.setParameters(parameters);
        request.executeAsync();
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
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }
}
