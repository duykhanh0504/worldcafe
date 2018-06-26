package com.aseanfan.worldcafe.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.worldcafe.R;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RegisterActivity extends AppCompatActivity {


    private ViewFlipper _viewfliper;
    private Button _signupButton;
    private LoginButton _signupFacebookButton;
    private EditText _passwordText;
    private EditText _emailText;
    private TextView _signupLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        _viewfliper = (ViewFlipper)this.findViewById(R.id.viewFlipper);

        View viewSignup = this.findViewById(R.id.flipViewRegister);

        _signupButton = (Button)viewSignup.findViewById(R.id.btn_Signup);
        _passwordText = (EditText)viewSignup.findViewById(R.id.input_password);
        _emailText = (EditText)viewSignup.findViewById(R.id.input_email);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

    }

    public void signup() {

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

   //     String name = _nameText.getText().toString();
      //  String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
      //  String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
      //  String reEnterPassword = _reEnterPasswordText.getText().toString();

       // Gson gson = new Gson();
      //  JsonObject dataJson = gson.toJsonTree(null).getAsJsonObject();
        JsonObject dataJson = new JsonObject();
       // dataJson.addProperty("username",name);
        dataJson.addProperty("password",password);
        dataJson.addProperty("email",email);

        RestAPI.PostDataMaster(getApplicationContext(), dataJson, RestAPI.POST_SIGNUP, new RestAPI.RestAPIListenner() {

            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }
                    JsonObject jsonObject = (new JsonParser()).parse(s).getAsJsonObject();
                    if(jsonObject.get("status").getAsInt() == 200)
                    {
                       progressDialog.dismiss();
                       _viewfliper.setDisplayedChild(1);
                        //Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                      //  startActivity(intent);
                       // finish();
                    }

                } catch (Exception ex) {

                    ex.printStackTrace();
                }

            }
        });

    }

    public boolean validate() {
        boolean valid = true;

      //  String name = _nameText.getText().toString();
       // String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
       // String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
       // String reEnterPassword = _reEnterPasswordText.getText().toString();

   /*     if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }*/

      /*  if (address.isEmpty()) {
            _addressText.setError("Enter Valid Address");
            valid = false;
        } else {
            _addressText.setError(null);
        }*/


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

       /* if (mobile.isEmpty() || mobile.length()!=10) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }*/

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

       /* if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }**/

        return valid;
    }

}
