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

import com.aseanfan.worldcafe.Helper.cropper.CropImage;
import com.aseanfan.worldcafe.Helper.cropper.CropImageView;
import com.aseanfan.worldcafe.Model.CityModel;
import com.aseanfan.worldcafe.Model.EventModel;
import com.aseanfan.worldcafe.UI.Adapter.ImageTimelineAdapter;
import com.aseanfan.worldcafe.UI.Adapter.SpinnerCityAdapter;
import com.aseanfan.worldcafe.Utils.Constants;
import com.aseanfan.worldcafe.Utils.Utils;
import com.aseanfan.worldcafe.worldcafe.R;
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

    private int isedit = 0;

    private List<CityModel> listcity;

    private SpinnerCityAdapter adaptercity;

    String[] arraytypetime={"week","month","year"};

    private int typeCreate = Constants.EVENT_CREATE;

    private String previousCleanString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        initView();

        initdata(getIntent().getExtras());

        setdata();

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
        }
        else
        {
            checktype.setChecked(true);
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

    public void setdata()
    {

        String formattedString;
        formattedString = Utils.formatInteger(event.getPrice().toString());

        formattedString = formattedString +   prefix;

        price.setText(formattedString);
        numberofparticipal.setText(String.valueOf(event.getLimitpersons()));
        times.setText(Utils.ConvertDateEvent(event.getStarttime()));
        title.setText(event.getTitle());
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
