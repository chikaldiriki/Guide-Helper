package ru.hse.guidehelper.ui.navigationbar.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import ru.hse.guidehelper.MainActivity;
import ru.hse.guidehelper.R;
import ru.hse.guidehelper.api.RequestHelper;
import ru.hse.guidehelper.model.Order;

public class AddOrderFragment extends Fragment {

    private EditText editDate;
    private Button buttonBook;
    private AwesomeValidation awesomeValidation;
    //private final String pattern = "yyyy-MM-dd HH:mm:ss";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_order, container, false);

        editDate = root.findViewById(R.id.editDate);
        buttonBook = root.findViewById(R.id.saveChangesButtonBook);

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
            }
        });

        awesomeValidation.addValidation(editDate, Pattern.compile(getString(R.string.NonEmptyStringRegexp)), "Дата не должна быть пустой!");
        awesomeValidation.addValidation(editDate, s -> {
            try {
                date[0] = LocalDateTime.parse(s, DateTimeFormatter.ofPattern(getString(R.string.order_format_date)));
                System.out.println(date[0]);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            }
            return true;
        }, "Формат даты " + getString(R.string.order_format_date) + "!");

        return root;
    }
}
