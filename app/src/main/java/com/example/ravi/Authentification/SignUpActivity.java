package com.example.ravi.Authentification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ravi.Quizz.Activities.Interests;
import com.example.ravi.Quizz.Activities.Restaurants;
import com.example.ravi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    EditText emailEditText;
    EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);

        Intent intent = getIntent();
        if(intent.getStringExtra("email") != null) emailEditText.setText(intent.getStringExtra("email"));
        if(intent.getStringExtra("password") != null) passwordEditText.setText(intent.getStringExtra("password"));
    }

    public void SignUp(View view) {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("RaVi", "createUserWithEmail:success");
                            FirebaseUser currentUser = mAuth.getCurrentUser();
//                            updateUI(user);

                            User user = new User(currentUser.getEmail(), currentUser.getUid());

                            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                            mDatabase.getReference().child("users").child(currentUser.getUid()).setValue(user);

                            startActivity(new Intent(SignUpActivity.this, Interests.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("RaVi", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });
    }
}
