package ru.hse.java.repetinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.bson.Document;

import java.util.Objects;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import ru.hse.java.repetinder.R;
import ru.hse.java.repetinder.user.Storage;

public class RegisterActivity extends AppCompatActivity {
    //  private User user;
    private MongoDatabase mongoDatabase;
    private MongoClient mongoClient;
    private String userRole;
    private String subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Storage storage = (Storage) getIntent().getSerializableExtra("Init");
        App app = new App(new AppConfiguration.Builder(storage.appId).build());
        setContentView(R.layout.activity_register);

        Button buttonRegister = findViewById(R.id.signUp);
        Button buttonLogin = findViewById(R.id.backToLogin);
        TextInputEditText editEmail = findViewById(R.id.email);
        TextInputEditText editFullname = findViewById(R.id.fullname);

        TextInputEditText editUsername = findViewById(R.id.username);
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
            String username = Objects.requireNonNull(editUsername.getText()).toString();
            String fullname = Objects.requireNonNull(editFullname.getText()).toString();

            if (email.isEmpty() || password.isEmpty() || username.isEmpty() || fullname.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "You need to fill in each field", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6) {
                Toast.makeText(RegisterActivity.this, "Password should contain at least 6 symbols", Toast.LENGTH_SHORT).show();
            } else {
                // Add user to database
                app.getEmailPassword().registerUserAsync(email, password, it -> {
                    if (it.isSuccess()) {
                        Log.v("User", "User is successfully registered");
                        io.realm.mongodb.User user = app.currentUser();
                        mongoClient = user.getMongoClient("mongodb-atlas");
                        mongoDatabase = mongoClient.getDatabase("RepetinderData");
                        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("UserData");

                        mongoCollection.insertOne(new Document("userId", user.getId())
                                .append("email", email)
                                .append("userRole", userRole)
                                .append("fullname", fullname)
                                .append("username", username)
                                .append("subject", subject)
                                .append("groupSize", 1))
                                .getAsync(result -> {
                                    if (result.isSuccess()) {
                                        Log.v("Data", "Data Inserted Successfully");
                                    } else {
                                        Log.v("Data", "Error:" + result.getError().toString());
                                    }
                                });
                    } else {
                        Log.v("User", "Failed to register user");
                        Log.v("User", it.getError().toString());
                    }
                });
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                // intent.putExtra("username", user.getUsername());
                storage.email = email;
                storage.fullname = fullname;
                storage.userRole = userRole;
                intent.putExtra("storage", storage);
                startActivity(intent);
                finish();
            }
        });

        buttonLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

    }
}