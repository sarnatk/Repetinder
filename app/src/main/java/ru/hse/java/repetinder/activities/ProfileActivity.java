package ru.hse.java.repetinder.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import ru.hse.java.repetinder.R;
import ru.hse.java.repetinder.user.Storage;

public class ProfileActivity extends AppCompatActivity {

    public static final String TEXT = "for profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        Bundle extras = getIntent().getExtras();
        Storage storage = (Storage)extras.getSerializable(TEXT);
        TextView userEmail = (TextView) findViewById(R.id.mailProfile);
        TextView userFullname = (TextView) findViewById(R.id.fullnameProfile);
        TextView userRole = (TextView) findViewById(R.id.userRoleProfile);
        userEmail.setText(storage.currentUser.getEmail());
        userFullname.setText(storage.currentUser.getFullname());
        userRole.setText(String.format("Status: %s", storage.userRole));

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

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonToHomeFromProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.TEXT, storage);
                startActivity(intent);
                finish();
            }
        });

        buttonToMatchesFromProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MatchesActivity.class);
                intent.putExtra(MatchesActivity.TEXT, storage);
                startActivity(intent);
                finish();
            }
        });
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
