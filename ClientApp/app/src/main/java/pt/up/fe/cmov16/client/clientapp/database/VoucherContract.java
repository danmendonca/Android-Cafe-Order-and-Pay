package pt.up.fe.cmov16.client.clientapp.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.swagger.client.model.Voucher;

public class VoucherContract {

    private static final String TAG = VoucherContract.class.toString();

    public VoucherContract() {

    }

    /* Inner class that defines the table contents */
    static abstract class VoucherEntry implements BaseColumns {
        static final String TABLE_NAME = "vouchers";
        static final String COLUMN_NAME_ID = "id";
        static final String COLUMN_NAME_TYPE = "type";
        static final String COLUMN_NAME_SIGNATURE = "signature";
    }

    private final static String SELECT_ALL_VOUCHERS =
            "SELECT * FROM " + VoucherEntry.TABLE_NAME;

    public static void saveVoucherInDB(Context context, List<Voucher> vouchers) {
        if (context == null) {
            Log.e(TAG, "NULL CONTEXT UPDATING PRODUCTS DB");
            return;
        }
        if (vouchers == null || vouchers.isEmpty()) {
            Log.e(TAG, "empty array");
            return;
        }
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db == null) {
            Log.e(TAG, "NULL DB UPDATING PRODUCTS DB");
            return;
        }
        int newVouchers = 0;
        for (Voucher voucher : vouchers) {
            ContentValues values = new ContentValues();
            values.put(VoucherEntry.COLUMN_NAME_ID, voucher.getId());
            values.put(VoucherEntry.COLUMN_NAME_TYPE, voucher.getType());
            values.put(VoucherEntry.COLUMN_NAME_SIGNATURE, voucher.getSignature());
            //INSERT
            if (db.insert(VoucherEntry.TABLE_NAME, null, values) != -1)
                newVouchers++;
        }
        Log.e(TAG, "NEW VOUCHERS: " + newVouchers);
    }

    public static ArrayList<Voucher> loadVouchers(final Context context) {

        ArrayList<Voucher> vouchers = new ArrayList<>();

        if (context == null)
            return vouchers;

        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        if (db == null)
            return vouchers;

        Cursor cursor = db.rawQuery(SELECT_ALL_VOUCHERS, null);
        // if Cursor is contains results
        if (cursor != null) {
            // move cursor to first row
            if (cursor.moveToFirst()) {
                do {
                    // Get version from Cursor
                    int id = cursor.getInt(cursor.getColumnIndex(
                            VoucherEntry.COLUMN_NAME_ID));

                    int type = cursor.getInt(cursor.getColumnIndex(
                            VoucherEntry.COLUMN_NAME_TYPE));

                    String signature = cursor.getString(cursor.getColumnIndex(
                            VoucherEntry.COLUMN_NAME_SIGNATURE));

                    Voucher voucher = new Voucher();
                    voucher.setId(id);
                    voucher.setType(type);
                    voucher.setSignature(signature);
                    vouchers.add(voucher);
                    // move to next row
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return vouchers;
    }
}
