package pt.up.fe.cmov16.cafe.cafeapp.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import pt.up.fe.cmov16.cafe.cafeapp.MainActivity;
import pt.up.fe.cmov16.cafe.cafeapp.R;


public class ScanQRCodeActivity extends Activity {

    private final static int PERMISSION_REQUEST_CAMERA = 0;
    private SurfaceView cameraView;
    private DisplayMetrics metrics;
    private boolean received;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);
        cameraView = (SurfaceView) findViewById(R.id.camera_view);
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        received = false;
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

        final BarcodeDetector barcodeDetector;
        final CameraSource cameraSource;

        barcodeDetector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.QR_CODE)
                        .build();

        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(this.metrics.widthPixels, this.metrics.heightPixels)
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
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barCodes = detections.getDetectedItems();

                if (barCodes.size() != 0 && !received) {
                    qrCodeReceived(barCodes.valueAt(0).displayValue);
                    received = true;
                }
            }
        });
    }

    private void qrCodeReceived(final String qrCodeString) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(MainActivity.ENCODED_STRING_KEY, qrCodeString);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}