package pt.up.fe.cmov16.client.clientapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import io.swagger.client.model.Voucher;
import pt.up.fe.cmov16.client.clientapp.R;
import pt.up.fe.cmov16.client.clientapp.logic.ProductMenuItem;

public class CartActivity extends AppCompatActivity {
    public static final String intentKey = "MAKE_REQUEST_INTENT_KEY";
    public static final String productsArrayKey = "PRODUCTS_KEY";

    ArrayList<ProductVoucherWrapper> requestLines;
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cart);

        if (savedInstanceState.get(productsArrayKey) == null) {
            finish();
            return;
        }
        requestLines = new ArrayList<>();


        for (ProductMenuItem pmi : (ArrayList<ProductMenuItem>) savedInstanceState.get(productsArrayKey))
            requestLines.add(new ProductVoucherWrapper(pmi));


        rv = (RecyclerView) findViewById(R.id.cart_items_rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setAdapter(new ItemsRvAdapter());

    }

    private class ProductVoucherWrapper {
        Voucher voucher;
        ProductMenuItem productItem;

        byte type;

        public ProductVoucherWrapper(Voucher voucher) {
            this.voucher = voucher;
            type = 1;
        }

        public ProductVoucherWrapper(ProductMenuItem productItem) {
            this.productItem = productItem;
            type = 2;
        }

        public String getName() {
            if (type == 1) {
                switch (voucher.getType()) {
                    case 1:
                        return "Voucher Coffee";
                    case 2:
                        return "Voucher Popcorn";
                    default:
                        return "Voucher Discount 5%";
                }
            }

            return productItem.getName();
        }

        public String getQuantity() {
            if (type == 1)
                return "1";
            else
                return String.valueOf(productItem.getQuantity());
        }

        public String getTotal() {
            if (type == 1)
                return "0 €";

            return String.valueOf(productItem.getQuantity() * productItem.getUnitPrice()) + " €";
        }

    }


    private class ItemsRvAdapter extends RecyclerView.Adapter<ItemsRvAdapter.Holder> {

        ArrayList<ProductVoucherWrapper> pvw = new ArrayList<>();

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_card, parent, false);
            Holder holder = new Holder(itemView);
            return holder;
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.quantity.setText(pvw.get(position).getQuantity());
            holder.name.setText(pvw.get(position).getName());
            holder.total.setText(pvw.get(position).getTotal());
        }

        @Override
        public int getItemCount() {
            return pvw.size();
        }

        public class Holder extends RecyclerView.ViewHolder {

            TextView quantity, name, total;

            public Holder(View itemView) {
                super(itemView);

                quantity = (TextView) itemView.findViewById(R.id.item_quantity);
                name = (TextView) itemView.findViewById(R.id.item_name);
                total = (TextView) itemView.findViewById(R.id.item_total);
            }
        }
    }

}
