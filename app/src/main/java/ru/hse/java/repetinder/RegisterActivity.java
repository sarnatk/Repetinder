package ru.hse.java.repetinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

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

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
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