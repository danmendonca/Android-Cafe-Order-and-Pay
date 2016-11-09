package pt.up.fe.cmov16.cafe.cafeapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;

import pt.up.fe.cmov16.cafe.cafeapp.logic.Request;

public class PendingRequestContract {

    private static final String TAG = PendingRequestContract.class.toString();
    private static final String SELECT_ALL_REQUESTS =
            "SELECT * FROM " + PendingRequestEntry.TABLE_NAME;

    /* Inner class that defines the table contents */
    static abstract class PendingRequestEntry implements BaseColumns {
        static final String TABLE_NAME = "pendingrequest";
        static final String COLUMN_NAME_ID = "id";
        static final String COLUMN_NAME_REQUEST = "request";
    }

    public PendingRequestContract() {

    }

    public static ArrayList<Request> getPendingRequests(Context context) {
        ArrayList<Request> pendingRequests = new ArrayList<>();

        if (context == null) {
            Log.e(TAG, "NULL CONTEXT UPDATING PENDING REQUESTS DB");
            return pendingRequests;
        }
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (db == null) {
            Log.e(TAG, "NULL DB UPDATING PENDING REQUESTS DB");
            return pendingRequests;
        }
        Cursor cursor = db.rawQuery(SELECT_ALL_REQUESTS, null);
        // if Cursor is contains results
        if (cursor != null) {
            // move cursor to first row
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(
                            PendingRequestEntry.COLUMN_NAME_ID));
                    // Get version from Cursor
                    String request = cursor.getString(cursor.getColumnIndex(
                            PendingRequestEntry.COLUMN_NAME_REQUEST));

                    pendingRequests.add(new Request(id, request));
                    // move to next row
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();

        return pendingRequests;
    }

    public static int deletePendingRequest(Context context, Request request) {
        if (context == null) {
            Log.e(TAG, "NULL CONTEXT UPDATING PENDING REQUESTS DB");
            return -1;
        }
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db == null) {
            Log.e(TAG, "NULL DB UPDATING PENDING REQUESTS DB");
            return -1;
        }
        String where = PendingRequestEntry.COLUMN_NAME_ID + " = " + request.getID();
        return db.delete(PendingRequestEntry.TABLE_NAME, where, null);
    }

    public static void savePendingRequest(Context context, String request) {
        if (context == null) {
            Log.e(TAG, "NULL CONTEXT UPDATING PENDING REQUESTS DB");
            return;
        }
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db == null) {
            Log.e(TAG, "NULL DB UPDATING PENDING REQUESTS DB");
            return;
        }
        int newReq = 0;

        ContentValues values = new ContentValues();
        values.put(PendingRequestEntry.COLUMN_NAME_REQUEST, request);

        if (db.insert(PendingRequestEntry.TABLE_NAME, null, values) != -1)
            newReq++;

        db.close();
        //Log.e(TAG, "NEW: " + newReq + " requests");
    }
}
