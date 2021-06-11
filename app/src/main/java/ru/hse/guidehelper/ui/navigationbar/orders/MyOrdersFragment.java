package ru.hse.guidehelper.ui.navigationbar.orders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.hse.guidehelper.MainActivity;
import ru.hse.guidehelper.excursions.ExcursionsFragment;

public class MyOrdersFragment extends ExcursionsFragment {

    @Override
    protected void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        OrdersTourRecyclerViewAdapter adapter = new OrdersTourRecyclerViewAdapter(((MainActivity) requireActivity()).getOrders());
        recyclerView.setAdapter(adapter);
    }
}