package pt.up.fe.cmov16.cafe.cafeapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.RequestParam;
import io.swagger.client.model.RequestResponse;
import pt.up.fe.cmov16.cafe.cafeapp.MainActivity;
import pt.up.fe.cmov16.cafe.cafeapp.R;
import pt.up.fe.cmov16.cafe.cafeapp.database.BlackListContract;
import pt.up.fe.cmov16.cafe.cafeapp.database.PendingRequestContract;
import pt.up.fe.cmov16.cafe.cafeapp.logic.Request;

public class ProcessRequestActivity extends AppCompatActivity {

    private static final String TAG = ProcessRequestActivity.class.toString();
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_request);

        textView = (TextView) findViewById(R.id.activity_process_request_tv);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String encoded = (String) bundle.get(MainActivity.ENCODED_STRING_KEY);
            sendRequest(encoded);
        }
    }

    private void sendRequest(final String encoded) {
        RequestParam requestParam = Request.generateRequestParam(encoded);

        textView.append("\nSending request to server");
        DefaultApi api = new DefaultApi();
        api.createRequest(requestParam, new Response.Listener<RequestResponse>() {
            @Override
            public void onResponse(RequestResponse response) {
                onlineRequestFinished(response);
                textView.append("\nServer responde: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    offlineRequest(encoded);
                }
                textView.append("\nError responde: " + error.toString());
            }
        });
        textView.append("\nWaiting response");
    }

    private void onlineRequestFinished(RequestResponse response) {
        //TODO
        Log.e(TAG, response.toString());
    }

    private void offlineRequest(String encoded) {
        if (Request.isValid(ProcessRequestActivity.this, encoded))
            PendingRequestContract.savePendingRequest(ProcessRequestActivity.this, encoded);
        else BlackListContract.blockUser(ProcessRequestActivity.this, encoded.split(";")[0]);
    }
}
