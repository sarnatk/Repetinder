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
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.textfield.TextInputEditText;
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

public class SettingsActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener {
    public static final String TAG = "SettingsActivity";

    private TextInputEditText name;
    // private TextView nameText;
    private TextInputEditText description;
    //private TextView descriptionText;
    private TextInputEditText city;
    //private TextView cityText;
    private TextView birthday;
    private Button saveButton;
    private Spinner genderSpinner;
    private TextView price;
    private ProgressBar progressBar;
    private SwitchCompat showOnAppSwitch;
    private boolean isSwitchChecked = true;
    private DatabaseReference databaseUser;
    private String userRole;
    private String newDate;
    private String newName;
    private String newPrice;
    private String newDescr;
    private String newCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        userRole = getIntent().getExtras().getString(TAG);
        newDate = getIntent().getExtras().getString("date");
        newPrice = getIntent().getExtras().getString("price");
        newCity = getIntent().getExtras().getString("city");
        newName = getIntent().getExtras().getString("name");
        newDescr = getIntent().getExtras().getString("descr");

        String currentUId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        databaseUser = getDatabaseInstance().getReference().child("Users").child(userRole).child(currentUId);
        getUserInfo();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setTitle("Edit profile - Salle Tinder");
        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.name);
        //nameText = findViewById(R.id.name);
        description = findViewById(R.id.description);
        //descriptionText = findViewById(R.id.description);
        birthday = findViewById(R.id.birthday);
        city = findViewById(R.id.city);
        saveButton = findViewById(R.id.save_button);
        progressBar = findViewById(R.id.progressBarSettings);
        progressBar.setVisibility(View.VISIBLE);
        showOnAppSwitch = findViewById(R.id.switch_showOnApp);
        showOnAppSwitch.setVisibility(View.GONE);

        saveButton.setOnClickListener(v -> sendData());
        birthday.setOnClickListener(v -> setBirth());

        final SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);

        price = findViewById(R.id.price);
        price.setText("0");
        showOnAppSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isSwitchChecked = isChecked;
        });

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        price.setText(String.valueOf(seekBar.getProgress()));
    }

    private FirebaseDatabase getDatabaseInstance() {
        return FirebaseDatabase.getInstance("https://repetinder-cb68d-default-rtdb.europe-west1.firebasedatabase.app/");
    }

    private void getUserInfo() {
        databaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isSeen = true;
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    if (userRole.equals("Tutor") && Objects.requireNonNull(map).get("seen") != null) {
                        isSeen = (boolean) map.get("seen");
                        showOnAppSwitch.setVisibility(View.VISIBLE);
                    }
                    showOnAppSwitch.setChecked(isSeen);
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
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> birthday.setText(String.format("\n   %02d.%02d.%02d", dayOfMonth, month + 1, year)), c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void sendData() {
        if (!birthday.getText().toString().isEmpty()) {
            newDate = birthday.getText().toString().substring(4);
        }
        if (!name.getText().toString().isEmpty()) {
            newName = name.getText().toString();
        }
        if (!price.getText().toString().isEmpty()) {
            newPrice = price.getText().toString();
        }
        if (!description.getText().toString().isEmpty()) {
            newDescr = description.getText().toString();
        }
        if (!city.getText().toString().isEmpty()) {
            newCity = city.getText().toString();
        }

        Map userInfo = new HashMap();
        userInfo.put("seen", isSwitchChecked);
        userInfo.put("name", newName);
        userInfo.put("dateOfBirth", newDate);
        userInfo.put("aboutMe", newDescr);
        userInfo.put("city", newCity);
        userInfo.put("price", newPrice);
        databaseUser.updateChildren(userInfo);

        Toast.makeText(SettingsActivity.this, "Successfully saved", Toast.LENGTH_SHORT).show();
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
