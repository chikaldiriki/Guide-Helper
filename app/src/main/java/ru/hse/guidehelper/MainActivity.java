package ru.hse.guidehelper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button mButtonToChat;
    public static int currentFragmentId = R.id.nav_host_fragment;
    private NavController navController;
    private BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications,
                R.id.navigation_profile
        ).build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        mButtonToChat = findViewById(R.id.buttonToChat);
        mAuth = FirebaseAuth.getInstance();

        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navView.setVisibility(BottomNavigationView.INVISIBLE);
                Fragment chosenFragment = new DialogFragment();
                getSupportFragmentManager().beginTransaction().replace(currentFragmentId,
                        chosenFragment).commit();

                currentFragmentId = chosenFragment.getId();
            }
        });*/

        mButtonToChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.dialogFragment2);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        /*navView.setOnNavigationItemSelectedListener(item -> {
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

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(currentFragmentId, chosenFragment).commit();

            currentFragmentId = chosenFragment.getId();

            return true;
        });*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}