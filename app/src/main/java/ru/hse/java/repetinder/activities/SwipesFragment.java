package ru.hse.java.repetinder.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.hse.java.repetinder.R;
import ru.hse.java.repetinder.card.Card;
import ru.hse.java.repetinder.card.CardsArrayAdapter;
import ru.hse.java.repetinder.user.Storage;
import ru.hse.java.repetinder.user.UserRepetinder;

public class SwipesFragment extends Fragment {
    private CardsArrayAdapter arrayAdapter;

    private DatabaseReference usersDb;
    private String currentUId;

    private List<Card> possibleMatchesQueue;
    private String userRole;
    private String oppositeUserRole;
    private ProgressBar progressBar;
    private UserRepetinder currentUser;

    public SwipesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_swipes, container, false);
        Storage storage = (Storage) this.getArguments().getSerializable(MainActivity.TEXT);
        userRole = storage.userRole;
        oppositeUserRole = storage.oppositeUserRole;
        currentUId = storage.userId;
        currentUser = storage.currentUser;
        usersDb = getDatabaseInstance().getReference().child("Users");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        getOppositeRoleUsers();
        progressBar = view.findViewById(R.id.progressBar);
        TextView textView = view.findViewById(R.id.textView4);
        textView.setVisibility(View.GONE);
        possibleMatchesQueue = new ArrayList<>();

        arrayAdapter = new CardsArrayAdapter(getActivity(), R.layout.item, possibleMatchesQueue);

        SwipeFlingAdapterView flingContainer = view.findViewById(R.id.frame);
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
                Card card = (Card) dataObject;
                String userId = card.getUserId();
                usersDb.child(oppositeUserRole).child(userId).child("Connections").child("No").child(currentUId).setValue(true);
                Toast.makeText(getActivity(), "no...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Card card = (Card) dataObject;
                String userId = card.getUserId();
                String key = getDatabaseInstance().getReference().child("Chats").push().getKey();
                usersDb.child(oppositeUserRole).child(userId).child("Connections").child("Yes").child(currentUId).child("ChatId").setValue(key);
                usersDb.child(userRole).child(currentUId).child("Connections").child("Yes").child(userId).child("ChatId").setValue(key);
                Toast.makeText(getActivity(), "yes!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                textView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onScroll(float scrollProgressPercent) {}
        });


        flingContainer.setOnItemClickListener((itemPosition, dataObject) ->
                Toast.makeText(getActivity(), "click", Toast.LENGTH_SHORT).show());
        return view;
    }

    private FirebaseDatabase getDatabaseInstance() {
        return FirebaseDatabase.getInstance("https://repetinder-cb68d-default-rtdb.europe-west1.firebasedatabase.app/");
    }

    private void getOppositeRoleUsers(){
        DatabaseReference oppositeSexDb = getDatabaseInstance().getReference().child("Users").child(oppositeUserRole);
        oppositeSexDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists() && !dataSnapshot.child("Connections").child("No").hasChild(currentUId)
                        && !dataSnapshot.child("Connections").child("Yes").hasChild(currentUId)) {
                    UserRepetinder.Subject matchSubject = UserRepetinder.Subject.valueOf(Objects.requireNonNull(dataSnapshot.child("subject").getValue()).toString());
                    boolean isSeen = (boolean) dataSnapshot.child("isSeen").getValue();
                    if (isSeen && matchSubject.equals(currentUser.getSubject())) {
                        String profileImageUrl = "default";
                        if (!Objects.equals(dataSnapshot.child("profileImageUrl").getValue(), "default")) {
                            profileImageUrl = Objects.requireNonNull(dataSnapshot.child("profileImageUrl").getValue()).toString();
                        }
                        Card card = new Card(dataSnapshot.getKey(), Objects.requireNonNull(dataSnapshot.child("fullname").getValue()).toString(),
                                profileImageUrl);
                        possibleMatchesQueue.add(card);
                        arrayAdapter.notifyDataSetChanged();
                    }
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

        oppositeSexDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
