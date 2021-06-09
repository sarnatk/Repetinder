package ru.hse.java.repetinder.database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class DatabaseWorker {

    public static FirebaseDatabase getDatabaseInstance() {
        return FirebaseDatabase.getInstance("https://repetinder-cb68d-default-rtdb.europe-west1.firebasedatabase.app/");
    }

    public static String getUserId() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        return Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
    }

    public static DatabaseReference getUserData(String userRole, String uId) {
        return getDatabaseInstance().getReference().child("Users").child(userRole).child(uId);
    }

    public static DatabaseReference getUserData(String userRole) {
        return getUserData(userRole, getUserId());
    }
}
