package ru.hse.java.repetinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import ru.hse.java.repetinder.R;
import ru.hse.java.repetinder.user.Storage;

public class LoginActivity extends AppCompatActivity {
    private final String appId = "repetinder-xlfqn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Realm.init(this);
        Storage storage = new Storage();
        App app = new App(new AppConfiguration.Builder(storage.appId).build());

        setContentView(R.layout.activity_login);
        Button buttonLogin = findViewById(R.id.login);
        Button buttonRegister = findViewById(R.id.register);
        TextInputEditText editEmail = findViewById(R.id.email);
        TextInputEditText editPassword = findViewById(R.id.password);

        buttonLogin.setOnClickListener(v -> {
            String emailToLogin = Objects.requireNonNull(editEmail.getText()).toString();
            String passwordToLogin = Objects.requireNonNull(editPassword.getText()).toString();
            if (!emailToLogin.isEmpty() && !passwordToLogin.isEmpty()) {
                Credentials credentials = Credentials.emailPassword(emailToLogin, passwordToLogin);

                app.loginAsync(credentials, result -> {
                    if (result.isSuccess()) {
                        Log.v("User", "Logged in Successfully");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        storage.email = emailToLogin;
                        // тут надо научиться получать инфу из базы по емейлу
                        // чтобы потом пихать сконструированного юзера в storage
                        // и это бы заполняло инфу в профиле
                        //storage.userRole =
                        intent.putExtra("storage", storage);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.v("User", "Failed to login");
                        Log.v("User", result.getError().toString());
                        Toast.makeText(LoginActivity.this, "Incorrect login or password", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(LoginActivity.this, "Write email and password first", Toast.LENGTH_SHORT).show();
            }
        });

        buttonRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            intent.putExtra("Init", storage);
            startActivity(intent);
            finish();
        });
    }
}