package pt.up.fe.cmov16.cafe.cafeapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.Request;
import io.swagger.client.model.RequestParam;
import io.swagger.client.model.RequestlineParam;
import io.swagger.client.model.Voucher;
import io.swagger.client.model.VoucherParam;
import pt.up.fe.cmov16.cafe.cafeapp.MainActivity;
import pt.up.fe.cmov16.cafe.cafeapp.R;
import pt.up.fe.cmov16.cafe.cafeapp.logic.ProductMenuItem;

public class ProcessRequestActivity extends AppCompatActivity {

    private static final String TAG = ProcessRequestActivity.class.toString();
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_request);

        textView = (TextView) findViewById(R.id.activity_process_request_tv);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ArrayList<ProductMenuItem> productMenuItems =
                    (ArrayList<ProductMenuItem>) bundle.get(MainActivity.PRODUCTS_KEY);
            ArrayList<Voucher> vouchers =
                    (ArrayList<Voucher>) bundle.get(MainActivity.VOUCHERS_KEY);
            String costumerID = (String) bundle.get(MainActivity.COSTUMER_KEY);

            sendRequest(productMenuItems, vouchers, costumerID);
        }
    }

    private void sendRequest(ArrayList<ProductMenuItem> productMenuItems, ArrayList<Voucher> vouchers, String costumerID) {
        textView.setText("Creating request");

        RequestParam requestParam = new RequestParam();
        requestParam.setCostumerUuid(costumerID);

        textView.append("\nAdding requestLines");
        List<RequestlineParam> requestLines = new ArrayList<>();
        for (ProductMenuItem productMenuItem : productMenuItems) {
            RequestlineParam requestline = new RequestlineParam();
            requestline.setProductId(productMenuItem.getId());
            requestline.setQuantity(productMenuItem.getQuantity());
            requestLines.add(requestline);
            textView.append("\n\t Prod: "+productMenuItem.getId()+" #: "+productMenuItem.getQuantity());
        }
        requestParam.setRequestlines(requestLines);

        textView.append("\nAdding vouchers");
        List<VoucherParam> requestVouchers = new ArrayList<>();
        for (Voucher voucher : vouchers) {
            VoucherParam voucherParam = new VoucherParam();
            voucherParam.setId(voucher.getId());
            voucherParam.setType(voucher.getType());
            voucherParam.setSignature(voucher.getSignature());
            textView.append("\n\t Voucher: "+voucher.getType()+" type: "+voucher.getType());
        }
        requestParam.setRequestvouchers(requestVouchers);

       // Log.e(TAG,requestParam.toString());
        textView.append("\nSending request to server");
        DefaultApi api = new DefaultApi();
        api.createRequest(requestParam, new Response.Listener<Request>() {
            @Override
            public void onResponse(Request response) {
                Log.e(TAG,"Server responde: "+ response.toString());
                textView.append("\nServer responde: "+ response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,"Error responde: "+ error.toString());
                textView.append("\nError responde: "+ error.toString());
            }
        });
        textView.append("\nWaiting response");
    }
}
