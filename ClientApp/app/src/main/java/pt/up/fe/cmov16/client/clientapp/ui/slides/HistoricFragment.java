package pt.up.fe.cmov16.client.clientapp.ui.slides;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import io.swagger.client.model.Request;
import pt.up.fe.cmov16.client.clientapp.R;

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
