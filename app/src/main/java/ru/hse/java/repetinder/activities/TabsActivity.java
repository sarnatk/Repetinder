package ru.hse.java.repetinder.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import ru.hse.java.repetinder.R;
import ru.hse.java.repetinder.user.Storage;

public class TabsActivity extends AppCompatActivity {
    public static final String TEXT = "for tabs";
    private FirebaseAuth mAuth;
    private DatabaseReference mCustomerDatabase;
    private String currentUId;
    private Storage storage = new Storage();
    private String userRole, oppositeUserRole;

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        tabLayout = findViewById(R.id.tabLayout_id);
        viewPager2 = findViewById(R.id.viewPager_id);
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPagerAdapter = new ViewPagerAdapter(fragmentManager, getLifecycle());
        Bundle bundle = new Bundle();
        Fragment swipeFragment = new SwipesFragment();
        Fragment profileFragment = new ProfileFragment();
        Fragment matchesFragment = new MatchesFragment();

      //  bundle.putSerializable("1", storage);

 //       swipeFragment.setArguments(bundle);
   //     profileFragment.setArguments(bundle);
  //      matchesFragment.setArguments(bundle);

        viewPagerAdapter.addFragment(swipeFragment);
        viewPagerAdapter.addFragment(profileFragment);
        viewPagerAdapter.addFragment(matchesFragment);

        viewPager2.setAdapter(viewPagerAdapter);
        List<String> tabTitles = new ArrayList<>();
        tabTitles.add("Swipes");
        tabTitles.add("Profile");
        tabTitles.add("Matches");
        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> tab.setText(tabTitles.get(position))
        ).attach();
   //     tabLayout.addTab(tabLayout.newTab().setText("First"));
     //   tabLayout.addTab(tabLayout.newTab().setText("Second"));
      //  tabLayout.addTab(tabLayout.newTab().setText("Third"));
    }
}