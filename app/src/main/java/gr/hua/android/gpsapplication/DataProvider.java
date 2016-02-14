package gr.hua.android.gpsapplication;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DataProvider extends ContentProvider {

    private static final String AUTHORITY = "gr.hua.android.dataprovider";
    private static final String PATH = "dataprovider";

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, PATH, 1);
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
        long id = -1;
        SQLiteDatabase mDB = mDBHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case 1:
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
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase mDB = mDBHelper.getReadableDatabase();
        Cursor mCursor;
        switch (sUriMatcher.match(uri)) {
            case 1:
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

