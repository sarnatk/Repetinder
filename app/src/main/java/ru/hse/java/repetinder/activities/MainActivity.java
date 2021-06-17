package ru.hse.java.repetinder.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
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
import java.util.Map;
import java.util.Objects;

import ru.hse.java.repetinder.R;
import ru.hse.java.repetinder.user.Storage;
import ru.hse.java.repetinder.user.Student;
import ru.hse.java.repetinder.user.Tutor;
import ru.hse.java.repetinder.user.UserRepetinder;

public class MainActivity extends AppCompatActivity {
    public static final String TEXT = "for storage";

    private FirebaseAuth mAuth;
    private DatabaseReference mCustomerDatabase;
    private String currentUId;
    private Storage storage = new Storage();
    private String userRole, oppositeUserRole;

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPagerAdapter viewPagerAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBarTabs);
        tabLayout = findViewById(R.id.tabLayout_id);
        viewPager2 = findViewById(R.id.viewPager_id);
        mAuth = FirebaseAuth.getInstance();
        checkUserRole();
    }

    private void makeTabs() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPagerAdapter = new ViewPagerAdapter(fragmentManager, getLifecycle());
        Bundle bundle = new Bundle();

        Fragment profileFragment = new ProfileFragment();
        Fragment matchesFragment = new MatchesFragment();

        bundle.putSerializable(TEXT, storage);

        profileFragment.setArguments(bundle);
        matchesFragment.setArguments(bundle);

        List<String> tabTitles = new ArrayList<>();
        if (storage.userRole.equals("Student")) {
            tabTitles.add("Swipes");
            Fragment swipeFragment = new SwipesFragment();
            swipeFragment.setArguments(bundle);
            viewPagerAdapter.addFragment(swipeFragment);
        }
        viewPagerAdapter.addFragment(profileFragment);
        viewPagerAdapter.addFragment(matchesFragment);

        viewPager2.setAdapter(viewPagerAdapter);


        tabTitles.add("Profile");
        tabTitles.add("Matches");
        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> tab.setText(tabTitles.get(position))
        ).attach();
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
                    storage.userRole = userRole;
                    oppositeUserRole = "Student";
                    storage.oppositeUserRole = oppositeUserRole;
                    currentUId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                    mCustomerDatabase = getDatabaseInstance().getReference().child("Users").child(storage.userRole).child(currentUId);
                    getUserInfo();
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
                    storage.oppositeUserRole = oppositeUserRole;
                    currentUId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                    mCustomerDatabase = getDatabaseInstance().getReference().child("Users").child(storage.userRole).child(currentUId);
                    getUserInfo();
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

    private void getUserInfo() {
        mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fullname = null, email = null, subject = null;
                int price = 0;
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    if (Objects.requireNonNull(map).get("fullname") != null) {
                        fullname = Objects.requireNonNull(map.get("fullname")).toString();
                    }
                    if (Objects.requireNonNull(map).get("email") != null) {
                        email = Objects.requireNonNull(map.get("email")).toString();
                    }
                    if (Objects.requireNonNull(map).get("subject") != null) {
                        subject = Objects.requireNonNull(map.get("subject")).toString();
                    }

                    if (Objects.requireNonNull(map).get("price") != null) {
                        price = ((Long) map.get("price")).intValue();
                    }

                    if (userRole.equals("Student")) {
                        storage.currentUser = new Student(fullname, email, UserRepetinder.Subject.valueOf(subject), price);
                    } else {
                        storage.currentUser = new Tutor(fullname, email, UserRepetinder.Subject.valueOf(subject), price);
                    }
                    storage.userId = currentUId;
                    makeTabs();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}