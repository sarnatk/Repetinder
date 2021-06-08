package ru.hse.java.repetinder.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.IOException;

import ru.hse.java.repetinder.R;
import ru.hse.java.repetinder.user.Storage;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    public static final String TEXT = "for profile";

    private static final int GALLERY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        /*
        Bundle extras = getIntent().getExtras();
        Storage storage = (Storage)extras.getSerializable(TEXT);
        TextView userEmail = (TextView) findViewById(R.id.mailProfile);
        TextView userFullname = (TextView) findViewById(R.id.fullnameProfile);
        TextView userRole = (TextView) findViewById(R.id.userRoleProfile);
         */
        //userEmail.setText(storage.currentUser.getEmail());
       // userFullname.setText(storage.currentUser.getFullname());
       // userRole.setText(String.format("Status: %s", storage.userRole));

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
        ImageView profileView = (ImageView) findViewById(R.id.profileImage);

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonToHomeFromProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                //intent.putExtra(MainActivity.TEXT, storage);
                startActivity(intent);
                finish();
            }
        });

        buttonToMatchesFromProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MatchesActivity.class);
                //intent.putExtra(MatchesActivity.TEXT, storage);
                startActivity(intent);
                finish();
            }
        });

        profileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Bitmap bitmap = null;
        ImageView profileView = (ImageView) findViewById(R.id.profileImage);

        switch(requestCode) {
            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
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
}
