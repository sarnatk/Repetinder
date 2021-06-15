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
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import ru.hse.java.repetinder.R;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "SettingsActivity";

    private EditText nameET;
    private EditText descriptionET;
    private TextView birthdayTV;
    private Button saveButton;
    private Spinner genderSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setTitle("Edit profile - Salle Tinder");
        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        nameET = findViewById(R.id.name_settings);
        descriptionET = findViewById(R.id.description_settings);
        birthdayTV = findViewById(R.id.birthday_settings);
        saveButton = findViewById(R.id.save_button);

        saveButton.setOnClickListener(v -> sendData());
        birthdayTV.setOnClickListener(v -> setBirth());

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