package pt.up.fe.cmov16.client.clientapp.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import io.swagger.client.model.Voucher;
import pt.up.fe.cmov16.client.clientapp.R;
import pt.up.fe.cmov16.client.clientapp.database.VoucherContract;
import pt.up.fe.cmov16.client.clientapp.logic.ProductMenuItem;
import pt.up.fe.cmov16.client.clientapp.logic.User;
import pt.up.fe.cmov16.client.clientapp.logic.VoucherMenuItem;
import pt.up.fe.cmov16.client.clientapp.util.RequestEncode;

public class CartActivity extends AppCompatActivity {
    private static final String TAG = CartActivity.class.toString();
    private boolean isDiscountSelected = false;
    private short commonVouchersSelected = 0;

    private ArrayList<ProductMenuItem> prods = new ArrayList<>();
    private ArrayList<VoucherMenuItem> voucherMenuItems = new ArrayList<>();
    private ItemsRvAdapter adapter;
    private double totalPrice;
    private TextView totalPriceTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        totalPrice = 0;
        Bundle b = getIntent().getExtras();
        if (b.get(RequestEncode.PRODUCTS_ARRAY_KEY) == null) {
            finish();
            return;
        }
        for (ProductMenuItem pmi : (ArrayList<ProductMenuItem>) b.get(RequestEncode.PRODUCTS_ARRAY_KEY)) {
            prods.add(pmi);
            totalPrice += pmi.getUnitPrice() * pmi.getQuantity();
        }
        totalPriceTV = (TextView) findViewById(R.id.total_price);
        totalPriceTV.setText(doubleToDecimalString(totalPrice));
        RecyclerView rv = (RecyclerView) findViewById(R.id.cart_items_rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        adapter = new ItemsRvAdapter();
        rv.setAdapter(adapter);

        findViewById(R.id.cart_buy_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPINDialog();
            }
        });

        findViewById(R.id.add_vouchers_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openVouchersDialog();
            }
        });

        for (Voucher v : VoucherContract.loadVouchers(this))
            voucherMenuItems.add(new VoucherMenuItem(v));


    }

    private void openVouchersDialog() {
        CharSequence[] vouchersName = new CharSequence[voucherMenuItems.size()];
        boolean[] selecteds = new boolean[voucherMenuItems.size()];
        for (int i = 0; i < voucherMenuItems.size(); i++) {
            vouchersName[i] = voucherMenuItems.get(i).getName();
            selecteds[i] = voucherMenuItems.get(i).isChecked;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set the dialog title
        builder.setTitle("Choose Vouchers")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(vouchersName, selecteds, new DialogListener())
                // Set the action buttons
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        adapter.updateSelectedVouchers();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder.create().show();

    }

    private void openPINDialog() {
        LayoutInflater li = LayoutInflater.from(CartActivity.this);
        View promptsView = li.inflate(R.layout.confirm_pin_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                CartActivity.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogPIN);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Buy",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit pwText
                                if (userInput.getText().toString()
                                        .equals(User.getInstance(CartActivity.this)
                                                .getPIN())) {
                                    makeRequest();
                                } else {
                                    Toast.makeText(CartActivity.this, "Invalid PIN", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        // show it
        alertDialog.show();
    }

    private void makeRequest() {
        NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        // Check for available NFC Adapter
        if (mNfcAdapter == null || !mNfcAdapter.isEnabled()) {
            Toast.makeText(getApplicationContext(), "NFC is not available/enabled on this device.", Toast.LENGTH_LONG).show();
            makeRequest("QRCode");
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    CartActivity.this);

            // set dialog message
            alertDialogBuilder
                    .setTitle("Choose method")
                    .setCancelable(true)
                    .setPositiveButton("QRCode",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    makeRequest("QRCode");
                                }
                            })
                    .setNeutralButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                    .setNegativeButton("NFC",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    makeRequest("NFC");
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            // show it
            alertDialog.show();
        }
    }


    private void makeRequest(String type) {
        ArrayList<Voucher> vouchers = new ArrayList<>();
        for (VoucherMenuItem v : voucherMenuItems) {
            if (v.isChecked)
                vouchers.add(v.voucher);
        }
        Intent i;
        switch (type) {
            case "QRCode":
                i = new Intent(CartActivity.this, QRCodeActivity.class);
                VoucherContract.deleteVouchersFromDB(CartActivity.this, vouchers);
                break;
            case "NFC":
                i = new Intent(CartActivity.this, NFCActivity.class);
                break;
            default:
                Log.e(TAG, "Wrong type for request encoding");
                return;
        }
        i.putExtra(RequestEncode.PRODUCTS_ARRAY_KEY, prods);
        i.putExtra(RequestEncode.VOUCHERS_ARRAY_KEY, vouchers);
        startActivity(i);
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private String doubleToDecimalString(double d) {
        return new DecimalFormat("0.00####",
                DecimalFormatSymbols.getInstance(Locale.ENGLISH)).format(d);
    }

    private class ItemsRvAdapter extends RecyclerView.Adapter<ItemsRvAdapter.Holder> {
        ArrayList<VoucherMenuItem> selectedVouchers = new ArrayList<>();

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cart_item_card, parent, false);
            return new Holder(itemView);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            if (position < prods.size()) {
                holder.quantity.setText(String.valueOf(prods.get(position).getQuantity()));
                holder.name.setText(prods.get(position).getName());
                double total =
                        prods.get(position).getUnitPrice() * prods.get(position).getQuantity();
                holder.total.setText(doubleToDecimalString(total));
            } else {
                position = position - prods.size();
                holder.quantity.setText("");
                holder.name.setText(selectedVouchers.get(position).getName());
                if (selectedVouchers.get(position).voucher.getType() == 3) {
                    String discountStr = "- ";
                    double discount = totalPrice * 0.05;
                    discountStr += (doubleToDecimalString(discount));
                    holder.total.setText(discountStr);
                } else {
                    holder.total.setText("");
                }
            }
        }

        @Override
        public int getItemCount() {
            return prods.size() + selectedVouchers.size();
        }

        private void updateSelectedVouchers() {
            selectedVouchers.clear();
            for (VoucherMenuItem voucherMenuItem : voucherMenuItems) {
                if (voucherMenuItem.isChecked)
                    selectedVouchers.add(voucherMenuItem);
            }
            if (isDiscountSelected)
                totalPriceTV.setText(doubleToDecimalString(totalPrice * 0.95));
            else totalPriceTV.setText(doubleToDecimalString(totalPrice));
            notifyDataSetChanged();
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView quantity, name, total;

            Holder(View itemView) {
                super(itemView);
                quantity = (TextView) itemView.findViewById(R.id.item_quantity);
                name = (TextView) itemView.findViewById(R.id.item_name);
                total = (TextView) itemView.findViewById(R.id.item_total);
            }
        }
    }

    private class DialogListener implements DialogInterface.OnMultiChoiceClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which,
                            boolean isChecked) {

            ListView list = ((AlertDialog) dialog).getListView();
            handleVoucherSelection(voucherMenuItems.get(which), isChecked, list, which);
        }

        private void handleVoucherSelection(VoucherMenuItem mVoucher, boolean isChecked,
                                            ListView list, int pos) {

            if (mVoucher.voucher.getType() == 3) {//desconto
                if (isChecked) {
                    if (isDiscountSelected) {
                        list.setItemChecked(pos, false);
                    } else {
                        mVoucher.isChecked = true;
                        isDiscountSelected = true;
                    }
                } else {
                    mVoucher.isChecked = false;
                    isDiscountSelected = false;
                }
            } else {//normais
                if (isChecked) {
                    if (commonVouchersSelected < 2) {
                        commonVouchersSelected++;
                        mVoucher.isChecked = true;
                    } else {
                        list.setItemChecked(pos, false);
                    }
                } else {
                    mVoucher.isChecked = false;
                    commonVouchersSelected--;
                }
            }
        }
    }
}
