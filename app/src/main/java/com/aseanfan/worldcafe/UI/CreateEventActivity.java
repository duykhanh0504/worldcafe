package com.aseanfan.worldcafe.UI;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.CityModel;
import com.aseanfan.worldcafe.Model.EventModel;
import com.aseanfan.worldcafe.UI.Adapter.ChooseAreaAdapter;
import com.aseanfan.worldcafe.UI.Adapter.ImageTimelineAdapter;
import com.aseanfan.worldcafe.UI.Adapter.SpinnerCityAdapter;
import com.aseanfan.worldcafe.UI.Component.ViewDialog;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.Utils.Utils;
import com.aseanfan.worldcafe.worldcafe.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.aseanfan.worldcafe.Helper.cropper.CropImage;
import com.aseanfan.worldcafe.Helper.cropper.CropImageView;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.album.impl.OnItemClickListener;
import com.yanzhenjie.album.widget.divider.Api21ItemDivider;
import com.yanzhenjie.album.widget.divider.Divider;

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

public class CreateEventActivity extends AppCompatActivity {

    private ImageTimelineAdapter mAdapter;
    private ImageView listimage;
    private ImageButton imgbutton;
    private ArrayList<AlbumFile> mAlbumFiles;
    private String listImageBase64 = "";
    private Spinner choosearea;
    private RecyclerView listarea;
    private List<String> l = new ArrayList<>();
    private TextView scheduel;
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

    private int isedit = 0;

    private List<CityModel> listcity;

    private SpinnerCityAdapter  adaptercity;

    String[] arraytypetime={"week","month","year"};

    private int typeCreate = Constants.EVENT_CREATE;

    private EventModel event;
    private String previousCleanString;

