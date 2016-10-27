package pt.up.fe.cmov16.client.clientapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;

import io.swagger.client.model.Product;

/**
 * Product model and related db operations
 */

public final class ProductContract {

    private static final String TAG = ProductContract.class.toString();

    public ProductContract() {
    }

    /* Inner class that defines the table contents */
    static abstract class ProductEntry implements BaseColumns {
        static final String TABLE_NAME = "products";
        static final String COLUMN_NAME_ID = "id";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_UNIT_PRICE = "unitPrice";
        static final String COLUMN_NAME_ACTIVE = "active";
        static final String COLUMN_NAME_UPDATED_AT = "updatedAt";
    }

    private static final String SELECT_LAST_UPDATED_PRODUCT =
            "SELECT " + ProductEntry.COLUMN_NAME_UPDATED_AT + " FROM " + ProductEntry.TABLE_NAME
                    + " order by " + ProductEntry.COLUMN_NAME_UPDATED_AT + " desc";

    private static final String SELECT_ALL_PRODUCTS =
            "SELECT * FROM " + ProductEntry.TABLE_NAME
                    + " order by " + ProductEntry.COLUMN_NAME_NAME + " asc";

    //private static final String DELETE_ALL_PRODUCTS = "DELETE FROM " + ProductEntry.TABLE_NAME;

    public void updateProducts(Context context, ArrayList<Product> products) {
        if (products == null || products.isEmpty()) {
            LogError("empty array");
            return;
        }
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int newProducts = 0, updatedProducts = 0;
        for (Product product : products) {
            if (product == null) {
                Log.e(TAG, "Contacts array has null user");
                continue;
            }

            Cursor c = db.rawQuery(selectByProductIDQuery(product.getId()), null);
            if (c == null)
                continue;

            ContentValues values = new ContentValues();
            values.put(ProductEntry.COLUMN_NAME_ID, product.getId());
            values.put(ProductEntry.COLUMN_NAME_NAME, product.getName());
            values.put(ProductEntry.COLUMN_NAME_ACTIVE, product.getActive());
            values.put(ProductEntry.COLUMN_NAME_UNIT_PRICE, product.getUnitprice());
            values.put(ProductEntry.COLUMN_NAME_UPDATED_AT, product.getUpdatedat());

            if (c.getCount() > 0) {
                //UPDATE
                String where = ProductEntry.COLUMN_NAME_ID + "=" + product.getId();
                db.update(ProductEntry.TABLE_NAME, values, where, null);
                updatedProducts++;
            } else {
                //INSERT
                if (db.insert(ProductEntry.TABLE_NAME, null, values) != -1)
                    newProducts++;
            }
            c.close();
        }
        db.close();
        Log.e(TAG, "UPDATED: " + updatedProducts + " and NEW: " + newProducts + " products");
    }

    private String selectByProductIDQuery(int id) {
        return "SELECT * FROM " + ProductEntry.TABLE_NAME + " where "
                + ProductEntry.COLUMN_NAME_ID + "=" + id;
    }

    public ArrayList<Product> loadProducts(final Context context) {

        ArrayList<Product> products = new ArrayList<>();

        if (context == null)
            return products;

        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        if (db == null)
            return products;

        Cursor cursor = db.rawQuery(ProductContract.SELECT_ALL_PRODUCTS, null);
        // if Cursor is contains results
        if (cursor != null) {
            // move cursor to first row
            if (cursor.moveToFirst()) {
                do {
                    // Get version from Cursor
                    String name = cursor.getString(cursor.getColumnIndex(
                            ProductEntry.COLUMN_NAME_NAME));

                    String unitPrice = cursor.getString(cursor.getColumnIndex(
                            ProductEntry.COLUMN_NAME_UNIT_PRICE));

                    Product prod = new Product();
                    prod.setUnitprice(Double.valueOf(unitPrice));
                    prod.setName(name);
                    products.add(prod);
                    // move to next row
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return products;
    }

    public String lastUpdatedProductDate(Context context) {
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_LAST_UPDATED_PRODUCT, null);
        if (cursor == null) {
            db.close();
            return null;
        }

        if (cursor.getCount() == 0) {
            db.close();
            return null;
        }
        String date;
        cursor.moveToFirst();
        date = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_NAME_UPDATED_AT));
        cursor.close();
        db.close();
        return date;
    }

    private void LogError(String msg) {
        Log.e(ProductContract.class.toString(), msg);
    }
}
