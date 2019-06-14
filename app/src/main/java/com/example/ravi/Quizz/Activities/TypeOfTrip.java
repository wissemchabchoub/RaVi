package com.example.ravi.Quizz.Activities;

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

public class TypeOfTrip extends AppCompatActivity {

    public FloatingActionButton next_btn;

    private List<Answer> typeOfTrip = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_of_trip);

        next_btn = findViewById(R.id.button);

        typeOfTrip.add(new Answer("Couch Surfing"));
        typeOfTrip.add(new Answer("Backpacking"));
        typeOfTrip.add(new Answer("Expatriation"));
        typeOfTrip.add(new Answer("Tourisme de luxe"));
        typeOfTrip.add(new Answer("Voyage d'affaire"));
        typeOfTrip.add(new Answer("Autre"));

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        Item2Adapter itemAdapter = new Item2Adapter(typeOfTrip);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onClickNextBtn(View view) {

    }
}
