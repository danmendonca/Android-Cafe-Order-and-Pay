package pt.up.fe.cmov16.cafe.cafeapp.util;


import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;

import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.RequestParam;
import io.swagger.client.model.RequestResponse;
import pt.up.fe.cmov16.cafe.cafeapp.database.PendingRequestContract;
import pt.up.fe.cmov16.cafe.cafeapp.logic.Request;

public class RequestsThread extends Thread {
    private static final String TAG = RequestsThread.class.toString();
    private final Context context;

    public RequestsThread(Context context) {
        super();
        this.context = context;
    }

    @Override
    public void run() {
        DefaultApi api = new DefaultApi();
        while (true) {
            ArrayList<Request> requests = PendingRequestContract.getPendingRequests(context);
            for (final Request request : requests) {
                RequestParam requestParam = Request.generateRequestParam(request.getRequest());
                api.createRequest(requestParam, new Response.Listener<RequestResponse>() {
                    @Override
                    public void onResponse(RequestResponse response) {
                        PendingRequestContract.deletePendingRequest(context, request);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
            }
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
