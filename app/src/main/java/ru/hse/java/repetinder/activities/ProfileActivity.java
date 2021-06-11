package ru.hse.java.repetinder.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ru.hse.java.repetinder.R;
import ru.hse.java.repetinder.photo.PhotoWorker;
import ru.hse.java.repetinder.user.Storage;
import ru.hse.java.repetinder.user.UserRepetinder;

public class ProfileActivity extends AppCompatActivity {

    public static final String TEXT = "for profile";

    private static final int GALLERY_REQUEST = 1;

    private String profileImageUrl;

    private String currentUId;
    private Storage storage;
    private DatabaseReference mCustomerDatabase;
    private ImageView profileView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ActionBar actionBar;
        actionBar = getSupportActionBar();


        Bundle extras = getIntent().getExtras();
        storage = (Storage)extras.getSerializable(TEXT);
        TextView userEmail = findViewById(R.id.mailProfile);
        TextView userFullname = findViewById(R.id.fullnameProfile);
        TextView userRole = findViewById(R.id.userRoleProfile);
        TextView userUsername = findViewById(R.id.usernameProfile);
        TextView userSubject = findViewById(R.id.subjectProfile);
        String role = storage.userRole;
        currentUId = storage.userId;

        userRole.setText(String.format("Status: %s", role));

        UserRepetinder currentUser = storage.currentUser;
        userFullname.setText(currentUser.getFullname());
        userUsername.setText(currentUser.getFullname());
        userEmail.setText(currentUser.getEmail());
        String subject = currentUser.getSubject().toString();
        userSubject.setText(subject.substring(0, 1)  + subject.substring(1).toLowerCase());

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#FF3700B3"));

        // Set BackgroundDrawable
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(colorDrawable);
        }

        Button buttonToHomeFromProfile = findViewById(R.id.toHomeFromProfile);
        Button buttonToMatchesFromProfile = findViewById(R.id.toMatchesFromProfile);
        Button logOutButton = findViewById(R.id.logOut);
        profileView = findViewById(R.id.profileImage);

        // set photo of profile
      /*  Bitmap profileBitmap = currentUser.getBitmap();
        if (profileBitmap != null) {
            profileView.setImageBitmap(profileBitmap);

            Log.v("Bitmap", "User's bitmap isn't null");
        } else {
            Log.v("Bitmap", "User's bitmap is null");
        }*/
        setProfilePhoto();

        logOutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        buttonToHomeFromProfile.setOnClickListener(v -> {
            finish();
        });

        buttonToMatchesFromProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MatchesActivity.class);
            //storage.currentUser = currentUser;
            intent.putExtra(MatchesActivity.TEXT, storage);
            startActivity(intent);
            finish();
        });

        profileView.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            // TODO: fix deprecated
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
        });
    }

    private void setProfilePhoto() {
        mCustomerDatabase = MainActivity.getDatabaseInstance().getReference().child("Users").child(storage.userRole).child(currentUId);
        mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    if (Objects.requireNonNull(map).get("profileImageUrl") != null) {
                        profileImageUrl = Objects.requireNonNull(map.get("profileImageUrl")).toString();
                        Glide.with(getApplication()).load(profileImageUrl).into(profileView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Bitmap bitmap = null;
        ImageView profileView = findViewById(R.id.profileImage);

        if (requestCode == GALLERY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = imageReturnedIntent.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    bitmap = PhotoWorker.cropBitmap(bitmap);
                    bitmap = PhotoWorker.createResizedBitmap(bitmap);
                    addImageToDatabase(bitmap);
                } catch (IOException e) {
                    Toast.makeText(ProfileActivity.this, "Can't load image", Toast.LENGTH_SHORT).show();
                }
                profileView.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.profile) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addImageToDatabase(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        StorageReference filepath = FirebaseStorage.getInstance().getReference().child("profileImages").child(currentUId);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] data = stream.toByteArray();
        UploadTask uploadTask = filepath.putBytes(data);
        uploadTask.addOnFailureListener(e -> finish());
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            if (taskSnapshot.getMetadata() != null) {
                if (taskSnapshot.getMetadata().getReference() != null) {
                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                    result.addOnSuccessListener(uri -> {
                        Map userInfo = new HashMap();
                        userInfo.put("profileImageUrl", uri.toString());
                        mCustomerDatabase.updateChildren(userInfo);
                    });
                }
            }
        });
    /*    Log.v("DB", "Start adding bitmap");
        SerializableBitmap serBitmap = new SerializableBitmap(bitmap);
        currentUser.setBitmap(bitmap);
        DatabaseReference mDatabase = DatabaseWorker.getUserData(userRoleForData);
        mDatabase.child("bitmap").setValue(serBitmap);
        Log.v("DB", "End adding bitmap");*/
    }
}
