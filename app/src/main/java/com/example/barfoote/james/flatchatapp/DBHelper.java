package com.example.barfoote.james.flatchatapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by James on 11/17/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SQLiteUser.db";
    private static final int DATABASE_VERSION = 1;

    public static final String USER_TABLE_NAME = "user";
    public static final String USER_COLUMN_ID = "_id";
    public static final String USER_COLUMN_USER_ID = "userID";
    public static final String USER_COLUMN_EMAIL = "email";
    public static final String USER_COLUMN_FLAT_GROUP = "flatgroup";
    public static final String USER_COLUMN_PICTURE = "pic";

    public static final String FLATGROUP_TABLE_NAME = "fgroup";
    public static final String FLATGROUP_COLUMN_ID = "_id";
    public static final String FLATGROUP_COLUMN_GROUP_ID = "groupID";
    public static final String FLATGROUP_COLUMN_GROUP_NAME = "groupName";
    public static final String FLATGROUP_COLUMN_SHOPPINGLIST = "shoppingList";
    public static final String FLATGROUP_COLUMN_CALENDAR = "calendar";
    public static final String FLATGROUP_COLUMN_MONEY= "money";
    public static final String FLATGROUP_COLUMN_TODO_LIST= "todoList";
    public static final String FLATGROUP_COLUMN_OWNER_ID= "ownerID";
    public String email;

    private static final String createFlatGroup = "CREATE TABLE " + FLATGROUP_TABLE_NAME + "(" +
            FLATGROUP_COLUMN_ID + " INTEGER PRIMARY KEY, " +
            FLATGROUP_COLUMN_GROUP_ID + " INTEGER, " +
            FLATGROUP_COLUMN_GROUP_NAME + " TEXT, " +
            FLATGROUP_COLUMN_SHOPPINGLIST + " TEXT," +
            FLATGROUP_COLUMN_CALENDAR + " TEXT," +
            FLATGROUP_COLUMN_MONEY + " TEXT," +
            FLATGROUP_COLUMN_TODO_LIST + " TEXT," +
            FLATGROUP_COLUMN_OWNER_ID + " TEXT" + ")";

    private static final String createUser = "CREATE TABLE " + USER_TABLE_NAME + "(" +
            USER_COLUMN_ID + " INTEGER PRIMARY KEY, " +
            USER_COLUMN_USER_ID + " INTEGER, " +
            USER_COLUMN_EMAIL + " TEXT, " +
            USER_COLUMN_PICTURE + " TEXT," +
            USER_COLUMN_FLAT_GROUP + " TEXT" + ")";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createUser);

        Log.v("Users table created", "");
        db.execSQL(createFlatGroup);
        Log.v("Group table created", "Group table created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {// TODO: 11/17/2015 change this so it properly upgrades
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertUser(int userID, String email, String pic, String flatGroup) {
        clearUserTable();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COLUMN_USER_ID, userID);
        contentValues.put(USER_COLUMN_EMAIL, email);
        contentValues.put(USER_COLUMN_PICTURE, pic);
        contentValues.put(USER_COLUMN_FLAT_GROUP, flatGroup);
        db.insert(USER_TABLE_NAME, null, contentValues);
        this.email = email;
        return true;
    }

    public boolean updatePerson(Integer id, int userID, String email, String pic, String flatGroup) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COLUMN_USER_ID, userID);
        contentValues.put(USER_COLUMN_EMAIL, email);
        contentValues.put(USER_COLUMN_PICTURE, pic);
        contentValues.put(USER_COLUMN_FLAT_GROUP, flatGroup);
        db.update(USER_TABLE_NAME, contentValues, USER_COLUMN_ID + " = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public Cursor getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + USER_TABLE_NAME + " WHERE " +
                USER_COLUMN_ID + "=?", new String[] { Integer.toString(id) } );
        return res;
    }

    public String getGroup()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + USER_TABLE_NAME;
        Cursor  cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            Log.d("in if", "");
                return cursor.getString(cursor.getColumnIndex("flatgroup"));

        }
        return "";
    }

    public int getUserID(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor userC = db.rawQuery("SELECT userID FROM " + USER_TABLE_NAME + " WHERE " +
                USER_COLUMN_ID + "=?", new String[] { Integer.toString(id) });

        return userC.getInt(0);
    }

//    public Cursor getGroup(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery( "SELECT flatgroup FROM " + USER_TABLE_NAME + " WHERE " +
//                USER_COLUMN_ID + "=?", new String[] { Integer.toString(id) } );
//        return res;
//    }
    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + USER_TABLE_NAME, null );
        return res;
    }

    public Cursor getAllGroup() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + FLATGROUP_TABLE_NAME, null );
        return res;
    }

    public Integer deleteUser(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(USER_TABLE_NAME,
                USER_COLUMN_ID + " = ? ",
                new String[] { Integer.toString(id) });
    }

    public boolean insertGroup(int group_id, String groupName, String shoppingList, String calendar, String money, String todoList, String ownerID)
    {
        clearGroupTable();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FLATGROUP_COLUMN_GROUP_ID, group_id);
        contentValues.put(FLATGROUP_COLUMN_GROUP_NAME, groupName);
        contentValues.put(FLATGROUP_COLUMN_SHOPPINGLIST, shoppingList);
        contentValues.put(FLATGROUP_COLUMN_CALENDAR, calendar);
        contentValues.put(FLATGROUP_COLUMN_MONEY, money);
        contentValues.put(FLATGROUP_COLUMN_TODO_LIST, todoList);
        contentValues.put(FLATGROUP_COLUMN_OWNER_ID, ownerID);
        db.insert(FLATGROUP_TABLE_NAME, null, contentValues);
        addGroupToUser(groupName);
        return true;
    }

    public void addGroupToUser(String groupname)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COLUMN_FLAT_GROUP, groupname);
        db.update(USER_TABLE_NAME, contentValues, USER_COLUMN_FLAT_GROUP + " = ? ", new String[]{Integer.toString(0)});

    }

    public String getEmail()
    {
        return this.email;
    }

    public void clearUserTable()   {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(USER_TABLE_NAME, null,null);
    }

    public void clearGroupTable()   {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FLATGROUP_TABLE_NAME, null,null);
    }

}
