package pt.up.fe.cmov16.cafe.cafeapp.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.Product;
import io.swagger.client.model.Products;
import pt.up.fe.cmov16.cafe.cafeapp.R;
import pt.up.fe.cmov16.cafe.cafeapp.database.ProductContract;

public class ProductsActivity extends AppCompatActivity {
    private static final int EDIT_PRODUCT = 0;
    private ArrayList<Product> products;
    private RVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        products = new ArrayList<>();
        loadProducts();

        final RecyclerView rv = (RecyclerView) findViewById(R.id.rv_products);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RVAdapter();
        rv.setAdapter(adapter);

        findViewById(R.id.fabButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(ProductsActivity.this, EditProductActivity.class);
                        startActivityForResult(i, EDIT_PRODUCT);
                    }
                }
        );

    }

    private void loadProducts() {
        DefaultApi api = new DefaultApi();

        //if updatedAt was saved before, the skip this step and use its value in lastDate
        String lastUpdated = ProductContract.lastUpdatedProductDate(this);
        boolean stored = !lastUpdated.isEmpty();
        if (!stored)
            lastUpdated = getDefaultTimestamp();

        api.getProducts(lastUpdated,
                new Response.Listener<Products>() {
                    @Override
                    public void onResponse(Products response) {
                        if (response.getProducts().size() > 0) {
                            ProductContract.updateProducts(ProductsActivity.this, response.getProducts());
                            updateListItems(ProductContract.loadProducts(ProductsActivity.this));
                        } else {
                            updateListItems(ProductContract.loadProducts(ProductsActivity.this));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProductsActivity.this,
                                "Connection failed, loading local products", Toast.LENGTH_SHORT).show();
                        updateListItems(ProductContract.loadProducts(ProductsActivity.this));
                    }
                });
    }

    private void updateListItems(List<Product> productsServer) {
        products.clear();
        products.addAll(productsServer);
        if (adapter != null)
            adapter.notifyDataSetChanged();
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

    public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ProductViewHolder> {
        private int VIEW_TYPE = 0;

        @Override
        public int getItemViewType(int position) {
            return VIEW_TYPE;
        }

        @Override
        public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.product_list_item, viewGroup, false);
            return new ProductViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ProductViewHolder holder, int i) {
            holder.productName.setText(products.get(i).getName());
            String p = products.get(i).getUnitprice() + "€";
            holder.productPrice.setText(p);
            if (!products.get(i).getActive()) {
                holder.productName.setTextColor(Color.GRAY);
                holder.productPrice.setTextColor(Color.GRAY);
            } else {
                int c = ContextCompat.getColor(ProductsActivity.this, R.color.colorPrimary);
                holder.productName.setTextColor(c);
                holder.productPrice.setTextColor(c);
            }
        }

        @Override
        public int getItemCount() {
            return products.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        class ProductViewHolder extends RecyclerView.ViewHolder {
            TextView productName, productPrice;

            ProductViewHolder(View itemView) {
                super(itemView);
                productName = (TextView) itemView.findViewById(R.id.product_name);
                productPrice = (TextView) itemView.findViewById(R.id.product_price);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = getAdapterPosition();
                        Intent i = new Intent(ProductsActivity.this, EditProductActivity.class);
                        i.putExtra("prod", products.get(pos));
                        startActivityForResult(i, EDIT_PRODUCT);
                    }
                });
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case EDIT_PRODUCT:
                if (RESULT_OK == resultCode) {
                    updateListItems(ProductContract.loadProducts(ProductsActivity.this));
                }
                break;
        }
    }
}
