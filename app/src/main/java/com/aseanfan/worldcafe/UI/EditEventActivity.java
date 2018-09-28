package com.aseanfan.worldcafe.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.DBHelper;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Helper.cropper.CropImage;
import com.aseanfan.worldcafe.Helper.cropper.CropImageView;
import com.aseanfan.worldcafe.Model.AreaModel;
import com.aseanfan.worldcafe.Model.CityModel;
import com.aseanfan.worldcafe.Model.EventModel;
import com.aseanfan.worldcafe.Model.UserModel;
import com.aseanfan.worldcafe.UI.Adapter.ImageTimelineAdapter;
import com.aseanfan.worldcafe.UI.Adapter.SpinnerCityAdapter;
import com.aseanfan.worldcafe.UI.Component.ViewDialog;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.Utils.Utils;
import com.aseanfan.worldcafe.worldcafe.R;
import com.google.gson.JsonObject;
import com.yanzhenjie.album.AlbumFile;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.aseanfan.worldcafe.Utils.Utils.MAX_LENGTH;
import static com.aseanfan.worldcafe.Utils.Utils.prefix;

public class EditEventActivity extends AppCompatActivity {

    private EventModel event = new EventModel();

    private ImageTimelineAdapter mAdapter;
    private ImageView listimage;
    private ImageButton imgbutton;
    private ArrayList<AlbumFile> mAlbumFiles;
    private String listImageBase64 = "";
    private Spinner choosearea;
    private RecyclerView listarea;
    private List<String> l = new ArrayList<>();
    private EditText scheduel;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private RadioButton friend,language,local,buissines;
    private RadioGroup radgroup;
    private Button btncreate;
    private EditText price;
    private EditText title;
    private EditText content;
    private EditText numberofparticipal;
    private EditText times;
    private LinearLayout container_type;
    private CheckBox checktype;
    private Spinner typetime;
    private EditText note;
    private Button privatepost;
    private ImageView cancel;
    private Button btnUpdate;
    private Button btnDelete;

    private int UPDATE_PUBLIC = 0;
    private int UPDATE_PRIVATE = 1;
    private int UPDATE_EVENT = 2;
    private int DELETE_EVENT = 3;


    private int isedit = 0;

    private List<CityModel> listcity;

    private SpinnerCityAdapter adaptercity;

    String[] arraytypetime={"week","month","year"};

    private int typeCreate = Constants.EVENT_CREATE;

    private String previousCleanString;

