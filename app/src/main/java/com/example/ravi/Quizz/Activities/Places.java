package com.example.ravi.Quizz.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class Places extends AppCompatActivity {

    public FloatingActionButton next_btn;

    private List<Answer> placesOfInterest = new ArrayList<>();

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
        setContentView(R.layout.activity_places);

        next_btn = findViewById(R.id.button);

        placesOfInterest.add(new Answer("Aéroport", ContextCompat.getDrawable(this, R.drawable.airport)));
        placesOfInterest.add(new Answer("Parc d'attraction", ContextCompat.getDrawable(this, R.drawable.Amusement_Park)));
        placesOfInterest.add(new Answer("Aquarium", ContextCompat.getDrawable(this, R.drawable.AQUARIUM)));
        placesOfInterest.add(new Answer("Gallerie d'art", ContextCompat.getDrawable(this, R.drawable.ART_GALERRY)));
        placesOfInterest.add(new Answer("Bar", ContextCompat.getDrawable(this, R.drawable.BAR)));
        placesOfInterest.add(new Answer("Bowling", ContextCompat.getDrawable(this, R.drawable.BOWLING)));
        placesOfInterest.add(new Answer("Café", ContextCompat.getDrawable(this, R.drawable.CAFE)));
        placesOfInterest.add(new Answer("Camping", ContextCompat.getDrawable(this, R.drawable.Campground)));
        placesOfInterest.add(new Answer("Casino", ContextCompat.getDrawable(this, R.drawable.casino)));
        placesOfInterest.add(new Answer("Église", ContextCompat.getDrawable(this, R.drawable.church)));
        placesOfInterest.add(new Answer("Temple hindou", ContextCompat.getDrawable(this, R.drawable.Hindu)));
        placesOfInterest.add(new Answer("Discothèque", ContextCompat.getDrawable(this, R.drawable.nightclub)));
        placesOfInterest.add(new Answer("Musée", ContextCompat.getDrawable(this, R.drawable.museum)));
        placesOfInterest.add(new Answer("Cinéma", ContextCompat.getDrawable(this, R.drawable.movie_theater)));
        placesOfInterest.add(new Answer("Mosquée", ContextCompat.getDrawable(this, R.drawable.mosquee)));
        placesOfInterest.add(new Answer("Parc", ContextCompat.getDrawable(this, R.drawable.park)));
        placesOfInterest.add(new Answer("Restaurant", ContextCompat.getDrawable(this, R.drawable.french)));
        placesOfInterest.add(new Answer("Centre commercial", ContextCompat.getDrawable(this, R.drawable.mall)));
        placesOfInterest.add(new Answer("Stade", ContextCompat.getDrawable(this, R.drawable.satde)));
        placesOfInterest.add(new Answer("Synagogue", ContextCompat.getDrawable(this, R.drawable.Synagogue)));
        placesOfInterest.add(new Answer("Zoo", ContextCompat.getDrawable(this, R.drawable.zoo)));

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        ItemAdapter itemAdapter = new ItemAdapter(placesOfInterest);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }

    public void onClickNextBtn(View view) {
        for (Answer a : placesOfInterest) {
            if (a.isSelected) {
                user.putPreference("placesOfInterest", a.getTitleCode());
            }
        }
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mDatabase.getReference().child("users").child(currentUser.getUid()).setValue(user);
        startActivity(new Intent(this, Restaurants.class));
    }
}
