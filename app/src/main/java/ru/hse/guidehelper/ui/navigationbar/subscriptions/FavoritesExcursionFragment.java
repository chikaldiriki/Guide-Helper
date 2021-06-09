package ru.hse.guidehelper.ui.navigationbar.subscriptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.hse.guidehelper.MainActivity;
import ru.hse.guidehelper.excursions.ExcursionsFragment;

public final class FavoritesExcursionFragment extends ExcursionsFragment {
    @Override
    protected void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new FavoritesTourRecyclerViewAdapter(((MainActivity) requireActivity()).getTours()));
    }
}
