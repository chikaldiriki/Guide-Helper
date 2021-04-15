package ru.hse.guidehelper.ui.bottomNavBar.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.hse.guidehelper.R;
import ru.hse.guidehelper.auth.ProfileActivity;
import ru.hse.guidehelper.auth.SignInActivity;
import ru.hse.guidehelper.auth.SignInFragment;

import android.content.Intent;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    // PROFILE
    private CircleImageView profileImageView;
    private TextView nameTextView, locationTextView, roleTextView;
    private TextView emailTextView, tagsTextView, mobileNumberTextView;

    // СТАЛО

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        View root = null;

        System.out.println(currentUser);

        root = inflater.inflate(R.layout.activity_profile, container, false);

        String personImage = Objects.requireNonNull(currentUser.getPhotoUrl()).toString();
        profileImageView = root.findViewById(R.id.profile_image);
        Glide.with(root.getContext()).load(personImage).into(profileImageView);

        nameTextView = root.findViewById(R.id.name);
        locationTextView = root.findViewById(R.id.location);
        roleTextView = root.findViewById(R.id.role);

        emailTextView = root.findViewById(R.id.email);
        tagsTextView = root.findViewById(R.id.tags);
        mobileNumberTextView = root.findViewById(R.id.mobileNumber);

        nameTextView.setText(currentUser.getDisplayName());
        locationTextView.setText("city");
        roleTextView.setText("tripper");

        emailTextView.setText(currentUser.getEmail());
        tagsTextView.setText("some tags"); //TODO
        mobileNumberTextView.setText(currentUser.getPhoneNumber() == null ? "None" : currentUser.getPhoneNumber());

        return root;
    }
}
