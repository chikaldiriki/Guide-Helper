package ru.hse.guidehelper.ui.navigationbar.favorites;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.hse.guidehelper.MainActivity;
import ru.hse.guidehelper.R;
import ru.hse.guidehelper.excursions.ExcursionsFragment;

public final class FavoritesExcursionFragment extends ExcursionsFragment {
    @Override
    protected View getViewIfListIsEmpty(@NonNull LayoutInflater inflater, ViewGroup container) {
        if (((MainActivity)requireActivity()).isAnyFavoritesTours()) {
            return null;
        }
        View view = inflater.inflate(R.layout.fragment_favorites_empty, container, false);
        return view;
    }

    @Override
    protected void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new FavoritesTourRecyclerViewAdapter(((MainActivity) requireActivity()).getFavoritesTours()));
    }
}
