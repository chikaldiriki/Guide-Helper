package ru.hse.guidehelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.hse.guidehelper.auth.ProfileActivity;
import ru.hse.guidehelper.auth.SignInActivity;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        } else {
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        }

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentUser != null) {
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                } else {
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                }
            }
        }, 2000);*/
    }
}