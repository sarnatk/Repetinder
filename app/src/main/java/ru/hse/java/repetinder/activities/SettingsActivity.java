package ru.hse.java.repetinder.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ru.hse.java.repetinder.R;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final String TAG = "SettingsActivity";

    private String userRole;
    private EditText nameET;
    private EditText descriptionET;
    private TextView birthdayTV;
    private Button saveButton;
    private Spinner genderSpinner;
    private ProgressBar progressBar;
    private SwitchCompat showOnAppSwitch;
    private boolean isSwitchChecked = true;
    private String currentUId;
    private DatabaseReference databaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        userRole = getIntent().getExtras().getString(TAG);
        currentUId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseUser = getDatabaseInstance().getReference().child("Users").child(userRole).child(currentUId);
        getUserInfo();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setTitle("Edit profile - Salle Tinder");
        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);



        nameET = findViewById(R.id.name_settings);
        descriptionET = findViewById(R.id.description_settings);
        birthdayTV = findViewById(R.id.birthday_settings);
        saveButton = findViewById(R.id.save_button);
        progressBar = findViewById(R.id.progressBarSettings);
        progressBar.setVisibility(View.VISIBLE);
        showOnAppSwitch = findViewById(R.id.switch_showOnApp);
   //     if (userRole.equals("Student")) {
            showOnAppSwitch.setVisibility(View.GONE);
     //   }

        saveButton.setOnClickListener(v -> sendData());
        birthdayTV.setOnClickListener(v -> setBirth());
        showOnAppSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isSwitchChecked = isChecked;
        });

    }

    private FirebaseDatabase getDatabaseInstance() {
        return FirebaseDatabase.getInstance("https://repetinder-cb68d-default-rtdb.europe-west1.firebasedatabase.app/");
    }

    private void getUserInfo() {
        databaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isFree = true;
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    if (userRole.equals("Tutor") && Objects.requireNonNull(map).get("isFree") != null) {
                        isFree = (boolean) map.get("isFree");
                        showOnAppSwitch.setVisibility(View.VISIBLE);
                    }
                    showOnAppSwitch.setChecked(isFree);
                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void logout() {
        runOnUiThread(() -> {
            SharedPreferences.Editor sharedPrefEditor = getSharedPreferences(
                    getString(R.string.logout), Context.MODE_PRIVATE).edit();
            sharedPrefEditor.putBoolean("rememberMe", false);
            sharedPrefEditor.apply();
            // Create the LoginActivity intent
            Intent loginActivityIntent = new Intent(this, LoginActivity.class);
            // Start MainActivity via the intent
            startActivity(loginActivityIntent);
            // Finish the LoginActivity
            finishAffinity();
        });
    }

    private void setBirth() {
        Calendar c = Calendar.getInstance();
        if (true) {
            c.add(Calendar.YEAR, -18);
        }
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> birthdayTV.setText(String.format("%02d/%02d/%02d", dayOfMonth, month + 1, year)), c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void sendData() {
        if (userRole.equals("Tutor")) {
            Map userInfo = new HashMap();
            userInfo.put("isFree", isSwitchChecked);
            databaseUser.updateChildren(userInfo);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
}
