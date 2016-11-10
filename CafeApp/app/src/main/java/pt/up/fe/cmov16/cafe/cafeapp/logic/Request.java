package pt.up.fe.cmov16.cafe.cafeapp.logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.IOException;
import java.io.StringReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
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
    private static final String TAG = Request.class.toString();
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

    public static boolean isValid(Context context, String encoded) {
        ArrayList<ProductMenuItem> productMenuItems = new ArrayList<>();
        ArrayList<Voucher> vouchers = new ArrayList<>();
        String costumerUUID = RequestDecode.decode(encoded, productMenuItems, vouchers);

        if (BlackListContract.isUserBlocked(context, costumerUUID)) {
            Log.d(TAG, "This user is blacklisted");
            return false;
        }

        if (vouchers.size() > 0)
            try {

                String original = Request.getPublicKey(context);
                String keyStr = original.replace("\\r", "");
                keyStr = keyStr.replace("\\n", "\n");
                PemReader pemReader = new PemReader(new StringReader(keyStr));
                PemObject pemObject = pemReader.readPemObject();
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                byte pubKeyBytes[] = pemObject.getContent();
                X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubKeyBytes);
                PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);

                Signature sig = Signature.getInstance("SHA1withRSA");
                sig.initVerify(pubKey);
                byte sigBytes[];
                for (Voucher voucher : vouchers) {
                    String vKey = voucher.getId() + " " + voucher.getType();
                    sig.update(vKey.getBytes());
                    sigBytes = Base64.decode(voucher.getSignature(), Base64.DEFAULT);
                    if (!sig.verify(sigBytes)) {
                        Log.d(TAG, "Signature verification failed");
                        return false;
                    }
                }
            } catch (NoSuchAlgorithmException | InvalidKeySpecException
                    | SignatureException | InvalidKeyException | IOException e) {
                e.printStackTrace();
            }
        return true;
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

    public static void saveUpdatedBlackListDate(Context context, String date) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("lastUpdatedBlackListDate", date);
        editor.apply();
    }

    public static String lastUpdatedBlackListDate(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return sharedPref.getString("lastUpdatedBlackListDate", "");
    }

    public int getID() {
        return id;
    }

    public String getRequest() {
        return request;
    }
}
