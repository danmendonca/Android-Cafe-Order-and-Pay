package pt.up.fe.cmov16.client.clientapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.LinesVouchersResponse;
import io.swagger.client.model.Product;
import io.swagger.client.model.Requestline;
import io.swagger.client.model.Voucher;
import pt.up.fe.cmov16.client.clientapp.R;
import pt.up.fe.cmov16.client.clientapp.database.ProductContract;
import pt.up.fe.cmov16.client.clientapp.ui.slides.VouchersFragment;

public class RequestDetailActivity extends AppCompatActivity {

    public static final String REQUESTID_KEY = "requestid";
    private ArrayList<LineVoucherWrapper> wrappers;
    private Adapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int requestId = getIntent().getIntExtra(REQUESTID_KEY, 0);
        if (requestId == 0) finish();

        wrappers = new ArrayList<>();
        arrayAdapter = new Adapter();
        setContentView(R.layout.activity_request_detail);

        DefaultApi api = new DefaultApi();
        api.getRequestlines(requestId,
                new Response.Listener<LinesVouchersResponse>() {
                    @Override
                    public void onResponse(LinesVouchersResponse response) {
                        ArrayList<Requestline> requestlines = (ArrayList<Requestline>) response.getRequestlines();
                        ArrayList<Voucher> vouchers = (ArrayList<Voucher>) response.getVouchers();
                        for (Requestline rl : requestlines)
                            wrappers.add(new LineVoucherWrapper(rl));
                        for (Voucher v : vouchers) wrappers.add(new LineVoucherWrapper(v));

                        if (arrayAdapter != null) arrayAdapter.notifyDataSetChanged();
                        updateRequestTotal();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR_REQUEST_DETAIL", error.getMessage());
                        finish();
                    }
                });

        RecyclerView rv = (RecyclerView) findViewById(R.id.requestdetail_list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(arrayAdapter);

    }

    private void updateRequestTotal() {
        TextView rTotalTv = (TextView) findViewById(R.id.requestdetail_total);
        float ammount = 0;
        for (LineVoucherWrapper wrapper : wrappers)
            ammount += wrapper.getTotal();
        if (rTotalTv != null)
            rTotalTv.setText(String.valueOf(ammount) + " €");

    }


    class LineVoucherWrapper {
        Voucher voucher;
        Requestline requestline;
        Product product;

        public LineVoucherWrapper(Voucher voucher) {
            this.voucher = voucher;
        }

        public LineVoucherWrapper(Requestline requestline) {
            this.requestline = requestline;
            product = ProductContract.getProductByID(getApplicationContext(), requestline.getProductId());
        }

        public String getDescription() {
            if (product != null)
                return product.getName();

            return VouchersFragment.types[voucher.getType()];
        }

        public int getQuantity() {

            if (requestline != null)
                return requestline.getQuantity();

            return 1;
        }

        public float getTotal() {
            if (requestline != null) {
                DecimalFormat twoDf = new DecimalFormat("#.##",
                        DecimalFormatSymbols.getInstance(Locale.ENGLISH));
                return Float.valueOf(twoDf.format(requestline.getQuantity() * requestline.getUnitprice()));
            }

            return 0.00f;
        }
    }


    class Adapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;
            WrapperViewHolder viewHolder = null;
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.requestdetail_list_item, parent, false);
            viewHolder = new WrapperViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((WrapperViewHolder) holder).descTv.setText(wrappers.get(position).getDescription());
            ((WrapperViewHolder) holder).quantityTv
                    .setText(String.valueOf(wrappers.get(position).getQuantity()));
            ((WrapperViewHolder) holder)
                    .totalTv.setText(String.valueOf(wrappers.get(position).getTotal()));
            ((WrapperViewHolder) holder).mItem = wrappers.get(position);

        }

        @Override
        public int getItemCount() {
            return wrappers.size();
        }

    }

    class WrapperViewHolder extends RecyclerView.ViewHolder {
        LineVoucherWrapper mItem;
        TextView descTv, totalTv, quantityTv;

        public WrapperViewHolder(View itemView) {
            super(itemView);
            descTv = (TextView) itemView.findViewById(R.id.requestdetail_item_description);
            quantityTv = (TextView) itemView.findViewById(R.id.requestdetail_item_quantity);
            totalTv = (TextView) itemView.findViewById(R.id.requestdetail_item_total);
        }
    }
}
