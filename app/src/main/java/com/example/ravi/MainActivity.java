package com.example.ravi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.ravi.localisation.LocalisationActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent toLocalalisation = new Intent(this, LocalisationActivity.class);
        startActivity(toLocalalisation);
    }
}
