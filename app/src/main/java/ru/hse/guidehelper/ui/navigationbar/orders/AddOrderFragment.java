package ru.hse.guidehelper.ui.navigationbar.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import ru.hse.guidehelper.MainActivity;
import ru.hse.guidehelper.R;
import ru.hse.guidehelper.api.RequestHelper;
import ru.hse.guidehelper.model.Order;
import ru.hse.guidehelper.model.TourOrder;

public class AddOrderFragment extends Fragment {

    private AwesomeValidation awesomeValidation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(MainActivity.currentUser == null) {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.signInFragment);
        }

        View root = inflater.inflate(R.layout.fragment_add_order, container, false);

        EditText editDate = root.findViewById(R.id.editDate);
        Button buttonBook = root.findViewById(R.id.saveChangesButtonBook);

        awesomeValidation = new AwesomeValidation(ValidationStyle.UNDERLABEL);
        awesomeValidation.setContext(root.getContext());

        final LocalDateTime[] date = new LocalDateTime[1];

        buttonBook.setOnClickListener(view -> {
            if (awesomeValidation.validate()) {
                Order order = new Order()
                        .setCustomerMail(MainActivity.currentUser.getUserMail())
                        .setTourId(MainActivity.currentTourId)
                        .setTourTime(date[0].toString());

                RequestHelper.addOrder(order);
                ((MainActivity)requireActivity()).addOrder(new TourOrder(
                        ((MainActivity)requireActivity()).getTourById(MainActivity.currentTourId),
                        order.getTourTime()));

                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.navigation_dashboard);

                requireActivity().findViewById(R.id.nav_view).setVisibility(BottomNavigationView.VISIBLE);
            }
        });

        awesomeValidation.addValidation(editDate, Pattern.compile(getString(R.string.NonEmptyStringRegexp)), "Дата не должна быть пустой!");
        awesomeValidation.addValidation(editDate, s -> {
            try {
                date[0] = LocalDateTime.parse(s, DateTimeFormatter.ofPattern(getString(R.string.order_format_date)));
            } catch (Exception e) {
                return false;
            }
            return true;
        }, "Формат даты " + getString(R.string.order_format_date) + "!");

        return root;
    }
}
