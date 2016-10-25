package pt.up.fe.cmov16.client.clientapp.ui.slides;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pt.up.fe.cmov16.client.clientapp.R;

public class VouchersFragment extends NamedFragment {
    public static VouchersFragment newInstance(int page) {
        Bundle args = new Bundle();
        // if necessary add arguments here
        //args.putInt(ARG_PAGE, page);
        VouchersFragment fragment = new VouchersFragment();
        fragment.setArguments(args);
        fragment.tittle = "Vouchers";
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_vouchers, container, false);
        return rootView;
    }
}
