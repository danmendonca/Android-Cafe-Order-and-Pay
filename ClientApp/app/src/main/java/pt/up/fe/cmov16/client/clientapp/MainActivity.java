package pt.up.fe.cmov16.client.clientapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import io.swagger.client.ApiInvoker;
import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.Costumer;
import io.swagger.client.model.LoginParam;
import io.swagger.client.model.RegisterParam;
import pt.up.fe.cmov16.client.clientapp.logic.User;
import pt.up.fe.cmov16.client.clientapp.ui.SlideActivity;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String FINISH_ACTIVITY_KEY = "isFinish";
    private static final String TAG = MainActivity.class.toString();
    private Button dateTextView;
    private DefaultApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApiInvoker.initializeInstance();

        api = new DefaultApi();

        if (getIntent().getBooleanExtra(FINISH_ACTIVITY_KEY, false)) {
            finish();
            return;
        }

        if (User.getInstance(this).isFirstTime())
            setFirstTimeScreen();
        else
            startSlideActivity();
        //Toast.makeText(this, "ALERT USING SERVER: " + (new DefaultApi()).getBasePath(), Toast.LENGTH_LONG).show();

        NfcManager manager = (NfcManager) this.getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();
        if (adapter == null) {
            Log.e(TAG, "Device haven't NFC ");
        } else if (!adapter.isEnabled()) {
            Log.e(TAG, "NFC isn't enabled ");
        } else {
            Log.i(TAG, "NFC is ready to use");
        }
    }

    private void setFirstTimeScreen() {
        setContentView(R.layout.login_register_menu);
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        setContentView(R.layout.form_login);
        final EditText usernameEditText = (EditText) findViewById(R.id.username);
        final EditText passwordEditText = (EditText) findViewById(R.id.password);

        findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginParam loginParam = new LoginParam();
                loginParam.setPassword(passwordEditText.getText().toString());
                loginParam.setUsername(usernameEditText.getText().toString());

                api.logMe(loginParam,
                        new Response.Listener<Costumer>() {
                            @Override
                            public void onResponse(Costumer response) {
                                saveUserDataAndShowPIN(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                showError(error);
                                Toast.makeText(MainActivity.this,
                                        "Invalid username/password", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }

    private void register() {
        setContentView(R.layout.form_register);

        dateTextView = (Button) findViewById(R.id.cardVal);

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        MainActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });


        final Button register = (Button) findViewById(R.id.registerBtn);
        final EditText nameEditText = (EditText) findViewById(R.id.name);
        final EditText usernameEditText = (EditText) findViewById(R.id.username);
        final EditText passwordEditText = (EditText) findViewById(R.id.password);
        final EditText cardNumEditText = (EditText) findViewById(R.id.cardNum);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String cardNum = cardNumEditText.getText().toString();

                if (name.isEmpty() || username.isEmpty() || password.isEmpty() || cardNum.isEmpty()) {
                    Toast.makeText(MainActivity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                } else if (!Character.isDigit(dateTextView.getText().toString().charAt(0))) {
                    Toast.makeText(MainActivity.this, "Insert credit card expiration date", Toast.LENGTH_SHORT).show();
                } else {
                    String [] dateInput = dateTextView.getText().toString().split("/");
                    String creditDate = dateInput[2] + "-" + dateInput[1] + "-" + dateInput[0];

                    RegisterParam registerParam = new RegisterParam();
                    registerParam.setUsername(username);
                    registerParam.setName(name);
                    registerParam.setPassword(password);
                    registerParam.setCreditcardnumber(cardNum);
                    registerParam.setCreditcarddate(creditDate);
                    api.createUser(registerParam,
                            new Response.Listener<Costumer>() {
                                @Override
                                public void onResponse(Costumer response) {
                                    saveUserDataAndShowPIN(response);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    showError(error);
                                }
                            });
                }
            }
        });
    }

    private void saveUserDataAndShowPIN(Costumer response) {
        User user = User.getInstance(this);
        user.createUserData(response.getName(), response.getUsername(),
                response.getPassword(), response.getCreditcardnumber(),
                response.getCreditcarddate(), response.getPin(),
                response.getUuid(), this);
        Log.i("MainActivity", "created user: " + response.toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("PIN: " + response.getPin() + "\n Don't forget this pin!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startSlideActivity();
                    }
                });
        builder.create().show();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
        dateTextView.setText(date);
    }

    private void showError(VolleyError error) {
        if (error == null || error.getCause() == null) {
            Toast.makeText(MainActivity.this, "Connection failed, please try again", Toast.LENGTH_SHORT).show();
            return;
        }
        VolleyError volleyError = (VolleyError) error.getCause();
        if (volleyError.networkResponse != null) {
            Log.e("MainActivity", "" + volleyError.networkResponse.statusCode + ":" + volleyError.getMessage());
        }
    }

    private void startSlideActivity() {
        Intent i = new Intent(this, SlideActivity.class);
        startActivity(i);
        finish();
    }

}
