package app.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import app.util.Utils;

import static android.provider.BaseColumns._ID;

public abstract class MainDAOHelper extends SQLiteOpenHelper implements MainDatabaseConstants {

    public static final String SERVER_ADDRESS_TABLE_NAME = "server_address";
    public static final String SERVER_ADDRESS_NAME = "name";
    public static final String SERVER_ADDRESS_API_HOST = "apiHost";
    public static final String SERVER_ADDRESS_API_PORT = "apiPort";
    public static final String SERVER_ADDRESS_IS_DEFAULT = "isDefault";
    public static final String[] SERVER_ADDRESS_ALL_COLUMS = {_ID, SERVER_ADDRESS_NAME,
            SERVER_ADDRESS_API_HOST, SERVER_ADDRESS_API_PORT, SERVER_ADDRESS_IS_DEFAULT};

    protected Context mContext;
    private Integer oldVersion = null;

    public MainDAOHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + SERVER_ADDRESS_TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SERVER_ADDRESS_NAME + " TEXT NOT NULL, " +
                SERVER_ADDRESS_API_HOST + " TEXT NOT NULL, " +
                SERVER_ADDRESS_API_PORT + " INTEGER NOT NULL, " +
                SERVER_ADDRESS_IS_DEFAULT + " INTEGER NOT NULL" +
                ");");

        if (oldVersion == null) {
            initServerAddress(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.oldVersion = oldVersion;
        db.execSQL("DROP TABLE IF EXISTS " + SERVER_ADDRESS_TABLE_NAME);
        onCreate(db);
    }

    protected void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    protected void closeDb(SQLiteDatabase db) {
        if (db != null) {
            db.close();
        }
    }

    protected Context getContext() {
        return this.mContext;
    }

    public SQLiteDatabase getReadableOrWritableDatabase() {
        SQLiteDatabase db = null;
        try {
            db = getReadableDatabase();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        if (db != null) {
            return db;
        } else {
            return getWritableDatabase();
        }
    }

    private void initServerAddress(SQLiteDatabase db) {
        String[] addresses = {"1", "2"};
        for (String str : addresses) {
            String[] column = Utils.getConfigColumn(str);
            ContentValues values = new ContentValues();
            values.put(SERVER_ADDRESS_NAME, column[0]);
            values.put(SERVER_ADDRESS_API_HOST, column[1]);
            values.put(SERVER_ADDRESS_API_PORT, Integer.valueOf(column[2]));
            values.put(SERVER_ADDRESS_IS_DEFAULT, Integer.valueOf(column[3]) > 0);
            db.insert(SERVER_ADDRESS_TABLE_NAME, null, values);
        }
    }
}
