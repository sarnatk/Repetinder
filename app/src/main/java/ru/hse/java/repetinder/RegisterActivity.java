package ru.hse.java.repetinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button buttonRegister = (Button) findViewById(R.id.signUp);
        Button buttonLogin = (Button) findViewById(R.id.backToLogin);
        TextInputEditText editEmail = (TextInputEditText) findViewById(R.id.email);
        TextInputEditText editFullname = (TextInputEditText) findViewById(R.id.fullname);

        TextInputEditText editUsername = (TextInputEditText) findViewById(R.id.username);
        TextInputEditText editPassword = (TextInputEditText) findViewById(R.id.password);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                              public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                                  String text = parent.getItemAtPosition(pos).toString();
                                                  if (text.equals("Tutor")) {
                                                      user = new Tutor(editFullname.getText().toString(), editPassword.getText().toString());
                                                  } else {
                                                      user = new Student(editFullname.getText().toString(), editPassword.getText().toString());
                                                  }
                                              }

                                              @Override
                                              public void onNothingSelected(AdapterView<?> parent) {

                                              }
                                          });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.putExtra("username", user.getUsername());
                startActivity(intent);
                finish();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}