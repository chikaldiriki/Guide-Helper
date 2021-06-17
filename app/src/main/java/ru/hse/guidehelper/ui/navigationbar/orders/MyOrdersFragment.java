package ru.hse.guidehelper.ui.navigationbar.orders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.hse.guidehelper.MainActivity;
import ru.hse.guidehelper.R;
import ru.hse.guidehelper.excursions.ExcursionsFragment;

public class MyOrdersFragment extends ExcursionsFragment {

    @Override
    protected View getViewIfListIsEmpty(@NonNull LayoutInflater inflater, ViewGroup container) {
        if ((MainActivity.currentUser != null) && ((MainActivity)requireActivity()).isAnyOrders()) {
            return null;
        }
        View view = inflater.inflate(R.layout.fragment_orders_empty, container, false);
        return view;
    }

    @Override
    protected void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        OrdersTourRecyclerViewAdapter adapter= new OrdersTourRecyclerViewAdapter(((MainActivity) requireActivity()).getOrders());
        recyclerView.setAdapter(adapter);
    }
}
