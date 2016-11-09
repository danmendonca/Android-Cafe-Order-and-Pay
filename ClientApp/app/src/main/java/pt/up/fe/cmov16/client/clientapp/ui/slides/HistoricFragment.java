package pt.up.fe.cmov16.client.clientapp.ui.slides;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;

import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.Consult;
import io.swagger.client.model.PinLoginParam;
import io.swagger.client.model.RequestResponse;
import pt.up.fe.cmov16.client.clientapp.R;
import pt.up.fe.cmov16.client.clientapp.database.VoucherContract;
import pt.up.fe.cmov16.client.clientapp.logic.User;
import pt.up.fe.cmov16.client.clientapp.ui.RequestDetailActivity;

public class HistoricFragment extends NamedFragment {

    private static final String STATE_REQUESTS = "REQUESTS_KEY";
    private ArrayList<RequestResponse> requestsMade = new ArrayList<>();
    private RVAdapter adapter = new RVAdapter();

    public static HistoricFragment newInstance(int page) {
        Bundle args = new Bundle();
        // if necessary add arguments here
        //args.putInt(ARG_PAGE, page);
        HistoricFragment fragment = new HistoricFragment();
        fragment.setArguments(args);
        fragment.title = "Historic";
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putSerializable(STATE_REQUESTS, requestsMade);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_historic, container, false);

        final RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_historic);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RVAdapter();
        rv.setAdapter(adapter);

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            requestsMade = (ArrayList<RequestResponse>) savedInstanceState.getSerializable(STATE_REQUESTS);
            adapter.notifyDataSetChanged();
        }

        return rootView;
    }

    private void askForRequests(final Context context) {
        DefaultApi api = new DefaultApi();
        PinLoginParam param = new PinLoginParam();
        User me = User.getInstance(getContext());
        param.setPin(me.getPIN());
        param.setUuid(me.getCostumerID());
        try {
            api.getCostumerRequests(param, new Response.Listener<Consult>() {
                        @Override
                        public void onResponse(Consult response) {
                            requestsMade.clear();
                            requestsMade.addAll(response.getRequests());
                            if (adapter != null)
                                adapter.notifyDataSetChanged();
                            //response.getRequests().get(0).
                            VoucherContract.saveVoucherInDB(context, response.getVouchers(),
                                    response.getRequests().size());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.getMessage() != null)
                                Log.d("RequestsOv-Fetch", error.getMessage());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refresh(Context context) {
        askForRequests(context);
        Toast.makeText(context, "Updating...", Toast.LENGTH_LONG).show();
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
            RequestOverviewViewHolder holder = null;
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
                RequestResponse request = requestsMade.get(position);
                ((RequestOverviewViewHolder) holder).header.setText(String.valueOf(request.getCreatedAt()));
                ((RequestOverviewViewHolder) holder).info.setText(String.valueOf(request.getNumber()));
                ((RequestOverviewViewHolder) holder).mItem = request;
            }
        }

        @Override
        public int getItemCount() {
            return requestsMade.size();
        }

        private class RequestOverviewViewHolder extends RecyclerView.ViewHolder {
            TextView header, info;
            RequestResponse mItem;

            RequestOverviewViewHolder(View view) {
                super(view);
                header = (TextView) view.findViewById(R.id.request_ov_header);
                info = (TextView) view.findViewById(R.id.request_ov_info);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getContext(), RequestDetailActivity.class);
                        i.putExtra(RequestDetailActivity.REQUESTID_KEY, mItem.getId());
                        startActivity(i);
                    }
                });
            }
        }
    }
}
