package pt.up.fe.cmov16.cafe.cafeapp.ui;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import io.swagger.client.model.Product;
import io.swagger.client.model.Voucher;
import pt.up.fe.cmov16.cafe.cafeapp.R;
import pt.up.fe.cmov16.cafe.cafeapp.logic.ProductMenuItem;


public class ScanQRCodeActivity extends Activity {
    private final static int PERMISSION_REQUEST_CAMERA = 0;
    private RelativeLayout cameraLayout, requestLayout;
    private SurfaceView cameraView;
    private DisplayMetrics metrics;
    private TextView requestTV;
    Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);
        cameraLayout = (RelativeLayout) findViewById(R.id.camera_layout);
        requestLayout = (RelativeLayout) findViewById(R.id.info_request);
        cameraView = (SurfaceView) findViewById(R.id.camera_view);
        requestTV = (TextView) findViewById(R.id.request_text);
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        handler = new Handler();
        requestCameraPermission();
    }

    /**
     * Android 6+ special permissions request
     */
    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(ScanQRCodeActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(ScanQRCodeActivity.this, new String[]{Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CAMERA);
        } else {
            onCreateCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    onCreateCamera();
                } else {
                    finish();
                }
            }
        }
    }

    /**
     * Load camera view
     */
    private void onCreateCamera() {
        cameraLayout.setVisibility(View.VISIBLE);

        BarcodeDetector barcodeDetector;
        final CameraSource cameraSource;

        barcodeDetector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.QR_CODE)
                        .build();

        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(this.metrics.widthPixels,
                        this.metrics.heightPixels)
                .setAutoFocusEnabled(true)
                .build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    cameraSource.start(cameraView.getHolder());
                } catch (SecurityException | IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {

            @Override
            public void release() {
                cameraSource.release();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barCodes = detections.getDetectedItems();

                if (barCodes.size() != 0) {
                    qrCodeReceived(barCodes.valueAt(0).displayValue);
                    release();
                }
            }
        });
    }

    private void qrCodeReceived(final String qrCodeString) {
        final String r = "Received: " + qrCodeString;
        Log.e("ScanQrCode", r);
        handler.post(new Runnable() {
            @Override
            public void run() {
                cameraLayout.setVisibility(View.GONE);
                requestLayout.setVisibility(View.GONE);
                loadFromJson(qrCodeString);
            }
        });
    }

    private void loadFromJson(String qrCodeString) {
        requestLayout.setVisibility(View.VISIBLE);
        requestTV.setText(qrCodeString);

        String[] fields = qrCodeString.split(";");

        String costumerID = "";
        ArrayList<ProductMenuItem> productMenuItems = new ArrayList<>();
        ArrayList<Voucher> vouchers = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            Log.e("Fields", fields[i]);
            String field = fields[i];
            if (i == 0) {//costumerID
                costumerID = field;
            } else if (i == 1) {//products
                String[] prods = field.split("\\|");
                for (String prod : prods) {
                    String[] prodFields = prod.split(",");
                    Product product = new Product();
                    product.setId(Integer.valueOf(prodFields[0]));
                    ProductMenuItem productMenuItem = new ProductMenuItem(product);
                    productMenuItem.setQuantity(Integer.valueOf(prodFields[1]));
                    productMenuItems.add(productMenuItem);
                }
            } else if (i == 2) {//vouchers
                String[] vcs = field.split("\\|");
                for (String voucherString : vcs) {
                    String[] voucherFields = voucherString.split(",");
                    Voucher voucher = new Voucher();
                    voucher.setId(Integer.valueOf(voucherFields[0]));
                    voucher.setType(Integer.valueOf(voucherFields[1]));
                    voucher.setKey(voucherFields[2]);
                    vouchers.add(voucher);
                }
            }
        }
        processRequest(costumerID, productMenuItems, vouchers);

//        String costumerID = "";
//        ArrayList<ProductMenuItem> productMenuItems = new ArrayList<>();
//        ArrayList<Voucher> vouchers = new ArrayList<>();
//        try {
//            JSONObject request = new JSONObject(qrCodeString);
//            costumerID = request.getString("costumerID");
//
//            JSONArray productsArray = request.getJSONArray("products");
//            JSONArray vouchersArray = request.getJSONArray("vouchers");
//
//            for (int i = 0; i < productsArray.length(); i++) {
//                JSONObject prodJson = (JSONObject) productsArray.get(i);
//                Product product = new Product();
//                product.setId(prodJson.getInt("id"));
//                ProductMenuItem productMenuItem = new ProductMenuItem(product);
//                productMenuItem.setQuantity(prodJson.getInt("quantity"));
//                productMenuItems.add(productMenuItem);
//            }
//            for (int i = 0; i < vouchersArray.length(); i++) {
//                JSONObject voucherJson = (JSONObject) vouchersArray.get(i);
//                Voucher voucher = new Voucher();
//                voucher.setId(voucherJson.getInt("id"));
//                voucher.setType(voucherJson.getInt("type"));
//                voucher.setKey(voucherJson.getString("signature"));
//                vouchers.add(voucher);
//            }
//
//            processRequest(costumerID,productMenuItems,vouchers);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    private void processRequest(String costumerID, ArrayList<ProductMenuItem> productMenuItems, ArrayList<Voucher> vouchers) {
        //TODO fazer qq com isto em vez de brincar às strings :P

        StringBuilder sb = new StringBuilder();
        sb.append("dados lidos do qrCode:\n");
        sb.append("\tcostumerID: ");
        sb.append(costumerID);
        sb.append("\n");

        sb.append("products: ");
        sb.append("\n");
        for (ProductMenuItem prod : productMenuItems) {
            sb.append("\tProd{id: ");
            sb.append(prod.getId());
            sb.append(";quant: ");
            sb.append(prod.getQuantity());
            sb.append("}\n");
        }

        sb.append("vouchers: ");
        sb.append("\n");
        for (Voucher voucher : vouchers) {
            sb.append("\tVoucher{id: ");
            sb.append(voucher.getId());
            sb.append(";type: ");
            sb.append(voucher.getType());
            sb.append(";signature: ");
            sb.append(voucher.getKey());
            sb.append("}\n");
        }
        requestTV.setText(sb.toString());
    }
}
