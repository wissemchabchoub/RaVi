package com.example.ravi.Quizz.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.ravi.Quizz.Adapters.ItemAdapter;
import com.example.ravi.Quizz.Answer;
import com.example.ravi.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Restaurant extends AppCompatActivity {

    public FloatingActionButton next_btn;

    private List<Answer> listOfCuisines = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        next_btn = findViewById(R.id.button);

        listOfCuisines.add(new Answer("Français", ContextCompat.getDrawable(this, R.drawable.cuisine_francaise)));
        listOfCuisines.add(new Answer("Chinois", ContextCompat.getDrawable(this, R.drawable.cuisine_francaise)));
        listOfCuisines.add(new Answer("Arménien", ContextCompat.getDrawable(this, R.drawable.cuisine_francaise)));
        listOfCuisines.add(new Answer("Indien", ContextCompat.getDrawable(this, R.drawable.cuisine_francaise)));
        listOfCuisines.add(new Answer("Marocain", ContextCompat.getDrawable(this, R.drawable.ic_launcher_background)));

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        ItemAdapter itemAdapter = new ItemAdapter(listOfCuisines);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }
}
