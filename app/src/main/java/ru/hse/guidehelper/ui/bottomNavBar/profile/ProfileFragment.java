package ru.hse.guidehelper.ui.bottomNavBar.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;
import lombok.SneakyThrows;
import ru.hse.guidehelper.MainActivity;
import ru.hse.guidehelper.R;
import ru.hse.guidehelper.auth.GuideInfoFragment;
import ru.hse.guidehelper.auth.SignInFragment;
import ru.hse.guidehelper.model.User;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    // PROFILE
    private CircleImageView profileImageView;
    private TextView nameTextView, roleTextView;
    private TextView emailTextView, locationTextView, mobileNumberTextView, descriptionTextView;
    private Button becomeGuideButton;

    // СТАЛО

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        //UserDTO userDTO = null;
        /* while (!userDTO) {
            UserDTO = server.getUser(currentUser.getEmail());
            sleep(1sec);
        } */
        if (Objects.equals(MainActivity.currentUser, null)) {
            MainActivity.currentUser = new User() // TODO only for test
                    .setUserMail(currentUser.getEmail())
                    .setGuide(false)
                    .setName("Me")
                    .setPhotoUrl(Objects.requireNonNull(currentUser.getPhotoUrl()).toString());
        }

        String personImage = MainActivity.currentUser.getPhotoUrl();
        profileImageView = root.findViewById(R.id.profile_image);
        Glide.with(root.getContext()).load(personImage).into(profileImageView);

        nameTextView = root.findViewById(R.id.name);
        roleTextView = root.findViewById(R.id.role);
        nameTextView.setText(currentUser.getDisplayName());
        roleTextView.setText(MainActivity.currentUser.isGuide() ? "Guide" : "Tripper");

        becomeGuideButton = root.findViewById(R.id.becomeGuideButton);
        becomeGuideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.guideInfoFragment);
            }
        });

        if (!MainActivity.currentUser.isGuide()) {
            ConstraintLayout layout = root.findViewById(R.id.profileGuideInfoLayout);
            layout.setVisibility(View.INVISIBLE);
            return root;
        }

        becomeGuideButton.setText("Редактировать профиль");
        emailTextView = root.findViewById(R.id.email);
        locationTextView = root.findViewById(R.id.location);
        mobileNumberTextView = root.findViewById(R.id.mobileNumber);
        descriptionTextView = root.findViewById(R.id.description);

        emailTextView.setText(currentUser.getEmail());
        locationTextView.setText(MainActivity.currentUser.getCity());
        mobileNumberTextView.setText(MainActivity.currentUser.getPhoneNumber());
        descriptionTextView.setText(MainActivity.currentUser.getDescription());

        return root;
    }
}
