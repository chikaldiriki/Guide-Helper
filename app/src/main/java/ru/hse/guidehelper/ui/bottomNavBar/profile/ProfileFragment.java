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
import ru.hse.guidehelper.R;
import ru.hse.guidehelper.auth.GuideInfoFragment;
import ru.hse.guidehelper.dto.UserDTO;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    // PROFILE
    private CircleImageView profileImageView;
    private TextView nameTextView, roleTextView;
    private TextView emailTextView, locationTextView, mobileNumberTextView, descriptionTextView;
    private Button becomeGuideButton;

    // СТАЛО

    @SneakyThrows
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //UserDTO userDTO = null;
        /* while (!userDTO) {
            UserDTO = server.getUser(currentUser.getEmail());
            sleep(1sec);
        } */
        UserDTO userDTO = new UserDTO() // TODO only for test
                .setUserMail(currentUser.getEmail())
                .setCity("Spb")
                .setDescription("allaallaalala")
                .setGuide(false)
                .setName("Me")
                .setPhoneNumber("8800553535")
                .setPhotoUrl(Objects.requireNonNull(currentUser.getPhotoUrl()).toString());

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        String personImage = userDTO.getPhotoUrl();
        profileImageView = root.findViewById(R.id.profile_image);
        Glide.with(root.getContext()).load(personImage).into(profileImageView);

        nameTextView = root.findViewById(R.id.name);
        roleTextView = root.findViewById(R.id.role);
        nameTextView.setText(currentUser.getDisplayName());
        roleTextView.setText(userDTO.isGuide() ? "Guide" : "Tripper");

        becomeGuideButton = root.findViewById(R.id.becomeGuideButton);
        becomeGuideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.guideInfoFragment);

                GuideInfoFragment.setUserDTO(userDTO);
            }
        });

        if (!userDTO.isGuide()) {
            ConstraintLayout layout = root.findViewById(R.id.profileGuideInfoLayout);
            layout.setVisibility(View.INVISIBLE);
            return root;
        }

        becomeGuideButton.setVisibility(View.INVISIBLE);
        emailTextView = root.findViewById(R.id.email);
        locationTextView = root.findViewById(R.id.location);
        mobileNumberTextView = root.findViewById(R.id.mobileNumber);
        descriptionTextView = root.findViewById(R.id.description);

        emailTextView.setText(currentUser.getEmail());
        locationTextView.setText(userDTO.getCity());
        mobileNumberTextView.setText(userDTO.getPhoneNumber());
        descriptionTextView.setText(userDTO.getDescription());

        return root;
    }
}
