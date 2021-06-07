package ru.hse.java.repetinder.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import org.bson.Document;

import java.util.ArrayList;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;
import ru.hse.java.repetinder.R;
import ru.hse.java.repetinder.user.Storage;
import ru.hse.java.repetinder.user.Student;
import ru.hse.java.repetinder.user.Tutor;
import ru.hse.java.repetinder.user.UserRepetinder;

public class MainActivity extends AppCompatActivity {

    public static final String TEXT = "for main";

    private ArrayList<String> possibleMatchesQueue;
    private ArrayAdapter<String> arrayAdapter;
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#FF3700B3"));

        // Set BackgroundDrawable
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(colorDrawable);
        }

        Bundle extras = getIntent().getExtras();
        Storage storage = (Storage)extras.getSerializable(TEXT);

        possibleMatchesQueue = new ArrayList<>();
        possibleMatchesQueue.add("Таня");
        possibleMatchesQueue.add("Саша");
        possibleMatchesQueue.add("Герман");
        App app = new App(new AppConfiguration.Builder("repetinder-xlfqn").build());
      /*      io.realm.mongodb.User user = app.currentUser();
            MongoClient mongoClient = user.getMongoClient("mongodb-atlas");
            MongoDatabase mongoDatabase = mongoClient.getDatabase("RepetinderData");
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("UserData");
         //   Document emailFilter = new Document().append("email", storage.email);
            RealmResultTask<MongoCursor<Document>> iteratorCurrentUser = mongoCollection.find().iterator();
            iteratorCurrentUser.getAsync(task -> {
                if (task.isSuccess()) {
                    MongoCursor<Document> results = task.get();
                    while (results.hasNext()) {
                        Log.v("FindFunction", "Found Something");
                        Document result = results.next();

                    }
                } else {
                    Log.v("Error", task.getError().toString());
                }
            });
*/
        for (io.realm.mongodb.User device : app.allUsers().values()) {
            // это должно было решить проблему с частичным добавлением юзеров
            // но не решило
            MongoClient mongoClient = device.getMongoClient("mongodb-atlas");
            MongoDatabase mongoDatabase = mongoClient.getDatabase("RepetinderData");
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("UserData");
            RealmResultTask<MongoCursor<Document>> iteratorAllUsers = mongoCollection.find().iterator();
            iteratorAllUsers.getAsync(task -> {
                if (task.isSuccess()) {
                    MongoCursor<Document> results = task.get();
                    while (results.hasNext()) {
                        Log.v("FindFunction", "Found Something");
                        Document result = results.next();
                        String email = result.get("email").toString();
                        if (email.equals(storage.email)) {
                            String userRole = result.get("userRole").toString();
                            String fullname = result.get("fullname").toString();
                            String username = result.get("username").toString();
                            String subject = result.get("subject").toString();
                            String userId = result.get("userId").toString();
                            int groupSize = Integer.parseInt(result.get("groupSize").toString());
                            storage.userRole = userRole;
                            if (userRole.equals("Tutor")) {
                                storage.currentUser = new Tutor(userId, fullname, username, email, UserRepetinder.Subject.valueOf(subject.toUpperCase()));
                            } else {
                                storage.currentUser = new Student(userId, fullname, username, email, UserRepetinder.Subject.valueOf(subject.toUpperCase()));
                            }
                        } else {
                            possibleMatchesQueue.add(result.get("fullname").toString());
                        }
                    }
                } else {
                    Log.v("Error", task.getError().toString());
                }
            });
        }

        arrayAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.helloText, possibleMatchesQueue);

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!");
                possibleMatchesQueue.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                Toast.makeText(MainActivity.this, "no...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(MainActivity.this, "yes!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                /*
                al.add("Имя ".concat(String.valueOf(i)));
                arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                i++;
                 */
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });


        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
            }
        });

        Button buttonToProfileFromHome = findViewById(R.id.toProfileFromHome);
        Button buttonToMatchesFromHome = findViewById(R.id.toMatchesFromHome);

        buttonToProfileFromHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                assert(storage != null);
                intent.putExtra(ProfileActivity.TEXT, storage);
                startActivity(intent);
                finish();
            }
        });


        buttonToMatchesFromHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MatchesActivity.class);
                assert(storage != null);
                intent.putExtra(MatchesActivity.TEXT, storage);
                startActivity(intent);
                finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.profile) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}