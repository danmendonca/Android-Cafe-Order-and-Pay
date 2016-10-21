package pt.up.fe.cmov16.client.clientapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import pt.up.fe.cmov16.client.clientapp.logic.Costumer;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Button dateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Costumer.getInstance(this).isFirstTime())
            setFirstTimeScreen();
        //TODO else
            //TODO jump this activity to the next
    }

    private void setFirstTimeScreen(){
        setContentView(R.layout.activity_main);
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
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        dateTextView.setText(date);
    }
}
