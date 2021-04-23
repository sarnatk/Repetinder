package ru.hse.java.repetinder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;

public class LoginActivity extends AppCompatActivity {
    private final String appId = "repetinder-xlfqn";
    //private final Object register = new Object();
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
                Credentials credentials = Credentials.emailPassword(editEmail.getText().toString(), editPassword.getText().toString());

                app.loginAsync(credentials, new App.Callback<io.realm.mongodb.User>() {
                    @Override
                    public void onResult(App.Result<io.realm.mongodb.User> result) {
                        if (result.isSuccess()) {
                            Log.v("User", "Logged in Successfully");
                        } else {
                            Log.v("User", "Failed to login");
                            Log.v("User", result.getError().toString());
                        }
                    }
                });

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("email", editEmail.getText().toString());
                intent.putExtra("password", editPassword.getText().toString());
                intent.putExtra("Init", init);
                startActivity(intent);
                finish();
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