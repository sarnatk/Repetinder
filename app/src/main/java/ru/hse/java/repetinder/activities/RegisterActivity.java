package ru.hse.java.repetinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import ru.hse.java.repetinder.R;
import ru.hse.java.repetinder.user.Student;
import ru.hse.java.repetinder.user.Tutor;
import ru.hse.java.repetinder.user.UserRepetinder;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private String userRole;
    private String subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = firebaseAuth -> {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        Button buttonRegister = findViewById(R.id.signUp);
        Button buttonLogin = findViewById(R.id.backToLogin);
        TextInputEditText editEmail = findViewById(R.id.email);
        TextInputEditText editFullname = findViewById(R.id.fullname);

        TextInputEditText editPassword = findViewById(R.id.password);

        Spinner spinnerUserType = findViewById(R.id.userType);
        ArrayAdapter<CharSequence> adapterUserType = ArrayAdapter.createFromResource(this,
                R.array.user_type, android.R.layout.simple_spinner_item);
        adapterUserType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUserType.setAdapter(adapterUserType);

        spinnerUserType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                userRole = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinnerSubject = findViewById(R.id.subject);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.subjects, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubject.setAdapter(adapter);

        spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                subject = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonRegister.setOnClickListener(v -> {
            String email = Objects.requireNonNull(editEmail.getText()).toString();
            String password = Objects.requireNonNull(editPassword.getText()).toString();
            String fullname = Objects.requireNonNull(editFullname.getText()).toString();

            String result = UserRepetinder.validate(fullname, email, password, subject);
            if (!result.equals("Success")) {
                Toast.makeText(RegisterActivity.this, result, Toast.LENGTH_SHORT).show();
            } else {
                // Add user to database
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Sign up error", Toast.LENGTH_SHORT).show();
                    } else {
                        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                        FirebaseDatabase database = FirebaseDatabase.getInstance("https://repetinder-cb68d-default-rtdb.europe-west1.firebasedatabase.app/");
                        DatabaseReference currentUserDb = database.getReference().child("Users").child(userRole).child(userId);
                        if (userRole.equals("Student")) {
                            Student student = new Student(fullname, email, UserRepetinder.Subject.valueOf(subject.toUpperCase()), 3000);
                            student.setProfileImageUrl("default");
                            currentUserDb.setValue(student);
                        } else {
                            Tutor tutor = new Tutor(fullname, email, UserRepetinder.Subject.valueOf(subject.toUpperCase()), 0);
                            tutor.setProfileImageUrl("default");
                            currentUserDb.setValue(tutor);
                        }
                    }
                });
            }
        });

        buttonLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
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
