package gr.hua.android.gpsapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper {

    public static final String TABLENAME = "user_locations";
    private static final int DBVERSION = 1;
    public static final String USERID = "_USERID";
    public static final String USERNAME = "_USERNAME";
    public static final String LOCATION = "_CURRENTLOCATION";

    public static ContentValues fillRow(Location loc) {
        ContentValues values = new ContentValues();
        values.put(USERNAME, loc.getUserID());
        values.put(LOCATION, loc.getCurrent_location());
        return values;
    }

    private String sqlQuery = "CREATE TABLE IF NOT EXISTS " + TABLENAME + " (" +
            USERID + " INTEGER PRIMARY KEY, " +
            USERNAME + " TEXT, " +
            LOCATION + " TEXT" +
            ");";

    public DBHelper(Context context) {
        super(context, TABLENAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insert(Location c) {
        ContentValues values = fillRow(c);
        return this.getWritableDatabase().insert(TABLENAME, null, values);
    }

    public ArrayList<Location> selectWhereId(int id) {
        String[] selArgs = new String[1];
        selArgs[0] = "" + id;
        Cursor result = this.getReadableDatabase().query(TABLENAME, null, "_ID=?", selArgs, null, null, null);
        return CursorToContact(result);
    }

    public ArrayList<Location> selectAll() {
        Cursor result = this.getReadableDatabase().query(TABLENAME, null, null, null, null, null, null);
        return CursorToContact(result);
    }

    private ArrayList<Location> CursorToContact(Cursor cursor) {
        ArrayList<Location> contacts = new ArrayList<>();
        if (!cursor.moveToFirst()) {
            return null;
        }
        do {
            Location loc = new Location();
            loc.setUserID(cursor.getString(0));
            loc.setUsername(cursor.getString(1));
            loc.setCurrent_location(cursor.getString(2));
            contacts.add(loc);
        } while (cursor.moveToNext());
        return contacts;
    }
}