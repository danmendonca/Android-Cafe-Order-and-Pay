package pt.up.fe.cmov16.client.clientapp.ui.slides;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.Consult;
import io.swagger.client.model.PinLoginParam;
import io.swagger.client.model.Request;
import io.swagger.client.model.Voucher;
import pt.up.fe.cmov16.client.clientapp.R;
import pt.up.fe.cmov16.client.clientapp.logic.User;
import pt.up.fe.cmov16.client.clientapp.util.ShPrefKeys;

public class HistoricFragment extends NamedFragment {

    private ArrayList<Request> requestsMade = new ArrayList<>();
    private RVAdapter adapter = new RVAdapter();

    public static HistoricFragment newInstance(int page) {
        Bundle args = new Bundle();
        // if necessary add arguments here
        //args.putInt(ARG_PAGE, page);
        HistoricFragment fragment = new HistoricFragment();
        fragment.setArguments(args);
        fragment.tittle = "Historic";
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_historic, container, false);

        SharedPreferences preferences = getContext().getSharedPreferences(getContext().getResources()
                .getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        Set<String> rqsts = preferences.getStringSet(ShPrefKeys.RequestsOvShPrefKey, null);

        if (rqsts != null && rqsts.size() > 0) {
            Gson gson = new Gson();
            for (String r : rqsts) {
                Request fromGson = gson.fromJson(r, Request.class);
                if (fromGson != null)
                    requestsMade.add(fromGson);
            }
        }

        if (requestsMade.size() == 0) {
            askForRequests();
        }

        final RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_historic);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RVAdapter();
        rv.setAdapter(adapter);

        return rootView;
    }

    private void askForRequests() {
        DefaultApi api = new DefaultApi();
        PinLoginParam param = new PinLoginParam();
        User me = User.getInstance(getContext());
        param.setPin(me.getPIN());
        param.setUuid(me.getCostumerID());
        try {
            api.getCostumerRequests(param, new Response.Listener<Consult>() {
                        @Override
                        public void onResponse(Consult response) {
                            for (Request r : response.getRequests())
                                requestsMade.add(r);
                            if (response.getVouchers().size() > 0) {
                                Set<String> vouchersJson = new HashSet<String>();
                                Gson gson = new Gson();
                                for (Voucher v : response.getVouchers()) {
                                    String vStr = gson.toJson(v);
                                    vouchersJson.add(vStr);
                                }
                                Context ctx = getContext();
                                SharedPreferences sp = ctx.getSharedPreferences(
                                        ctx.getResources().getString(R.string.preference_file_key),
                                        Context.MODE_PRIVATE);

                                SharedPreferences.Editor editor = sp.edit();
                                editor.putStringSet(ShPrefKeys.vouchersShPrefKey, vouchersJson);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("RequestsOv-Fetch", error.getMessage());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        Context ctx = getContext();
        SharedPreferences sp = ctx.getSharedPreferences(
                ctx.getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        Set<String> requestsJson = new HashSet<String>();
        Gson gson = new Gson();
        for (Request r : requestsMade)
            requestsJson.add(gson.toJson(r));

        editor.putStringSet(ShPrefKeys.RequestsOvShPrefKey, requestsJson);
        editor.commit();
        super.onPause();
    }


    private class RVAdapter extends RecyclerView.Adapter {
        private int VIEW_TYPE = 1;

        @Override
        public int getItemViewType(int position) {
            return VIEW_TYPE;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            RecyclerView.ViewHolder holder = null;
            if (viewType == VIEW_TYPE) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.request_overview_list_item, parent, false);
                holder = new RequestOverviewViewHolder(view);
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof RequestOverviewViewHolder) {
                Request request = requestsMade.get(position);
                ((RequestOverviewViewHolder) holder).header.setText(request.getId());
                ((RequestOverviewViewHolder) holder).info.setText(request.getCostumerUuid());
            }
        }

        @Override
        public int getItemCount() {
            return requestsMade.size();
        }

        private class RequestOverviewViewHolder extends RecyclerView.ViewHolder {
            TextView header, info;

            public RequestOverviewViewHolder(View view) {
                super(view);
                header = (TextView) view.findViewById(R.id.request_ov_header);
                info = (TextView) view.findViewById(R.id.request_ov_info);
            }
        }
    }
}
