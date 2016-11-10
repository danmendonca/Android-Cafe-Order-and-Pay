package pt.up.fe.cmov16.cafe.cafeapp.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.List;

import io.swagger.client.model.Blacklist;
import pt.up.fe.cmov16.cafe.cafeapp.logic.Request;

public class BlackListContract {

    private static final String TAG = BlackListEntry.class.toString();

    public BlackListContract() {

    }

    /* Inner class that defines the table contents */
    static abstract class BlackListEntry implements BaseColumns {
        static final String TABLE_NAME = "blacklist";
        static final String COLUMN_NAME_ID = "id";
        static final String COLUMN_NAME_COSTUMER_UUID = "costumer_uuid";
    }


    public static void blockUser(Context context, String costumerUUID) {
        if (context == null) {
            Log.e(TAG, "NULL CONTEXT UPDATING BLACKLIST DB");
            return;
        }
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db == null) {
            Log.e(TAG, "NULL DB UPDATING BLACKLIST DB");
            return;
        }
        int newBlock = 0;

        ContentValues values = new ContentValues();
        values.put(BlackListEntry.COLUMN_NAME_COSTUMER_UUID, costumerUUID);
        if (!isUserBlocked(context, costumerUUID))
            if (db.insertWithOnConflict(BlackListEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_ABORT) != -1)
                newBlock++;

        db.close();
        Log.e(TAG, "NEW USER BLACLISTED: " + newBlock);
    }

    public static boolean isUserBlocked(Context context, String costumerUUID) {
        if (context == null) {
            Log.e(TAG, "NULL CONTEXT UPDATING BLACKLIST DB");
            return false;
        }
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (db == null) {
            Log.e(TAG, "NULL DB UPDATING BLACKLIST DB");
            return false;
        }

        Cursor cursor = db.rawQuery(selectByCostumerUUID(costumerUUID), null);
        // if Cursor is contains results
        boolean blacklisted = false;
        if (cursor != null) {
            blacklisted = cursor.moveToFirst();
            cursor.close();
        }
        db.close();
        return blacklisted;
    }

    private static String selectByCostumerUUID(String costumerUUID) {
        return "SELECT * FROM " + BlackListEntry.TABLE_NAME
                + " WHERE " + BlackListEntry.COLUMN_NAME_COSTUMER_UUID + " = '" + costumerUUID + "'";
    }

    public static void blockUsers(Context context, List<Blacklist> blacklists, String createdAt) {
        if (context == null) {
            Log.e(TAG, "NULL CONTEXT blockUsers");
            return;
        }
        if (blacklists == null || blacklists.isEmpty()) {
            return;
        }
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db == null) {
            Log.e(TAG, "NULL DB blockUsers");
            return;
        }
        int newBlacklist = 0;
        for (Blacklist blacklist : blacklists) {

            Cursor c = db.rawQuery(selectByCostumerUUID(blacklist.getCostumerUuid()), null);
            if (c == null)
                continue;

            ContentValues values = new ContentValues();
            values.put(BlackListEntry.COLUMN_NAME_ID, blacklist.getId());
            values.put(BlackListEntry.COLUMN_NAME_COSTUMER_UUID, blacklist.getCostumerUuid());

            if (c.getCount() > 0) {
                c.moveToFirst();
                Log.e(TAG, "User: " + blacklist.getCostumerUuid() + " already in blacklist");
            } else {
                //INSERT
                if (db.insert(BlackListEntry.TABLE_NAME, null, values) != -1)
                    newBlacklist++;
            }
            c.close();
        }
        db.close();
        Log.e(TAG, "NEW: " + newBlacklist + " blacklisted" + "lastDate: " + createdAt);
        if (!createdAt.isEmpty()) {
            Request.saveUpdatedBlackListDate(context, createdAt);
        }

    }
}
