package ru.hse.java.repetinder.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.hse.java.repetinder.R;
import ru.hse.java.repetinder.match.Match;
import ru.hse.java.repetinder.match.MatchesAdapter;
import ru.hse.java.repetinder.user.Storage;

public class MatchesFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Match> resultsMatches = new ArrayList<>();
    private String currentUId;
    private String userRole, oppositeUserRole;
    private View view;

    public MatchesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_matches, container, false);
        Storage storage = (Storage) this.getArguments().getSerializable(MainActivity.TEXT);
        userRole = storage.userRole;
        oppositeUserRole = storage.oppositeUserRole;
        currentUId = storage.userId;

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MatchesAdapter(getDataSetMatches(), getActivity());
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        getMatchesInfoFromDb();
        return view;
    }

    private FirebaseDatabase getDatabaseInstance() {
        return FirebaseDatabase.getInstance("https://repetinder-cb68d-default-rtdb.europe-west1.firebasedatabase.app/");
    }


    private void getMatchesInfoFromDb() {
        DatabaseReference matchDb = getDatabaseInstance().getReference()
                .child("Users").child(userRole).child(currentUId).child("Connections").child("Yes");
        matchDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    getMatchInfo(snapshot.getKey());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getMatchInfo(String matchId) {
        DatabaseReference userDb = getDatabaseInstance().getReference()
                .child("Users").child(oppositeUserRole).child(matchId);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String fullname = null, profileImageUrl = null, email = null;
                      String userId = snapshot.getKey();
                    if (snapshot.child("fullname").getValue() != null) {
                        fullname = Objects.requireNonNull(snapshot.child("fullname").getValue()).toString();
                    }

                    if (snapshot.child("email").getValue() != null) {
                        email = Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                    }

                    if (snapshot.child("profileImageUrl").getValue() != null) {
                        profileImageUrl = Objects.requireNonNull(snapshot.child("profileImageUrl").getValue()).toString();
                    }

                    Match match = new Match(userId, fullname, email, profileImageUrl, oppositeUserRole);
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
