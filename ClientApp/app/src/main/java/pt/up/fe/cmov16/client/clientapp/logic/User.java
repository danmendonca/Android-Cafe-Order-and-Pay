package pt.up.fe.cmov16.client.clientapp.logic;


import android.content.Context;
import android.content.SharedPreferences;

import pt.up.fe.cmov16.client.clientapp.R;


public class User {

    private static User instance = null;
    private String name, username, password, creditCardNum, creditCardDate, PIN, costumerID;

    /* ***************************************************************************************** */
    private User(Context context) {
        loadCostumerData(context);
    }

    public static User getInstance(Context context) {
        if (instance == null) {
            instance = new User(context);
        }
        return instance;
    }

    public boolean isFirstTime() {
        return costumerID.isEmpty();

    }

    public void createUserData(String name, String username, String password,
                               String creditCardNum, String creditCardDate,
                               String PIN, String costumerID, Context context) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.creditCardNum = creditCardNum;
        this.creditCardDate = creditCardDate;
        this.PIN = PIN;
        this.costumerID = costumerID;
        saveCostumerData(context);
    }

    private void saveCostumerData(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
//        Gson gson = new Gson();
//        String userStr = gson.toJson(this);
//        editor.putString(userSharedPreferencesKey ,userStr);
        editor.putString("name", name);
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("creditCardNum", creditCardNum);
        editor.putString("creditCardDate", creditCardDate);
        editor.putString("PIN", PIN);
        editor.putString("costumerID", costumerID);
        editor.apply();
    }

    private void loadCostumerData(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
//        Gson gson = new Gson();
//        User myUser = gson.fromJson(sharedPref.getString(userSharedPreferencesKey, ""), User.class);
        name = sharedPref.getString("name", "");
        username = sharedPref.getString("username", "");
        password = sharedPref.getString("password", "");
        creditCardNum = sharedPref.getString("creditCardNum", "");
        creditCardDate = sharedPref.getString("creditCardDate", "");
        PIN = sharedPref.getString("PIN", "");
        costumerID = sharedPref.getString("costumerID", "");
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCreditCardNum() {
        return creditCardNum;
    }

    public String getCreditCardDate() {
        return creditCardDate;
    }

    public String getPIN() {
        return PIN;
    }

    public String getCostumerID() {
        return costumerID;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User {\n");

        sb.append("  costumerID: ").append(costumerID).append("\n");
        sb.append("  username: ").append(username).append("\n");
        sb.append("  pin: ").append(PIN).append("\n");
        sb.append("  name: ").append(name).append("\n");
        sb.append("  creditcardnumber: ").append(creditCardNum).append("\n");
        sb.append("  creditcarddate: ").append(creditCardDate).append("\n");
        sb.append("  password: ").append(password).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