    public void getEventDetail(Long eventid)
    {
        String url =  String.format(RestAPI.GET_EVENTDETAIL,AccountController.getInstance().getAccount().getId(),eventid);

        RestAPI.GetDataMasterWithToken(this,url, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {

                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }
                    JsonObject json = (new JsonParser()).parse(s).getAsJsonObject().get("result").getAsJsonObject();
                    Gson gson = new Gson();


                }
                catch (Exception ex) {

                    ex.printStackTrace();
                }
            }
        });
    }

    public void CreateEvent(final EventModel event)
    {
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("account_id", AccountController.getInstance().getAccount().getId());
        dataJson.addProperty("contents",event.getContent());
        dataJson.addProperty("title",event.getTitle());
        dataJson.addProperty("genre",event.getType());
        dataJson.addProperty("price",event.getPrice());
        dataJson.addProperty("starttime",event.getStarttime());
        dataJson.addProperty("endtime","");
        dataJson.addProperty("address","");
        dataJson.addProperty("country","");
        dataJson.addProperty("note",event.getNote());
        dataJson.addProperty("city",event.getCityid());
        dataJson.addProperty("limit_persons",event.getLimitpersons());
        dataJson.addProperty("schedueltype",event.getSchedule_type());
        dataJson.addProperty("numbertime",event.getNumber());
        dataJson.addProperty("pertime",event.getPertime());
        dataJson.addProperty("base64",listImageBase64);
        dataJson.addProperty("image","event" + System.currentTimeMillis());
        dataJson.addProperty("type","image/jpeg");

        RestAPI.PostDataMasterWithToken(this,dataJson,RestAPI.POST_CREATEEVENT, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }
                    ViewDialog dialog = new ViewDialog();
                    dialog.showDialogOK(CreateEventActivity.this,"Create Event Successfully", new ViewDialog.DialogListenner() {
                        @Override
                        public void OnClickConfirm() {
                            finish();
                        }
                    });


               //     ListComment(timelineid);

                }
                catch (Exception ex) {

                    ex.printStackTrace();
                }
            }
        });
    }


    private  void handleSelection() {
        if (price.getText().length() <= MAX_LENGTH) {
            price.setSelection(price.getText().length());
        } else {
            price.setSelection(MAX_LENGTH);
        }
    }

    private void changeGenre(RadioGroup group, int checkedId) {
        int checkedRadioId = group.getCheckedRadioButtonId();

        if(checkedRadioId== R.id.radfriend) {
            event.setType(Constants.EVENT_FRIEND);
        } else if(checkedRadioId== R.id.radbusiness ) {
            event.setType(Constants.EVENT_BUSSINESS);
        } else if(checkedRadioId== R.id.radlanguage) {
            event.setType(Constants.EVENT_LANGUAGE);
        }
        else if(checkedRadioId== R.id.radlocal) {
            event.setType(Constants.EVENT_LOCAL);
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

        listcity = Utils.initDefaultCity();

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,arraytypetime);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typetime.setAdapter(adapter);
    }

    public void setdata()
    {
        isedit = getIntent().getIntExtra("isedit",0);
        if(isedit == 1 )
        {

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        initView();

        typetime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                event.setPertime(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        checktype.setChecked(false);
        container_type.setVisibility(View.GONE);

        checktype.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b == false)
                {
                    container_type.setVisibility(View.GONE);
                    event.setSchedule_type(1);
                }
                else
                {
                    container_type.setVisibility(View.VISIBLE);
                    event.setSchedule_type(0);
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

        btncreate.setOnClickListener(new View.OnClickListener() {
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
                    if(event.getSchedule_type() == 1) {
                        event.setNumber(Integer.valueOf(times.getText().toString()));
                    }
                    event.setNote(note.getText().toString());
                    CreateEvent(event);
                }
            }
        });

        radgroup.check(R.id.radfriend);

        event.setType(Constants.EVENT_FRIEND);

        radgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                changeGenre(group, checkedId);
            }
        });



        adaptercity = new SpinnerCityAdapter(CreateEventActivity.this,
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


        imgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // selectImage();
                Intent intent = CropImage.activity(null)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAutoZoomEnabled(false)
                        .setMultiTouchEnabled(true)
                        .setFixAspectRatio(true)
                       // .setInitialCropWindowRectangle(new Rect(0,0,Utils.getwidthScreen(CreateEventActivity.this),Utils.convertDpToPixel(240,CreateEventActivity.this)))
                        .getIntent(CreateEventActivity.this);
                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEventActivity.this,
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

    }

    private boolean validateInput()
    {
        ViewDialog dialog = new ViewDialog();
        if(title.getText().toString().isEmpty())
        {
            dialog.showDialogCancel(CreateEventActivity.this,"please input title");
            return false;
        }
        else if (price.getText().toString().isEmpty())
        {
            dialog.showDialogCancel(CreateEventActivity.this,"please input price");
            return false;
        }
        else if(scheduel.getText().toString().isEmpty())
        {
            dialog.showDialogCancel(CreateEventActivity.this,"please input start date");
            return false;
        }
        else if(numberofparticipal.getText().toString().isEmpty())
        {
            dialog.showDialogCancel(CreateEventActivity.this,"please input limit persons");
            return false;
        }
        return true;
    }

    private void selectImage() {
        Album.image(this)
                .multipleChoice()
                .camera(true)
                .columnCount(2)
                .selectCount(6)
                .checkedList(mAlbumFiles)
                .widget(
                        Widget.newDarkBuilder(this)
                                .title("album")
                                .build()
                )
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        mAlbumFiles = result;
                        mAdapter.notifyDataSetChanged(mAlbumFiles);

                        for (int i = 0; i < mAlbumFiles.size(); i++) {
                            String[] bb = Utils.compressFormat(mAlbumFiles.get(i).getPath(), CreateEventActivity.this);
                            String ba1 = bb[0];
                            listImageBase64 += ba1 + ",";
                        }
                        //  mTvMessage.setVisibility(result.size() > 0 ? View.VISIBLE : View.GONE);
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                        // Toast.makeText(ImageActivity.this, R.string.canceled, Toast.LENGTH_LONG).show();
                    }
                })
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {

            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Uri selectedAvatar  = result.getUri();
                    String[] bb = Utils.compressFormat(selectedAvatar.getPath(), this);
                    listImageBase64 = bb[0] + ",";
                   // String imagename = System.currentTimeMillis() + "." + bb[1];
                 //   listImageBase64
                    listimage.setVisibility(View.VISIBLE);
                    Glide.with(this).load( selectedAvatar).into(listimage);
                }
                break;
        }
    }

    private void previewImage(int position) {
        if (mAlbumFiles == null || mAlbumFiles.size() == 0) {
            Toast.makeText(this, "no selected", Toast.LENGTH_LONG).show();
        } else {
            Album.galleryAlbum(this)
                    .checkable(true)
                    .checkedList(mAlbumFiles)
                    .currentPosition(position)
                    .widget(
                            Widget.newDarkBuilder(this)
                                    .title("album")
                                    .build()
                    )
                    .onResult(new Action<ArrayList<AlbumFile>>() {
                        @Override
                        public void onAction(@NonNull ArrayList<AlbumFile> result) {
                            mAlbumFiles = result;
                            //   mAdapter.notifyDataSetChanged(mAlbumFiles);
                            // mTvMessage.setVisibility(result.size() > 0 ? View.VISIBLE : View.GONE);
                        }
                    })
                    .start();
        }
    }


}
