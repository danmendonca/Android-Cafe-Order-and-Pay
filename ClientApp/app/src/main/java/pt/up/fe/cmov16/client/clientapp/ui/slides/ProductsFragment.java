package pt.up.fe.cmov16.client.clientapp.ui.slides;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import pt.up.fe.cmov16.client.clientapp.R;
import pt.up.fe.cmov16.client.clientapp.database.ProductContract;

public class ProductsFragment extends NamedFragment {

    private ArrayList<Product> PRODUCTS = new ArrayList<>();
    private RVAdapter adapter;

    public static ProductsFragment newInstance(int page) {
        Bundle args = new Bundle();
        // if necessary add arguments here
        //args.putInt(ARG_PAGE, page);
        ProductsFragment fragment = new ProductsFragment();
        fragment.setArguments(args);
        fragment.tittle = "Menu";
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_products, container, false);
        if(PRODUCTS.size() == 0){
            loadProducts();
        }
//        if (PRODUCTS == null) {
//            PRODUCTS = new ArrayList<>();// NEVER REPLACE THIS REFERENCE, if needed clear it.
//
//        }

        //PREPARE LIST VIEW
        final RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_products);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RVAdapter();
        rv.setAdapter(adapter);

        return rootView;
    }

    private void loadProducts() {
        //TODO fix double function call on screen rotation
        DefaultApi api = new DefaultApi();

        final Context context = getContext();
        if (context == null)
            return;
        final ProductContract productContract = new ProductContract();
        //if updatedAt was saved before, the skip this step and use its value in lastDate
        String lastUpdated = productContract.lastUpdatedProductDate(context);
        boolean stored = !lastUpdated.isEmpty();
        if (!stored)
            lastUpdated = getDefaultTimestamp();

        Log.e("products", lastUpdated);
        api.getProducts(lastUpdated,
                new Response.Listener<Products>() {
                    @Override
                    public void onResponse(Products response) {
                        if (response.getProducts().size() > 0) {
                            productContract.updateProducts(context, response.getProducts());
                            updateListItems(productContract.loadProducts(context));
                        } else {
                            updateListItems(productContract.loadProducts(context));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Get-Products", error.toString());
                        Toast.makeText(getContext(),
                                "Connection failed, loading local products", Toast.LENGTH_SHORT).show();
                        updateListItems(productContract.loadProducts(context));
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

    private void updateListItems(List<Product> products) {
        PRODUCTS.clear();
        PRODUCTS.addAll(products);
        adapter.notifyDataSetChanged();
    }

    public class RVAdapter extends RecyclerView.Adapter {
        private int VIEW_TYPE = 0;

        @Override
        public int getItemViewType(int position) {
            return VIEW_TYPE;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v;
            RecyclerView.ViewHolder evh = null;
            if (viewType == VIEW_TYPE) {
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_list_item, viewGroup, false);
                evh = new ProductViewHolder(v);
            }
            return evh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
            if (holder instanceof ProductViewHolder) {
                ((ProductViewHolder) holder).productName.setText(PRODUCTS.get(i).getName());
                String p = PRODUCTS.get(i).getUnitprice() + "€";
                ((ProductViewHolder) holder).productPrice.setText(p);
            }
        }

        @Override
        public int getItemCount() {
            return PRODUCTS.size();
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
            }
        }
    }

    //update all times that user see this screen
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            loadProducts();
        }
    }
}
