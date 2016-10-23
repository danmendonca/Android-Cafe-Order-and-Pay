package pt.up.fe.cmov16.client.clientapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import pt.up.fe.cmov16.client.clientapp.logic.User;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText("Já está logado como: "+
                User.getInstance(this).toString()+
                "\n Aqui em vez disto deveria aparecer o menu de produtos :D");
    }
}
