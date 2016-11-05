package pt.up.fe.cmov16.client.clientapp.logic;

import io.swagger.client.model.Voucher;

public class VoucherMenuItem {
    public Voucher voucher;
    public boolean isChecked;

    private static String[] TYPES = {"", "1 Free coffee", "1 Free popcorn pack", "5% Discount"};

    public VoucherMenuItem(Voucher v) {
        this.voucher = v;
        isChecked = false;
    }
    public String getName(){
        return TYPES[voucher.getType()];
    }
}
