package com.aseanfan.worldcafe.Helper;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "CroseaDB.db";
    private static final int DATABASE_VERSION = 2;

    public static final String PERSON_TABLE_USER = "user";
    public static final String PERSON_COLUMN_ID = "account_id";
    public static final String PERSON_COLUMN_NAME = "username";
    public static final String PERSON_COLUMN_EMAIL = "email";
    public static final String PERSON_PHONE_NUMBER = "phonenumber";
    public static final String PERSON_AVATAR_URL = "avarta";

    private static SQLiteDatabase db;
    private static DBHelper instance;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    public boolean insertPerson(Long id,String name, String email, String phonenumber , String avarta) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PERSON_COLUMN_ID, id);
        contentValues.put(PERSON_COLUMN_NAME, name);
        contentValues.put(PERSON_COLUMN_EMAIL, email);
        contentValues.put(PERSON_PHONE_NUMBER, phonenumber);
        contentValues.put(PERSON_AVATAR_URL, avarta);


        db.insert(PERSON_TABLE_USER, null, contentValues);
        return true;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, PERSON_TABLE_USER);
        return numRows;
    }

    public boolean updatePerson(Long id, String name, String email, String phonenumber,String avatar) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PERSON_COLUMN_NAME, name);
        contentValues.put(PERSON_COLUMN_EMAIL, email);
        contentValues.put(PERSON_PHONE_NUMBER, phonenumber);
        contentValues.put(PERSON_AVATAR_URL, avatar);
        db.update(PERSON_TABLE_USER, contentValues, PERSON_COLUMN_ID + " = ? ", new String[] { Long.toString(id) } );
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

    public Cursor getAllPersons() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + PERSON_TABLE_USER, null );
        return res;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE " + PERSON_TABLE_USER +
                        "(" + PERSON_COLUMN_ID + " INTEGER, " /*INTEGER PRIMARY KEY,*/  +
                        PERSON_COLUMN_NAME + " TEXT, " +
                        PERSON_COLUMN_EMAIL + " TEXT, " +
                        PERSON_PHONE_NUMBER + " TEXT, " +
                        PERSON_AVATAR_URL + " TEXT)"
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
