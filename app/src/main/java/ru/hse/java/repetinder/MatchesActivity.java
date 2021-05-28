package ru.hse.java.repetinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

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
