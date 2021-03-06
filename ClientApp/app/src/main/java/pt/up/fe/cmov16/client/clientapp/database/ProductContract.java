package pt.up.fe.cmov16.client.clientapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.swagger.client.model.Product;
import pt.up.fe.cmov16.client.clientapp.R;

/**
 * Product model and related db operations
 */

public final class ProductContract {

    private static final String TAG = ProductContract.class.toString();
    private static final String SELECT_ALL_ACTIVE_PRODUCTS =
            "SELECT * FROM " + ProductEntry.TABLE_NAME
                    + " where " + ProductEntry.COLUMN_NAME_ACTIVE + " = 1"
                    + " order by " + ProductEntry.COLUMN_NAME_NAME + " asc";

    public ProductContract() {
    }

    /**
     * Update local database products
     *
     * @param context
     * @param products
     * @return products that are active
     */
    public static void updateProducts(Context context, List<Product> products) {
        if (context == null) {
            Log.e(TAG, "NULL CONTEXT UPDATING PRODUCTS DB");
            return;
        }
        String updatedAt = "";
        if (products == null || products.isEmpty()) {
            return;
        }
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db == null) {
            Log.e(TAG, "NULL DB UPDATING PRODUCTS DB");
            return;
        }
        int newProducts = 0, updatedProducts = 0;
        for (Product product : products) {
            if (product == null) {
                Log.e(TAG, "products array has null product");
                continue;
            }

            if (updatedAt.isEmpty())
                updatedAt = product.getUpdatedAt();
            else if (product.getUpdatedAt().compareTo(updatedAt) > 0)
                updatedAt = product.getUpdatedAt();

            Cursor c = db.rawQuery(selectByProductIDQuery(product.getId()), null);
            if (c == null)
                continue;

            ContentValues values = new ContentValues();
            values.put(ProductEntry.COLUMN_NAME_ID, product.getId());
            values.put(ProductEntry.COLUMN_NAME_NAME, product.getName());
            values.put(ProductEntry.COLUMN_NAME_ACTIVE, product.getActive());
            values.put(ProductEntry.COLUMN_NAME_UNIT_PRICE, product.getUnitprice());

            if (c.getCount() > 0) {
                c.moveToFirst();
                String name = c.getString(c.getColumnIndex(
                        ProductEntry.COLUMN_NAME_NAME));

                float unitPrice = Float.valueOf(c.getString(c.getColumnIndex(
                        ProductEntry.COLUMN_NAME_UNIT_PRICE)));

                boolean active = c.getString(c.getColumnIndex(
                        ProductEntry.COLUMN_NAME_ACTIVE)).equals("1");

                if (!name.equals(product.getName()) || unitPrice != product.getUnitprice()
                        || active != product.getActive()) {
                    //UPDATE
                    String where = ProductEntry.COLUMN_NAME_ID + "=" + product.getId();
                    db.update(ProductEntry.TABLE_NAME, values, where, null);
                    updatedProducts++;
                }
            } else {
                //INSERT
                if (db.insert(ProductEntry.TABLE_NAME, null, values) != -1)
                    newProducts++;
            }
            c.close();
        }
        db.close();
        Log.e(TAG, "UPDATED: " + updatedProducts + " and NEW: " + newProducts + " products");
        if (!updatedAt.isEmpty())
            saveUpdatedProductDate(context, updatedAt);
    }

    //private static final String DELETE_ALL_PRODUCTS = "DELETE FROM " + ProductEntry.TABLE_NAME;

    private static String selectByProductIDQuery(int id) {
        return "SELECT * FROM " + ProductEntry.TABLE_NAME + " where "
                + ProductEntry.COLUMN_NAME_ID + "=" + id;
    }

    public static ArrayList<Product> loadProducts(final Context context) {

        ArrayList<Product> products = new ArrayList<>();

        if (context == null)
            return products;

        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        if (db == null)
            return products;

        Cursor cursor = db.rawQuery(ProductContract.SELECT_ALL_ACTIVE_PRODUCTS, null);
        // if Cursor is contains results
        if (cursor != null) {
            // move cursor to first row
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(
                            ProductEntry.COLUMN_NAME_ID));

                    // Get version from Cursor
                    String name = cursor.getString(cursor.getColumnIndex(
                            ProductEntry.COLUMN_NAME_NAME));

                    String unitPrice = cursor.getString(cursor.getColumnIndex(
                            ProductEntry.COLUMN_NAME_UNIT_PRICE));

                    Product prod = new Product();
                    prod.setUnitprice(Double.valueOf(unitPrice));
                    prod.setName(name);
                    prod.setId(id);
                    products.add(prod);
                    // move to next row
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return products;
    }

    public static void saveUpdatedProductDate(Context context, String date) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("lastUpdatedProductDate", date);
        editor.apply();
    }

    public static String lastUpdatedProductDate(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return sharedPref.getString("lastUpdatedProductDate", "");
    }

    public static Product getProductByID(Context context, int id) {
        if (context == null) {
            Log.e(TAG, "NULL CONTEXT UPDATING PRODUCTS DB");
            return null;
        }
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectByProductIDQuery(id), null);

        if (c != null && c.moveToFirst()) {
            // Get version from Cursor
            String name = c.getString(c.getColumnIndex(
                    ProductEntry.COLUMN_NAME_NAME));

            String unitPrice = c.getString(c.getColumnIndex(
                    ProductEntry.COLUMN_NAME_UNIT_PRICE));

            Product prod = new Product();
            prod.setUnitprice(Double.valueOf(unitPrice));
            prod.setName(name);
            prod.setId(id);
            c.close();
            db.close();
            return prod;
        } else {
            if (c != null)
                c.close();
            db.close();
            return null;
        }


    }

    /* Inner class that defines the table contents */
    static abstract class ProductEntry implements BaseColumns {
        static final String TABLE_NAME = "products";
        static final String COLUMN_NAME_ID = "id";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_UNIT_PRICE = "unitPrice";
        static final String COLUMN_NAME_ACTIVE = "active";
    }
}
