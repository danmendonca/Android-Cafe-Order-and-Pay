package pt.up.fe.cmov16.cafe.cafeapp.ui;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import pt.up.fe.cmov16.cafe.cafeapp.MainActivity;

import static pt.up.fe.cmov16.cafe.cafeapp.MainActivity.ENCODED_STRING_KEY;

public class ListenNFC extends AppCompatActivity {

    private static final String TAG = ListenNFC.class.toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    void processIntent(Intent intent) {
        if (intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES) == null)
            return;

        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        intent.removeExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        String NFCMessage = new String(msg.getRecords()[0].getPayload());
        Log.e(TAG, NFCMessage);
        Bundle bundle = new Bundle();
        bundle.putString(ENCODED_STRING_KEY, NFCMessage);
        Intent i = new Intent(ListenNFC.this, ProcessRequestActivity.class);
        i.replaceExtras(bundle);
        startActivity(i);
        setIntent(null);
        finish();
    }
}
