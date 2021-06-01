package ru.hse.guidehelper.ui.navigationbar.subscriptions;

import java.util.HashMap;
import java.util.Map;

import ru.hse.guidehelper.MainActivity;
import ru.hse.guidehelper.api.RequestHelper;
import ru.hse.guidehelper.excursions.TourRecyclerViewAdapter;
import ru.hse.guidehelper.model.Tour;

public class FavoritesTourRecyclerViewAdapter extends TourRecyclerViewAdapter {

    public static Map<Long, Tour> mapIdTour;

    @Override
    protected void initConstructor() {
        System.out.println("------- FAVOR initConstructor ------- ");

        tours = RequestHelper.getFavoriteTours(MainActivity.currentUser.getId());
        mapIdTour = new HashMap<>();
        for(int i = 0; i < tours.size(); i++) {
            Tour tour = tours.get(i);
            mapIdTour.put(tour.getId(), tour);
        }

        System.out.println(mapIdTour.hashCode());

        System.out.println("------ getFavoriteTours ------");
        for(int i = 0; i < tours.size(); i++) {
            System.out.println(mapIdTour.get(tours.get(i).getId()).getTitle());
        }

        System.out.println("Contains tour 2 - msk");
        System.out.println(mapIdTour.containsKey(2));
        System.out.println(mapIdTour.get(2));
        System.out.println("---------------------");
    }

    public static Tour getTourByIdFavorite(Long id) {
        System.out.println("==== FavoritegetTourById ====");
        System.out.println(mapIdTour.hashCode());
        return mapIdTour.get(id);
    }

//    @Override
//    public static Tour getTourById(Long id) {
//        return mapIdTour.get(id);
//    }

}
