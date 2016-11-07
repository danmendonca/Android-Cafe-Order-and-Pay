package pt.up.fe.cmov16.cafe.cafeapp;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
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
import io.swagger.client.model.Products;
import pt.up.fe.cmov16.cafe.cafeapp.database.ProductContract;
import pt.up.fe.cmov16.cafe.cafeapp.ui.ProcessRequestActivity;
import pt.up.fe.cmov16.cafe.cafeapp.ui.ScanQRCodeActivity;
import pt.up.fe.cmov16.cafe.cafeapp.util.NfcApp;

public class MainActivity extends AppCompatActivity {
    private static int QR_CODE_CODE = 1;
    public static final String PRODUCTS_KEY = "PRODUCTS";
    public static final String VOUCHERS_KEY = "VOUCHERS";
    public static final String COSTUMER_KEY = "COSTUMERID";
    private NfcApp app;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = (NfcApp) getApplication();

        ApiInvoker.initializeInstance();
        loadProducts();
        findViewById(R.id.scanQRCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ScanQRCodeActivity.class);
                startActivityForResult(i, QR_CODE_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        if (requestCode == QR_CODE_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                Intent i = new Intent(MainActivity.this, ProcessRequestActivity.class);
                i.replaceExtras(bundle);
                startActivity(i);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        app.reply = new String(msg.getRecords()[0].getPayload());
        Log.e("Main", app.reply);
    }

    private void loadProducts() {
        DefaultApi api = new DefaultApi();
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
