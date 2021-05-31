package ru.hse.guidehelper.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

import ru.hse.guidehelper.MainActivity;
import ru.hse.guidehelper.R;
import ru.hse.guidehelper.api.RequestHelper;
import ru.hse.guidehelper.config.ApplicationConfig;
import ru.hse.guidehelper.model.User;

public class SignInFragment extends Fragment {

    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;
    int RC_SIGN_IN = 120;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        View root = inflater.inflate(R.layout.activity_signin, container, false);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(root.getContext(), gso);

        ImageButton login = root.findViewById(R.id.signInGoogle);
        login.setOnClickListener(view -> signIn());

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
                    if (account != null) {
                        Log.d("LoginActivity", "firebaseAuthWithGoogle:" + account.getId());
                        if (MainActivity.currentUser == null) {
                            System.out.println("Очко 1");
                        }
                        firebaseAuthWithGoogle(account.getIdToken());
                        if (MainActivity.currentUser == null) {
                            System.out.println("Очко 2");
                        }
                    }
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("LoginActivity", "Google sign in failed", e);
                }
            } else {
                Log.w("LoginActivity", exception);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        if (Objects.equals(MainActivity.currentUser, null)) {
                            MainActivity.currentUser = new User()
                                    .setUserMail(currentUser.getEmail())
                                    .setGuide(false)
                                    .setName(currentUser.getDisplayName())
                                    .setAvatarUrl(Objects.requireNonNull(currentUser.getPhotoUrl()).toString());
                        }

                        //System.out.println(MainActivity.currentUser.getAvatar());
                        RequestHelper.addUser(MainActivity.currentUser);
                        MainActivity.writeUserToFile(ApplicationConfig.cachedUserDTOfile, MainActivity.currentUser);
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("LoginActivity", "signInWithCredential:success");

                        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                        SignInFragment.this.requireActivity().onBackPressed();

                        navController.navigate(R.id.navigation_profile);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("LoginActivity", "signInWithCredential:failure", task.getException());
                    }
                });
    }
}