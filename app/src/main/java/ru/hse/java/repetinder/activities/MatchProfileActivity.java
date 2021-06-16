package ru.hse.java.repetinder.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Objects;

import ru.hse.java.repetinder.R;

public class MatchProfileActivity extends AppCompatActivity {
    public static final String TEXT1 = "matchId";
    public static final String TEXT2 = "matchUserRole";


    private String profileImageUrl;
    private ImageView matchImage;
    TextView matchFullname, matchSubject, matchRole, matchEmail, matchPrice, matchCity, matchAbout;
    private DatabaseReference databaseUser;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        String matchId = getIntent().getExtras().getString(TEXT1);
        String matchUserRole = getIntent().getExtras().getString(TEXT2);

        databaseUser = getDatabaseInstance().getReference().child("Users").child(matchUserRole).child(matchId);
        getMatchInfo();

        matchFullname = findViewById(R.id.fullname_match);
        matchSubject = findViewById(R.id.subject_match);
        matchAbout = findViewById(R.id.about_match);
        matchRole = findViewById(R.id.userRole_match);
        matchEmail = findViewById(R.id.mail_match);
        matchPrice = findViewById(R.id.price_match);
        matchCity = findViewById(R.id.city_match);
        matchImage = findViewById(R.id.image_match);
        matchRole.setText(String.format("Status: %s", matchUserRole));
    }

    private FirebaseDatabase getDatabaseInstance() {
        return FirebaseDatabase.getInstance("https://repetinder-cb68d-default-rtdb.europe-west1.firebasedatabase.app/");
    }

    private void getMatchInfo() {
        databaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    if (Objects.requireNonNull(map).get("profileImageUrl") != null) {
                        profileImageUrl = Objects.requireNonNull(map.get("profileImageUrl")).toString();
                        if (!profileImageUrl.equals("default")) {
                            Glide.with(getApplication()).load(profileImageUrl).into(matchImage);
                        }
                    }
                    if (map.get("fullname") != null) {
                        matchFullname.setText(map.get("fullname").toString());
                    }
                    if (map.get("subject") != null) {
                        String subject = map.get("subject").toString();
                        String tmp = subject.substring(0, 1) + subject.substring(1).toLowerCase();
                        matchSubject.setText(tmp);
                    }
                    /*if (map.get("aboutMe") != null) {
                        matchAbout.setText(map.get("aboutMe").toString());
                    }
                    if (map.get("city") != null) {
                        matchCity.setText(map.get("city").toString());
                    }*/
                    if (map.get("email") != null) {
                        matchEmail.setText(map.get("email").toString());
                    }
                    if (matchRole.equals("Tutor")) {
                        if (map.get("minPrice") != null) {
                            matchPrice.setText(map.get("minPrice").toString());
                        }
                    } else {
                        if (map.get("maxPrice") != null) {
                            matchPrice.setText(map.get("maxPrice").toString());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
