package ru.hse.java.repetinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.hse.java.repetinder.R;
import ru.hse.java.repetinder.match.Match;
import ru.hse.java.repetinder.match.MatchesAdapter;
import ru.hse.java.repetinder.user.Storage;

public class MatchesActivity extends AppCompatActivity {

    public static final String TEXT = "for matches";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Match> resultsMatches = new ArrayList<Match>();

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

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(MatchesActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MatchesAdapter(getDataSetMatches(), MatchesActivity.this);
        recyclerView.setAdapter(adapter);

        for (int i = 0; i < 10; i++) {
            Match obj = new Match("UserID #" + i);
            resultsMatches.add(obj);
        }

        adapter.notifyDataSetChanged();


        Bundle extras = getIntent().getExtras();
        Storage storage = (Storage) extras.getSerializable(TEXT);

        Button buttonToProfileFromMatches = findViewById(R.id.toProfilefromMatches);
        Button buttonToHomeFromMatches = findViewById(R.id.toHomeFromMatches);

        buttonToProfileFromMatches.setOnClickListener(v -> {
            Intent intent = new Intent(MatchesActivity.this, ProfileActivity.class);
            intent.putExtra(ProfileActivity.TEXT, storage);
            startActivity(intent);
            finish();
        });

        buttonToHomeFromMatches.setOnClickListener(v -> finish());
    }


    private List<Match> getDataSetMatches() {
        return resultsMatches;
    }
}
