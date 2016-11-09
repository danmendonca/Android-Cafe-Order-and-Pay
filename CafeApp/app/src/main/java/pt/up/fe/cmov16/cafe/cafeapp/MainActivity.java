package pt.up.fe.cmov16.cafe.cafeapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import io.swagger.client.ApiInvoker;
import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.HelloWorldResponse;
import io.swagger.client.model.Products;
import pt.up.fe.cmov16.cafe.cafeapp.database.BlackListContract;
import pt.up.fe.cmov16.cafe.cafeapp.database.DbHelper;
import pt.up.fe.cmov16.cafe.cafeapp.database.ProductContract;
import pt.up.fe.cmov16.cafe.cafeapp.logic.Request;
import pt.up.fe.cmov16.cafe.cafeapp.ui.ProcessRequestActivity;
import pt.up.fe.cmov16.cafe.cafeapp.ui.ScanQRCodeActivity;
import pt.up.fe.cmov16.cafe.cafeapp.util.RequestsThread;

public class MainActivity extends AppCompatActivity {
    public static final String ENCODED_STRING_KEY = "ENCODED_STRING";
    private static final String TAG = MainActivity.class.toString();
    private static final int NFC_CODE = 2;
    private static int QR_CODE_CODE = 1;

    DefaultApi api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApiInvoker.initializeInstance();
        api = new DefaultApi();
        loadProducts();
        loadPublicKey();
//      Request.isValid(MainActivity.this,"");
        findViewById(R.id.scanQRCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ScanQRCodeActivity.class);
                startActivityForResult(i, QR_CODE_CODE);
            }
        });

        //Toast.makeText(this, "ALERT USING SERVER: " + (new DefaultApi()).getBasePath(), Toast.LENGTH_SHORT).show();

        api.hello("Acme", new Response.Listener<HelloWorldResponse>() {
                    @Override
                    public void onResponse(HelloWorldResponse response) {
                        Toast.makeText(getApplicationContext(), "ACME POWER - Connected", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error == null || error.getMessage() == null || error.getMessage().isEmpty())
                            return;
                        Log.e(TAG, error.getMessage());
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        NfcManager manager = (NfcManager) this.getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();
        if (adapter == null) {
            Log.e(TAG, "Device haven't NFC ");
        } else if (!adapter.isEnabled()) {
            Log.e(TAG, "NFC isn't enabled ");
        } else {
            Log.i(TAG, "NFC is ready to use");
        }
        RequestsThread requestsThread = new RequestsThread(MainActivity.this);
        requestsThread.start();

//        DbHelper dbHelper = new DbHelper(MainActivity.this);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//        db.execSQL("DELETE FROM "+BlackListContract.BlackListEntry.TABLE_NAME);
//        Cursor c = db.rawQuery("SELECT * FROM " + BlackListContract.BlackListEntry.TABLE_NAME, null, null);
//        Log.e(TAG,c.getCount()+"");
//        c.close();
//        db.close();
    }

    private void loadPublicKey() {
        if (!Request.hasPublicKey(MainActivity.this)) {
            api.getPublicKey(new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Request.savePublicKey(MainActivity.this, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Unable to get public key from server");
                    throw new RuntimeException("Unable to get public key from server");
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        if (requestCode == QR_CODE_CODE || requestCode == NFC_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Intent i = new Intent(MainActivity.this, ProcessRequestActivity.class);
                i.replaceExtras(data.getExtras());
                startActivity(i);
            }
        }
    }

    private void loadProducts() {
        //if updatedAt was saved before, the skip this step and use its value in lastDate
        String lastUpdated = ProductContract.lastUpdatedProductDate(this);
        boolean stored = !lastUpdated.isEmpty();
        if (!stored)
            lastUpdated = getDefaultTimestamp();

        Log.e("products", lastUpdated);
        api.getProducts(lastUpdated,
                new Response.Listener<Products>() {
                    @Override
                    public void onResponse(Products response) {
                        if (response.getProducts().size() > 0) {
                            ProductContract.updateProducts(MainActivity.this, response.getProducts());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,
                                "Connection failed, using local products", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getDefaultTimestamp() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        java.util.Date date;
        Timestamp ts;
        try {
            date = dateFormat.parse("1900/01/01");
            long time = date.getTime();
            ts = new Timestamp(time);
        } catch (ParseException e) {
            e.printStackTrace();
            ts = new Timestamp(0);
        }


        return ts.toString().replace(' ', 'T');
    }
}
