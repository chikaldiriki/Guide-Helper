package ru.hse.guidehelper.ui.bottomNavBar.excursion;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.navigation.NavigationView;

import ru.hse.guidehelper.R;
import ru.hse.guidehelper.excursions.ExcursionsListDetailActivity;
import ru.hse.guidehelper.excursions.ExcursionsListDetailFragment;
import ru.hse.guidehelper.excursions.ExcursionsListListActivity;

public class ExcursionFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_excursion, container, false); //  fragment_excursion | activity_main

        //Intent in = new Intent(getActivity(), ExcursionsListListActivity.class);
        //startActivity(in);

        return root;
    }
}