package pt.up.fe.cmov16.cafe.cafeapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;

import io.swagger.client.ApiInvoker;
import io.swagger.client.model.Voucher;
import pt.up.fe.cmov16.cafe.cafeapp.logic.ProductMenuItem;
import pt.up.fe.cmov16.cafe.cafeapp.ui.ProcessRequestActivity;
import pt.up.fe.cmov16.cafe.cafeapp.ui.ScanQRCodeActivity;

public class MainActivity extends AppCompatActivity {
    private static int QR_CODE_CODE = 1;
    public static final String PRODUCTS_KEY = "PRODUCTS";
    public static final String VOUCHERS_KEY = "VOUCHERS";
    public static final String COSTUMER_KEY = "COSTUMERID";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApiInvoker.initializeInstance();

        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();

        if(sp.getString(MainActivity.COSTUMER_KEY, null) != null){

             ArrayList<ProductMenuItem> productMenuItems = new ArrayList<>();
            for(String s : sp.getStringSet(MainActivity.PRODUCTS_KEY, new HashSet<String>()))
                productMenuItems.add(gson.fromJson(s, ProductMenuItem.class));

            ArrayList<Voucher> vouchers = new ArrayList<>();
            for(String s : sp.getStringSet(MainActivity.VOUCHERS_KEY, new HashSet<String>()))
                vouchers.add(gson.fromJson(s, Voucher.class));

            String costumerID = sp.getString(MainActivity.COSTUMER_KEY, null);

            Intent i = new Intent(this, ProcessRequestActivity.class);
            i.putExtra(PRODUCTS_KEY, productMenuItems);
            i.putExtra(VOUCHERS_KEY, vouchers);
            i.putExtra(COSTUMER_KEY, costumerID);
            startActivity(i);
            return;
        }


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
                Gson gson = new Gson();
                SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();

                ArrayList<ProductMenuItem> productMenuItems =
                        (ArrayList<ProductMenuItem>) bundle.get(MainActivity.PRODUCTS_KEY);
                ArrayList<Voucher> vouchers =
                        (ArrayList<Voucher>) bundle.get(MainActivity.VOUCHERS_KEY);
                String costumerID = (String) bundle.get(MainActivity.COSTUMER_KEY);

                HashSet<String> pmis = new HashSet<>();
                for (ProductMenuItem pmi : productMenuItems)
                    pmis.add(gson.toJson(pmi, ProductMenuItem.class));
                HashSet<String> vs = new HashSet<>();
                for (Voucher v : vouchers)
                    vs.add(gson.toJson(v, Voucher.class));

                editor.putStringSet(MainActivity.VOUCHERS_KEY, vs);
                editor.putStringSet(MainActivity.PRODUCTS_KEY, vs);
                editor.putString(MainActivity.COSTUMER_KEY, costumerID);

                editor.commit();

                Intent i = new Intent(this, ProcessRequestActivity.class);
                i.replaceExtras(bundle);
                startActivity(i);
            }
        }
    }


    @Override
    protected void onDestroy() {
        Log.d("MAIN_ACTIVITY", "Destroyed");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.d("MAIN_ACTIVITY", "Paused");
        super.onPause();
    }
}
