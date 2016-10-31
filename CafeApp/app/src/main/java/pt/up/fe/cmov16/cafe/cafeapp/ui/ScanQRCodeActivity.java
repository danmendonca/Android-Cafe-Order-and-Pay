package pt.up.fe.cmov16.cafe.cafeapp.ui;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
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

import java.io.IOException;

import pt.up.fe.cmov16.cafe.cafeapp.R;


public class ScanQRCodeActivity extends Activity{
    private final static int PERMISSION_REQUEST_CAMERA = 0;
    private RelativeLayout cameraLayout;
    private SurfaceView cameraView;
    private DisplayMetrics metrics;
    private TextView codeInfo;
    Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);
        cameraLayout = (RelativeLayout) findViewById(R.id.camera_layout);
        cameraView = (SurfaceView) findViewById(R.id.camera_view);

        codeInfo = (TextView) findViewById(R.id.code_info);
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
                                           String permissions[], int[] grantResults) {
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
        final String r = "Received: "+qrCodeString;
        Log.e("ScanQrCode",r);
        handler.post(new Runnable() {
            @Override
            public void run() {
                codeInfo.setText(r);
            }
        });
    }
}
