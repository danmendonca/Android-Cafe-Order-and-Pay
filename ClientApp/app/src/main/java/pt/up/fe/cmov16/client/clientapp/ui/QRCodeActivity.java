package pt.up.fe.cmov16.client.clientapp.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import net.glxn.qrgen.android.QRCode;

import pt.up.fe.cmov16.client.clientapp.R;
import pt.up.fe.cmov16.client.clientapp.util.RequestEncode;

public class QRCodeActivity extends Activity {

    private static final String TAG = QRCodeActivity.class.toString();
    private DisplayMetrics metrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        final ImageView img = (ImageView) findViewById(R.id.qrcodeView);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            Log.e(TAG, "Missing bundle");
            finish();
        }
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Bitmap bitmap = generateBitMap(bundle);
        if (bitmap != null)
            img.setImageBitmap(bitmap);
        else finish();
    }

    private Bitmap generateBitMap(Bundle bundle) {
        int w = (metrics.widthPixels >= metrics.heightPixels) ? metrics.widthPixels : metrics.heightPixels;
        String encoded = RequestEncode.encode(QRCodeActivity.this, bundle);
        if (encoded.isEmpty()) {
            Log.e(TAG, "Error generating QRCode message");
            return null;
        }
        return QRCode.from(encoded)
                .withSize(w, w)
                .withColor(0xFFEF4B4D, 0x0FFFFFFF)
                .bitmap();
    }
}
