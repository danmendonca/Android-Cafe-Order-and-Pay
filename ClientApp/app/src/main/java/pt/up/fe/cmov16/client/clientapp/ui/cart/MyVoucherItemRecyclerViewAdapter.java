package pt.up.fe.cmov16.client.clientapp.ui.cart;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import io.swagger.client.model.Voucher;
import pt.up.fe.cmov16.client.clientapp.R;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Voucher} and makes a call to the
 * specified {@link VoucherItemFragment.OnVoucherFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyVoucherItemRecyclerViewAdapter extends RecyclerView.Adapter<MyVoucherItemRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Voucher> mValues;
    private final VoucherItemFragment.OnVoucherFragmentInteractionListener mListener;
    private short nrSelected;
    private boolean discountSelected;


    public MyVoucherItemRecyclerViewAdapter(ArrayList<Voucher> items, VoucherItemFragment.OnVoucherFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        nrSelected = 0;
        discountSelected = false;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_voucheritem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mContentView.setText(getVoucherContent(mValues.get(position)));

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (mListener != null) {
                    if (isChecked)
                        voucherSelectedHandler(holder);
                    else
                        voucherDeselectHandler(holder);
                }
            }
        });
    }

    private void voucherDeselectHandler(ViewHolder holder) {
        if (holder.mItem.getType() < 3) {
            nrSelected--;
            Log.d("nrVouSelNor", String.valueOf(nrSelected));
        } else {
            if (!discountSelected)
                Log.d("nrVouSelDis", "False->false");
            discountSelected = false;
            Log.d("nrVouSelDis", "True->false");
        }
        mListener.onRemVoucherFragmentInteraction(holder.mItem);
    }

    private void voucherSelectedHandler(ViewHolder holder) {
        if (holder.mItem.getType() < 3 && nrSelected < 2) {
            nrSelected++;
            Log.d("nrVouSelNor", String.valueOf(nrSelected));
            mListener.onAddVoucherFragmentInteraction(holder.mItem);
        } else if (holder.mItem.getType() == 3 && !discountSelected) {
            if (discountSelected)
                Log.d("nrVouSelDis", "True->True");
            else
                Log.d("nrVouSelDis", "False->True");
            discountSelected = true;
            mListener.onAddVoucherFragmentInteraction(holder.mItem);
        } else {
            holder.mCheckBox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public String getVoucherContent(Voucher v) {
        switch (v.getType()) {
            case 1:
                return "Voucher Coffee";
            case 2:
                return "Voucher Popcorn";
            default:
                return "Voucher 5% Off";
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final CheckBox mCheckBox;
        public Voucher mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
            mCheckBox = (CheckBox) view.findViewById(R.id.voucher_checkbox);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
