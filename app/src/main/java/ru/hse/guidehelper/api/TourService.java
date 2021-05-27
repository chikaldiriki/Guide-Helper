package ru.hse.guidehelper.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import ru.hse.guidehelper.model.Tour;

public interface TourService {

    @GET("tours/all")
    Call<List<Tour>> getAllTours();

    @POST("tours")
    Call<Void> addTour(@Body Tour tour);
}
