package ru.hse.java.repetinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import ru.hse.java.repetinder.R;
import ru.hse.java.repetinder.user.Storage;

public class MatchesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);
        Bundle extras = getIntent().getExtras();
        Storage storage = (Storage)extras.getSerializable("storage");
        Button buttonToProfileFromMatches = findViewById(R.id.toProfilefromMatches);
        Button buttonToHomeFromMatches = findViewById(R.id.toHomeFromMatches);

        buttonToProfileFromMatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MatchesActivity.this, ProfileActivity.class);
                intent.putExtra("storage", storage);
                startActivity(intent);
            }
        });

        buttonToHomeFromMatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
