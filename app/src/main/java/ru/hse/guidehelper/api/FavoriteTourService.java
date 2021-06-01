package ru.hse.guidehelper.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FavoriteTourService {

    @GET("favorites/{userMail}/{tourId}")
    Call<Boolean> isFavorite(@Path("userMail") String userMail, @Path("tourId") Long tourId);

}
