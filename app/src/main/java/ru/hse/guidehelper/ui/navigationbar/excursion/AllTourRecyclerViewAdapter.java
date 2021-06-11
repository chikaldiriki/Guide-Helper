package ru.hse.guidehelper.ui.navigationbar.excursion;

import java.util.List;

import ru.hse.guidehelper.excursions.TourRecyclerViewAdapter;
import ru.hse.guidehelper.model.Tour;

public final class AllTourRecyclerViewAdapter extends TourRecyclerViewAdapter<Tour> {

    public AllTourRecyclerViewAdapter(List<Tour> tours) {
        super(tours);
    }

    @Override
    protected void initConstructor(List<Tour> allTours) {
        tours = allTours;
    }
}
