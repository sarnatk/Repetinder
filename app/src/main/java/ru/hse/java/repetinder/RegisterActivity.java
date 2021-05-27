package ru.hse.java.repetinder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.bson.Document;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class RegisterActivity extends AppCompatActivity {
  //  private User user;
    private MongoDatabase mongoDatabase;
    private MongoClient mongoClient;
    private String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Init init = (Init)getIntent().getSerializableExtra("Init");
        App app = new App(new AppConfiguration.Builder(init.appId).build());
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
                                                  userRole = parent.getItemAtPosition(pos).toString();
                                              }

                                              @Override
                                              public void onNothingSelected(AdapterView<?> parent) {

                                              }
                                          });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Add user to database
                    app.getEmailPassword().registerUserAsync(editEmail.getText().toString(), editPassword.getText().toString(), it -> {
                        if (it.isSuccess()) {
                            Log.v("User", "User is successfully registered");
                            io.realm.mongodb.User user = app.currentUser();
                            mongoClient = user.getMongoClient("mongodb-atlas");
                            mongoDatabase = mongoClient.getDatabase("RepetinderData");
                            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("UserData");

                            /* TODO: (написано, чтобы не забыть, а еще выговориться хочется)
                            1. убрать username, ибо он не нужен, видимо
                            2. вместо него добавить выбор предмета (сейчас по стандарту математика), но список предметов какой-то длинный, поэтому возможно для него нужна отдельная страница?
                            но тогда вопрос в том: можно ли связать два ***.xml файла с одним ***Activity.java.
                            3. Сейчас по стандарту выбирается группа из 1 человека, но потом в настройках эту оптию можно будет поменять. Так точно удобнее
                            и вроде правильно
                            4. Наверное, стоит тут создать все же класс user, но с этим json он как-то и не нужен особо ы
                            5. Я вот сделала Init класс, предполагая, что в нем можно хранить важные штуки, которые нужны всем. Видимо, текущего юзера, например
                            пока непонятно, можно ли это как-то улучшить, а еще его передавать между activity разными больно, если в нем хранить свои кастомные классы,
                            а не библиотечные

                            вроде выговорилась

                            Саша, надо выговариваться в гуглдоке
                            https://docs.google.com/document/d/1RdkUbMPiBuivK4qnpHG56w1fy9gCEodbRnx8VBJtqLo/edit
                            *стикос кота*
                             */
                            mongoCollection.insertOne(new Document("userId", user.getId())
                                    .append("email", editEmail.getText().toString())
                                    .append("userRole", userRole)
                                    .append("fullname", editFullname.getText().toString())
                                    .append("username", editUsername.getText().toString())
                                    .append("subject", "Math")
                                    .append("groupSize", 1))
                                    .getAsync(result -> {
                                if (result.isSuccess()) {
                                    Log.v("Data","Data Inserted Successfully");
                                } else {
                                    Log.v("Data","Error:"+result.getError().toString());
                                }
                            });
                        } else {
                            Log.v("User", "Failed to register user");
                            Log.v("User", it.getError().toString());
                        }
                    });
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
               // intent.putExtra("username", user.getUsername());
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