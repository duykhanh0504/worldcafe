package com.aseanfan.worldcafe.Helper;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.aseanfan.worldcafe.Model.ChatMessageModel;
import com.aseanfan.worldcafe.Model.NotificationModel;
import com.aseanfan.worldcafe.Model.UserModel;

import java.security.acl.Group;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "CroseaDB.db";
    private static final int DATABASE_VERSION = 2;

    public static final String PERSON_TABLE_USER = "user";
    public static final String PERSON_COLUMN_ID = "account_id";
    public static final String PERSON_COLUMN_NAME = "username";
    public static final String PERSON_COLUMN_EMAIL = "email";
    public static final String PERSON_PHONE_NUMBER = "phonenumber";
    public static final String PERSON_AVATAR_URL = "avarta";
    public static final String PERSON_SEX = "sex";
    public static final String PERSON_BIRTHDAY = "birthday";
    public static final String PERSON_ADDRESS = "address";
    public static final String PERSON_DISTRICT = "district";
    public static final String PERSON_CITY = "city";
    public static final String PERSON_COUNTRY = "country";
    public static final String PERSON_COMPANY = "company";
    public static final String PERSON_SCHOOL = "school";
    public static final String PERSON_INTRODUCTION = "introduction";
    public static final String PERSON_STATUS = "status";
    public static final String PERSON_FACEBOOKID = "facebookid";
    public static final String OWNER = "owner";


    public static final String TABLE_MESSAGE_CHAT = "message_chat";
    public static final String MESSAGE_ID = "message_id";
    public static final String MESSAGE = "message";
    public static final String TYPE = "type";
    public static final String RECEIVER_ACCOUNT = "receiver_account";
    public static final String RECEIVED_ACCOUNT = "received_account";
    public static final String SEND_ACCOUNT = "send_account";
    public static final String GROUP_ID = "group_id";
    public static final String CREATE_TIME = "create_time";
    public static final String RECEIVER_TIME = "receiver_time";

    public static final String TABLE_NOTIFICATION = "notification";
    public static final String NOTIFY_ID = "notify_id";
    public static final String NOTIFY_MESSAGE = "notify_message";
    public static final String NOTIFY_TITLE= "notify_title";
    public static final String NOTIFY_TIME= "notify_time";
    public static final String NOTIFY_STATUS= "notify_status";
    public static final String NOTIFY_FROMID= "notify_fromid";
    public static final String NOTIFY_AVATAR= "notify_avatar";
    public static final String NOTIFY_TYPE= "notify_type";


    private static SQLiteDatabase db;
    private static DBHelper instance;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    public boolean insertPerson(UserModel user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PERSON_COLUMN_ID, user.getId());
        contentValues.put(PERSON_COLUMN_NAME, user.getUsername());
        contentValues.put(PERSON_COLUMN_EMAIL, user.getEmail());
        contentValues.put(PERSON_PHONE_NUMBER, user.getPhonenumber());
        contentValues.put(PERSON_AVATAR_URL, user.getAvarta());
        contentValues.put(PERSON_SEX, user.getSex());
        contentValues.put(PERSON_BIRTHDAY, user.getBirthday());
        contentValues.put(PERSON_ADDRESS, user.getAddress());
        contentValues.put(PERSON_DISTRICT, user.getDistrict());
        contentValues.put(PERSON_CITY, user.getCity());
        contentValues.put(PERSON_COUNTRY, user.getCountry());
        contentValues.put(PERSON_COMPANY, user.getCompany());
        contentValues.put(PERSON_SCHOOL, user.getSchool());
        contentValues.put(PERSON_INTRODUCTION, user.getIntroduction());

        db.insert(PERSON_TABLE_USER, null, contentValues);
        return true;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, PERSON_TABLE_USER);
        return numRows;
    }

    public boolean updatePerson(UserModel user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PERSON_COLUMN_NAME, user.getUsername());
        contentValues.put(PERSON_COLUMN_EMAIL, user.getEmail());
        contentValues.put(PERSON_PHONE_NUMBER, user.getPhonenumber());
        contentValues.put(PERSON_AVATAR_URL, user.getAvarta());
        contentValues.put(PERSON_SEX, user.getSex());
        contentValues.put(PERSON_BIRTHDAY, user.getBirthday());
        contentValues.put(PERSON_ADDRESS, user.getAddress());
        contentValues.put(PERSON_DISTRICT, user.getDistrict());
        contentValues.put(PERSON_CITY, user.getCity());
        contentValues.put(PERSON_COUNTRY, user.getCountry());
        contentValues.put(PERSON_COMPANY, user.getCompany());
        contentValues.put(PERSON_SCHOOL, user.getSchool());
        contentValues.put(PERSON_INTRODUCTION, user.getIntroduction());
        contentValues.put(PERSON_STATUS, user.getStatus());
        contentValues.put(PERSON_FACEBOOKID, user.getFacebookid());
        db.update(PERSON_TABLE_USER, contentValues, PERSON_COLUMN_ID + " = ? ", new String[] { Long.toString(user.getId()) } );
        db.close();
        return true;
    }

    public Integer deletePerson(Long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(PERSON_TABLE_USER,
                PERSON_COLUMN_ID + " = ? ",
                new String[] { Long.toString(id) });
    }

    public void deleteTableChat() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE_CHAT);
    /*    return db.delete(TABLE_MESSAGE_CHAT,
                null,
              null);*/
    }

    public void deleteTableNotify() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION);
    /*    return db.delete(TABLE_MESSAGE_CHAT,
                null,
              null);*/
    }

    public Cursor getPerson(Long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM " + PERSON_TABLE_USER + " WHERE " +
                PERSON_COLUMN_ID + "=?", new String[]{Long.toString(id)});
        return res;
    }

    public Cursor getNotify() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM " + TABLE_NOTIFICATION + " LIMIT 50" ,null);
        return res;
    }

    public void CreateMessageTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_MESSAGE_CHAT +
                        "(" + MESSAGE_ID + " INTEGER, " /*INTEGER PRIMARY KEY,*/  +
                        MESSAGE + " TEXT, " +
                        TYPE + " INTEGER, " +
                        RECEIVED_ACCOUNT + " TEXT, " +
                        RECEIVER_ACCOUNT + " INTEGER, " +
                        SEND_ACCOUNT + " INTEGER, " +
                        GROUP_ID + " INTEGER, " +
                        RECEIVER_TIME + " TEXT, " +
                        CREATE_TIME + " TEXT)"

        );
    }

    public void CreateNotifyTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_NOTIFICATION +
                        "(" + NOTIFY_ID + " INTEGER, " /*INTEGER PRIMARY KEY,*/  +
                        NOTIFY_MESSAGE + " TEXT, " +
                        NOTIFY_TIME + " TEXT, " +
                        NOTIFY_TITLE + " TEXT, " +
                        NOTIFY_AVATAR + " INTEGER, " +
                        NOTIFY_STATUS + " INTEGER, " +
                        NOTIFY_TYPE + " INTEGER, " +
                        NOTIFY_STATUS + " INTEGER)"

        );
    }

    public Cursor getAllPersons() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + PERSON_TABLE_USER, null );
        return res;
    }

    public Cursor getAllMessageChat(Long accountid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT DISTINCT * FROM " + TABLE_MESSAGE_CHAT + " WHERE " + SEND_ACCOUNT + " = " + accountid + " OR " + RECEIVER_ACCOUNT +
                " = " + accountid  + " LIMIT 100" , null );
        return res;
    }

    public Long getlastMessageChat(Long accountid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT DISTINCT "+ MESSAGE_ID + " FROM " + TABLE_MESSAGE_CHAT  + " ORDER BY " +  MESSAGE_ID  +" DESC LIMIT 1", null );
        if (res != null) {
            res.moveToFirst();
            if(res.isAfterLast()==false) {
                return res.getLong(res.getColumnIndex(DBHelper.MESSAGE_ID));
            }

        }

        return Long.valueOf(0);
    }

    public Cursor getAllMessage(Long accountid ) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + TABLE_MESSAGE_CHAT + " WHERE " + SEND_ACCOUNT + " = " + accountid  +
                 " AND " + GROUP_ID + " = -1" , null );
        return res;
    }

    public Cursor getAllMessage() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + TABLE_MESSAGE_CHAT , null );
        return res;
    }




    public boolean InsertMessageChat(ChatMessageModel message) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(MESSAGE_ID, message.getMessage_id());
        contentValues.put(MESSAGE, message.getMessageText());
        contentValues.put(TYPE, message.getType());
        contentValues.put(RECEIVED_ACCOUNT, message.getReceived());
        contentValues.put(RECEIVER_ACCOUNT, message.getReceiver());
        contentValues.put(SEND_ACCOUNT, message.getSend_account());
        contentValues.put(GROUP_ID, message.getGroupid());
        contentValues.put(CREATE_TIME, message.getCreate_day());
        contentValues.put(RECEIVER_TIME, 0);

        db.insert(TABLE_MESSAGE_CHAT, null, contentValues);
        db.close();
        return true;

    }

    public boolean InsertNotify(NotificationModel notify) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(NOTIFY_AVATAR, notify.getAvarta());
        contentValues.put(NOTIFY_FROMID, notify.getFromid());
        contentValues.put(NOTIFY_MESSAGE, notify.getMessage());
        contentValues.put(NOTIFY_STATUS, notify.getStatus());
        contentValues.put(NOTIFY_TIME, notify.getCreatetime());
        contentValues.put(NOTIFY_TYPE, notify.getType());
        contentValues.put(NOTIFY_TITLE, notify.getTitle());

        db.insert(TABLE_NOTIFICATION, null, contentValues);
        db.close();
        return true;

    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS " + PERSON_TABLE_USER +
                        "(" + PERSON_COLUMN_ID + " INTEGER, " /*INTEGER PRIMARY KEY,*/  +
                        PERSON_COLUMN_NAME + " TEXT, " +
                        PERSON_COLUMN_EMAIL + " TEXT, " +
                        PERSON_PHONE_NUMBER + " TEXT, " +
                        PERSON_AVATAR_URL + " TEXT, " +
                        PERSON_SEX + " INTEGER, " +
                        PERSON_BIRTHDAY + " TEXT, " +
                        PERSON_ADDRESS+ " TEXT, " +
                        PERSON_DISTRICT + " INTEGER, " +
                        PERSON_CITY + " INTEGER, " +
                        PERSON_COUNTRY + " TEXT, " +
                        PERSON_COMPANY + " TEXT, " +
                        PERSON_SCHOOL + " TEXT, " +
                        PERSON_STATUS + " INTEGER, " +
                        OWNER + " INTEGER DEFAULT '0', " +
                        PERSON_FACEBOOKID + " TEXT , " +
                        PERSON_INTRODUCTION + " TEXT)"
        );

        sqLiteDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_MESSAGE_CHAT +
                        "(" + MESSAGE_ID + " INTEGER, " /*INTEGER PRIMARY KEY,*/  +
                        MESSAGE + " TEXT, " +
                        TYPE + " INTEGER, " +
                        RECEIVED_ACCOUNT + " TEXT, " +
                        RECEIVER_ACCOUNT + " INTEGER, " +
                        SEND_ACCOUNT + " INTEGER, " +
                        GROUP_ID + " INTEGER, " +
                        RECEIVER_TIME + " TEXT, " +
                        CREATE_TIME + " TEXT)"

        );

        sqLiteDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_NOTIFICATION +
                        "(" + NOTIFY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " /*INTEGER PRIMARY KEY,*/  +
                        NOTIFY_MESSAGE + " TEXT, " +
                        NOTIFY_TIME + " TEXT, " +
                        NOTIFY_TITLE + " TEXT, " +
                        NOTIFY_AVATAR + " INTEGER, " +
                        NOTIFY_FROMID + " INTEGER, " +
                        NOTIFY_TYPE + " INTEGER, " +
                        NOTIFY_STATUS + " INTEGER)"

        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PERSON_TABLE_USER);
        onCreate(sqLiteDatabase);
    }

    @Override
    public synchronized void close() {
        if (instance != null)
            db.close();
    }


    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
            db = instance.getWritableDatabase();
        }

        return instance;
    }


}
