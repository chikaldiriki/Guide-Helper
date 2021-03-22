package ru.hse.guidehelper.ui.bottomNavBar.excursion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import ru.hse.guidehelper.R;

public class ExcursionFragment extends Fragment {

    private ExcursionViewModel excursionViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        excursionViewModel =
                new ViewModelProvider(this).get(ExcursionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_excursion, container, false);
        final TextView textView = root.findViewById(R.id.text_excursion);
        excursionViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}