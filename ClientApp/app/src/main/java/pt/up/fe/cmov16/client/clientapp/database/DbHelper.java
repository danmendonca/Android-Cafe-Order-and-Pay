package pt.up.fe.cmov16.client.clientapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pt.up.fe.cmov16.client.clientapp.database.ProductContract.ProductEntry;
import pt.up.fe.cmov16.client.clientapp.database.VoucherContract.VoucherEntry;

/**
 * Database model
 */
class DbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "acme_cafe.db";

    DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PRODUCTS);
        db.execSQL(SQL_CREATE_VOUCHERS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_PRODUCTS);
        db.execSQL(SQL_DELETE_VOUCHERS);
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

    private static final String SQL_CREATE_VOUCHERS =
            "CREATE TABLE " + VoucherEntry.TABLE_NAME + " (" +
                    VoucherEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    VoucherEntry.COLUMN_NAME_TYPE + TEXT_TYPE + "," +
                    VoucherEntry.COLUMN_NAME_SIGNATURE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_PRODUCTS =
            "DROP TABLE IF EXISTS " + ProductEntry.TABLE_NAME;
    private static final String SQL_DELETE_VOUCHERS =
            "DROP TABLE IF EXISTS " + VoucherEntry.TABLE_NAME;
}
