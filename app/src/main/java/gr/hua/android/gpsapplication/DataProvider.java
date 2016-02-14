package gr.hua.android.gpsapplication;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DataProvider extends ContentProvider {

    private static final String AUTHORITY = "gr.hua.android.gpsapplication/";
    private static final String PATH = "locations";
    private static final Uri uri = Uri.parse("content://" + AUTHORITY + PATH);

    private static final int POST = 1;
    private static final int GET = 2;


    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mUriMatcher.addURI(AUTHORITY, PATH + "/add", POST);
    }

    static {
        mUriMatcher.addURI(AUTHORITY, PATH + "/get", GET);
    }

    DBHelper mDBHelper;

    @Override
    public int delete(Uri arg0, String arg1, String[] arg2) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id;
        SQLiteDatabase mDB = mDBHelper.getWritableDatabase();
        switch (mUriMatcher.match(uri)) {
            case POST:
                id = mDB.insert(DBHelper.TABLENAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Content URI pattern not recognizable: " + uri);
        }
        return Uri.parse(uri.toString() + "/" + id);
    }

    @Override
    public boolean onCreate() {
        mDBHelper = new DBHelper(this.getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase mDB = mDBHelper.getReadableDatabase();
        Cursor mCursor;
        switch (mUriMatcher.match(uri)) {
            case GET:
                mCursor = mDB.query(DBHelper.TABLENAME, null, null, null, null, null, null);
                break;
            default:
                throw new IllegalArgumentException("Content URI pattern not recognizable: " + uri);
        }
        return mCursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

}

