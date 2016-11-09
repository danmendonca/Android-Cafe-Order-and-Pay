package pt.up.fe.cmov16.cafe.cafeapp.logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import io.swagger.client.model.RequestParam;
import io.swagger.client.model.RequestlineParam;
import io.swagger.client.model.Voucher;
import io.swagger.client.model.VoucherParam;
import pt.up.fe.cmov16.cafe.cafeapp.R;
import pt.up.fe.cmov16.cafe.cafeapp.database.BlackListContract;
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

    public static boolean isValid(Context context, String encoded) {
        ArrayList<ProductMenuItem> productMenuItems = new ArrayList<>();
        ArrayList<Voucher> vouchers = new ArrayList<>();
        String costumerUUID = RequestDecode.decode(encoded, productMenuItems, vouchers);

        if (BlackListContract.isUserBlocked(context, costumerUUID))
            return false;

        return true;
//        KeyPairGenerator kgen = null;  //RSA keys
//        try {
//            kgen = KeyPairGenerator.getInstance("RSA");
//            kgen.initialize(368);                                         //size in bits
//            KeyPair kp = kgen.generateKeyPair();
//            PrivateKey pri = kp.getPrivate();                             // private key in a Java class
//            PublicKey pub = kp.getPublic();
//            String res ="";// the corresponding public key in a Java class
//            res += ("Private: (" + pri.toString() +")\n");
//            res += ("Public: (" + pub.toString() + ")\n");
//            Log.e("asd",res);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//
//
//        byte[] keyBytes;
//        KeyFactory keyFactory;
//        PublicKey pub;
//        String keyStr = Request.getPublicKey(context);
//        keyStr = keyStr.replace("-----BEGIN RSA PUBLIC KEY-----\\n", "");
//        keyStr = keyStr.replace("\\n-----END RSA PUBLIC KEY-----\\n", "");
//        //keyStr = keyStr.replace("\\n", "");
//        Log.e("Req", keyStr);
//        try {
//            keyBytes = Base64.decode(Base64.encode(keyStr.getBytes("utf-8"), Base64.DEFAULT), Base64.DEFAULT);
//            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
//            keyFactory = KeyFactory.getInstance("RSA");
//            pub = keyFactory.generatePublic(spec);
//        } catch (UnsupportedEncodingException | InvalidKeySpecException | NoSuchAlgorithmException e) {
//            e.printStackTrace();
//            return false;
//        }
//
//        Signature sg;
//        try {
//            sg = Signature.getInstance("SHA1WithRSA");
//            sg.initVerify(pub);
//            for (Voucher voucher : vouchers) {
//                String vKey = voucher.getId() + " " + voucher.getType();
//                sg.update(vKey.getBytes());
//                if (!sg.verify(voucher.getSignature().getBytes()))
//                    return false;
//            }
//        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
    }

    public static boolean hasPublicKey(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return !sharedPref.getString("public_key", "").isEmpty();
    }

    public static void savePublicKey(Context context, String publicKey) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("public_key", publicKey);
        editor.apply();
    }

    private static String getPublicKey(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return sharedPref.getString("public_key", "");
    }
}
