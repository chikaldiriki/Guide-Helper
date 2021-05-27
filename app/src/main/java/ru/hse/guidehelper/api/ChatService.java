package ru.hse.guidehelper.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ChatService {

    @GET("chat/{firstUserId}/{secondUserId}")
    Call<Long> getChatId(@Path("firstUserId") String firstUserId, @Path("secondUserId") String secondUserId);
}
