package ru.hse.guidehelper.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.hse.guidehelper.model.FavoriteTour;

public interface FavoriteTourService {

    @POST("favorites/add")
    Call<Void> addFavoriteTour(@Body FavoriteTour newFavorite);

    @GET("favorites/{userMail}/{tourId}")
    Call<Boolean> isFavorite(@Path("userMail") String userMail, @Path("tourId") Long tourId);

    @DELETE("favorites/delete")
    Call<Void> deleteFavoriteTour(@Body FavoriteTour favoriteTour);

}
