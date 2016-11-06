package pt.up.fe.cmov16.cafe.cafeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import io.swagger.client.ApiInvoker;
import pt.up.fe.cmov16.cafe.cafeapp.ui.ProcessRequestActivity;
import pt.up.fe.cmov16.cafe.cafeapp.ui.ScanQRCodeActivity;

public class MainActivity extends AppCompatActivity {
    private static int QR_CODE_CODE = 1;
    public static final String PRODUCTS_KEY = "PRODUCTS";
    public static final String VOUCHERS_KEY = "VOUCHERS";
    public static final String COSTUMER_KEY = "COSTUMERID";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApiInvoker.initializeInstance();

        findViewById(R.id.scanQRCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ScanQRCodeActivity.class);
                startActivityForResult(i, QR_CODE_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        if (requestCode == QR_CODE_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                Intent i = new Intent(MainActivity.this, ProcessRequestActivity.class);
                i.replaceExtras(bundle);
                startActivity(i);
            }
        }
    }
}
