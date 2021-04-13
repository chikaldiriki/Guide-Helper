package ru.hse.guidehelper.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.hse.guidehelper.R;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView profileImageView;
    private TextView nameTextView, locationTextView, roleTextView;
    private TextView emailTextView, tagsTextView, mobileNumberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String personImage = Objects.requireNonNull(currentUser.getPhotoUrl()).toString();
        profileImageView = findViewById(R.id.profile_image);
        Glide.with(this).load(personImage).into(profileImageView);

        nameTextView = findViewById(R.id.name);
        locationTextView = findViewById(R.id.location);
        roleTextView = findViewById(R.id.role);

        emailTextView = findViewById(R.id.email);
        tagsTextView = findViewById(R.id.tags);
        mobileNumberTextView = findViewById(R.id.mobileNumber);

        nameTextView.setText(currentUser.getDisplayName());
        locationTextView.setText("city");
        roleTextView.setText("tripper");

        emailTextView.setText(currentUser.getEmail());
        tagsTextView.setText("some tags"); //TODO
        mobileNumberTextView.setText(currentUser.getPhoneNumber() == null ? "None" : currentUser.getPhoneNumber());
    }
}