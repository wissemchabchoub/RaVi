package com.example.ravi.Quizz.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ravi.Authentification.User;
import com.example.ravi.MainActivity;
import com.example.ravi.Quizz.Adapters.Item2Adapter;
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

public class TypeOfTrip extends AppCompatActivity {

    public FloatingActionButton next_btn;

    private List<Answer> typeOfTrip = new ArrayList<>();

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
        for (Answer a : typeOfTrip) {
            if (a.isSelected) {
                user.putPreference("typeOfTrip", a.getTitle());
            }
        }
        user.quizzDone = true;
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mDatabase.getReference().child("users").child(currentUser.getUid()).setValue(user);
        startActivity(new Intent(this, MainActivity.class));
    }
}
