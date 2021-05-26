package ru.hse.guidehelper.excursions;

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
import ru.hse.guidehelper.auth.GuideInfoFragment;
import ru.hse.guidehelper.config.ApplicationConfig;
import ru.hse.guidehelper.model.Tour;

public class AddExcursionFragment extends Fragment {
    private EditText editTitle, editCity, editDescription, editCost, editImage;
    private AwesomeValidation awesomeValidation;

    public AddExcursionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_excursion, container, false);

        editTitle = root.findViewById(R.id.editTitle);
        editCity = root.findViewById(R.id.editCity);
        editDescription = root.findViewById(R.id.editDescription);
        editCost = root.findViewById(R.id.editСost);
        editImage = root.findViewById(R.id.editImage);

        this.requireActivity().findViewById(R.id.buttonToChat).setVisibility(View.INVISIBLE);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        Button saveChangesButton = root.findViewById(R.id.saveChangesButton);
        saveChangesButton.setOnClickListener(view -> {
            if (awesomeValidation.validate()) {
                Toast.makeText(AddExcursionFragment.this.getActivity(), "Добавлено", Toast.LENGTH_LONG).show();

                Tour addedTour = new Tour()
                        .setId(0L)
                        .setTitle(editTitle.getText().toString())
                        .setCity(editCity.getText().toString())
                        .setGuide(MainActivity.currentUser.getUserMail())
                        .setDescription(editDescription.getText().toString())
                        .setCost(Long.parseLong(editCost.getText().toString()))
                        .setImage(new Byte[3]);// TODO

                // добавить tour в БД
                AddExcursionFragment.this.requireActivity().onBackPressed();
                AddExcursionFragment.this.requireActivity().onBackPressed();
                AddExcursionFragment.this.requireActivity().findViewById(R.id.buttonToChat).setVisibility(View.VISIBLE);

                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.navigation_profile);
            }
        });

        awesomeValidation.addValidation(editTitle, Pattern.compile(getString(R.string.NonEmptyStringRegexp)), "Название не должно быть пустым!");
        awesomeValidation.addValidation(editCity, Pattern.compile(getString(R.string.NonEmptyStringRegexp)), "Локация не должна быть пустой!");
        awesomeValidation.addValidation(editCost, Pattern.compile(getString(R.string.NaturalNumberRegexp)), "Введите стоимость в рублях, например : 1000");
        awesomeValidation.addValidation(editDescription, Pattern.compile(getString(R.string.NonEmptyStringRegexp)), "Описание не должно быть пустым!");

        return root;
    }
}