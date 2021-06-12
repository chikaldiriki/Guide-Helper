package ru.hse.guidehelper.excursions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ru.hse.guidehelper.R;

public abstract class ExcursionsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        View root = getViewIfListIsEmpty(inflater, container);
        if(root != null) {
            return root;
        }

        root = inflater.inflate(R.layout.fragment_excursions, container, false);

        FloatingActionButton chatButton = root.findViewById(R.id.chatButton);
        chatButton.setOnClickListener(view -> Navigation
                .findNavController(requireActivity(), R.id.nav_host_fragment)
                .navigate(R.id.dialogFragment2));

        View recyclerView = root.findViewById(R.id.excursionslist_list);
        assert recyclerView != null;

        setupRecyclerView((RecyclerView) recyclerView);

        return root;
    }

    protected abstract View getViewIfListIsEmpty(@NonNull LayoutInflater inflater, ViewGroup container);

    protected abstract void setupRecyclerView(@NonNull RecyclerView recyclerView);
}