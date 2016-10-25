package pt.up.fe.cmov16.client.clientapp.ui.slides;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pt.up.fe.cmov16.client.clientapp.R;

public class HistoricFragment extends NamedFragment {
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
        return rootView;
    }
}
