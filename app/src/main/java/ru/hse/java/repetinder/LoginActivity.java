package ru.hse.java.repetinder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.bson.Document;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class LoginActivity extends AppCompatActivity {
    private final String appId = "repetinder-xlfqn";
    private MongoDatabase mongoDatabase;
    private MongoClient mongoClient;
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

                io.realm.mongodb.User user = app.currentUser();
                mongoClient = user.getMongoClient("mongodb-atlas");
                mongoDatabase = mongoClient.getDatabase("RepetinderData");
                MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("UserData");
                mongoCollection.insertOne(new Document("userId", user.getId()).append("data", editEmail.getText().toString())).getAsync(result -> {
                    if (result.isSuccess()) {
                        Log.v("Data","Data Inserted Successfully");
                    } else {
                        Log.v("Data","Error:"+result.getError().toString());
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