package pt.up.fe.cmov16.client.clientapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.Costumer;
import io.swagger.client.model.RegisterParam;
import pt.up.fe.cmov16.client.clientapp.R;
import pt.up.fe.cmov16.client.clientapp.logic.User;

/**
 * A login screen that offers login via email/password.
 */
public class CostumerEdition extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {


    // UI references.
    private EditText mCreditCardNr;
    private Button mDateTextView;
    private Button mConfirmEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costumer_edition);

        mCreditCardNr = (EditText) findViewById(R.id.cardNum);
        mDateTextView = (Button) findViewById(R.id.cardVal);
        mDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        CostumerEdition.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        final User myUsr = User.getInstance(getApplicationContext());
        mDateTextView.setText(myUsr.getCreditCardDate());

        mConfirmEdit = (Button) findViewById(R.id.confirm_edit_btn);
        mConfirmEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cardNum = mCreditCardNr.getText().toString();
                String[] dateInput = mDateTextView.getText().toString().split("/");

                String creditDate = "";
                if (dateInput.length > 1)
                    creditDate = dateInput[2] + "-" + dateInput[1] + "-" + dateInput[0];

                DefaultApi api = new DefaultApi();
                RegisterParam registerParam = new RegisterParam();
                registerParam.setCreditcarddate(
                        (creditDate.compareTo("") != 0) ? creditDate : myUsr.getCreditCardDate());
                registerParam.setCreditcardnumber(
                        (cardNum.compareTo("") != 0) ? cardNum : myUsr.getCreditCardNum());
                registerParam.setPassword(myUsr.getPassword());
                registerParam.setName(myUsr.getName());
                registerParam.setUsername(myUsr.getUsername());

                api.editCostumer(myUsr.getCostumerID(), registerParam,
                        new Response.Listener<Costumer>() {
                            @Override
                            public void onResponse(Costumer response) {
                                User.getInstance(getApplicationContext()).createUserData(response.getName(),
                                        response.getUsername(), response.getPassword(), response.getCreditcardnumber(),
                                        response.getCreditcarddate(), response.getPin(), response.getUuid(),
                                        getApplicationContext());

                                Toast.makeText(getApplicationContext(), "Updated information", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
        mDateTextView.setText(date);
    }
}

