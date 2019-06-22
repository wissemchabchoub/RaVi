package com.example.ravi.Quizz.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ravi.Authentification.SignInActivity;
import com.example.ravi.Authentification.User;
import com.example.ravi.MainActivity;
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

public class Interests extends AppCompatActivity {

    public FloatingActionButton next_btn;

    private List<Answer> listOfInterests = new ArrayList<>();

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
                Log.i("RaVi", user.email);
                Log.i("RaVi", String.valueOf(user.quizzDone));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

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
        for (Answer a : listOfInterests) {
            if (a.isSelected) {
                user.putPreference("interests", a.getTitle());
            }
        }
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mDatabase.getReference().child("users").child(currentUser.getUid()).setValue(user);
        startActivity(new Intent(this, Places.class));
    }
}
