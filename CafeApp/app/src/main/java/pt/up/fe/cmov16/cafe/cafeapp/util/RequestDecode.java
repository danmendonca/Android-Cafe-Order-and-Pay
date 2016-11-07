package pt.up.fe.cmov16.cafe.cafeapp.util;

import java.util.ArrayList;

import io.swagger.client.model.Product;
import io.swagger.client.model.Voucher;
import pt.up.fe.cmov16.cafe.cafeapp.logic.ProductMenuItem;

public final class RequestDecode {
    /**
     * decode string to array of products and vouchers
     *
     * @param encoded          string encoded
     * @param productMenuItems array to return the products
     * @param vouchers         array to return the vouchers
     * @return costumerID
     */
    public static String decode(String encoded,
                                ArrayList<ProductMenuItem> productMenuItems,
                                ArrayList<Voucher> vouchers) {

        String[] fields = encoded.split(";");

        String costumerID = "";
        productMenuItems.clear();
        vouchers.clear();
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            if (i == 0) {//costumerID
                costumerID = field;
            } else if (i == 1) {//products
                String[] prods = field.split("\\|");
                for (String prod : prods) {
                    String[] prodFields = prod.split(",");
                    Product product = new Product();
                    product.setId(Integer.valueOf(prodFields[0]));
                    ProductMenuItem productMenuItem = new ProductMenuItem(product);
                    productMenuItem.setQuantity(Integer.valueOf(prodFields[1]));
                    productMenuItems.add(productMenuItem);
                }
            } else if (i == 2) {//vouchers
                String[] vcs = field.split("\\|");
                for (String voucherString : vcs) {
                    String[] voucherFields = voucherString.split(",");
                    Voucher voucher = new Voucher();
                    voucher.setId(Integer.valueOf(voucherFields[0]));
                    voucher.setType(Integer.valueOf(voucherFields[1]));
                    voucher.setSignature(voucherFields[2]);
                    vouchers.add(voucher);
                }
            }
        }
        return costumerID;
    }
}
