package ru.hse.guidehelper.api;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    private static final String BASE_URL = "http://192.168.3.17:8080/";

    private final TourService tourService;

    private final Retrofit retrofit;
    private static Api instance;

    public static Api getInstance() {
        if (instance == null) {
            instance = new Api();
        }
        return instance;
    }

    private Api() {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        tourService = retrofit.create(TourService.class);
    }

    public TourService getTourService() {
        return tourService;
    }
}