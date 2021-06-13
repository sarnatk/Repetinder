package ru.hse.java.repetinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import ru.hse.java.repetinder.R;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button buttonLogin = findViewById(R.id.login);
        Button buttonRegister = findViewById(R.id.register);
        TextInputEditText editEmail = findViewById(R.id.email);
        TextInputEditText editPassword = findViewById(R.id.password);
    //    FirebaseAuth.getInstance().signOut();
        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = firebaseAuth -> {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
            //    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                Intent intent = new Intent(LoginActivity.this, TabsActivity.class);
                startActivity(intent);
                finish();
            }
        };

        buttonLogin.setOnClickListener(v -> {
            String email = Objects.requireNonNull(editEmail.getText()).toString();
            String password = Objects.requireNonNull(editPassword.getText()).toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "You need to fill in each field", Toast.LENGTH_SHORT).show();
            } else {
                // Add user to database
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Sign up error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        buttonRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}
