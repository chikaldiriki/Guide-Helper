package ru.hse.guidehelper.ui.navigationbar.excursion;

import java.util.HashMap;
import java.util.List;

import ru.hse.guidehelper.excursions.TourRecyclerViewAdapter;
import ru.hse.guidehelper.model.Tour;

public final class AllTourRecyclerViewAdapter extends TourRecyclerViewAdapter {

    public AllTourRecyclerViewAdapter(List<Tour> tours) {
        super(tours);
    }

    @Override
    protected void initConstructor(List<Tour> allTours) {
        tours = allTours;
        mapIdTour = new HashMap<>();
        for (int i = 0; i < tours.size(); i++) {
            Tour tour = tours.get(i);
            mapIdTour.put(tour.getId(), tour);
        }
    }

//    TourRecyclerViewAdapterAll() {
//        super();
//    }

}
