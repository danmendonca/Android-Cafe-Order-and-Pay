package pt.up.fe.cmov16.client.clientapp.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

    private ArrayList<ProductMenuItem> products;
    private ArrayList<Voucher> vouchers;
    private DisplayMetrics metrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        final ImageView img = (ImageView) findViewById(R.id.qrcodeView);

        metrics = new DisplayMetrics();

        Product p = new Product();
        p.setId(1);
        ProductMenuItem p1 = new ProductMenuItem(p);
        p1.setQuantity(2);

        Product pp = new Product();
        pp.setId(2);
        ProductMenuItem p2 = new ProductMenuItem(pp);
        p2.setQuantity(1);

        products = new ArrayList<>();
        products.add(p1);
        products.add(p2);

        Voucher v = new Voucher();
        v.setId(1);
        v.setType(1);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 46; i++)
            sb.append("a");
//        v.setKey(sb.toString());

        vouchers = new ArrayList<>();
        vouchers.add(v);

        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        img.setImageBitmap(generateBitMap());
    }

    private Bitmap generateBitMap() {
        int w = (metrics.widthPixels >= metrics.heightPixels) ? metrics.widthPixels : metrics.heightPixels;

        return QRCode.from(requestToJson())
                .withSize(w, w)
                .withColor(0xFFEF4B4D, 0x0FFFFFFF)
                .bitmap();
    }

    private String requestToJson() {
        JsonObject request = new JsonObject();
        request.addProperty("costumerID", User.getInstance(QRCodeActivity.this).getCostumerID());

        JsonArray prodsJsonArray = new JsonArray();
        for (ProductMenuItem prod : products) {
            JsonObject prodJson = new JsonObject();
            prodJson.addProperty("id", prod.getId());
            prodJson.addProperty("quantity", prod.getQuantity());
            prodsJsonArray.add(prodJson);
        }

        JsonArray vouchersJsonArray = new JsonArray();
        for (Voucher voucher : vouchers) {
            JsonObject voucherJson = new JsonObject();
            voucherJson.addProperty("id", voucher.getId());
            voucherJson.addProperty("type", voucher.getType());
//            voucherJson.addProperty("signature", voucher.getKey());
            vouchersJsonArray.add(voucherJson);
        }

        request.add("products", prodsJsonArray);
        request.add("vouchers", vouchersJsonArray);
        return request.toString();
    }
}
