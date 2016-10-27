package pt.up.fe.cmov16.client.clientapp;

import android.content.DialogInterface;
import android.content.Intent;
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
import pt.up.fe.cmov16.client.clientapp.logic.User;
import pt.up.fe.cmov16.client.clientapp.ui.SlideActivity;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Button dateTextView;
    private DefaultApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApiInvoker.initializeInstance();
        api = new DefaultApi();

        if (User.getInstance(this).isFirstTime()) {
            setFirstTimeScreen();
        } else {
            startSlideActivity();
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
                api.logMe(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(),
                        new Response.Listener<Costumer>() {
                            @Override
                            public void onResponse(Costumer response) {
                                saveUserDataAndShowPIN(response);
                            }
                        }, new Response.ErrorListener() {
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
        TextView info = (TextView) findViewById(R.id.infoView);
        info.setText("Atenção: A app quer o server em: "
                + (new DefaultApi()).getBasePath()
                + " se tens o server noutro endereço altera isto em:\n" +
                "android-client > java > io.swagger.client.api > DefaultApi > var \"basePath\"");

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


        Button register = (Button) findViewById(R.id.registerBtn);
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
                    Toast.makeText(MainActivity.this, "Preencher tudo", Toast.LENGTH_SHORT).show();
                } else if (!Character.isDigit(dateTextView.getText().toString().charAt(0))) {
                    Toast.makeText(MainActivity.this, "Inserir data de validade", Toast.LENGTH_SHORT).show();
                } else {
                    String cardDate = "";
                    String dateInput = dateTextView.getText().toString();
                    cardDate += dateInput.split("/")[2];
                    cardDate += "-";
                    cardDate += dateInput.split("/")[1];
                    cardDate += "-";
                    cardDate += dateInput.split("/")[0];
                    cardDate += "T23:59:59.000Z";

                    api.createUser(name, username, password, cardNum, cardDate,
                            new Response.Listener<Costumer>() {
                                @Override
                                public void onResponse(Costumer response) {
                                    saveUserDataAndShowPIN(response);
                                }
                            }, new Response.ErrorListener() {
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
        Log.e("MainActivity", "created user: " + response.toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("PIN: " + response.getPin() + "\n Guarde num local seguro")
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
    }

}
