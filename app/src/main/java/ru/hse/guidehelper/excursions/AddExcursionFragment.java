package ru.hse.guidehelper.excursions;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.regex.Pattern;

import ru.hse.guidehelper.MainActivity;
import ru.hse.guidehelper.R;
import ru.hse.guidehelper.api.RequestHelper;
import ru.hse.guidehelper.model.Tour;

public class AddExcursionFragment extends Fragment {
    private EditText editTitle;
    private EditText editCity;
    private EditText editDescription;
    private EditText editCost;
    private Button buttonAddImage;
    public static final int ADD_TOUR_IMAGE_REQUEST_CODE = 1;


    public ImageView addedImage = null;
    private AwesomeValidation awesomeValidation;

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
        buttonAddImage = root.findViewById(R.id.buttonAddImage);
        buttonAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(intent, ADD_TOUR_IMAGE_REQUEST_CODE);
            }
        });

        addedImage = root.findViewById(R.id.addedImage);

        this.requireActivity().findViewById(R.id.buttonToChat).setVisibility(View.INVISIBLE);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        Button saveChangesButton = root.findViewById(R.id.saveChangesButton);
        saveChangesButton.setOnClickListener(view -> {
            if (awesomeValidation.validate() && addedImage != null) {
                Toast.makeText(AddExcursionFragment.this.getActivity(), "Добавлено", Toast.LENGTH_LONG).show();

                Tour addedTour = new Tour()
                        .setId(0L)
                        .setTitle(editTitle.getText().toString())
                        .setCity(editCity.getText().toString())
                        .setGuide(MainActivity.currentUser.getUserMail())
                        .setDescription(editDescription.getText().toString())
                        .setCost(Long.parseLong(editCost.getText().toString()))
                        .setImage("test".getBytes()); // TODO

                RequestHelper.addTour(addedTour);

                AddExcursionFragment.this.requireActivity().onBackPressed();
                AddExcursionFragment.this.requireActivity().onBackPressed();

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if (resultCode == -1) {
            System.out.println(requestCode);
            if (requestCode == ADD_TOUR_IMAGE_REQUEST_CODE) {
                System.out.println(3);
                try {
                    System.out.println(data.getData());
                    InputStream inputStream = requireActivity().getContentResolver().openInputStream(data.getData());

                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    addedImage.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}