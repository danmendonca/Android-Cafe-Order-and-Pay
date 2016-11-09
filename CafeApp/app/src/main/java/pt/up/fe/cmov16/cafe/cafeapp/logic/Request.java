package pt.up.fe.cmov16.cafe.cafeapp.logic;

import java.util.ArrayList;
import java.util.List;

import io.swagger.client.model.RequestParam;
import io.swagger.client.model.RequestlineParam;
import io.swagger.client.model.Voucher;
import io.swagger.client.model.VoucherParam;
import pt.up.fe.cmov16.cafe.cafeapp.util.RequestDecode;

public class Request {
    private int id;
    private String request;

    public Request(int id, String request) {
        this.id = id;
        this.request = request;
    }

    public static RequestParam generateRequestParam(String encoded) {
        ArrayList<ProductMenuItem> productMenuItems = new ArrayList<>();
        ArrayList<Voucher> vouchers = new ArrayList<>();
        String costumerID = RequestDecode.decode(encoded, productMenuItems, vouchers);

        RequestParam requestParam = new RequestParam();
        requestParam.setCostumerUuid(costumerID);

        List<RequestlineParam> requestLines = new ArrayList<>();
        for (ProductMenuItem productMenuItem : productMenuItems) {
            RequestlineParam requestline = new RequestlineParam();
            requestline.setProductId(productMenuItem.getId());
            requestline.setQuantity(productMenuItem.getQuantity());
            requestLines.add(requestline);
        }
        requestParam.setRequestlines(requestLines);

        List<VoucherParam> requestVouchers = new ArrayList<>();
        for (Voucher voucher : vouchers) {
            VoucherParam voucherParam = new VoucherParam();
            voucherParam.setId(voucher.getId());
            voucherParam.setType(voucher.getType());
            voucherParam.setSignature(voucher.getSignature());
            requestVouchers.add(voucherParam);
        }
        requestParam.setRequestvouchers(requestVouchers);
        return requestParam;
    }

    public int getID() {
        return id;
    }

    public String getRequest() {
        return request;
    }
}
