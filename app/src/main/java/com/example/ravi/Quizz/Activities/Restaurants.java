package com.example.ravi.Quizz.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ravi.Quizz.Adapters.ItemAdapter;
import com.example.ravi.Quizz.Answer;
import com.example.ravi.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Restaurants extends AppCompatActivity {

    public FloatingActionButton next_btn;

    private List<Answer> listOfCuisines = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        next_btn = findViewById(R.id.button);

        listOfCuisines.add(new Answer("Français", ContextCompat.getDrawable(this, R.drawable.french)));
        listOfCuisines.add(new Answer("Chinois", ContextCompat.getDrawable(this, R.drawable.chineese)));
        listOfCuisines.add(new Answer("Japonais", ContextCompat.getDrawable(this, R.drawable.japonais)));
        listOfCuisines.add(new Answer("Mexicain", ContextCompat.getDrawable(this, R.drawable.mexicain)));
        listOfCuisines.add(new Answer("Méditerranéen", ContextCompat.getDrawable(this, R.drawable.mediterranean)));

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        ItemAdapter itemAdapter = new ItemAdapter(listOfCuisines);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }

    public void onClickNextBtn(View view) {
        startActivity(new Intent(this, Interets.class));
    }
}
