package ru.hse.guidehelper.ui.navigationbar.subscriptions;

import java.util.HashMap;

import ru.hse.guidehelper.MainActivity;
import ru.hse.guidehelper.api.RequestHelper;
import ru.hse.guidehelper.excursions.TourRecyclerViewAdapter;
import ru.hse.guidehelper.model.Tour;

public final class FavoritesTourRecyclerViewAdapter extends TourRecyclerViewAdapter {

    @Override
    protected void initConstructor() {
        tours = RequestHelper.getFavoriteTours(MainActivity.currentUser.getId());
        mapIdTour = new HashMap<>();
        for(int i = 0; i < tours.size(); i++) {
            Tour tour = tours.get(i);
            mapIdTour.put(tour.getId(), tour);
        }
    }
}
