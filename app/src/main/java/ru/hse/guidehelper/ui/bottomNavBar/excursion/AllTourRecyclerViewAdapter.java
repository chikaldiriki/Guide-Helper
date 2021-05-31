package ru.hse.guidehelper.ui.bottomNavBar.excursion;

import java.util.HashMap;

import ru.hse.guidehelper.api.RequestHelper;
import ru.hse.guidehelper.excursions.ExcursionsFragment;
import ru.hse.guidehelper.excursions.TourRecyclerViewAdapter;
import ru.hse.guidehelper.model.Tour;

public final class AllTourRecyclerViewAdapter extends TourRecyclerViewAdapter {

    @Override
    protected void initConstructor() {
        tours = RequestHelper.getAllTours();
        mapIdTour = new HashMap<>();
        for(int i = 0; i < tours.size(); i++) {
            Tour tour = tours.get(i);
            mapIdTour.put(tour.getId(), tour);
        }
    }

//    TourRecyclerViewAdapterAll() {
//        super();
//    }

}
