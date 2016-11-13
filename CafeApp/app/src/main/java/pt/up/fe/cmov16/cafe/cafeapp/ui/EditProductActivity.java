package pt.up.fe.cmov16.cafe.cafeapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;

import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.Product;
import io.swagger.client.model.ProductParam;
import pt.up.fe.cmov16.cafe.cafeapp.R;
import pt.up.fe.cmov16.cafe.cafeapp.database.ProductContract;

public class EditProductActivity extends AppCompatActivity {
    private Product product = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        final EditText prodName = (EditText) findViewById(R.id.prod_name);
        final EditText prodPrice = (EditText) findViewById(R.id.prod_price);
        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);

        if (getIntent().getExtras() != null) {
            product = (Product) getIntent().getExtras().getSerializable("prod");
            if (product != null) {
                prodName.setText(product.getName());
                prodPrice.setText(String.valueOf(product.getUnitprice()));
                checkBox.setChecked(product.getActive());
            }
        }

        findViewById(R.id.saveBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DefaultApi api = new DefaultApi();
                ProductParam productParam = new ProductParam();
                productParam.setActive(checkBox.isChecked());
                productParam.setName(prodName.getText().toString());
                productParam.setUnitprice(Float.valueOf(prodPrice.getText().toString()));
                productParam.setMyPass("");
                productParam.setMyUser("");

                if (product != null) {
                    api.editProduct(product.getId(), productParam, new Response.Listener<Product>() {
                        @Override
                        public void onResponse(Product response) {
                            successResponse(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            errorResponse();
                        }
                    });
                } else {
                    api.createProduct(productParam, new Response.Listener<Product>() {
                        @Override
                        public void onResponse(Product response) {
                            successResponse(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            errorResponse();
                        }
                    });
                }
            }
        });
    }

    private void errorResponse() {
        Toast.makeText(EditProductActivity.this,
                "Connection failed, please try again later", Toast.LENGTH_LONG).show();
        setResult(RESULT_CANCELED);
        finish();
    }

    private void successResponse(Product response) {
        ArrayList<Product> prods = new ArrayList<>();
        prods.add(response);
        ProductContract.updateProducts(EditProductActivity.this, prods);
        Toast.makeText(EditProductActivity.this,
                "Product updated", Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
        finish();
    }
}
