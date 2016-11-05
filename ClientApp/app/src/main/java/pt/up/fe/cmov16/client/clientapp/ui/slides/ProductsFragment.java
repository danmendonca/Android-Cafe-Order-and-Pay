package pt.up.fe.cmov16.client.clientapp.ui.slides;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.Product;
import io.swagger.client.model.Products;
import pt.up.fe.cmov16.client.clientapp.R;
import pt.up.fe.cmov16.client.clientapp.database.ProductContract;
import pt.up.fe.cmov16.client.clientapp.logic.ProductMenuItem;

import static android.view.View.GONE;

public class ProductsFragment extends NamedFragment {

    private ArrayList<ProductMenuItem> PRODUCTS = new ArrayList<>();
    private RVAdapter adapter;
    static final String STATE_PRODS = "STATE_PRODS";

    private boolean restoringState = false;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putSerializable(STATE_PRODS, PRODUCTS);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public static ProductsFragment newInstance(int page) {
        Bundle args = new Bundle();
        // if necessary add arguments here
        //args.putInt(ARG_PAGE, page);
        ProductsFragment fragment = new ProductsFragment();
        fragment.setArguments(args);
        fragment.title = "Menu";
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_products, container, false);
        //PREPARE LIST VIEW
        final RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_products);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RVAdapter();
        rv.setAdapter(adapter);
        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            PRODUCTS = (ArrayList<ProductMenuItem>) savedInstanceState.getSerializable(STATE_PRODS);
            restoringState = true;
        } else {
            loadProducts();
        }
        return rootView;
    }

    private void loadProducts() {
        final HashMap<Integer, Integer> quantities = new HashMap<>();
        if (PRODUCTS.size() > 0) {
            for (ProductMenuItem productMenuItem : PRODUCTS) {
                if (productMenuItem.getQuantity() > 0) {
                    quantities.put(productMenuItem.getId(), productMenuItem.getQuantity());
                }
            }
        }
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
                            updateListItems(productContract.loadProducts(context), quantities);
                        } else {
                            updateListItems(productContract.loadProducts(context), quantities);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Get-Products", error.toString());
                        Toast.makeText(getContext(),
                                "Connection failed, loading local products", Toast.LENGTH_SHORT).show();
                        updateListItems(productContract.loadProducts(context), quantities);
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

    private void updateListItems(List<Product> products, HashMap<Integer, Integer> quantities) {
        PRODUCTS.clear();
        for (Product p : products) {
            ProductMenuItem productMenuItem = new ProductMenuItem(p);
            if (quantities.containsKey(p.getId())) {
                productMenuItem.setQuantity(quantities.get(p.getId()));
            }
            PRODUCTS.add(productMenuItem);

        }
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    public ArrayList<ProductMenuItem> getProducts() {
        ArrayList<ProductMenuItem> products = new ArrayList<>();
        for (ProductMenuItem prod : PRODUCTS) {
            if (prod.getQuantity() > 0)
                products.add(prod);
        }
        return products;
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
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int i) {
            if (holder instanceof ProductViewHolder) {
                final ProductMenuItem product = PRODUCTS.get(i);
                ((ProductViewHolder) holder).productName.setText(product.getName());
                String p = product.getUnitPrice() + "€";
                ((ProductViewHolder) holder).productPrice.setText(p);
                ((ProductViewHolder) holder).quantityTV.setText(String.valueOf(product.getQuantity()));
                updateButtonsVisibility((ProductViewHolder) holder, product.getQuantity());

                ((ProductViewHolder) holder).addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int quantityActual = Integer.valueOf(((ProductViewHolder) holder).quantityTV.getText().toString());
                        quantityActual++;
                        ((ProductViewHolder) holder).quantityTV.setText(String.valueOf(quantityActual));
                        product.setQuantity(quantityActual);
                        updateButtonsVisibility((ProductViewHolder) holder, quantityActual);
                    }
                });
                ((ProductViewHolder) holder).subButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int quantityActual = Integer.valueOf(((ProductViewHolder) holder).quantityTV.getText().toString());
                        quantityActual--;
                        product.setQuantity(quantityActual);
                        ((ProductViewHolder) holder).quantityTV.setText(String.valueOf(quantityActual));
                        updateButtonsVisibility((ProductViewHolder) holder, quantityActual);
                    }
                });

            }
        }

        private void updateButtonsVisibility(ProductViewHolder holder, int quantity) {
            if (quantity == 0) {
                holder.subButton.setVisibility(GONE);
                holder.quantityTV.setVisibility(GONE);
            } else {
                holder.subButton.setVisibility(View.VISIBLE);
                holder.quantityTV.setVisibility(View.VISIBLE);
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
            TextView productName, productPrice, quantityTV;
            Button addButton, subButton;

            ProductViewHolder(View itemView) {
                super(itemView);
                productName = (TextView) itemView.findViewById(R.id.product_name);
                productPrice = (TextView) itemView.findViewById(R.id.product_price);
                quantityTV = (TextView) itemView.findViewById(R.id.quantityTextView);
                addButton = (Button) itemView.findViewById(R.id.addButton);
                subButton = (Button) itemView.findViewById(R.id.subButton);
            }
        }
    }

    //update all times that user see this screen
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!restoringState && isVisibleToUser) {
            loadProducts();
        } else if (restoringState)
            restoringState = false;
    }
}
