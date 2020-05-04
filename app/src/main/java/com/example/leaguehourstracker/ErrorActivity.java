package com.example.leaguehourstracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ErrorActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Error", "Error Activity started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        TextView error = findViewById(R.id.errorHandler);
        String user = getIntent().getStringExtra("USERNAME");
        error.setText("The username \"" + user + "\" does not seem to exist within this region. Please try again.");
        Button retry = findViewById(R.id.retryButton);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);}
        });
    }
}
