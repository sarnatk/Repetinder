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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class MatchesFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Match> resultsMatches = new ArrayList<Match>();
    private FirebaseAuth mAuth;
    private DatabaseReference mCustomerDatabase;
    private String currentUId;
    private String userRole;
    private String oppositeUserRole;
    private View view;

    public MatchesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.matches_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        checkUserRole();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MatchesAdapter(getDataSetMatches(), getActivity());
        recyclerView.setAdapter(adapter);

        return view;
    }

    private FirebaseDatabase getDatabaseInstance() {
        return FirebaseDatabase.getInstance("https://repetinder-cb68d-default-rtdb.europe-west1.firebasedatabase.app/");
    }

    private void checkUserRole() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference tutorDb = getDatabaseInstance().getReference().child("Users").child("Tutor");
        tutorDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                if (Objects.equals(dataSnapshot.getKey(), Objects.requireNonNull(user).getUid())) {
                    userRole = "Tutor";
                    oppositeUserRole = "Student";
                    currentUId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                    mCustomerDatabase = getDatabaseInstance().getReference().child("Users").child(userRole).child(currentUId);
                    getMatchesInfoFromDb();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        DatabaseReference studentDb = getDatabaseInstance().getReference().child("Users").child("Student");
        studentDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                if (Objects.equals(dataSnapshot.getKey(), Objects.requireNonNull(user).getUid())){
                    userRole = "Student";
                    oppositeUserRole = "Tutor";
                    currentUId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                    mCustomerDatabase = getDatabaseInstance().getReference().child("Users").child(userRole).child(currentUId);
                    getMatchesInfoFromDb();
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }


    private void getMatchesInfoFromDb() {
        DatabaseReference matchDb = MainActivity.getDatabaseInstance().getReference()
                .child("Users").child(userRole).child(currentUId).child("Connections").child("Yes");
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
