package pt.up.fe.cmov16.client.clientapp.ui.slides;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import io.swagger.client.model.Voucher;
import pt.up.fe.cmov16.client.clientapp.R;
import pt.up.fe.cmov16.client.clientapp.database.VoucherContract;

public class VouchersFragment extends NamedFragment {
    private static String[] types = {"", "1 Free coffee", "1 Free popcorn pack", "5% Discount"};
    private ArrayList<VoucherMenuItem> vouchers = new ArrayList<>();
    private VouchersFragment.RVAdapter adapter;
    private boolean isDiscountSelected = false;
    private short commonVouchersSelected = 0;

    public static VouchersFragment newInstance(int page) {
        Bundle args = new Bundle();
        VouchersFragment fragment = new VouchersFragment();
        fragment.setArguments(args);
        fragment.title = "Vouchers";
        return fragment;
    }

    public void loadVouchers() {
        vouchers.clear();

        vouchers.addAll(getConvertedArrList(VoucherContract.loadVouchers(getContext())));
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    public ArrayList<Voucher> getSelectedVouchers() {
        ArrayList<Voucher> vs = new ArrayList<>();
        for (VoucherMenuItem vmi : vouchers)
            if (vmi.isChecked) vs.add(vmi.v);

        return vs;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_vouchers, container, false);
        final RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_vouchers);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new VouchersFragment.RVAdapter();
        rv.setAdapter(adapter);

        if (vouchers.size() == 0)
            loadVouchers();

        return rootView;
    }

    //update all times that user see this screen
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            loadVouchers();
        }
    }

    public ArrayList<VoucherMenuItem> getConvertedArrList(ArrayList<Voucher> vs) {
        ArrayList<VoucherMenuItem> vmis = new ArrayList<>();
        for (Voucher v : vs)
            vmis.add(new VoucherMenuItem(v));

        return vmis;
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
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.voucher_list_item, viewGroup, false);
                evh = new VouchersFragment.RVAdapter.VoucherViewHolder(v);
            }
            return evh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
            if (holder instanceof VouchersFragment.RVAdapter.VoucherViewHolder) {
//                ((VouchersFragment.RVAdapter.VoucherViewHolder) holder).voucherNumber.setText("" + vouchers.get(i).getNumber());
                ((VoucherViewHolder) holder).voucherType.setText(types[vouchers.get(i).v.getType()]);
                ((VoucherViewHolder) holder).checkBox.setChecked(vouchers.get(i).isChecked);
                ((VoucherViewHolder) holder).mVoucher = vouchers.get(i);
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
            CheckBox checkBox;
            VoucherMenuItem mVoucher;

            VoucherViewHolder(View itemView) {
                super(itemView);
                voucherNumber = (TextView) itemView.findViewById(R.id.voucher_number);
                voucherType = (TextView) itemView.findViewById(R.id.voucher_type);
                checkBox = (CheckBox) itemView.findViewById(R.id.voucher_checkbox);

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (mVoucher == null) return;
                        if (mVoucher.v.getType() == 3)
                            handleDiscountVoucherSelection(b);
                        else
                            handleVoucherSelection(b);
                    }
                });
            }

            private void handleVoucherSelection(boolean b) {
                if (b) {
                    if (commonVouchersSelected < 2) {
                        commonVouchersSelected++;
                        mVoucher.isChecked = true;
                    } else {
                        checkBox.setChecked(false);
                    }
                } else {
                    mVoucher.isChecked = false;
                    commonVouchersSelected--;
                }
            }

            private void handleDiscountVoucherSelection(boolean b) {
                if (b) {
                    if (isDiscountSelected) {
                        checkBox.setChecked(false);
                    } else {
                        mVoucher.isChecked = true;
                        isDiscountSelected = true;
                    }
                } else {
                    mVoucher.isChecked = false;
                    isDiscountSelected = false;
                }
            }
        }
    }

    class VoucherMenuItem {
        public Voucher v;
        public boolean isChecked;

        public VoucherMenuItem(Voucher v) {
            this.v = v;
            isChecked = false;
        }
    }
}
