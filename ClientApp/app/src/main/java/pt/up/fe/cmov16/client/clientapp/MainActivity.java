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
import pt.up.fe.cmov16.client.clientapp.ui.MenuActivity;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Button dateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApiInvoker.initializeInstance();

        if (User.getInstance(this).isFirstTime()) {
            setFirstTimeScreen();
        } else {
            Intent i = new Intent(this, MenuActivity.class);
            startActivity(i);
        }

    }

    private void setFirstTimeScreen() {
        setContentView(R.layout.activity_main);

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


        Button registar = (Button) findViewById(R.id.registar);
        final EditText nameEditText = (EditText) findViewById(R.id.name);
        final EditText usernameEditText = (EditText) findViewById(R.id.username);
        final EditText passwordEditText = (EditText) findViewById(R.id.password);
        final EditText cardNumEditText = (EditText) findViewById(R.id.cardNum);

        registar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DefaultApi api = new DefaultApi();
                String name = nameEditText.getText().toString();
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String cardNum = cardNumEditText.getText().toString();

                if (name.isEmpty() || username.isEmpty() || password.isEmpty() || cardNum.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Preencher tudo", Toast.LENGTH_SHORT).show();
                } else if (!Character.isDigit(dateTextView.getText().toString().charAt(0))) {
                    Toast.makeText(MainActivity.this, "Inserir data de validade", Toast.LENGTH_SHORT).show();
                } else {
                    Costumer user = new Costumer();
                    user.setName(name);
                    String cardDate = "";
                    String dateInput = dateTextView.getText().toString();
                    cardDate += dateInput.split("/")[2];
                    cardDate += "-";
                    cardDate += dateInput.split("/")[1];
                    cardDate += "-";
                    cardDate += dateInput.split("/")[0];
                    cardDate += "T23:59:59.000Z";
                    user.setCreditcarddate(cardDate);
                    user.setCreditcardnumber(cardNum);
                    user.setPassword(password);
                    user.setPin("pin");
                    user.setUsername(username);
                    user.setUuid("uuid");

                    api.createUser(user, new Response.Listener<Costumer>() {
                        @Override
                        public void onResponse(Costumer response) {
                            saveUserDataAndShowPIN(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyError volleyError = (VolleyError) error.getCause();
                            if (volleyError.networkResponse != null) {
                                Log.e("MainActivity", "" + volleyError.networkResponse.statusCode + ":" + volleyError.getMessage());
                            }
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

                    }
                });
        builder.create().show();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
        dateTextView.setText(date);
    }

}
