package ru.hse.guidehelper.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import java.util.Objects;
import java.util.regex.Pattern;

import ru.hse.guidehelper.MainActivity;
import ru.hse.guidehelper.R;
import ru.hse.guidehelper.chat.MessagesFragment;
import ru.hse.guidehelper.config.ApplicationConfig;
import ru.hse.guidehelper.dto.UserDTO;
import ru.hse.guidehelper.ui.bottomNavBar.profile.ProfileFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuideInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuideInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText editLocation, editMobilePhone, editDescription;
    private Button saveСhangesButton;
    private AwesomeValidation awesomeValidation;

    public GuideInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GuideInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GuideInfoFragment newInstance(String param1, String param2) {
        GuideInfoFragment fragment = new GuideInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_guide_info, container, false);
        this.getActivity().findViewById(R.id.buttonToChat).setVisibility(View.INVISIBLE);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        editLocation = root.findViewById(R.id.editLocation);
        if (!Objects.equals(MainActivity.currentUser.getCity(), null)) {
            editLocation.setText(MainActivity.currentUser.getCity());
        }

        editMobilePhone = root.findViewById(R.id.editMobilePhone);
        if (!Objects.equals(MainActivity.currentUser.getPhoneNumber(), null)) {
            editMobilePhone.setText(MainActivity.currentUser.getPhoneNumber());
        }

        editDescription = root.findViewById(R.id.editDescription);
        if (!Objects.equals(MainActivity.currentUser.getDescription(), null)) {
            editDescription.setText(MainActivity.currentUser.getDescription());
        }

        saveСhangesButton = root.findViewById(R.id.saveChangesButton);
        saveСhangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()) {
                    Toast.makeText(GuideInfoFragment.this.getActivity(), "Registration Successfull", Toast.LENGTH_LONG).show();
                    // обновить пользователя в БД
                    MainActivity.currentUser
                            .setCity(editLocation.getText().toString())
                            .setPhoneNumber(editMobilePhone.getText().toString())
                            .setDescription(editDescription.getText().toString())
                            .setGuide(true);

                    MainActivity.writeUserToFile(ApplicationConfig.cachedUserDTOfile, MainActivity.currentUser);

                    // updateUser(MainActivity.currentUser.getEmail,MainActivity.currentUser);
                    GuideInfoFragment.this.requireActivity().onBackPressed();
                    GuideInfoFragment.this.requireActivity().onBackPressed();
                    GuideInfoFragment.this.getActivity().findViewById(R.id.buttonToChat).setVisibility(View.VISIBLE);

                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                    navController.navigate(R.id.navigation_profile);
                }
            }
        });

        awesomeValidation.addValidation(editLocation, Pattern.compile("^((\\s*)[^\\s]+(\\s*))+$"), "Локация не должна быть пустой!");
        awesomeValidation.addValidation(editMobilePhone, Patterns.PHONE, "Введите корректный номер!");
        awesomeValidation.addValidation(editDescription, Pattern.compile("^((\\s*)[^\\s]+(\\s*))+$"), "Описание не должно быть пустым!");

        return root;
    }
}