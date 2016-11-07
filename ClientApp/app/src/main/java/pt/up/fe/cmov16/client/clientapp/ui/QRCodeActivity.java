package pt.up.fe.cmov16.client.clientapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.glxn.qrgen.android.QRCode;

import java.util.ArrayList;

import io.swagger.client.model.Product;
import io.swagger.client.model.Voucher;
import pt.up.fe.cmov16.client.clientapp.R;
import pt.up.fe.cmov16.client.clientapp.logic.ProductMenuItem;
import pt.up.fe.cmov16.client.clientapp.logic.User;

public class QRCodeActivity extends Activity {

    private static final String TAG = QRCodeActivity.class.toString();
    private ArrayList<ProductMenuItem> products;
    private ArrayList<Voucher> vouchers;
    private DisplayMetrics metrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        final ImageView img = (ImageView) findViewById(R.id.qrcodeView);

        metrics = new DisplayMetrics();
        products = new ArrayList<>();
        vouchers = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            if(bundle.get(CartActivity.PRODUCTS_ARRAY_KEY)!=null){
                products = (ArrayList<ProductMenuItem>) bundle.get(CartActivity.PRODUCTS_ARRAY_KEY);
            }
            if(bundle.get(CartActivity.PRODUCTS_ARRAY_KEY)!=null){
                vouchers = (ArrayList<Voucher>) bundle.get(CartActivity.VOUCHERS_ARRAY_KEY);
            }
        }

        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        img.setImageBitmap(generateBitMap());
    }

    private Bitmap generateBitMap() {
        int w = (metrics.widthPixels >= metrics.heightPixels) ? metrics.widthPixels : metrics.heightPixels;

        return QRCode.from(requestToString())
                .withSize(w, w)
                .withColor(0xFFEF4B4D, 0x0FFFFFFF)
                .bitmap();
    }

    private String requestToString() {

        StringBuilder sb = new StringBuilder();
        sb.append(User.getInstance(QRCodeActivity.this).getCostumerID());
        sb.append(";");

        for (int i = 0; i < products.size(); i++) {
            sb.append(products.get(i).getId());
            sb.append(",");
            sb.append(products.get(i).getQuantity());
            if(i!=products.size()-1){
                sb.append("|");
            }
        }
        if(vouchers.size()==0) {
            Log.e(TAG,"QRCode string:" +sb.toString());
            return sb.toString();
        }

        sb.append(";");

        for (int i = 0; i < vouchers.size(); i++) {
            sb.append(vouchers.get(i).getId());
            sb.append(",");
            sb.append(vouchers.get(i).getType());
            sb.append(",");
            sb.append(vouchers.get(i).getSignature());
            if(i!=vouchers.size()-1){
                sb.append("|");
            }
        }
        Log.e(TAG,"QRCode string:" +sb.toString());
        return sb.toString();
    }
}
