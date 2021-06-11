package ru.hse.guidehelper.ui.navigationbar.orders;

import java.util.HashMap;
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
        mapIdTour = new HashMap<>();
        for (int i = 0; i < tours.size(); i++) {
            Tour tour = tours.get(i);
            mapIdTour.put(tour.getId(), tour);
        }
    }
}
