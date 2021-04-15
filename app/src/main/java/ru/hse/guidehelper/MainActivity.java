package ru.hse.guidehelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.hse.guidehelper.auth.ProfileActivity;
import ru.hse.guidehelper.auth.SignInActivity;
import ru.hse.guidehelper.auth.SignInFragment;
import ru.hse.guidehelper.ui.bottomNavBar.excursion.ExcursionFragment;
import ru.hse.guidehelper.ui.bottomNavBar.orders.MyOrdersFragment;
import ru.hse.guidehelper.ui.bottomNavBar.profile.ProfileFragment;
import ru.hse.guidehelper.ui.bottomNavBar.subscriptions.SubscriptionsFragment;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    public static int currentFragmentId = R.id.nav_host_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(item -> {
            Fragment chosenFragment = null;
            FirebaseUser currentUser = mAuth.getCurrentUser();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    chosenFragment = new ExcursionFragment();
                    break;
                case R.id.navigation_dashboard:
                    chosenFragment = new MyOrdersFragment();
                    break;
                case R.id.navigation_notifications:
                    chosenFragment = new SubscriptionsFragment();
                    break;
                case R.id.navigation_profile:
                    if (currentUser != null) {
                        chosenFragment = new ProfileFragment();
                    } else {
                        chosenFragment = new SignInFragment();
                    }
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(currentFragmentId,
                    chosenFragment).commit();

            currentFragmentId = chosenFragment.getId();

            return true;
        });
    }
}