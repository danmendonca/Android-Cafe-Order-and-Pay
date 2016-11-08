package pt.up.fe.cmov16.client.clientapp.util;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import io.swagger.client.model.Voucher;
import pt.up.fe.cmov16.client.clientapp.logic.ProductMenuItem;
import pt.up.fe.cmov16.client.clientapp.logic.User;

public final class RequestEncode {
    public static final String PRODUCTS_ARRAY_KEY = "PRODUCTS_KEY";
    public static final String VOUCHERS_ARRAY_KEY = "VOUCHERS_KEY";
    private static final String TAG = RequestEncode.class.toString();

    public static String encode(Context context, Bundle bundle) {

        ArrayList<ProductMenuItem> products = null;
        ArrayList<Voucher> vouchers = null;

        if (bundle != null) {
            if (bundle.get(PRODUCTS_ARRAY_KEY) != null) {
                products = (ArrayList<ProductMenuItem>) bundle.get(PRODUCTS_ARRAY_KEY);
            }
            if (bundle.get(PRODUCTS_ARRAY_KEY) != null) {
                vouchers = (ArrayList<Voucher>) bundle.get(VOUCHERS_ARRAY_KEY);
            }
        } else {
            Log.e(TAG, "missing bundle");
            return "";
        }

        if (products == null) {
            Log.e(TAG, "missing products");
            return "";
        }
        if (vouchers == null) {
            vouchers = new ArrayList<>();
        }

        StringBuilder sb = new StringBuilder();
        sb.append(User.getInstance(context).getCostumerID());
        sb.append(";");

        for (int i = 0; i < products.size(); i++) {
            sb.append(products.get(i).getId());
            sb.append(",");
            sb.append(products.get(i).getQuantity());
            if (i != products.size() - 1) {
                sb.append("|");
            }
        }
        if (vouchers.size() == 0) {
            return sb.toString();
        }

        sb.append(";");

        for (int i = 0; i < vouchers.size(); i++) {
            sb.append(vouchers.get(i).getId());
            sb.append(",");
            sb.append(vouchers.get(i).getType());
            sb.append(",");
            sb.append(vouchers.get(i).getSignature());
            if (i != vouchers.size() - 1) {
                sb.append("|");
            }
        }
        return sb.toString();
    }
}
