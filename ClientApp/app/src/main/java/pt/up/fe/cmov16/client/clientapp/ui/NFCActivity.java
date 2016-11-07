package pt.up.fe.cmov16.client.clientapp.ui;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.nio.charset.Charset;

import pt.up.fe.cmov16.client.clientapp.R;
import pt.up.fe.cmov16.client.clientapp.util.NfcApp;
import pt.up.fe.cmov16.client.clientapp.util.RequestEncode;

public class NFCActivity extends AppCompatActivity implements NfcAdapter.OnNdefPushCompleteCallback {

    private static final String TAG = NFCActivity.class.toString();
    private NfcApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        Bundle bundle = getIntent().getExtras();
        String encoded = "";
        if (bundle != null) {
            encoded = RequestEncode.encode(NFCActivity.this, bundle);
            if (encoded.isEmpty()) {
                Log.e(TAG, "Error generating NFC message");
                finish();
            }
        }else{
            Log.e(TAG, "Missing bundle");
            finish();
        }

        app = (NfcApp) getApplication();
        String tag = "text/plain";
        byte[] message = encoded.getBytes();
        NdefMessage msg = new NdefMessage(new NdefRecord[]{createMimeRecord(tag, message)});

        NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        // Register a NDEF message to be sent in a beam operation (P2P)
        mNfcAdapter.setNdefPushMessage(msg, this);
        mNfcAdapter.setOnNdefPushCompleteCallback(this, this);
        app.reply = "Entered NfcSend";
    }

    public NdefRecord createMimeRecord(String mimeType, byte[] payload) {
        byte[] mimeBytes = mimeType.getBytes(Charset.forName("ISO-8859-1"));
        return new NdefRecord(
                NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);
    }

    @Override
    public void onNdefPushComplete(NfcEvent arg0) {
        app.reply = "";
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), "Request sent.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
