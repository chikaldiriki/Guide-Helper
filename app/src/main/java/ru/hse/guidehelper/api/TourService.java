package ru.hse.guidehelper.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.hse.guidehelper.model.Tour;

public interface TourService {

    @GET("tours/all")
    Call<List<Tour>> getAllTours();

    @POST("tours")
    Call<Void> addTour(@Body Tour tour);

    @GET("tours/favorites/{userMail}")
    Call<List<Tour>> getFavoriteTours(@Path("userMail") String userMail);

    @GET("tours/cost")
    Call<List<Tour>> getToursWithCostLimit(@Query("costLimit") Long costLimit);
}
