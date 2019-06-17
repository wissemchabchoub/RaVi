package com.example.ravi.Quizz.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ravi.Authentification.SignInActivity;
import com.example.ravi.MainActivity;
import com.example.ravi.Quizz.Adapters.ItemAdapter;
import com.example.ravi.Quizz.Answer;
import com.example.ravi.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Interets extends AppCompatActivity {

    public FloatingActionButton next_btn;

    private List<Answer> listOfInterests = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);

        next_btn = findViewById(R.id.button);

        listOfInterests.add(new Answer("Architecture", ContextCompat.getDrawable(this, R.drawable.architecture)));
        listOfInterests.add(new Answer("Arts", ContextCompat.getDrawable(this, R.drawable.arts)));
        listOfInterests.add(new Answer("Aventure", ContextCompat.getDrawable(this, R.drawable.aventure)));
        listOfInterests.add(new Answer("Histoire", ContextCompat.getDrawable(this, R.drawable.histoire)));
        listOfInterests.add(new Answer("Shopping", ContextCompat.getDrawable(this, R.drawable.shopping)));
        listOfInterests.add(new Answer("Nourriture", ContextCompat.getDrawable(this, R.drawable.nourriture)));
        listOfInterests.add(new Answer("Détente", ContextCompat.getDrawable(this, R.drawable.detente)));

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        ItemAdapter itemAdapter = new ItemAdapter(listOfInterests);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }

    public void onClickNextBtn(View view) {
        startActivity(new Intent(this, NumberOfTravelers.class));
    }





}
