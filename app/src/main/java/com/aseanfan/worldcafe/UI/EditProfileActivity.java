package com.aseanfan.worldcafe.UI;

import android.app.ProgressDialog;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.AreaModel;
import com.aseanfan.worldcafe.Model.CityModel;
import com.aseanfan.worldcafe.Model.UserModel;
import com.aseanfan.worldcafe.UI.Adapter.SpinnerAreaAdapter;
import com.aseanfan.worldcafe.UI.Adapter.SpinnerCityAdapter;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.Utils.Utils;
import com.aseanfan.worldcafe.worldcafe.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

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
                AccountController.getInstance().getAccount().setCountry(countryid);
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
                AccountController.getInstance().getAccount().setCity(getlistcity(countryid).get(i).getid());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        if(user.getSex() == 0)
          radgroup.check(R.id.rad_male);
        else
          radgroup.check(R.id.rad_female);

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

    }

    public void update(UserModel u) {



        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("account_id",u.getId());
        dataJson.addProperty("sex",u.getSex());
        dataJson.addProperty("birthday",u.getBirthday());
        dataJson.addProperty("username",u.getUsername());
        dataJson.addProperty("city",u.getCity());
        dataJson.addProperty("country",u.getCountry());


        RestAPI.PostDataMaster(getApplicationContext(), dataJson, RestAPI.POST_UPDATEUSER, new RestAPI.RestAPIListenner() {

            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }




                } catch (Exception ex) {

                    ex.printStackTrace();
                }

            }
        });
        // TODO: Implement your own authentication logic here.

    }
}
