package com.example.ravi.Quizz.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ravi.Quizz.Adapters.Item2Adapter;
import com.example.ravi.Quizz.Answer;
import com.example.ravi.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class NumberOfTravelers extends AppCompatActivity {

    public FloatingActionButton next_btn;

    private List<Answer> numberOfTravelers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_of_travelers);

        next_btn = findViewById(R.id.button);

        numberOfTravelers.add(new Answer("Seul"));
        numberOfTravelers.add(new Answer("Avec des amis"));
        numberOfTravelers.add(new Answer("En couple"));
        numberOfTravelers.add(new Answer("En famille"));

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        Item2Adapter itemAdapter = new Item2Adapter(numberOfTravelers);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onClickNextBtn(View view) {
        startActivity(new Intent(this, TypeOfTrip.class));
    }
}
