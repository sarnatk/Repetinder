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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ru.hse.java.repetinder.R;
import ru.hse.java.repetinder.card.Card;
import ru.hse.java.repetinder.card.CardsArrayAdapter;
import ru.hse.java.repetinder.user.Storage;
import ru.hse.java.repetinder.user.Student;
import ru.hse.java.repetinder.user.Tutor;
import ru.hse.java.repetinder.user.UserRepetinder;

public class SwipesFragment extends Fragment {

    public static final String TEXT = "for main";

    private CardsArrayAdapter arrayAdapter;

    private FirebaseAuth mAuth;
    private DatabaseReference usersDb, mCustomerDatabase;
    private String currentUId;
    private Storage storage = new Storage();

    private List<Card> possibleMatchesQueue;
    private View view;
    private String userRole;
    private String oppositeUserRole;
    private UserRepetinder userRepetinder;
    private ProgressBar progressBar;
    private TextView textView;

    public SwipesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.swipes_fragment, container, false);
        usersDb = getDatabaseInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        checkUserRole();
        textView = view.findViewById(R.id.textView4);
        textView.setVisibility(View.GONE);
        progressBar = view.findViewById(R.id.progressBar);
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
                // TODO: сейчас connections есть у tutor и у student. Так как tutor выбирать не сможет потом, то connections может создаватть только
                // student у tutorа
                if (userRole.equals("Student")) {
                    Card card = (Card) dataObject;
                    String userId = card.getUserId();
                    usersDb.child(oppositeUserRole).child(userId).child("Connections").child("No").child(currentUId).setValue(true);
                }
                Toast.makeText(getActivity(), "no...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                if (userRole.equals("Student")) {
                    Card card = (Card) dataObject;
                    String userId = card.getUserId();
                    usersDb.child(oppositeUserRole).child(userId).child("Connections").child("Yes").child(currentUId).setValue(true);
                    usersDb.child(userRole).child(currentUId).child("Connections").child("Yes").child(userId).setValue(true);
                }
                Toast.makeText(getActivity(), "yes!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {}

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

    public void checkUserRole() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference tutorDb = getDatabaseInstance().getReference().child("Users").child("Tutor");
        tutorDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                if (Objects.equals(dataSnapshot.getKey(), Objects.requireNonNull(user).getUid())) {
                    userRole = "Tutor";
                    storage.userRole = userRole;
                    oppositeUserRole = "Student";
                    currentUId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                    mCustomerDatabase = getDatabaseInstance().getReference().child("Users").child(storage.userRole).child(currentUId);
                    getUserInfo();
                    getOppositeRoleUsers();
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
                    storage.userRole = userRole;
                    oppositeUserRole = "Tutor";
                    currentUId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                    mCustomerDatabase = getDatabaseInstance().getReference().child("Users").child(storage.userRole).child(currentUId);
                    getUserInfo();
                    getOppositeRoleUsers();
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

    public void getOppositeRoleUsers(){
        DatabaseReference oppositeSexDb = getDatabaseInstance().getReference().child("Users").child(oppositeUserRole);
        oppositeSexDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists() // ) {
                        && !dataSnapshot.child("Connections").child("No").hasChild(currentUId) && !dataSnapshot.child("Connections").child("Yes").hasChild(currentUId)) {
                    String profileImageUrl = "default";
                    if (!dataSnapshot.child("profileImageUrl").getValue().equals("default")) {
                        profileImageUrl = Objects.requireNonNull(dataSnapshot.child("profileImageUrl").getValue()).toString();
                    }
                    Card card = new Card(dataSnapshot.getKey(), Objects.requireNonNull(dataSnapshot.child("fullname").getValue()).toString(),
                            profileImageUrl);
                    possibleMatchesQueue.add(card);
                    arrayAdapter.notifyDataSetChanged();
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

    private void getUserInfo() {
        mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fullname = null, username = null, email = null, subject = null;
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    if (Objects.requireNonNull(map).get("fullname") != null) {
                        fullname = Objects.requireNonNull(map.get("fullname")).toString();
                    }
                    if (Objects.requireNonNull(map).get("username") != null) {
                        username = Objects.requireNonNull(map.get("username")).toString();
                    }
                    if (Objects.requireNonNull(map).get("email") != null) {
                        email = Objects.requireNonNull(map.get("email")).toString();
                    }
                    if (Objects.requireNonNull(map).get("subject") != null) {
                        subject = Objects.requireNonNull(map.get("subject")).toString();
                    }

                    if (userRole.equals("Student")) {
                        storage.currentUser = new Student(fullname, username, email, UserRepetinder.Subject.valueOf(subject));
                    } else {
                        storage.currentUser = new Tutor(fullname, username, email, UserRepetinder.Subject.valueOf(subject));
                    }
                    storage.userId = currentUId;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
