package ru.hse.java.repetinder;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> al;
    private ArrayAdapter<String> arrayAdapter;
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        String username = extras.getString("username");
        al = new ArrayList<>();
        al.add(username);
        al.add("Таня");
        al.add("Саша");
        al.add("Герман");

        arrayAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.helloText, al);


        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!");
                al.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                Toast.makeText(MainActivity.this, "no...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(MainActivity.this, "yes!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                al.add("Имя ".concat(String.valueOf(i)));
                arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });


        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
            }
        });

    }
}