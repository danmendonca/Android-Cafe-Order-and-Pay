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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.Product;
import io.swagger.client.model.Products;
import pt.up.fe.cmov16.client.clientapp.R;
import pt.up.fe.cmov16.client.clientapp.database.ProductContract;

public class ProductsFragment extends NamedFragment {

    private ArrayList<Product> PRODUCTS;
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
        PRODUCTS = new ArrayList<>();// NEVER REPLACE THIS REFERENCE, if needed clear it.

        //PREPARE LIST VIEW
        final RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_products);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RVAdapter();
        rv.setAdapter(adapter);

        loadProducts();

        return rootView;
    }

    @Override
    public void focusObtained(){
        loadProducts();
    }

    private void loadProducts() {
        final ProductContract productContract = new ProductContract();
        String lastDate = productContract.lastUpdatedProductDate(getContext());

        DefaultApi api = new DefaultApi();
        final Context context = getContext();
        if (lastDate == null || lastDate.isEmpty()){
            api.getProducts(new Response.Listener<Products>() {
                @Override
                public void onResponse(Products response) {
                    updateListItems(response.getProducts());
                    productContract.updateProducts(context,PRODUCTS);
                    Calendar c = Calendar.getInstance();
                    String date = String.valueOf(c.get(Calendar.YEAR)) + "-" +
                            c.get(Calendar.MONTH) +
                            "-" +
                            c.get(Calendar.DAY_OF_MONTH) +
                            "T00:00:00.000Z";
                    productContract.saveUpdatedProductDate(context, date);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(),
                            "Connection failed, loading local products", Toast.LENGTH_SHORT).show();
                    updateListItems(productContract.loadProducts(context));
                }
            });
        }else {
            //TODO pedir produtos a partir desta data
            updateListItems(productContract.loadProducts(context));
            Log.e("prods","update com base na data: "+lastDate);
        }
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
}
