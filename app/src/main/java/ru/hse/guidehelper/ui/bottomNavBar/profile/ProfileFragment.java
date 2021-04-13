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

import android.content.Intent;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    // БЫЛО
    private ProfileViewModel profileViewModel;

    // AUTH
    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;
    private ImageButton login;
    int RC_SIGN_IN = 120;

    // PROFILE
    private CircleImageView profileImageView;
    private TextView nameTextView, locationTextView, roleTextView;
    private TextView emailTextView, tagsTextView, mobileNumberTextView;

    // СТАЛО

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        System.out.println(currentUser);

        View root = null;

        root = inflater.inflate(R.layout.activity_signin, container, false);


        if (currentUser != null) {
            root = inflater.inflate(R.layout.activity_signin, container, false);

            final TextView textView = root.findViewById(R.id.text_profile);

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            googleSignInClient = GoogleSignIn.getClient(root.getContext(), gso);

            mAuth = FirebaseAuth.getInstance();
            login = root.findViewById(R.id.signInGoogle);
            login.setOnClickListener(view -> signIn());

        } else {
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

        }
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        return root;

        /*profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        //return root;
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            Exception exception = task.getException();
            if (task.isSuccessful()) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d("LoginActivity", "firebaseAuthWithGoogle:" + account.getId());
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("LoginActivity", "Google sign in failed", e);
                }
            } else {
                Log.w("LoginActivity", exception.toString());
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this.getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LoginActivity", "signInWithCredential:success");
                            startActivity(new Intent(getActivity().getApplicationContext(), ProfileActivity.class));
                            //root = inflater.inflate(R.layout.activity_signin, container, false);
                            getActivity().finish(); // maybe wrong
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("LoginActivity", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
}
