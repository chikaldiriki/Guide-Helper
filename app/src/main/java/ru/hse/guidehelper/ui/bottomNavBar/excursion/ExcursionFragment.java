package ru.hse.guidehelper.ui.bottomNavBar.excursion;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import ru.hse.guidehelper.R;
import ru.hse.guidehelper.excursions.ExcursionsListActivity;

public class ExcursionFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_excursion, container, false); //  fragment_excursion | activity_main

        Intent in = new Intent(getActivity(), ExcursionsListActivity.class);
        startActivity(in);

        return root;
    }
}