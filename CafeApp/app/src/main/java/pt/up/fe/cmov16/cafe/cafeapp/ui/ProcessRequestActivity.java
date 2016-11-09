package pt.up.fe.cmov16.cafe.cafeapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.RequestParam;
import io.swagger.client.model.RequestResponse;
import io.swagger.client.model.Voucher;
import pt.up.fe.cmov16.cafe.cafeapp.MainActivity;
import pt.up.fe.cmov16.cafe.cafeapp.R;
import pt.up.fe.cmov16.cafe.cafeapp.database.BlackListContract;
import pt.up.fe.cmov16.cafe.cafeapp.database.PendingRequestContract;
import pt.up.fe.cmov16.cafe.cafeapp.database.ProductContract;
import pt.up.fe.cmov16.cafe.cafeapp.logic.ProductMenuItem;
import pt.up.fe.cmov16.cafe.cafeapp.logic.Request;
import pt.up.fe.cmov16.cafe.cafeapp.util.RequestDecode;

public class ProcessRequestActivity extends AppCompatActivity {
    private static String[] TYPES = {"", "1 Free coffee", "1 Free popcorn pack", "5% Discount"};
    private static final String TAG = ProcessRequestActivity.class.toString();
    private ArrayList<ProductMenuItem> prods = new ArrayList<>();
    private ArrayList<Voucher> vouchers = new ArrayList<>();
    private double totalPrice = 0;
    private ItemsRvAdapter adapter;
    private TextView totalTV, numberTV;
    private boolean discount = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_request);

        totalTV = (TextView) findViewById(R.id.total_price);
        numberTV = (TextView) findViewById(R.id.request_num);
        RecyclerView rv = (RecyclerView) findViewById(R.id.items_rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ItemsRvAdapter();
        rv.setAdapter(adapter);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String encoded = (String) bundle.get(MainActivity.ENCODED_STRING_KEY);
            sendRequest(encoded);
        }
    }

    private void sendRequest(final String encoded) {
        RequestParam requestParam = Request.generateRequestParam(encoded);
        DefaultApi api = new DefaultApi();
        api.createRequest(requestParam, new Response.Listener<RequestResponse>() {
            @Override
            public void onResponse(RequestResponse response) {
                onlineRequestFinished(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError)
                    offlineRequest(encoded);
                else Log.e(TAG, error.getMessage());
            }
        });
    }

    private void onlineRequestFinished(RequestResponse response) {
        //TODO
        Log.e(TAG, response.toString());
    }

    private void offlineRequest(String encoded) {
        if (Request.isValid(ProcessRequestActivity.this, encoded)) {
            PendingRequestContract.savePendingRequest(ProcessRequestActivity.this, encoded);
            RequestDecode.decode(encoded, prods, vouchers);
            totalPrice = ProductContract.loadMoreInfoFomLocalDB(ProcessRequestActivity.this, prods);
            numberTV.setText("Pending...");
            double total = totalPrice;
            for (Voucher v : vouchers) {
                if (v.getType() == 3) {
                    discount = true;
                    break;
                }
            }
            if (discount)
                total = totalPrice * 0.95;
            totalTV.setText(doubleToDecimalString(total));
            adapter.notifyDataSetChanged();
        } else BlackListContract.blockUser(ProcessRequestActivity.this, encoded.split(";")[0]);
    }

    private String doubleToDecimalString(double d) {
        return new DecimalFormat("0.00",
                DecimalFormatSymbols.getInstance(Locale.ENGLISH)).format(d);
    }

    private class ItemsRvAdapter extends RecyclerView.Adapter<ItemsRvAdapter.Holder> {

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_card, parent, false);
            return new Holder(itemView);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            if (position < prods.size()) {
                holder.quantity.setText(String.valueOf(prods.get(position).getQuantity()));
                holder.name.setText(prods.get(position).getName());
                double total =
                        prods.get(position).getUnitPrice() * prods.get(position).getQuantity();
                holder.total.setText(doubleToDecimalString(total));
            } else {
                position = position - prods.size();
                holder.quantity.setText("");
                holder.name.setText(TYPES[vouchers.get(position).getType()]);
                if (vouchers.get(position).getType() == 3) {
                    String discountStr = "- ";
                    double discount = totalPrice * 0.05;
                    discountStr += (doubleToDecimalString(discount));
                    holder.total.setText(discountStr);
                } else {
                    holder.total.setText("");
                }
            }
        }

        @Override
        public int getItemCount() {
            return prods.size() + vouchers.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView quantity, name, total;

            Holder(View itemView) {
                super(itemView);
                quantity = (TextView) itemView.findViewById(R.id.item_quantity);
                name = (TextView) itemView.findViewById(R.id.item_name);
                total = (TextView) itemView.findViewById(R.id.item_total);
            }
        }
    }
}
