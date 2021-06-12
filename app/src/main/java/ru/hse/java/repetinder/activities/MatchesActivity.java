package ru.hse.java.repetinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private String currentUserId;
    private String userRole, oppositeUserRole;

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

        currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        Bundle extras = getIntent().getExtras();
        Storage storage = (Storage)extras.getSerializable(TEXT);
        if (storage.userRole.equals("Student")) {
            userRole = storage.userRole;
            oppositeUserRole = "Tutor";
        } else {
            userRole = storage.userRole;
            oppositeUserRole = "Student";
        }
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(MatchesActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MatchesAdapter(getDataSetMatches(), MatchesActivity.this);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        Button buttonToProfileFromMatches = findViewById(R.id.toProfilefromMatches);
        Button buttonToHomeFromMatches = findViewById(R.id.toHomeFromMatches);

        buttonToProfileFromMatches.setOnClickListener(v -> {
            Intent intent = new Intent(MatchesActivity.this, ProfileActivity.class);
            intent.putExtra(ProfileActivity.TEXT, storage);
            startActivity(intent);
            finish();
        });

        buttonToHomeFromMatches.setOnClickListener(v -> finish());

        getMatchesInfoFromDb();
    }

    private void getMatchesInfoFromDb() {
        DatabaseReference matchDb = MainActivity.getDatabaseInstance().getReference()
                .child("Users").child(userRole).child(currentUserId).child("Connections").child("Yes");
        matchDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for(DataSnapshot match : snapshot.getChildren()) {
                        getMatchInfo(match.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getMatchInfo(String matchId) {
        DatabaseReference userDb = MainActivity.getDatabaseInstance().getReference()
                .child("Users").child(oppositeUserRole).child(matchId);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String fullname = null, profileImageUrl = null, email = null;
                  //  String userId = snapshot.getKey();
                    if (snapshot.child("fullname").getValue() != null) {
                        fullname = Objects.requireNonNull(snapshot.child("fullname").getValue()).toString();
                    }

                    if (snapshot.child("email").getValue() != null) {
                        email = Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                    }

                    if (snapshot.child("profileImageUrl").getValue() != null) {
                        profileImageUrl = Objects.requireNonNull(snapshot.child("profileImageUrl").getValue()).toString();
                    }

                    Match match = new Match(/*userId, */fullname, email, profileImageUrl);
                    resultsMatches.add(match);
                    adapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private List<Match> getDataSetMatches() {
        return resultsMatches;
    }
}
