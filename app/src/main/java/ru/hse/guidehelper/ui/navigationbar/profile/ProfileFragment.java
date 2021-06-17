package ru.hse.guidehelper.ui.navigationbar.profile;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.hse.guidehelper.MainActivity;
import ru.hse.guidehelper.R;
import ru.hse.guidehelper.model.User;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        if (Objects.equals(MainActivity.currentUser, null)) {
            MainActivity.currentUser = new User()
                    .setUserMail(currentUser.getEmail())
                    .setIsGuide(false)
                    .setName(currentUser.getDisplayName())
                    .setAvatarUrl(Objects.requireNonNull(currentUser.getPhotoUrl()).toString());
        }

        String personImage = MainActivity.currentUser.getAvatarUrl();
        CircleImageView profileImageView = root.findViewById(R.id.profile_image);
        Glide.with(root.getContext()).load(personImage).into(profileImageView);

        TextView nameTextView = root.findViewById(R.id.name);
        TextView roleTextView = root.findViewById(R.id.role);
        nameTextView.setText(currentUser.getDisplayName());
        roleTextView.setText(MainActivity.currentUser.getIsGuide() ? "Guide" : "Tripper");

        Button buttonToChat = root.findViewById(R.id.buttonToChat);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        buttonToChat.setOnClickListener(view -> navController.navigate(R.id.dialogFragment2));

        Button becomeGuideButton = root.findViewById(R.id.becomeGuideButton);
        becomeGuideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.guideInfoFragment);
            }
        });

        Button addExcursionButton = root.findViewById(R.id.addExcursionButton);
        addExcursionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.addExcursionFragment);
            }
        });

        if (!MainActivity.currentUser.getIsGuide()) {
            ConstraintLayout layout = root.findViewById(R.id.profileGuideInfoLayout);
            layout.setVisibility(View.INVISIBLE);
            addExcursionButton.setVisibility(View.INVISIBLE);
            return root;
        }

        becomeGuideButton.setText("Редактировать профиль");
        TextView emailTextView = root.findViewById(R.id.email);
        TextView locationTextView = root.findViewById(R.id.location);
        TextView mobileNumberTextView = root.findViewById(R.id.mobileNumber);
        TextView descriptionTextView = root.findViewById(R.id.description);

        emailTextView.setText(currentUser.getEmail());
        locationTextView.setText(MainActivity.currentUser.getCity());
        mobileNumberTextView.setText(MainActivity.currentUser.getPhoneNumber());
        descriptionTextView.setText(MainActivity.currentUser.getDescription());

        return root;
    }
}
