package ru.hse.java.repetinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import ru.hse.java.repetinder.R;
import ru.hse.java.repetinder.user.Storage;

public class MatchesActivity extends AppCompatActivity {

    public static final String TEXT = "for matches";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Log.v("Bundle", "Bundle is null!");
        } else {
            Log.v("Bundle", "Bundle isn't null!");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);
        if (getIntent() == null || getIntent().getExtras() == null) {
            Log.v("Extras", "Extras is null!");
        } else {
            Log.v("Extras", "Extras isn't null!");
        }
        /*
        Bundle extras = getIntent().getExtras();
        Storage storage = (Storage) extras.getSerializable(TEXT);
         */
        Button buttonToProfileFromMatches = findViewById(R.id.toProfilefromMatches);
        Button buttonToHomeFromMatches = findViewById(R.id.toHomeFromMatches);

        buttonToProfileFromMatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MatchesActivity.this, ProfileActivity.class);
                //intent.putExtra(ProfileActivity.TEXT, storage);
                startActivity(intent);
                finish();
            }
        });

        buttonToHomeFromMatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MatchesActivity.this, MainActivity.class);
                //intent.putExtra(MainActivity.TEXT, storage);
                startActivity(intent);
                finish();
            }
        });
    }
}
