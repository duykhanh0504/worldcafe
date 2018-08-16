package com.aseanfan.worldcafe.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.DBHelper;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.AreaModel;
import com.aseanfan.worldcafe.Model.CityModel;
import com.aseanfan.worldcafe.Model.UserModel;
import com.aseanfan.worldcafe.UI.Adapter.SpinnerAreaAdapter;
import com.aseanfan.worldcafe.UI.Adapter.SpinnerCityAdapter;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.Utils.Utils;
import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity {

    private FrameLayout cover;
    private ImageView avatar;
    private EditText username;
    private EditText introduce;
    private EditText school;
    private EditText company;
    private RadioGroup radgroup;
    private Spinner country;
    private Spinner city;
    private UserModel user;
    private Button Button;

    private static List<AreaModel> listarea = new ArrayList<>();

    private static  int countryid = 1;
    private SpinnerCityAdapter adaptercity;
    private SpinnerAreaAdapter adaptercountry;

    public void initDefaultCountry()
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

    }

    public void UpdateImage(Uri image, final int type)
    {

        if(image==null)
            return;

        String[] bb = Utils.compressFormat(image.getPath(), this);
        String base64 = bb[0];
        String imagename = System.currentTimeMillis() + "." + bb[1];

        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("account_id",AccountController.getInstance().getAccount().getId());
        dataJson.addProperty("base64",base64);
        dataJson.addProperty("image",imagename);
        dataJson.addProperty("type","image/");

        String url = "";
        if(type ==1)
        {
            url =RestAPI.POST_UPDATECOVER;
        }
        else if(type == 0)
        {
            url =RestAPI.POST_UPDATEAVATAR;
        }

        RestAPI.PostDataMasterWithToken(this, dataJson, url, new RestAPI.RestAPIListenner() {

            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }

                    if(type==0) {
                        JsonObject jsons = (new JsonParser()).parse(s).getAsJsonObject();
                        AccountController.getInstance().getAccount().setAvarta(jsons.get("url").getAsString());
                        DBHelper.getInstance(EditProfileActivity.this).updatePerson(AccountController.getInstance().getAccount());
                    }

                    //  String email = _emailText.getText().toString();
                    //   String password = _passwordText.getText().toString();


                } catch (Exception ex) {

                    ex.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode) {
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if(result.getUri()!=null) {
                        if(data.getIntExtra("type",0) ==0) {

                            UpdateImage(result.getUri(), 0);
                            Glide.with(this).load( result.getUri()).apply(RequestOptions.circleCropTransform()).into( avatar);
                        }
                        else if(data.getIntExtra("type",0) ==1) {

                            UpdateImage(result.getUri(), 1);
                            Glide.with(this).load( result.getUri()).into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    cover.setBackgroundDrawable(resource);
                                }
                            });
                        }
                    }
                    // Glide.with(this).load( selectedAvatar).apply(RequestOptions.circleCropTransform()).into( _avatarimage);
                }
                break;
        }
    }

    void getlistcountry()
    {
        RestAPI.GetDataMaster(this, RestAPI.GET_LISTCOUNTRYANDCITY, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

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

                    ex.printStackTrace();
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
        setContentView(R.layout.activity_edit_profile);

        initDefaultCountry();

        intview();

        getlistcountry();


        user = AccountController.getInstance().getAccount();

        if(user.getCover()==null)
        {
            Glide.with(this).load( "https://png.pngtree.com/thumb_back/fh260/back_pic/00/15/30/4656e81f6dc57c5.jpg").into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    cover.setBackgroundDrawable(resource);
                }
            });
        }
        else
        {
            Glide.with(this).load( user.getCover()).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    cover.setBackgroundDrawable(resource);
                }
            });
        }

        final Drawable mDefaultBackground1 = this.getResources().getDrawable(R.drawable.avata_defaul);
        Glide.with(this).load( user.getAvarta()).apply(RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.ALL).error(mDefaultBackground1)).into(avatar);

    if(user.getUsername()!=null) {
        username.setText(user.getUsername());
    }

        if(user.getIntroduction()!=null) {
            introduce.setText(user.getIntroduction());
        }
        if(user.getSchool()!=null) {
            school.setText(user.getSchool());
        }
        if(user.getCompany()!=null) {
            company.setText(user.getCompany());
        }


        radgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                changeSex(group, checkedId);
            }
        });

        adaptercountry = new SpinnerAreaAdapter(this,
                android.R.layout.simple_spinner_item,listarea);

        adaptercountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country.setAdapter(adaptercountry);
        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                countryid = listarea.get(i).getid();
                getlistcity(countryid);
                adaptercity.setdata( getlistcity(countryid));
                user.setCountry(countryid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        adaptercity = new SpinnerCityAdapter(this,
                android.R.layout.simple_spinner_item,getlistcity(countryid));

        adaptercity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city.setAdapter(adaptercity);
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                user.setCity(getlistcity(countryid).get(i).getid());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        if(user.getSex() == Constants.MALE)
          radgroup.check(R.id.rad_male);
        else
          radgroup.check(R.id.rad_female);

        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user.setUsername(username.getText().toString());
                user.setIntroduction(introduce.getText().toString());
                user.setSchool(school.getText().toString());
                user.setCompany(company.getText().toString());
                update(user);
            }
        });

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = CropImage.activity(null)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAutoZoomEnabled(false)
                        .setMultiTouchEnabled(false)
                        .getIntent(EditProfileActivity.this);
                intent.putExtra("type",0);
                EditProfileActivity.this.startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = CropImage.activity(null)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAutoZoomEnabled(false)
                        .setMultiTouchEnabled(false)
                        .getIntent(EditProfileActivity.this);
                intent.putExtra("type",1);
                EditProfileActivity.this.startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    private void changeSex(RadioGroup group, int checkedId) {
        int checkedRadioId = group.getCheckedRadioButtonId();

        if(checkedRadioId== R.id.rad_male) {
            user.setSex(Constants.MALE);
        } else if(checkedRadioId== R.id.rad_female ) {
            user.setSex(Constants.FEMALE);
        }
    }

    public void intview()
    {
        cover = (FrameLayout)this.findViewById(R.id.cover);
        avatar = (ImageView) this.findViewById(R.id.avatar);
        username = (EditText) this.findViewById(R.id.Name);
        introduce = (EditText) this.findViewById(R.id.edtintroduce);
        school = (EditText) this.findViewById(R.id.edtschool);
        company = (EditText) this.findViewById(R.id.edtcompany);
        radgroup = (RadioGroup) this.findViewById(R.id.rad_sex);
        country = (Spinner) this.findViewById(R.id.spinner_country);
        city = (Spinner) this.findViewById(R.id.spinner_city);
        Button = (Button) this.findViewById(R.id.btn_update);

    }

    public void update(final UserModel u) {



        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("account_id",u.getId());
        dataJson.addProperty("sex",u.getSex());
        dataJson.addProperty("birthday",u.getBirthday());
        dataJson.addProperty("username",u.getUsername());
        dataJson.addProperty("city",u.getCity());
        dataJson.addProperty("country",u.getCountry());

        if(u.getIntroduction()!=null) {
            dataJson.addProperty("introduction",u.getIntroduction());
        }
        if(u.getSchool()!=null) {
            dataJson.addProperty("school",u.getSchool());
        }
        if(u.getCompany()!=null) {
            dataJson.addProperty("company",u.getCompany());
        }


        RestAPI.PostDataMasterWithToken(getApplicationContext(), dataJson, RestAPI.POST_UPDATEUSER, new RestAPI.RestAPIListenner() {

            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }
                AccountController.getInstance().SetAccount(u);



                } catch (Exception ex) {

                    ex.printStackTrace();
                }

            }
        });
        // TODO: Implement your own authentication logic here.

    }
}
