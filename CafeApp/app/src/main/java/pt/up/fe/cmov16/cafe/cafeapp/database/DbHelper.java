package pt.up.fe.cmov16.cafe.cafeapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pt.up.fe.cmov16.cafe.cafeapp.database.PendingRequestContract.PendingRequestEntry;
import pt.up.fe.cmov16.cafe.cafeapp.database.ProductContract.ProductEntry;
import pt.up.fe.cmov16.cafe.cafeapp.database.BlackListContract.BlackListEntry;

/**
 * Database model
 */
class DbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "acme_cafe.db";

    DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PRODUCTS);
        db.execSQL(SQL_CREATE_PENDING_REQUESTS);
        db.execSQL(SQL_CREATE_BLACKLIST);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_PRODUCTS);
        db.execSQL(SQL_DELETE_PENDING_REQUESTS);
        db.execSQL(SQL_DELETE_BLACKLIST);
        onCreate(db);
    }

    private static final String TEXT_TYPE = " TEXT ";

    private static final String SQL_CREATE_PRODUCTS =
            "CREATE TABLE " + ProductEntry.TABLE_NAME + " (" +
                    ProductEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    ProductEntry.COLUMN_NAME_NAME + TEXT_TYPE + "," +
                    ProductEntry.COLUMN_NAME_UNIT_PRICE + TEXT_TYPE + "," +
                    ProductEntry.COLUMN_NAME_ACTIVE + TEXT_TYPE +
                    " )";

    private static final String SQL_CREATE_BLACKLIST =
            "CREATE TABLE " + BlackListEntry.TABLE_NAME + " (" +
                    BlackListEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    BlackListEntry.COLUMN_NAME_COSTUMER_UUID + TEXT_TYPE +
                    " )";

    private static final String SQL_CREATE_PENDING_REQUESTS =
            "CREATE TABLE " + PendingRequestEntry.TABLE_NAME + " (" +
                    PendingRequestEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    PendingRequestEntry.COLUMN_NAME_REQUEST + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_PRODUCTS =
            "DROP TABLE IF EXISTS " + ProductEntry.TABLE_NAME;

    private static final String SQL_DELETE_PENDING_REQUESTS =
            "DROP TABLE IF EXISTS " + PendingRequestEntry.TABLE_NAME;

    private static final String SQL_DELETE_BLACKLIST =
            "DROP TABLE IF EXISTS " + BlackListEntry.TABLE_NAME;
}
