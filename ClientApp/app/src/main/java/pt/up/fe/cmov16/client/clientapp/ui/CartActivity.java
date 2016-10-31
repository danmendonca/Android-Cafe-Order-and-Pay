package pt.up.fe.cmov16.client.clientapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import pt.up.fe.cmov16.client.clientapp.R;
import pt.up.fe.cmov16.client.clientapp.logic.ProductMenuItem;

public class CartActivity extends AppCompatActivity {
    public static final String intentKey = "MAKE_REQUEST_INTENT_KEY";
    public static final String productsArrayKey = "PRODUCTS_KEY";

    ArrayList<ProductMenuItem> requestLines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
    }
}
