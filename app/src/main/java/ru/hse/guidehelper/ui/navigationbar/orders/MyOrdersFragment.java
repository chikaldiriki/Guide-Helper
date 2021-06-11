package ru.hse.guidehelper.ui.navigationbar.orders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.hse.guidehelper.MainActivity;
import ru.hse.guidehelper.excursions.ExcursionsFragment;

public class MyOrdersFragment extends ExcursionsFragment {

    private OrdersTourRecyclerViewAdapter adapter;

    @Override
    protected void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        adapter = new OrdersTourRecyclerViewAdapter(((MainActivity) requireActivity()).getOrders());
        recyclerView.setAdapter(adapter);
    }
}