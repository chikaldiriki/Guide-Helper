package ru.hse.guidehelper.ui.navigationbar.subscriptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.hse.guidehelper.MainActivity;
import ru.hse.guidehelper.api.RequestHelper;
import ru.hse.guidehelper.excursions.TourRecyclerViewAdapter;
import ru.hse.guidehelper.model.Tour;

public class FavoritesTourRecyclerViewAdapter extends TourRecyclerViewAdapter {

    public static Map<Long, Tour> mapIdTour;

    public FavoritesTourRecyclerViewAdapter(List<Tour> tours) {
        super(tours);
    }

    @Override
    protected void initConstructor(List<Tour> allTours) {
        tours = RequestHelper.getFavoriteTours(MainActivity.currentUser.getId());
        mapIdTour = new HashMap<>();
        for(int i = 0; i < tours.size(); i++) {
            Tour tour = tours.get(i);
            mapIdTour.put(tour.getId(), tour);
        }
    }

    public static Tour getTourByIdFavorite(Long id) {
        return mapIdTour.get(id);
    }
}
