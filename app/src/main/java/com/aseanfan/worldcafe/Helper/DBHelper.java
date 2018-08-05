package com.aseanfan.worldcafe.Helper;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.aseanfan.worldcafe.Model.UserModel;

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
    public static final String MAEEAGE = "message";
    public static final String TYPE = "type";
    public static final String FROM_ACCOUNT_ID = "from_account_id";
    public static final String ISRECEIVE = "isreceive";
    public static final String CREATE_TIME = "create_time";
    public static final String STATUS = "status";
    public static final String GENDER = "gender";


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
        return true;
    }

    public Integer deletePerson(Long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(PERSON_TABLE_USER,
                PERSON_COLUMN_ID + " = ? ",
                new String[] { Long.toString(id) });
    }

    public Cursor getPerson(Long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM " + PERSON_TABLE_USER + " WHERE " +
                PERSON_COLUMN_ID + "=?", new String[]{Long.toString(id)});
        return res;
    }

    public void CreateMessageTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_MESSAGE_CHAT +
                        "(" + MESSAGE_ID + " INTEGER, " /*INTEGER PRIMARY KEY,*/  +
                        MAEEAGE + " TEXT, " +
                        TYPE + " INTEGER, " +
                        FROM_ACCOUNT_ID + " INTEGER, " +
                        PERSON_AVATAR_URL + " TEXT, " +
                        ISRECEIVE + " INTEGER, " +
                        CREATE_TIME + " TEXT, " +
                        STATUS+ " TEXT, " +
                        GENDER + " TEXT)"

        );
    }

    public Cursor getAllPersons() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + PERSON_TABLE_USER, null );
        return res;
    }

    public Cursor getAllMessagebyId(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + TABLE_MESSAGE_CHAT + "WHERE " + FROM_ACCOUNT_ID + " = " + id, null );
        return res;
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
                        MAEEAGE + " TEXT, " +
                        TYPE + " INTEGER, " +
                        FROM_ACCOUNT_ID + " INTEGER, " +
                        PERSON_AVATAR_URL + " TEXT, " +
                        ISRECEIVE + " INTEGER, " +
                        CREATE_TIME + " TEXT, " +
                        STATUS+ " TEXT, " +
                        GENDER + " TEXT)"

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
