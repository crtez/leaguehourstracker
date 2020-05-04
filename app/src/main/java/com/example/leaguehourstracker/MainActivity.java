package com.example.leaguehourstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    /* user-imputed username*/
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText startField = findViewById(R.id.startField);
        username = startField.getText().toString();
        Button startButton = findViewById(R.id.goButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = startField.getText().toString();
                Intent intent = new Intent(v.getContext(), HoursActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);}
            });
        EditText searchBar = findViewById(R.id.startField);
        searchBar.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    username = startField.getText().toString();
                    Intent intent = new Intent(v.getContext(), HoursActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    return true;
                }
            return false;
            }
        });
    }
}
