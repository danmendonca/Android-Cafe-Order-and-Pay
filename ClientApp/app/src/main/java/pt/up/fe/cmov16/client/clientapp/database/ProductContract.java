package pt.up.fe.cmov16.client.clientapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;

import io.swagger.client.model.Product;
import pt.up.fe.cmov16.client.clientapp.util.IFunction;

/**
 * Event model
 */

public final class ProductContract {

    public ProductContract() {
    }

    /* Inner class that defines the table contents */
    static abstract class ProductEntry implements BaseColumns {
        static final String TABLE_NAME = "products";
        static final String COLUMN_NAME_ID = "id";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_UNITPRICE = "unitprice";
    }

    private static final String SELECT_ALL_PRODUCTS =
            "SELECT * FROM " + ProductEntry.TABLE_NAME
                    + " order by " + ProductEntry.COLUMN_NAME_NAME + " asc";

    private static final String DELETE_ALL_PRODUCTS = "DELETE FROM " + ProductEntry.TABLE_NAME;

    public void replaceProducts(Context context, ArrayList<Product> products) {
        if (products == null || products.isEmpty()) {
            LogError("empty array");
            return;
        }
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.execSQL(DELETE_ALL_PRODUCTS);

        ContentValues values = new ContentValues();

        for (Product prod : products) {
            values.clear();
            values.put(ProductEntry.COLUMN_NAME_ID, prod.getId());
            values.put(ProductEntry.COLUMN_NAME_NAME, prod.getName());
            values.put(ProductEntry.COLUMN_NAME_UNITPRICE, String.valueOf(prod.getUnitprice()));
            db.insert(ProductEntry.TABLE_NAME, "null", values);
        }
        db.close();
    }

    public void loadProducts(final Context context, final IFunction onLoadExecute) {
        new AsyncTask<Void, Void, ArrayList<Product>>() {

            @Override
            protected ArrayList<Product> doInBackground(Void... params) {
                ArrayList<Product> products = new ArrayList<>();

                if (context == null)
                    return products;

                DbHelper mDbHelper = new DbHelper(context);
                if (mDbHelper == null)
                    return products;
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
                                    ProductEntry.COLUMN_NAME_UNITPRICE));

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

            @Override
            protected void onPostExecute(ArrayList products) {
                onLoadExecute.execute(products);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }



    private void LogError(String msg) {
        Log.e(ProductContract.class.toString(), msg);
    }
}
