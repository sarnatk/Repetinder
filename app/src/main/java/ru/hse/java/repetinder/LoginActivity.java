package ru.hse.java.repetinder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;

public class LoginActivity extends AppCompatActivity {
    private final String appId = "repetinder-xlfqn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Realm.init(this);
        Init init = new Init();
        App app = new App(new AppConfiguration.Builder(init.appId).build());

       // init.app = new App(new AppConfiguration.Builder(init.appId).build());

        setContentView(R.layout.activity_login);
        Button buttonLogin = (Button) findViewById(R.id.login);
        Button buttonRegister = (Button) findViewById(R.id.register);
        TextInputEditText editEmail = (TextInputEditText) findViewById(R.id.email);
        TextInputEditText editPassword = (TextInputEditText) findViewById(R.id.password);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailToLogin = Objects.requireNonNull(editEmail.getText()).toString();
                String passwordToLogin = Objects.requireNonNull(editPassword.getText()).toString();
                if (!emailToLogin.isEmpty() && !passwordToLogin.isEmpty()) {
                    Credentials credentials = Credentials.emailPassword(emailToLogin, passwordToLogin);

                    app.loginAsync(credentials, new App.Callback<io.realm.mongodb.User>() {
                        @Override
                        public void onResult(App.Result<io.realm.mongodb.User> result) {
                            if (result.isSuccess()) {
                                Log.v("User", "Logged in Successfully");
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("email", editEmail.getText().toString());
                                intent.putExtra("password", editPassword.getText().toString());
                                intent.putExtra("Init", init);
                                startActivity(intent);
                                finish();
                            } else {
                                Log.v("User", "Failed to login");
                                Log.v("User", result.getError().toString());
                                Toast.makeText(LoginActivity.this, "Incorrect login or password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Write email and password first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.putExtra("Init", init);
                startActivity(intent);
                finish();
            }
        });
    }
}