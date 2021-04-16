package ru.hse.guidehelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.hse.guidehelper.auth.SignInFragment;
import ru.hse.guidehelper.chat.DialogsActivity;
import ru.hse.guidehelper.ui.bottomNavBar.excursion.ExcursionFragment;
import ru.hse.guidehelper.ui.bottomNavBar.orders.MyOrdersFragment;
import ru.hse.guidehelper.ui.bottomNavBar.profile.ProfileFragment;
import ru.hse.guidehelper.ui.bottomNavBar.subscriptions.SubscriptionsFragment;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button button;
    public static int currentFragmentId = R.id.nav_host_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.buttonToChat);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DialogsActivity.class));
            }
        });

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