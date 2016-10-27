package pt.up.fe.cmov16.client.clientapp.ui.slides;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;

import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.Product;
import io.swagger.client.model.Products;
import pt.up.fe.cmov16.client.clientapp.MainActivity;
import pt.up.fe.cmov16.client.clientapp.R;
import pt.up.fe.cmov16.client.clientapp.database.ProductContract;
import pt.up.fe.cmov16.client.clientapp.util.IFunction;

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
        PRODUCTS = new ArrayList<>();
        //PREPARE LIST VIEW
        final RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_products);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RVAdapter();
        rv.setAdapter(adapter);

        loadProducts();

        return rootView;
    }

    private void loadProducts() {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                DefaultApi api = new DefaultApi();
                final ProductContract prodsDb = new ProductContract();
                api.getProducts(new Response.Listener<Products>() {
                    @Override
                    public void onResponse(Products response) {
                        //TODO verificar a data da ultima atualização(last product updated or created date) da tabela para
                        //TODO verificar se vale a pena pedir os produtos todos e atualizar a db
                        //Get products from server and update local db
                        PRODUCTS.addAll(response.getProducts());
                        prodsDb.replaceProducts(getContext(), PRODUCTS);
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Api always print errors in the Log.e
                        //Load products from db
                        prodsDb.loadProducts(getContext(), new IFunction() {
                            @Override
                            public void execute(Object s) {
                                if (s != null) {
                                    ArrayList<Product> prods = (ArrayList<Product>) s;
                                    PRODUCTS.addAll(prods);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });

                    }
                });

            }
        })).start();
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