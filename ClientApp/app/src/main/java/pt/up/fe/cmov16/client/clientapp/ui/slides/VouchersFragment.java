package pt.up.fe.cmov16.client.clientapp.ui.slides;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import io.swagger.client.model.Voucher;
import pt.up.fe.cmov16.client.clientapp.R;
import pt.up.fe.cmov16.client.clientapp.database.VoucherContract;

public class VouchersFragment extends NamedFragment {
    private static String[] types = {"", "1 Free coffee", "1 Free popcorn pack", "5% Discount"};
    private ArrayList<Voucher> vouchers = new ArrayList<>();
    private VouchersFragment.RVAdapter adapter;

    public static VouchersFragment newInstance(int page) {
        Bundle args = new Bundle();
        VouchersFragment fragment = new VouchersFragment();
        fragment.setArguments(args);
        fragment.title = "Vouchers";
        return fragment;
    }

    public void loadVouchers() {
        vouchers.clear();
        vouchers.addAll(VoucherContract.loadVouchers(getContext()));
        if (adapter != null)//when data loads before adapter creation
            adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView =
                (ViewGroup) inflater.inflate(R.layout.fragment_vouchers, container, false);

        final RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_vouchers);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new VouchersFragment.RVAdapter();
        rv.setAdapter(adapter);

        if (vouchers.size() == 0)
            loadVouchers();

        return rootView;
    }

    public class RVAdapter extends RecyclerView.Adapter {
        private int VIEW_TYPE = 0;

        @Override
        public int getItemViewType(int position) {
            return VIEW_TYPE;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v;
            RecyclerView.ViewHolder evh = null;
            if (viewType == VIEW_TYPE) {
                v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.voucher_list_item, viewGroup, false);
                evh = new VouchersFragment.RVAdapter.VoucherViewHolder(v);
            }
            return evh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
            if (holder instanceof VouchersFragment.RVAdapter.VoucherViewHolder) {
                ((VouchersFragment.RVAdapter.VoucherViewHolder) holder)
                        .voucherNumber.setText(String.valueOf(vouchers.get(i).getNumber()));
                ((VouchersFragment.RVAdapter.VoucherViewHolder) holder)
                        .voucherType.setText(types[vouchers.get(i).getType()]);
            }
        }

        @Override
        public int getItemCount() {
            return vouchers.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        class VoucherViewHolder extends RecyclerView.ViewHolder {
            TextView voucherNumber, voucherType;

            VoucherViewHolder(View itemView) {
                super(itemView);
                voucherNumber = (TextView) itemView.findViewById(R.id.voucher_number);
                voucherType = (TextView) itemView.findViewById(R.id.voucher_type);
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            loadVouchers();
        }
    }
}
