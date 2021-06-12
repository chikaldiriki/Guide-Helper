package ru.hse.guidehelper.ui.navigationbar.favorites;

import java.util.List;

import ru.hse.guidehelper.MainActivity;
import ru.hse.guidehelper.api.RequestHelper;
import ru.hse.guidehelper.excursions.TourRecyclerViewAdapter;
import ru.hse.guidehelper.model.Tour;

public class FavoritesTourRecyclerViewAdapter extends TourRecyclerViewAdapter<Tour> {

    public FavoritesTourRecyclerViewAdapter(List<Tour> tours) {
        super(tours);
    }

    @Override
    protected void initConstructor(List<Tour> favoriteTours) {
        tours = favoriteTours;
    }
}
