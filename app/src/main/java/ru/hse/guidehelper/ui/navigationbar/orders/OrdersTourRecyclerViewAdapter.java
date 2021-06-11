package ru.hse.guidehelper.ui.navigationbar.orders;

import java.util.List;

import ru.hse.guidehelper.excursions.TourRecyclerViewAdapter;
import ru.hse.guidehelper.model.TourOrder;

public class OrdersTourRecyclerViewAdapter extends TourRecyclerViewAdapter<TourOrder> {

    public OrdersTourRecyclerViewAdapter(List<TourOrder> orders) {
        super(orders);
    }

    @Override
    protected void initConstructor(List<TourOrder> orders) {
        tours = orders;
    }
}
