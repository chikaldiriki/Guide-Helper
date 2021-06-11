package ru.hse.guidehelper.ui.navigationbar.orders;

import java.util.List;

import ru.hse.guidehelper.excursions.TourRecyclerViewAdapter;
import ru.hse.guidehelper.model.Tour;

public class OrdersTourRecyclerViewAdapter extends TourRecyclerViewAdapter {

    public OrdersTourRecyclerViewAdapter(List<Tour> orders) {
        super(orders);
    }

    @Override
    protected void initConstructor(List<Tour> orders) {
        tours = orders;
    }
}
