package com.example.ravi.Quizz.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ravi.Authentification.User;
import com.example.ravi.Quizz.Adapters.ItemAdapter;
import com.example.ravi.Quizz.Answer;
import com.example.ravi.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Restaurants extends AppCompatActivity {

    public FloatingActionButton next_btn;

    private List<Answer> listOfCuisines = new ArrayList<>();

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private User user;

    @Override
    protected void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("users").child(currentUser.getUid());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

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
        for (Answer a : listOfCuisines) {
            if (a.isSelected) {
                user.putPreference("restaurant", a.getTitle());
            }
        }
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mDatabase.getReference().child("users").child(currentUser.getUid()).setValue(user);
        startActivity(new Intent(this, NumberOfTravelers.class));
    }
}
