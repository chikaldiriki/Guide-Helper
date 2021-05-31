package ru.hse.guidehelper.excursions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.hse.guidehelper.MainActivity;
import ru.hse.guidehelper.R;
import ru.hse.guidehelper.api.RequestHelper;
import ru.hse.guidehelper.model.Tour;


public abstract class ExcursionsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_excursions, container, false);

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        View recyclerView = root.findViewById(R.id.excursionslist_list);
        assert recyclerView != null;

        setupRecyclerView((RecyclerView) recyclerView);

        return root;
    }

    protected abstract void setupRecyclerView(@NonNull RecyclerView recyclerView);
}