    public void update(final EventModel e , final int typeupdate) {

        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("account_id",AccountController.getInstance().getAccount().getId());
        dataJson.addProperty("event_id",e.getEventid());
        if(typeupdate ==UPDATE_PRIVATE||typeupdate ==UPDATE_PUBLIC )
        {
            dataJson.addProperty("is_private",typeupdate);
        }
        else if(typeupdate == UPDATE_EVENT)
        {
            dataJson.addProperty("title",e.getTitle());
            dataJson.addProperty("contents",e.getContent());
            dataJson.addProperty("price",e.getPrice());
            dataJson.addProperty("city",e.getCityid());
            dataJson.addProperty("limit_persons",e.getLimitpersons());
            dataJson.addProperty("schedule_type",e.getSchedule_type());
            dataJson.addProperty("number",e.getNumber());
            dataJson.addProperty("per_time",e.getPertime());
            dataJson.addProperty("note",e.getNote());
            dataJson.addProperty("genre",e.getType());
            dataJson.addProperty("start_time",e.getStarttime());
        }
        else if(typeupdate == DELETE_EVENT)
        {
            dataJson.addProperty("status",5);
        }

        RestAPI.PostDataMasterWithToken(getApplicationContext(), dataJson, RestAPI.POST_UPDATEEVENT, new RestAPI.RestAPIListenner() {

            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }

                    ViewDialog dialog = new ViewDialog();
                    String str = "";
                    if(typeupdate==UPDATE_PRIVATE)
                    {
                        str = "Event updated to private";
                    }
                    else if (typeupdate ==UPDATE_PUBLIC)
                    {
                        str = "Event updated to public";
                    }
                    else if(typeupdate == UPDATE_EVENT)
                    {
                        str = "Update Successful";
                    }
                    else if(typeupdate == DELETE_EVENT)
                    {
                        str = "Delete Successful";
                    }
                    dialog.showDialogCancel(EditEventActivity.this, str);

                } catch (Exception ex) {

                    ex.printStackTrace();
                }

            }
        });
        // TODO: Implement your own authentication logic here.

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        initView();

        initdata(getIntent().getExtras());


        typetime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                event.setPertime(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        if(event.getSchedule_type() ==0) {
            checktype.setChecked(false);
            container_type.setVisibility(View.GONE);
        }
        else
        {
            container_type.setVisibility(View.VISIBLE);
            checktype.setChecked(true);
            typetime.setSelection(event.getPertime());
            times.setText(String.valueOf(event.getNumber()));
        }
      //  container_type.setVisibility(View.GONE);

        checktype.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b == false)
                {
                    container_type.setVisibility(View.GONE);
                    event.setSchedule_type(0);

                }
                else
                {
                    container_type.setVisibility(View.VISIBLE);
                    event.setSchedule_type(1);
                    event.setPertime(0);
                }
            }
        });

        price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = editable.toString();
             /*   if (str.length() < prefix.length()) {
                    price.setText(prefix);
                    price.setSelection(prefix.length());
                    return;
                }
                if (str.equals(prefix)) {
                    return;
                }*/
                // cleanString this the string which not contain prefix and 100
                String cleanString = str.replace(prefix, "").replaceAll("[,]", "");
                // for prevent afterTextChanged recursive call
                if (cleanString.equals(previousCleanString) || cleanString.isEmpty()) {
                    return;
                }
                previousCleanString = cleanString;

                String formattedString;
                if (cleanString.contains(".")) {
                    formattedString =Utils.formatDecimal(cleanString);
                } else {
                    formattedString = Utils.formatInteger(cleanString);
                }
                formattedString = formattedString +   prefix;
                price.removeTextChangedListener(this); // Remove listener
                price.setText(formattedString);
                handleSelection();
                price.addTextChangedListener(this); // Add back the listener
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(event.getType() == Constants.EVENT_FRIEND +1)
        {
            radgroup.check(R.id.radfriend);
        }
        else if (event.getType() == Constants.EVENT_LANGUAGE +1)
        {
            radgroup.check(R.id.radlanguage);
        }
        else if (event.getType() == Constants.EVENT_LOCAL +1)
        {
            radgroup.check(R.id.radlocal);
        }
        else if (event.getType() == Constants.EVENT_BUSSINESS +1)
        {
            radgroup.check(R.id.radbusiness);
        }


        radgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                changeGenre(group, checkedId);
            }
        });



        adaptercity = new SpinnerCityAdapter(EditEventActivity.this,
                android.R.layout.simple_spinner_item,listcity);

        adaptercity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        choosearea.setAdapter(adaptercity);

        choosearea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                event.setCityid(listcity.get(i).getid());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

       /* imgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // selectImage();
                Intent intent = CropImage.activity(null)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAutoZoomEnabled(false)
                        .setMultiTouchEnabled(true)
                        .setFixAspectRatio(true)
                        // .setInitialCropWindowRectangle(new Rect(0,0,Utils.getwidthScreen(CreateEventActivity.this),Utils.convertDpToPixel(240,CreateEventActivity.this)))
                        .getIntent(EditEventActivity.this);
                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });*/

       privatepost.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(event.getPrivate() ==0)
               {
                   update(event,UPDATE_PRIVATE);
               }
               else
               {
                   update(event,UPDATE_PUBLIC);
               }
           }
       });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update(event,DELETE_EVENT);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateInput()== true) {
                    event.setTitle(title.getText().toString());
                    try {
                        event.setPrice(Utils.parse(price.getText().toString(), Locale.US).longValue());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    event.setContent(content.getText().toString());
                    TimeZone tz = TimeZone.getTimeZone("UTC");
                    DateFormat df = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
                    df.setTimeZone(tz);
                    Date convertedDate = new Date();
                    try {
                        convertedDate = df.parse(scheduel.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String nowAsISO = df.format(convertedDate);

                    event.setStarttime(nowAsISO);
                    event.setLimit_personse(Integer.valueOf(numberofparticipal.getText().toString()));
                    if (event.getSchedule_type() == 1) {
                        event.setNumber(Integer.valueOf(times.getText().toString()));
                    }
                    event.setNote(note.getText().toString());
                    update(event, UPDATE_EVENT);
                }
            }
        });

        scheduel.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                scheduel.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        setdata();

    }

    private  void handleSelection() {
        if (price.getText().length() <= MAX_LENGTH) {
            price.setSelection(price.getText().length());
        } else {
            price.setSelection(MAX_LENGTH);
        }
    }

    private boolean validateInput()
    {
        ViewDialog dialog = new ViewDialog();
        if(title.getText().toString().isEmpty())
        {
            dialog.showDialogCancel(EditEventActivity.this,"please input title");
            return false;
        }
        else if (price.getText().toString().isEmpty())
        {
            dialog.showDialogCancel(EditEventActivity.this,"please input price");
            return false;
        }
        else if(scheduel.getText().toString().isEmpty())
        {
            dialog.showDialogCancel(EditEventActivity.this,"please input start date");
            return false;
        }
        else if(numberofparticipal.getText().toString().isEmpty())
        {
            dialog.showDialogCancel(EditEventActivity.this,"please input limit persons");
            return false;
        }
        return true;
    }

    private void changeGenre(RadioGroup group, int checkedId) {
        int checkedRadioId = group.getCheckedRadioButtonId();

        if(checkedRadioId== R.id.radfriend) {
            event.setType(Constants.EVENT_FRIEND +1);
        } else if(checkedRadioId== R.id.radbusiness ) {
            event.setType(Constants.EVENT_BUSSINESS +1);
        } else if(checkedRadioId== R.id.radlanguage) {
            event.setType(Constants.EVENT_LANGUAGE +1);
        }
        else if(checkedRadioId== R.id.radlocal) {
            event.setType(Constants.EVENT_LOCAL +1);
        }
    }

    public int getIndexCity(int id  )
    {
        //int pos =0;

        for( int j=0 ; j<listcity.size();j++) {
            if (listcity.get(j).getid() == id) {
                return j;
            }
        }

        return 0;
    }


    public void setdata()
    {

        if(event.getPrivate() ==0)
        {
            privatepost.setText("Private");
        }
        else
        {
            privatepost.setText("Public");
        }

        String formattedString;
        formattedString = Utils.formatInteger(event.getPrice().toString());

        formattedString = formattedString +   prefix;

        price.setText(formattedString);
        numberofparticipal.setText(String.valueOf(event.getLimitpersons()));
        scheduel.setText(Utils.ConvertDateEvent(event.getStarttime()));
        title.setText(event.getTitle());
        int countrypos = getIndexCity(event.getCityid());
        choosearea.setSelection(countrypos);
        if(event.getContent()!=null)
        {
            content.setText(event.getContent());
        }
        if(event.getNote()!=null)
        {
            note.setText(event.getNote());
        }

    }


    public void initView()
    {
        event = new EventModel();

        listimage= findViewById(R.id.list_image);
        listimage.setVisibility(View.GONE);
        imgbutton= findViewById(R.id.selectimage);
        choosearea= findViewById(R.id.choosearea);
        scheduel =  findViewById(R.id.timepicker);

        friend =  findViewById(R.id.radfriend);
        language =  findViewById(R.id.radlanguage);
        local =  findViewById(R.id.radlocal);
        buissines =  findViewById(R.id.radbusiness);
        radgroup = findViewById(R.id.radgroup);
        btncreate = findViewById(R.id.btn_create);
        price = findViewById(R.id.input_price);
        title = findViewById(R.id.input_title);
        content = findViewById(R.id.input_details);
        numberofparticipal = findViewById(R.id.input_numbetofpartiicipants);
        times = findViewById(R.id.input_times);
        container_type = findViewById(R.id.container_type);
        checktype  = findViewById(R.id.checktype);
        typetime = findViewById(R.id.spinner_typetimes);
        note = findViewById(R.id.input_note);
        privatepost = findViewById(R.id.btnprivate);
        cancel = findViewById(R.id.btncancel);
        btnUpdate = findViewById(R.id.btndone);
        btnDelete = findViewById(R.id.btndeleted);

        listcity = Utils.initDefaultCity();

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,arraytypetime);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typetime.setAdapter(adapter);
    }


    public void initdata(Bundle bundle) {
        if (bundle != null) {
            event.setIsJoin(bundle.getInt("isJoin"));
            event.setEventid(bundle.getLong("eventid"));
            event.setTitle(bundle.getString("title"));
            event.setContent(bundle.getString("content"));
            event.setType(bundle.getInt("type"));
            event.setStarttime(bundle.getString("startime"));
            event.setUpdatetime(bundle.getString("updatetime"));
            event.setCityname(bundle.getString("place"));
            event.setNumberComment(bundle.getInt("numbercomment"));
            event.setNumberLike(bundle.getInt("numberlike"));
            event.setUrlAvatar(bundle.getString("avatar"));
            event.setUsername(bundle.getString("username"));
            event.setAccount_id(bundle.getLong("accountid"));
            event.setNumber(bundle.getInt("number"));
            event.setPertime(bundle.getInt("pertime"));
            event.setLimit_personse(bundle.getInt("limitperson"));
            event.setNote(bundle.getString("note"));
            event.setIslike(bundle.getInt("islike"));
            event.setSchedule_type(bundle.getInt("schedule_type"));
            event.setPrivate(bundle.getInt("is_private"));
            event.setPrice(bundle.getLong("price"));
        }
    }
}
