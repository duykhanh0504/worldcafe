package com.aseanfan.worldcafe.UI;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aseanfan.worldcafe.App.AccountController;
import com.aseanfan.worldcafe.Helper.RestAPI;
import com.aseanfan.worldcafe.Model.EventModel;
import com.aseanfan.worldcafe.UI.Adapter.ChooseAreaAdapter;
import com.aseanfan.worldcafe.UI.Adapter.ImageTimelineAdapter;
import com.aseanfan.worldcafe.Utils.Utils;
import com.aseanfan.worldcafe.worldcafe.R;
import com.google.gson.JsonObject;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.album.impl.OnItemClickListener;
import com.yanzhenjie.album.widget.divider.Api21ItemDivider;
import com.yanzhenjie.album.widget.divider.Divider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateEventActivity extends AppCompatActivity {

    private ImageTimelineAdapter mAdapter;
    private RecyclerView listimage;
    private ImageButton imgbutton;
    private ArrayList<AlbumFile> mAlbumFiles;
    private String listImageBase64 = "";
    private TextView choosearea;
    private RecyclerView listarea;
    private List<String> l = new ArrayList<>();
    private ChooseAreaAdapter areaAdapter;
    private TextView scheduel;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private RadioButton friend,language,local,buissines;
    private RadioGroup radgroup;
    private Button btncreate;


    public void CreateEvent(final EventModel event)
    {
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("account_id", AccountController.getInstance().getAccount().getId());
       dataJson.addProperty("contents","");
        dataJson.addProperty("title","");
        dataJson.addProperty("genre","");
        dataJson.addProperty("price",0);
        dataJson.addProperty("starttime","");
        dataJson.addProperty("endtime","");
        dataJson.addProperty("updatetime","");
        dataJson.addProperty("address","");
        dataJson.addProperty("country","");
        dataJson.addProperty("city","");
        dataJson.addProperty("limit_persons",0);
        dataJson.addProperty("schedueltype",0);
        dataJson.addProperty("numbertime",0);
        dataJson.addProperty("pertime","");

        RestAPI.PostDataMaster(this,dataJson,RestAPI.POST_CREATEEVENT, new RestAPI.RestAPIListenner() {
            @Override
            public void OnComplete(int httpCode, String error, String s) {
                try {
                    if (!RestAPI.checkHttpCode(httpCode)) {
                        //AppFuncs.alert(getApplicationContext(),s,true);

                        return;
                    }

               //     ListComment(timelineid);

                }
                catch (Exception ex) {

                    ex.printStackTrace();
                }
            }
        });
    }

    private void changeGenre(RadioGroup group, int checkedId) {
        int checkedRadioId = group.getCheckedRadioButtonId();

        if(checkedRadioId== R.id.radfriend) {
            Toast.makeText(this,"You choose the level of difficulty: friend",Toast.LENGTH_SHORT).show();
        } else if(checkedRadioId== R.id.radbusiness ) {
            Toast.makeText(this,"You choose the level of difficulty: bussine",Toast.LENGTH_SHORT).show();
        } else if(checkedRadioId== R.id.radlanguage) {
            Toast.makeText(this,"You choose the level of difficulty: langudage",Toast.LENGTH_SHORT).show();
        }
        else if(checkedRadioId== R.id.radlocal) {
            Toast.makeText(this,"You choose the level of difficulty: local",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        listimage= findViewById(R.id.list_image);
        imgbutton= findViewById(R.id.selectimage);
        choosearea= findViewById(R.id.choosearea);
        listarea= findViewById(R.id.area_check);
        scheduel =  findViewById(R.id.timepicker);

        friend =  findViewById(R.id.radfriend);
        language =  findViewById(R.id.radlanguage);
        local =  findViewById(R.id.radlocal);
        buissines =  findViewById(R.id.radbusiness);
        radgroup = findViewById(R.id.radgroup);
        btncreate = findViewById(R.id.btn_create);

        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateEvent(null);
            }
        });

        radgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                changeGenre(group, checkedId);
            }
        });


        listimage.setLayoutManager(new GridLayoutManager(this, 3));
        Divider divider = new Api21ItemDivider(Color.TRANSPARENT, 10, 10);
        listimage.addItemDecoration(divider);

        areaAdapter = new ChooseAreaAdapter(null);
        listarea.setLayoutManager(new GridLayoutManager(this, 3));
        listarea.setAdapter(areaAdapter);

        mAdapter = new ImageTimelineAdapter(this, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                previewImage(position);
            }
        });
        listimage.setAdapter(mAdapter);

        imgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
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

        choosearea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(CreateEventActivity.this);
                builder.setTitle("Choose some animals");

// add a checkbox list
                final String[] areas = {"hanoi", "hcm", "da nang"};
                final boolean[] checkedItems = {false, false, false};
                builder.setMultiChoiceItems(areas, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        // user checked or unchecked a box
                    }
                });

// add OK and Cancel buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        l.clear();
                        // user clicked OK
                        for(int i=0 ; i< checkedItems.length ; i++)
                        {
                            if( checkedItems[i]== true)
                            {
                                l.add(areas[i]);
                            }
                        }
                        areaAdapter.updatedata(l);
                    }
                });
                builder.setNegativeButton("Cancel", null);

// create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
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
