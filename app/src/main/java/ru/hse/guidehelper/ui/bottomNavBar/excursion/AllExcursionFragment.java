package ru.hse.guidehelper.ui.bottomNavBar.excursion;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.hse.guidehelper.excursions.ExcursionsFragment;

public final class AllExcursionFragment extends ExcursionsFragment {
    @Override
    protected void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new AllTourRecyclerViewAdapter());
    }
}
